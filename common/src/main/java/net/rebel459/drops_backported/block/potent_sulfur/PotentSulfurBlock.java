package net.rebel459.drops_backported.block.potent_sulfur;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.rebel459.drops_backported.registry.DBBlockEntityTypes;
import net.rebel459.drops_backported.registry.DBBlockStateProperties;
import net.rebel459.drops_backported.registry.DBParticleTypes;
import net.rebel459.drops_backported.sound.DBSoundEvents;
import net.rebel459.drops_backported.tag.DBBlockTags;
import org.jspecify.annotations.Nullable;

public class PotentSulfurBlock extends BaseEntityBlock {
   public static final int ALLOWED_WATER_BLOCKS_ABOVE = 4;
   public static final MapCodec<PotentSulfurBlock> CODEC = simpleCodec(PotentSulfurBlock::new);
   public static final EnumProperty<PotentSulfurState> STATE = DBBlockStateProperties.POTENT_SULFUR_STATE;

   @Override
   public MapCodec<PotentSulfurBlock> codec() {
      return CODEC;
   }

   public PotentSulfurBlock(final Properties properties) {
      super(properties);
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(STATE, PotentSulfurState.DRY));
   }

   @Override
   protected void createBlockStateDefinition(final Builder<Block, BlockState> builder) {
      builder.add(STATE);
   }

   public @Nullable BlockEntity newBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
      return new PotentSulfurBlockEntity(worldPosition, blockState);
   }

   @Override
   protected BlockState updateShape(
      final BlockState state,
      final LevelReader level,
      final ScheduledTickAccess ticks,
      final BlockPos pos,
      final Direction directionToNeighbour,
      final BlockPos neighbourPos,
      final BlockState neighbourState,
      final RandomSource random
   ) {
      return validBlockState(state, level, pos);
   }

   @Override
   public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
      return validBlockState(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
   }

   private static BlockState validBlockState(final BlockState state, final LevelReader level, final BlockPos pos) {
      if (!level.getFluidState(pos.above()).isSourceOfType(Fluids.WATER)) {
         return (BlockState)state.setValue(STATE, PotentSulfurState.DRY);
      } else {
         BlockState belowState = level.getBlockState(pos.below());
         if (belowState.is(DBBlockTags.CAUSES_CONTINUOUS_GEYSER_ERUPTIONS) && isSourceIfFluid(belowState)) {
            return (BlockState)state.setValue(STATE, PotentSulfurState.CONTINUOUS);
         } else if (belowState.is(DBBlockTags.CAUSES_PERIODIC_GEYSER_ERUPTIONS) && isSourceIfFluid(belowState)) {
            boolean isGeyser = state.getValue(STATE) == PotentSulfurState.ERUPTING || state.getValue(STATE) == PotentSulfurState.DORMANT;
            if (!isGeyser && level.getBlockEntity(pos) instanceof PotentSulfurBlockEntity potentSulfurEntity) {
               potentSulfurEntity.resetCountdown();
            }

            return state.getValue(STATE) == PotentSulfurState.ERUPTING ? state : (BlockState)state.setValue(STATE, PotentSulfurState.DORMANT);
         } else {
            return (BlockState)state.setValue(STATE, PotentSulfurState.WET);
         }
      }
   }

   private static boolean isSourceIfFluid(final BlockState belowState) {
      FluidState fluidState = belowState.getFluidState();
      return fluidState.isEmpty() || fluidState.isSource();
   }

   @Override
   protected void onPlace(final BlockState state, final Level level, final BlockPos pos, final BlockState oldState, final boolean movedByPiston) {
      super.onPlace(state, level, pos, oldState, movedByPiston);
      if (state.getValue(STATE) == PotentSulfurState.ERUPTING || state.getValue(STATE) == PotentSulfurState.CONTINUOUS) {
         level.blockEvent(pos, this, 0, 0);
         level.playSound(
            null,
            pos,
            state.getValue(STATE) == PotentSulfurState.CONTINUOUS ? DBSoundEvents.GEYSER_CONTINUOUS_START.get() : DBSoundEvents.GEYSER_ERUPTION_START.get(),
            SoundSource.BLOCKS,
            1.0F,
            1.0F
         );
         level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, Context.of(state));
      }
   }

   @Override
   public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
      if (state.getValue(STATE) != PotentSulfurState.DRY) {
         if (level.getFluidState(pos.above()).isSourceOfType(Fluids.WATER)) {
            spawnBubbleParticlesAt(level, random, pos.getX(), pos.getY() + 1, pos.getZ());
            spawnBubbleParticlesAt(level, random, pos.getX(), pos.getY() + 1, pos.getZ());
            if (random.nextInt(10) == 0) {
               level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), DBSoundEvents.NOXIOUS_GAS.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }
         }
      }
   }

   private static void spawnBubbleParticlesAt(final Level level, final RandomSource random, final double x, final double y, final double z) {
      level.addAlwaysVisibleParticle(DBParticleTypes.SULFUR_BUBBLES.get(), x + random.nextFloat(), y + random.nextFloat(), z + random.nextFloat(), 0.0, 0.0, 0.0);
   }

   @Override
   protected boolean triggerEvent(final BlockState state, final Level level, final BlockPos pos, final int b0, final int b1) {
      if (level.getBlockEntity(pos) instanceof PotentSulfurBlockEntity entity) {
         entity.eruptionTick = level.getGameTime();
      }

      return true;
   }

   public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(final Level level, final BlockState blockState, final BlockEntityType<T> type) {
      boolean client = level.isClientSide();
      BlockEntityTicker<PotentSulfurBlockEntity> ticker = switch ((PotentSulfurState)blockState.getValue(STATE)) {
         case DRY -> null;
         case WET -> client ? PotentSulfurBlockEntity.CLIENT_NOXIOUS_GAS_TICKER : PotentSulfurBlockEntity.SERVER_NAUSEA_EFFECT_TICKER;
         case DORMANT -> client
            ? PotentSulfurBlockEntity.CLIENT_NOXIOUS_GAS_TICKER
            : chain(PotentSulfurBlockEntity.SERVER_WAITING_COUNTDOWN_TICKER, PotentSulfurBlockEntity.SERVER_NAUSEA_EFFECT_TICKER);
         case ERUPTING -> client
            ? chain(PotentSulfurBlockEntity.CLIENT_GEYSER_PLUME_TICKER.apply(DBSoundEvents.GEYSER_ERUPTION_ACTIVE.get()), PotentSulfurBlockEntity.LAUNCH_ENTITY_TICKER)
            : chain(PotentSulfurBlockEntity.LAUNCH_ENTITY_TICKER, PotentSulfurBlockEntity.SERVER_WAITING_COUNTDOWN_TICKER);
         case CONTINUOUS -> client
            ? chain(PotentSulfurBlockEntity.CLIENT_GEYSER_PLUME_TICKER.apply(DBSoundEvents.GEYSER_CONTINUOUS_ACTIVE.get()), PotentSulfurBlockEntity.LAUNCH_ENTITY_TICKER)
            : PotentSulfurBlockEntity.LAUNCH_ENTITY_TICKER;
      };

      return createTickerHelper(
         type,
         DBBlockEntityTypes.POTENT_SULFUR.get(),
         ticker
      );
   }

   private static BlockEntityTicker<PotentSulfurBlockEntity> chain(
      final BlockEntityTicker<PotentSulfurBlockEntity> first,
      final BlockEntityTicker<PotentSulfurBlockEntity> second
   ) {
      return (level, pos, state, entity) -> {
         first.tick(level, pos, state, entity);
         second.tick(level, pos, state, entity);
      };
   }
}
