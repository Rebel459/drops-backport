package net.rebel459.drops_backported.sound;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.Supplied;

public final class DBSoundEvents {
    private static final UnifiedRegistries.SoundEvents SOUNDS = UnifiedRegistries.SoundEvents.create(DropsBackported.VANILLA_ID);

    public static final Supplied<SoundEvent> MUSIC_DISC_BOUNCE = SOUNDS.register("music_disc.bounce");

    public static final Supplied<SoundEvent> BUCKET_EMPTY_SULFUR_CUBE = SOUNDS.register("item.bucket.empty_sulfur_cube");
    public static final Supplied<SoundEvent> BUCKET_FILL_SULFUR_CUBE = SOUNDS.register("item.bucket.fill_sulfur_cube");
    public static final Supplied<SoundEvent> SULFUR_CUBE_ABSORB = SOUNDS.register("entity.sulfur_cube.absorb");
    public static final Supplied<SoundEvent> SULFUR_CUBE_BOUNCE = SOUNDS.register("entity.sulfur_cube.bounce");
    public static final Supplied<SoundEvent> SULFUR_CUBE_DEATH = SOUNDS.register("entity.sulfur_cube.death");
    public static final Supplied<SoundEvent> SULFUR_CUBE_EJECT = SOUNDS.register("entity.sulfur_cube.eject");
    public static final Supplied<SoundEvent> SULFUR_CUBE_HURT = SOUNDS.register("entity.sulfur_cube.hurt");
    public static final Supplied<SoundEvent> SULFUR_CUBE_JUMP = SOUNDS.register("entity.sulfur_cube.jump");
    public static final Supplied<SoundEvent> SULFUR_CUBE_SQUISH = SOUNDS.register("entity.sulfur_cube.squish");
    public static final Supplied<SoundEvent> SULFUR_CUBE_SMALL_DEATH = SOUNDS.register("entity.small_sulfur_cube.death");
    public static final Supplied<SoundEvent> SULFUR_CUBE_SMALL_HURT = SOUNDS.register("entity.small_sulfur_cube.hurt");
    public static final Supplied<SoundEvent> SULFUR_CUBE_SMALL_JUMP = SOUNDS.register("entity.small_sulfur_cube.jump");
    public static final Supplied<SoundEvent> SULFUR_CUBE_SMALL_SQUISH = SOUNDS.register("entity.small_sulfur_cube.squish");
    public static final Supplied<SoundEvent> SULFUR_CUBE_SMALL_EAT = SOUNDS.register("entity.small_sulfur_cube.eat");

    public static final Holder<SoundEvent> SULFUR_CUBE_REGULAR_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.regular.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_REGULAR_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.regular.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_BOUNCY_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.bouncy.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_BOUNCY_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.bouncy.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_BOUNCY_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.slow_bouncy.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_BOUNCY_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.slow_bouncy.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_FLAT_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.slow_flat.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_FLAT_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.slow_flat.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_FAST_FLAT_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.fast_flat.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_FAST_FLAT_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.fast_flat.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_LIGHT_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.light.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_LIGHT_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.light.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_FAST_SLIDING_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.fast_sliding.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_FAST_SLIDING_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.fast_sliding.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_SLIDING_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.slow_sliding.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_SLIDING_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.slow_sliding.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_STICKY_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.sticky.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_STICKY_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.sticky.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_HIGH_RESISTANCE_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.high_resistance.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_HIGH_RESISTANCE_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.high_resistance.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_EXPLOSIVE_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.explosive.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_EXPLOSIVE_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.explosive.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_HOT_HIT = SOUNDS.registerForHolder("entity.sulfur_cube.hot.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_HOT_PUSH = SOUNDS.registerForHolder("entity.sulfur_cube.hot.push");

    public static final Supplied<SoundEvent> SULFUR_SPIKE_BREAK = SOUNDS.register("block.sulfur_spike.break");
    public static final Supplied<SoundEvent> SULFUR_SPIKE_STEP = SOUNDS.register("block.sulfur_spike.step");
    public static final Supplied<SoundEvent> SULFUR_SPIKE_PLACE = SOUNDS.register("block.sulfur_spike.place");
    public static final Supplied<SoundEvent> SULFUR_SPIKE_HIT = SOUNDS.register("block.sulfur_spike.hit");
    public static final Supplied<SoundEvent> SULFUR_SPIKE_FALL = SOUNDS.register("block.sulfur_spike.fall");
    public static final Supplied<SoundEvent> SULFUR_SPIKE_LAND = SOUNDS.register("block.sulfur_spike.land");

    public static final Supplied<SoundEvent> SULFUR_BREAK = SOUNDS.register("block.sulfur.break");
    public static final Supplied<SoundEvent> SULFUR_STEP = SOUNDS.register("block.sulfur.step");
    public static final Supplied<SoundEvent> SULFUR_PLACE = SOUNDS.register("block.sulfur.place");
    public static final Supplied<SoundEvent> SULFUR_HIT = SOUNDS.register("block.sulfur.hit");
    public static final Supplied<SoundEvent> SULFUR_FALL = SOUNDS.register("block.sulfur.fall");
    public static final Supplied<SoundEvent> POTENT_SULFUR_BREAK = SOUNDS.register("block.potent_sulfur.break");
    public static final Supplied<SoundEvent> POTENT_SULFUR_STEP = SOUNDS.register("block.potent_sulfur.step");
    public static final Supplied<SoundEvent> POTENT_SULFUR_PLACE = SOUNDS.register("block.potent_sulfur.place");
    public static final Supplied<SoundEvent> POTENT_SULFUR_HIT = SOUNDS.register("block.potent_sulfur.hit");
    public static final Supplied<SoundEvent> POTENT_SULFUR_FALL = SOUNDS.register("block.potent_sulfur.fall");
    public static final Supplied<SoundEvent> GEYSER_CONTINUOUS_START = SOUNDS.register("block.potent_sulfur.geyser_continuous_eruption");
    public static final Supplied<SoundEvent> GEYSER_CONTINUOUS_ACTIVE = SOUNDS.register("block.potent_sulfur.geyser_continuous_eruption_active");
    public static final Supplied<SoundEvent> GEYSER_ERUPTION_START = SOUNDS.register("block.potent_sulfur.geyser_eruption");
    public static final Supplied<SoundEvent> GEYSER_ERUPTION_ACTIVE = SOUNDS.register("block.potent_sulfur.geyser_eruption_active");
    public static final Supplied<SoundEvent> NOXIOUS_GAS = SOUNDS.register("block.potent_sulfur.noxious_gas");

    public static final Supplied<SoundEvent> CINNABAR_BREAK = SOUNDS.register("block.cinnabar.break");
    public static final Supplied<SoundEvent> CINNABAR_STEP = SOUNDS.register("block.cinnabar.step");
    public static final Supplied<SoundEvent> CINNABAR_PLACE = SOUNDS.register("block.cinnabar.place");
    public static final Supplied<SoundEvent> CINNABAR_HIT = SOUNDS.register("block.cinnabar.hit");
    public static final Supplied<SoundEvent> CINNABAR_FALL = SOUNDS.register("block.cinnabar.fall");

    private DBSoundEvents() {}

    public static void init() {}
}
