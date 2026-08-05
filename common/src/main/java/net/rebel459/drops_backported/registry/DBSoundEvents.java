package net.rebel459.drops_backported.registry;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.Supplied;

public final class DBSoundEvents {
    private static final UnifiedRegistries.SoundEvents SOUNDS = UnifiedRegistries.SoundEvents.create(DropsBackported.VANILLA_ID);

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

    private DBSoundEvents() {}

    public static void init() {}
}
