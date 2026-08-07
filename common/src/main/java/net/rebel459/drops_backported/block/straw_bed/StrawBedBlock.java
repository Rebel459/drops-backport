package net.rebel459.drops_backported.block.straw_bed;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rebel459.drops_backported.sound.DBSoundEvents;

import java.util.Map;
import java.util.OptionalDouble;

public class StrawBedBlock extends AbstractBedBlock {
    private static final VoxelShape BASE_SHAPE = Block.column(16.0, 0.0, 4.0);
    private static final VoxelShape PILLOW_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 8.0);
    private static final Map<Direction, VoxelShape> FOOT_SHAPES = Util.make(() -> Shapes.rotateHorizontal(BASE_SHAPE));
    private static final Map<Direction, VoxelShape> HEAD_SHAPES = Util.make(() -> Shapes.rotateHorizontal(Shapes.or(BASE_SHAPE, PILLOW_SHAPE)));

    public StrawBedBlock(final Properties properties) {
        super(properties);
    }

    private void destroyBed(final Level level, final BlockPos pos) {
        level.playSound(null, pos, DBSoundEvents.STRAW_BED_BREAK_LEAVE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }

    @Override
    protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        Map<Direction, VoxelShape> shapes = state.getValue(PART) == BedPart.HEAD ? HEAD_SHAPES : FOOT_SHAPES;
        return (VoxelShape) shapes.get(getConnectedDirection(state).getOpposite());
    }

    @Override
    public void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        level.levelEvent(2014, pos, getId(state));
    }

/*   @Override
   public Identifier getSleptInBedStatType() {
      return Stats.SLEEP_IN_STRAW_BED;
   }*/

    @Override
    public OptionalDouble getSleepHeight(final BlockState state, final Level level, final BlockPos pos) {
        BlockPos layingOnPos;
        BlockState layingOnState;
        if (state.getValue(BedBlock.PART) == BedPart.HEAD) {
            layingOnPos = pos.relative(getConnectedDirection(state));
            layingOnState = level.getBlockState(layingOnPos);
            if (!layingOnState.is(this) || layingOnState.getValue(BedBlock.PART) != BedPart.FOOT) {
                return OptionalDouble.empty();
            }
        } else {
            layingOnPos = pos;
            layingOnState = state;
        }

        return super.getSleepHeight(layingOnState, level, layingOnPos);
    }

    @Override
    public boolean shouldDestroyOnUse(Level level, BlockPos pos, BedRule bedRule) {
        return !bedRule.canSleep(level);
    }

    @Override
    public boolean shouldDestroyOnLeave(Level level, BlockPos pos, BedRule bedRule) {
        return bedRule.canSleep(level);
    }

    @Override
    protected InteractionResult destroyOnUse(final BlockState state, final Level level, final BlockPos pos, final Player player) {
        this.destroyBed(level, pos);
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    protected void destroyOnLeave(final Level level, final BlockPos pos) {
        this.destroyBed(level, pos);
    }
}