package net.rebel459.drops_backported.block;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rebel459.drops_backported.sound.DBSoundEvents;
import org.jspecify.annotations.Nullable;

public class ShelfMushroomBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
   public static final int MAX_AGE = 1;
   public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
   private static final List<Map<Direction, VoxelShape>> SHAPES = IntStream.rangeClosed(0, 1)
      .mapToObj(
         i -> Shapes.rotateHorizontal(
            Shapes.or(
               Block.column(10 + i * 4, 7 + i * 3, 1 + i, 3 + i * 2).move(0.0, (8 - i * 2) / 16.0, -(i * 1.5 - 4.5) / 16.0).optimize(),
               Block.column(6 + i * 2, 4 + i * 2, 0.0, 1 + i).move(0.0, (8 - i * 2) / 16.0, -(i - 6) / 16.0).optimize()
            )
         )
      )
      .toList();

   public ShelfMushroomBlock(final Properties properties) {
      super(properties);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, 0));
   }

   @Override
   protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
      Direction facing = state.getValue(FACING);
      BlockPos supportPos = pos.relative(facing.getOpposite());
      BlockState supportState = level.getBlockState(supportPos);
      return supportState.isFaceSturdy(level, supportPos, facing);
   }

   @Override
   protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
      return (VoxelShape)((Map)SHAPES.get((Integer)state.getValue(AGE))).get(state.getValue(FACING));
   }

   @Override
   public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
      BlockState state = this.defaultBlockState();
      LevelReader level = context.getLevel();
      BlockPos pos = context.getClickedPos();

      for (Direction direction : context.getNearestLookingDirections()) {
         if (direction.getAxis().isHorizontal()) {
            state = state.setValue(FACING, direction.getOpposite());
            if (state.canSurvive(level, pos)) {
               return state;
            }
         }
      }

      return null;
   }

   @Override
   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
      if (!(entity instanceof ItemEntity) && !(entity instanceof PrimedTnt)) {
         level.playSound(null, pos, DBSoundEvents.SHELF_MUSHROOM_BOUNCE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
         super.fallOn(level, state, pos, entity, fallDistance / 2);
      }
   }

   @Override
   public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) {
      if (entity.isSuppressingBounce()) {
         super.updateEntityMovementAfterFallOn(level, entity);
      } else {
         this.bounceUp(entity);
      }
   }

   private void bounceUp(Entity entity) {
      Vec3 movement = entity.getDeltaMovement();
      if (movement.y < 0.0) {
         double factor = entity instanceof LivingEntity ? 1.0 : 0.8;
         factor *= 0.75D;
         entity.setDeltaMovement(movement.x, -movement.y * factor, movement.z);
      }
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
      return directionToNeighbour == ((Direction)state.getValue(FACING)).getOpposite() && !state.canSurvive(level, pos)
         ? Blocks.AIR.defaultBlockState()
         : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
   }

   @Override
   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      return state.getValue(AGE) < 1;
   }

   @Override
   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
      return true;
   }

   @Override
   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
      level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), 2);
   }

   @Override
   protected void createBlockStateDefinition(final Builder<Block, BlockState> builder) {
      builder.add(FACING, AGE);
   }

   @Override
   protected boolean isPathfindable(final BlockState state, final PathComputationType type) {
      return false;
   }
}