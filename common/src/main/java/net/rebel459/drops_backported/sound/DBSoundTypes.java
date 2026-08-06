package net.rebel459.drops_backported.sound;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Supplier;

public class DBSoundTypes {

    public static final Supplier<SoundType> SULFUR = () -> new SoundType(
            1.0F,
            1.0F,
            DBSoundEvents.SULFUR_BREAK.get(),
            DBSoundEvents.SULFUR_STEP.get(),
            DBSoundEvents.SULFUR_PLACE.get(),
            DBSoundEvents.SULFUR_HIT.get(),
            DBSoundEvents.SULFUR_FALL.get()
    );
    public static final Supplier<SoundType> POTENT_SULFUR = () -> new SoundType(
            1.0F,
            1.0F,
            DBSoundEvents.POTENT_SULFUR_BREAK.get(),
            DBSoundEvents.POTENT_SULFUR_STEP.get(),
            DBSoundEvents.POTENT_SULFUR_PLACE.get(),
            DBSoundEvents.POTENT_SULFUR_HIT.get(),
            DBSoundEvents.POTENT_SULFUR_FALL.get()
    );
    public static final Supplier<SoundType> SULFUR_SPIKE = () -> new SoundType(
            1.0F,
            1.0F,
            DBSoundEvents.SULFUR_SPIKE_BREAK.get(),
            DBSoundEvents.SULFUR_SPIKE_STEP.get(),
            DBSoundEvents.SULFUR_SPIKE_PLACE.get(),
            DBSoundEvents.SULFUR_SPIKE_HIT.get(),
            DBSoundEvents.SULFUR_SPIKE_FALL.get()
    );
    public static final Supplier<SoundType> CINNABAR = () -> new SoundType(
            1.0F,
            1.0F,
            DBSoundEvents.CINNABAR_BREAK.get(),
            DBSoundEvents.CINNABAR_STEP.get(),
            DBSoundEvents.CINNABAR_PLACE.get(),
            DBSoundEvents.CINNABAR_HIT.get(),
            DBSoundEvents.CINNABAR_FALL.get()
    );

    public static final Supplier<SoundType> SHELF_MUSHROOM = () -> new SoundType(
            1.0F,
            1.0F,
            DBSoundEvents.SHELF_MUSHROOM_BREAK.get(),
            DBSoundEvents.SHELF_MUSHROOM_STEP.get(),
            DBSoundEvents.SHELF_MUSHROOM_PLACE.get(),
            SoundEvents.EMPTY,
            DBSoundEvents.SHELF_MUSHROOM_FALL.get()
    );
    public static final Supplier<SoundType> POPLAR_LEAVES = () -> new SoundType(
            1.0F,
            1.0F,
            DBSoundEvents.POPLAR_LEAVES_BREAK.get(),
            DBSoundEvents.POPLAR_LEAVES_STEP.get(),
            DBSoundEvents.POPLAR_LEAVES_PLACE.get(),
            DBSoundEvents.POPLAR_LEAVES_HIT.get(),
            DBSoundEvents.POPLAR_LEAVES_FALL.get()
    );
    public static final Supplier<SoundType> STRAW_BED = () -> new SoundType(
            1.0F,
            1.0F,
            DBSoundEvents.STRAW_BED_BREAK.get(),
            DBSoundEvents.STRAW_BED_STEP.get(),
            DBSoundEvents.STRAW_BED_PLACE.get(),
            DBSoundEvents.STRAW_BED_HIT.get(),
            DBSoundEvents.STRAW_BED_FALL.get()
    );
    public static final Supplier<SoundType> RED_SHRUB = () -> new SoundType(
            1.0F,
            1.0F,
            DBSoundEvents.RED_SHRUB_BREAK.get(),
            SoundEvents.EMPTY,
            DBSoundEvents.RED_SHRUB_PLACE.get(),
            SoundEvents.EMPTY,
            SoundEvents.EMPTY
    );
}
