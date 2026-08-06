package net.rebel459.drops_backported.entity.sulfur_cube;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.rebel459.drops_backported.registry.DBAttributes;
import net.rebel459.drops_backported.registry.DBEntityTypes;
import net.rebel459.drops_backported.registry.DBItems;
import net.rebel459.drops_backported.registry.DBParticleTypes;
import net.rebel459.drops_backported.sound.DBSoundEvents;
import org.jspecify.annotations.Nullable;

public class SulfurCube extends AbstractCubeMob implements Bucketable, Shearable {
    private static final TagKey<net.minecraft.world.item.Item> FOOD = TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace("sulfur_cube_food"));
    private static final TagKey<net.minecraft.world.item.Item> SWALLOWABLE = TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace("sulfur_cube_swallowable"));
    private static final TagKey<DamageType> SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO = TagKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("sulfur_cube_with_block_immune_to"));
    private static final Predicate<ItemEntity> ALLOWED_ITEMS = e -> !e.hasPickUpDelay() && e.isAlive() && isSwallowableItem(e.getItem());
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SulfurCube.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MAX_FUSE = SynchedEntityData.defineId(SulfurCube.class, EntityDataSerializers.INT);

    private int pickupTimer = 0;
    private int pushSoundCooldown = 0;
    private boolean floatsInLiquids = false;
    private float archetypeBounce = 0.0F;
    private Optional<SulfurCubeArchetype.ExplosionData> explosionData = Optional.empty();
    private SulfurCubeArchetype.KnockbackModifiers knockbackModifier = SulfurCubeArchetype.DEFAULT_KNOCKBACK_MODIFIERS;
    private SulfurCubeArchetype.SoundSettings soundSettings = SulfurCubeArchetype.DEFAULT_SOUND_SETTINGS;
    private List<SulfurCubeArchetype.ContactDamage> contactDamages = List.of();
    private ItemStack lastBodyItem = ItemStack.EMPTY;
    private @Nullable DamageSource currentKnockbackSource;
    private float currentKnockbackDamage;
    private int fuse = -1;

    public SulfurCube(EntityType<? extends SulfurCube> type, Level level) {
        super(type, level);
        this.lookControl = new SulfurCubeLookControl();
        this.moveControl = new SulfurCubeMoveControl<>(this);
    }

    public static Supplier<AttributeSupplier> createSulfurCubeAttributes() {
        return () -> Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 0.0)
                .add(DBAttributes.AIR_DRAG_MODIFIER, 1.0)
                .add(DBAttributes.BOUNCINESS, 0.0)
                .add(DBAttributes.FRICTION_MODIFIER, 1.0)
                .add(Attributes.TEMPT_RANGE, 8.0)
                .build();
    }

    public static boolean checkSulfurCubeSpawnRules(EntityType<SulfurCube> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(FROM_BUCKET, false);
        entityData.define(MAX_FUSE, -1);
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new SulfurCubeTemptGoal(this, 1.0, stack -> this.isBaby() ? stack.is(FOOD) : isSwallowableItem(stack), false, 1.0));
        this.goalSelector.addGoal(3, new SulfurCubeSearchForItemsGoal(this));
    }

    @Override
    protected void addTargetingGoals() {
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    public int getFuse() {
        return this.fuse;
    }

    public boolean isPrimed() {
        return this.fuse >= 0;
    }

    private void setFuse(int fuse) {
        this.fuse = fuse;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (MAX_FUSE.equals(accessor)) {
            this.setFuse(this.entityData.get(MAX_FUSE));
        }

        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public SoundEvent getPickupSound() {
        return DBSoundEvents.BUCKET_FILL_SULFUR_CUBE.get();
    }

    @Override
    public void saveToBucketTag(ItemStack bucket) {
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> {
            tag.putBoolean("from_bucket", this.fromBucket());
            tag.putInt("age", this.getAge());
            tag.putBoolean("age_locked", this.isAgeLocked());
        });
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        this.setAge(tag.getIntOr("age", 0));
        this.setAgeLocked(tag.getBooleanOr("age_locked", false));
        this.setFromBucket(tag.getBooleanOr("from_bucket", false));
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(DBItems.SULFUR_CUBE_BUCKET.get());
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.hasBodyItem() || super.canBreatheUnderwater();
    }

    @Override
    public double getFluidJumpThreshold() {
        return this.getBbHeight() * 0.2;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    protected boolean isDealsDamage() {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.hasBodyItem() || this.fromBucket();
    }

    @Override
    public boolean canBeLeashed() {
        return this.hasBodyItem();
    }

    public boolean hasBodyItem() {
        return !this.getItemBySlot(EquipmentSlot.BODY).isEmpty();
    }

    public boolean canExplode() {
        return this.explosionData.isPresent() && this.isAlive() && !this.isPrimed();
    }

    @Override
    public void tick() {
        boolean wasOnGround = this.onGround();
        double previousYd = this.getDeltaMovement().y;
        this.updateArchetypesFromBodyItem();
        this.tickFuse();
        this.primeWhenOnPoweredPosition();
        super.tick();
        this.applyArchetypePhysics(wasOnGround, previousYd);
    }

    private void tickFuse() {
        if (this.fuse > 0) {
            this.fuse--;
        }

        if (this.fuse == 0 && this.level() instanceof ServerLevel level) {
            this.dropLeash();
            this.dead = true;
            if (level.getGameRules().get(GameRules.TNT_EXPLODES)) {
                Level.ExplosionInteraction interaction = level.getGameRules().get(GameRules.MOB_GRIEFING)
                    ? Level.ExplosionInteraction.TNT
                    : Level.ExplosionInteraction.NONE;
                SulfurCubeArchetype.ExplosionData explosion = this.explosionData.orElse(new SulfurCubeArchetype.ExplosionData(3, false, 120));
                level.explode(this, Explosion.getDefaultDamageSource(this.level(), this), null, this.getX(), this.getY(0.0625), this.getZ(), explosion.power(), explosion.causesFire(), interaction);
            }

            this.discard();
        }
    }

    private void primeWhenOnPoweredPosition() {
        if (this.level() instanceof ServerLevel level && this.canExplode()) {
            if (level.getBestNeighborSignal(BlockPos.containing(this.position())) != 0) {
                this.primeTime(false);
            }
        }
    }

    public boolean primeTime(boolean imminent) {
        if (this.canExplode() && this.level() instanceof ServerLevel level && level.getGameRules().get(GameRules.TNT_EXPLODES) && !this.isPrimed()) {
            int fuse = this.explosionData.map(SulfurCubeArchetype.ExplosionData::fuse).orElse(120);
            int fuseTime = imminent ? 10 + this.random.nextInt(Math.max(1, fuse / 4)) : fuse;
            this.setInvulnerable(true);
            this.setFuse(fuseTime);
            this.entityData.set(MAX_FUSE, fuseTime);
            this.playSound(SoundEvents.TNT_PRIMED);
            this.gameEvent(GameEvent.PRIME_FUSE);
            return true;
        }

        return false;
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        if (this.pickupTimer > 0) {
            this.pickupTimer--;
        }

        if (this.pushSoundCooldown > 0) {
            this.pushSoundCooldown--;
        }
    }

    @Override
    public float maxUpStep() {
        return this.hasBodyItem() ? 0.0F : super.maxUpStep();
    }

    @Override
    public boolean canFreeze() {
        return this.hasBodyItem() ? false : super.canFreeze();
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (this.isBaby()) {
            if (heldItem.is(FOOD) && this.canAgeUp()) {
                this.usePlayerItem(player, hand, heldItem);
                this.ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(-this.getAge()), true);
                this.playEatingSound();
                return InteractionResult.SUCCESS;
            }

            return super.mobInteract(player, hand);
        }

        if (this.isPrimed()) {
            return InteractionResult.PASS;
        }

        if (this.canExplode() && (heldItem.is(Items.FLINT_AND_STEEL) || heldItem.is(Items.FIRE_CHARGE))) {
            if (this.level() instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
                return InteractionResult.PASS;
            }

            this.primeTime(false);
            if (heldItem.is(Items.FLINT_AND_STEEL)) {
                heldItem.hurtAndBreak(1, player, hand.asEquipmentSlot());
            } else {
                heldItem.consume(1, player);
            }

            return InteractionResult.SUCCESS_SERVER;
        }

        if (heldItem.is(Items.SHEARS) && this.readyForShearing()) {
            if (this.level() instanceof ServerLevel level) {
                this.shear(level, SoundSource.PLAYERS, heldItem);
                this.gameEvent(GameEvent.SHEAR, player);
                heldItem.hurtAndBreak(1, player, hand.asEquipmentSlot());
            }

            return InteractionResult.SUCCESS;
        }

        if (isSwallowableItem(heldItem)) {
            boolean equipped = this.equipItem(heldItem);
            if (equipped) {
                heldItem.consume(1, player);
                this.gameEvent(GameEvent.ENTITY_INTERACT);
            }

            return equipped ? InteractionResult.SUCCESS_SERVER : InteractionResult.PASS;
        }

        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    public boolean equipItem(ItemStack stack) {
        if (this.isBaby() || !isSwallowableItem(stack)) {
            return false;
        }

        ItemStack previous = this.getItemBySlot(EquipmentSlot.BODY);
        if (!previous.isEmpty()) {
            if (stack.is(previous.getItem())) {
                return false;
            }

            if (this.level() instanceof ServerLevel level) {
                this.spawnAtLocation(level, previous, this.getAttachments().getAverage(EntityAttachment.PASSENGER));
            }
        }

        this.setItemSlotAndDropWhenKilled(EquipmentSlot.BODY, stack.copyWithCount(1));
        this.updateArchetypesFromBodyItem();
        this.playSound(this.getAbsorbSound());
        return true;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isTiny() ? DBSoundEvents.SULFUR_CUBE_SMALL_HURT.get() : DBSoundEvents.SULFUR_CUBE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTiny() ? DBSoundEvents.SULFUR_CUBE_SMALL_DEATH.get() : DBSoundEvents.SULFUR_CUBE_DEATH.get();
    }

    @Override
    protected SoundEvent getSquishSound() {
        if (this.isTiny()) {
            return DBSoundEvents.SULFUR_CUBE_SMALL_SQUISH.get();
        }

        return this.hasBodyItem() ? DBSoundEvents.SULFUR_CUBE_BOUNCE.get() : DBSoundEvents.SULFUR_CUBE_SQUISH.get();
    }

    @Override
    protected SoundEvent getJumpSound() {
        return this.isTiny() ? DBSoundEvents.SULFUR_CUBE_SMALL_JUMP.get() : DBSoundEvents.SULFUR_CUBE_JUMP.get();
    }

    private SoundEvent getAbsorbSound() {
        return DBSoundEvents.SULFUR_CUBE_ABSORB.get();
    }

    private SoundEvent getEjectSound() {
        return DBSoundEvents.SULFUR_CUBE_EJECT.get();
    }

    protected void playEatingSound() {
        this.makeSound(DBSoundEvents.SULFUR_CUBE_SMALL_EAT.get());
    }

    @Override
    protected @Nullable ParticleOptions getParticleType() {
        return DBParticleTypes.SULFUR_CUBE_GOO.get();
    }

    @Override
    public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
        Vec3 equipmentSpawnOffset = this.getAttachments().getAverage(EntityAttachment.PASSENGER);
        ItemStack sheared = this.getItemBySlot(EquipmentSlot.BODY);
        this.setItemSlot(EquipmentSlot.BODY, ItemStack.EMPTY);
        this.updateArchetypesFromBodyItem();
        this.spawnAtLocation(level, sheared, equipmentSpawnOffset);
        this.playSound(this.getEjectSound());
        this.pickupTimer = 100;
    }

    @Override
    public boolean readyForShearing() {
        return this.hasBodyItem();
    }

    @Override
    public boolean canPickUpLoot() {
        return !this.hasBodyItem();
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return !this.hasBodyItem() && isSwallowableItem(stack) && !this.isBaby();
    }

    @Override
    protected void pickUpItem(ServerLevel level, ItemEntity entity) {
        ItemStack stack = entity.getItem();
        if (this.canHoldItem(stack) && this.pickupTimer <= 0) {
            this.onItemPickup(entity);
            this.setItemSlot(EquipmentSlot.BODY, stack.split(1));
            this.updateArchetypesFromBodyItem();
            this.playSound(this.getAbsorbSound());
            this.setGuaranteedDrop(EquipmentSlot.BODY);
            this.take(entity, 1);
        }
    }

    @Override
    protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.BODY;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return this.isBaby() ? 0 : 1 + this.random.nextInt(2);
    }

    @Override
    protected int getSplitCount() {
        return this.isPrimed() ? 0 : 2;
    }

    @Override
    protected void setSpawnSize(ServerLevelAccessor level, DifficultyInstance difficulty) {
        this.setSize(this.isBaby() ? 1 : 2, true);
    }

    @Override
    public void setSize(int size, boolean updateHealth) {
        super.setSize(size, updateHealth);
        if (updateHealth && size == 1 && !this.isBaby()) {
            this.setBaby(true);
        }
    }

    @Override
    protected void setUpSplitCube(AbstractCubeMob cubeMob, int halfSize, float xd, float zd) {
        super.setUpSplitCube(cubeMob, halfSize, xd, zd);
        cubeMob.setBaby(true);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        SulfurCube sulfurCube = DBEntityTypes.SULFUR_CUBE.get().create(level, EntitySpawnReason.BREEDING);
        if (sulfurCube != null) {
            sulfurCube.setSize(1, true);
        }

        return sulfurCube;
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!this.isBaby()) {
            this.setSize(2, true);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("pickup_timer", this.pickupTimer);
        output.putBoolean("from_bucket", this.fromBucket());
        output.putInt("fuse", this.getFuse());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.pickupTimer = input.getIntOr("pickup_timer", 0);
        this.setFromBucket(input.getBooleanOr("from_bucket", false));
        this.setFuse(input.getIntOr("fuse", -1));
        this.entityData.set(MAX_FUSE, this.getFuse());
        super.readAdditionalSaveData(input);
        this.updateArchetypesFromBodyItem();
    }

    @Override
    protected void doPush(Entity entity) {
        super.doPush(entity);
        this.applyContactDamage(entity);
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        this.playerPush(player);
    }

    private void playerPush(Player player) {
        if (this.hasBodyItem()) {
            Entity pusher = player.isPassenger() ? player.getRootVehicle() : player;
            Vec3 cubeToPusher = this.position().subtract(pusher.position());
            double bottom = this.getY();
            double top = bottom + this.getBbHeight();
            double pusherTop = pusher.getY() + pusher.getBbHeight();
            if (cubeToPusher.horizontalDistance() < 1.3F && pusher.getY() <= top && pusherTop > bottom) {
                double resistance = Math.max(0.0, 1.0 - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                double speed = Mth.clamp(player.getKnownSpeed().length() * 2.0 * (player.isPassenger() ? 0.16F : 0.3F), 0.0, 0.5);
                Vec3 direction = cubeToPusher.horizontal().normalize().scale(resistance);
                Vec3 pushVelocity = new Vec3(direction.x, this.onGround() ? resistance * 0.3F : 0.0, direction.z).scale(speed);
                this.needsSync = true;
                float threshold = this.soundSettings.pushSoundImpulseThreshold();
                if (pushVelocity.lengthSqr() > threshold * threshold && this.pushSoundCooldown <= 0) {
                    this.pushSoundCooldown = (int)(this.soundSettings.pushSoundCooldown() * 20.0F);
                    this.playSound(this.soundSettings.pushSound().value());
                }

                this.addDeltaMovement(pushVelocity);
                this.applyContactDamage(player);
            }
        }
    }

    private void updateArchetypesFromBodyItem() {
        ItemStack current = this.getItemBySlot(EquipmentSlot.BODY);
        if (ItemStack.matches(current, this.lastBodyItem)) {
            return;
        }

        this.lastBodyItem = current.copy();
        this.removeArchetypeAttributeModifiers();
        this.floatsInLiquids = false;
        this.archetypeBounce = 0.0F;
        this.explosionData = Optional.empty();
        this.contactDamages = List.of();
        this.knockbackModifier = SulfurCubeArchetype.DEFAULT_KNOCKBACK_MODIFIERS;
        this.soundSettings = SulfurCubeArchetype.DEFAULT_SOUND_SETTINGS;

        List<SulfurCubeArchetype> matches = SulfurCubeArchetype.matching(current);
        if (matches.isEmpty()) {
            return;
        }

        this.archetypeBounce = matches.getLast().bounce();
        this.floatsInLiquids = matches.stream().anyMatch(SulfurCubeArchetype::buoyant);
        this.explosionData = matches.stream().map(SulfurCubeArchetype::explosion).filter(Optional::isPresent).map(Optional::get).findFirst();
        this.contactDamages = matches.stream().map(SulfurCubeArchetype::contactDamage).filter(Optional::isPresent).map(Optional::get).toList();
        this.knockbackModifier = matches.getLast().knockbackModifiers();
        this.soundSettings = matches.getLast().soundSettings();
        matches.forEach(this::applyArchetypeAttributeModifiers);
    }

    private void removeArchetypeAttributeModifiers() {
        for (SulfurCubeArchetype archetype : SulfurCubeArchetype.REGISTERED) {
            this.removeArchetypeAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, archetype, "add_knockback_resistance");
            this.removeArchetypeAttributeModifier(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, archetype, "add_explosion_knockback_resistance");
            this.removeArchetypeAttributeModifier(DBAttributes.BOUNCINESS, archetype, "add_bounciness");
            this.removeArchetypeAttributeModifier(DBAttributes.FRICTION_MODIFIER, archetype, "mul_friction_modifier");
            this.removeArchetypeAttributeModifier(DBAttributes.AIR_DRAG_MODIFIER, archetype, "mul_air_drag_modifier");
        }
    }

    private void applyArchetypeAttributeModifiers(SulfurCubeArchetype archetype) {
        this.addArchetypeAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, archetype, "add_knockback_resistance", -archetype.speed(), AttributeModifier.Operation.ADD_VALUE);
        this.addArchetypeAttributeModifier(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, archetype, "add_explosion_knockback_resistance", -archetype.speed(), AttributeModifier.Operation.ADD_VALUE);
        this.addArchetypeAttributeModifier(DBAttributes.BOUNCINESS, archetype, "add_bounciness", archetype.bounce(), AttributeModifier.Operation.ADD_VALUE);
        this.addArchetypeAttributeModifier(DBAttributes.FRICTION_MODIFIER, archetype, "mul_friction_modifier", archetype.friction() - 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addArchetypeAttributeModifier(DBAttributes.AIR_DRAG_MODIFIER, archetype, "mul_air_drag_modifier", archetype.airDrag() - 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    private void addArchetypeAttributeModifier(Holder<Attribute> attribute, SulfurCubeArchetype archetype, String suffix, double amount, AttributeModifier.Operation operation) {
        AttributeInstance instance = this.getAttribute(attribute);
        if (instance != null) {
            instance.addOrUpdateTransientModifier(new AttributeModifier(archetypeModifierId(archetype, suffix), amount, operation));
        }
    }

    private void removeArchetypeAttributeModifier(Holder<Attribute> attribute, SulfurCubeArchetype archetype, String suffix) {
        AttributeInstance instance = this.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(archetypeModifierId(archetype, suffix));
        }
    }

    private static Identifier archetypeModifierId(SulfurCubeArchetype archetype, String suffix) {
        return Identifier.fromNamespaceAndPath(archetype.id().getNamespace(), archetype.id().getPath() + "_" + suffix);
    }

    private void applyArchetypePhysics(boolean wasOnGround, double previousYd) {
        if (!this.hasBodyItem()) {
            return;
        }

        Vec3 movement = this.getDeltaMovement();
        if (this.floatsInLiquids && (this.isInWater() || this.isInLava())) {
            float vibeAmount = 0.2F * Mth.sin(this.tickCount * 0.4F);
            double immersion = this.getFluidHeight(this.isInWater() ? FluidTags.WATER : FluidTags.LAVA) - this.getFluidJumpThreshold() + vibeAmount;
            if (immersion > 0.0) {
                movement = movement.add(0.0, Math.min(1.0, immersion) * 0.04F, 0.0);
            }
        }

        float bounciness = (float)this.getAttributeValue(DBAttributes.BOUNCINESS);
        if (!wasOnGround && this.onGround() && previousYd < -0.08 && bounciness > 0.0F) {
            movement = new Vec3(movement.x, -previousYd * bounciness, movement.z);
        }

        this.setDeltaMovement(movement);
    }

    private void applyContactDamage(Entity entity) {
        if (!(this.level() instanceof ServerLevel level)) {
            return;
        }

        for (SulfurCubeArchetype.ContactDamage damage : this.contactDamages) {
            var holder = level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(damage.damageType());
            entity.hurtServer(level, new DamageSource(holder, damage.attributeToSource() ? this : null), damage.amount().sample(this.getRandom()));
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.hasBodyItem()) {
            if (this.canExplode() && !this.isPrimed()) {
                Entity sourceEntity = source.getDirectEntity();
                if (source.is(DamageTypeTags.IS_FIRE) || sourceEntity instanceof AbstractArrow arrow && arrow.isOnFire()) {
                    this.primeTime(false);
                } else if (source.is(DamageTypeTags.IS_EXPLOSION)) {
                    this.primeTime(true);
                }
            }

            if (source.is(SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO)) {
                if (!source.is(DamageTypeTags.NO_KNOCKBACK)) {
                    this.dealSourceKnockback(source, damage);
                }

                return true;
            }
        }

        this.currentKnockbackSource = source;
        this.currentKnockbackDamage = damage;
        try {
            return super.hurtServer(level, source, damage);
        } finally {
            this.currentKnockbackSource = null;
            this.currentKnockbackDamage = 0.0F;
        }
    }

    private void dealSourceKnockback(DamageSource source, float damage) {
        double xd = 0.0;
        double zd = 0.0;
        if (source.getSourcePosition() != null) {
            xd = source.getSourcePosition().x() - this.getX();
            zd = source.getSourcePosition().z() - this.getZ();
        }

        this.currentKnockbackSource = source;
        this.currentKnockbackDamage = damage;
        try {
            this.knockback(0.4F, xd, zd);
        } finally {
            this.currentKnockbackSource = null;
            this.currentKnockbackDamage = 0.0F;
        }
    }

    @Override
    public void knockback(double power, double xd, double zd) {
        if (this.currentKnockbackSource != null && this.currentKnockbackSource.getEntity() != null && this.hasBodyItem()) {
            this.applyArchetypeKnockback(this.currentKnockbackSource, this.currentKnockbackDamage, xd, zd);
        } else {
            super.knockback(power, xd, zd);
        }
    }

    private void applyArchetypeKnockback(DamageSource source, float damage, double xd, double zd) {
        Entity attacker = source.getEntity();
        if (attacker == null) {
            return;
        }

        float horizontalPower = this.knockbackModifier.horizontalPower();
        float verticalPower = this.knockbackModifier.verticalPower();
        float originalHorizontalPower = horizontalPower;
        float originalVerticalPower = verticalPower;
        Vec2 originalAngle = new Vec2((float)xd, (float)zd);
        Vec2 newAngle = rotateHorizontalHitAngle(originalAngle, attacker.getEyePosition(), attacker.getLookAngle().normalize(), this.getBoundingBox().getCenter());
        Vec2 newPower = applyVerticalHitAnglePowerTransfer(horizontalPower, verticalPower, attacker.getEyePosition(), attacker.getLookAngle().normalize(), this.getBoundingBox().getCenter(), this.getBbHeight());
        newPower = applyVerticalPositionAnglePowerRotation(newPower.x, newPower.y, originalHorizontalPower, originalVerticalPower, attacker.position(), this.position());
        float powerMultiplier = Mth.sqrt(damage);
        float knockbackScale = Math.max(0.0F, 1.0F - (float)this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        horizontalPower = newPower.x * powerMultiplier * knockbackScale * 0.4F;
        verticalPower = newPower.y * powerMultiplier * knockbackScale;
        horizontalPower = Mth.clamp(horizontalPower, -128.0F, 128.0F);
        verticalPower = Mth.clamp(verticalPower, -128.0F, 128.0F);
        Vec3 horizontalKnockback = new Vec3(newAngle.x, 0.0, newAngle.y).normalize().scale(horizontalPower);
        Vec3 movement = this.getDeltaMovement();
        this.setDeltaMovement(movement.x - horizontalKnockback.x, movement.y + verticalPower * 1.2, movement.z - horizontalKnockback.z);
        this.needsSync = true;
        this.playSound(this.soundSettings.hitSound().value());
    }

    private static Vec2 rotateHorizontalHitAngle(Vec2 originalAngle, Vec3 attackerPosition, Vec3 attackerAimDirection, Vec3 targetCenter) {
        Vec3 attackerToTarget = targetCenter.subtract(attackerPosition).normalize();
        float angleDiff = (float)Math.atan2(
            attackerAimDirection.x * attackerToTarget.z - attackerAimDirection.z * attackerToTarget.x,
            attackerAimDirection.x * attackerToTarget.x + attackerAimDirection.z * attackerToTarget.z
        );
        return rotate(originalAngle, angleDiff * 1.6F);
    }

    private static Vec2 applyVerticalHitAnglePowerTransfer(float horizontalPower, float verticalPower, Vec3 attackerPosition, Vec3 attackerAimDirection, Vec3 targetCenteredPosition, float targetHeight) {
        float targetHalfHeight = 0.5F * targetHeight;
        Vec3 targetTopPos = targetCenteredPosition.add(0.0, targetHalfHeight, 0.0);
        Vec3 targetBottomPos = targetCenteredPosition.add(0.0, -targetHalfHeight, 0.0);
        Vec3 attackerToTargetTop = targetTopPos.subtract(attackerPosition).normalize();
        Vec3 attackerToTargetBottom = targetBottomPos.subtract(attackerPosition).normalize();
        float verticalHitAngleFactor = (float)Mth.clampedMap(attackerAimDirection.y, attackerToTargetTop.y, attackerToTargetBottom.y, -1.0, 1.0);
        float transferredPowerRatio = Math.abs(verticalHitAngleFactor * 0.5F);
        if (verticalHitAngleFactor < 0.0F) {
            transferredPowerRatio = -transferredPowerRatio;
        }

        return new Vec2(horizontalPower * (1.0F - transferredPowerRatio), verticalPower * (1.0F + transferredPowerRatio));
    }

    private static Vec2 applyVerticalPositionAnglePowerRotation(float horizontalPower, float verticalPower, float originalHorizontalPower, float originalVerticalPower, Vec3 attackerFeetPosition, Vec3 targetFeetPosition) {
        Vec3 attackerFeetToTargetFeet = targetFeetPosition.subtract(attackerFeetPosition);
        float verticalPositionAngle = (float)Math.atan2(-attackerFeetToTargetFeet.y, attackerFeetToTargetFeet.horizontalDistance());
        Vec2 rotatedPower = rotate(new Vec2(horizontalPower, verticalPower), -verticalPositionAngle * 0.8F);
        float horizontalRatio = originalHorizontalPower > 0.0F ? Mth.abs(rotatedPower.x) / originalHorizontalPower : 0.0F;
        float verticalRatio = originalVerticalPower > 0.0F ? Mth.abs(rotatedPower.y) / originalVerticalPower : 0.0F;
        float maxRatio = Math.max(horizontalRatio, verticalRatio);
        return maxRatio > 1.0F ? rotatedPower.scale(1.0F / maxRatio) : rotatedPower;
    }

    private static Vec2 rotate(Vec2 vec, float radians) {
        float sin = Mth.sin(radians);
        float cos = Mth.cos(radians);
        return new Vec2(vec.x * cos - vec.y * sin, vec.x * sin + vec.y * cos);
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getBbHeight() / 2.0F, 0.0);
    }

    @Override
    protected void setCubeMobHealth(int actualSize) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(4.0 * actualSize);
    }

    private static boolean isSwallowableItem(ItemStack stack) {
        return stack.is(SWALLOWABLE);
    }

    private class SulfurCubeLookControl extends LookControl {
        private SulfurCubeLookControl() {
            super(SulfurCube.this);
        }

        @Override
        public void tick() {
            if (!SulfurCube.this.hasBodyItem()) {
                super.tick();
            } else {
                float closeAngle = wrapDegrees90(SulfurCube.this.getYRot());
                SulfurCube.this.setYRot(SulfurCube.this.getYRot() - closeAngle);
                SulfurCube.this.setYHeadRot(SulfurCube.this.getYRot());
            }
        }
    }

    protected static class SulfurCubeMoveControl<T extends SulfurCube> extends AbstractCubeMob.CubeMobMoveControl<T> {
        public SulfurCubeMoveControl(T cubeMob) {
            super(cubeMob);
        }

        @Override
        public void tick() {
            if (!this.cubeMob.hasBodyItem()) {
                super.tick();
            }
        }
    }

    private class SulfurCubeSearchForItemsGoal extends Goal {
        private final SulfurCube sulfurCube;
        private @Nullable ItemEntity targetItem;

        public SulfurCubeSearchForItemsGoal(SulfurCube sulfurCube) {
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
            this.sulfurCube = sulfurCube;
        }

        @Override
        public boolean canUse() {
            if (!this.sulfurCube.isBaby() && this.sulfurCube.pickupTimer <= 0) {
                this.targetItem = null;
                double bestDistance = Double.MAX_VALUE;
                for (ItemEntity item : this.sulfurCube.level().getEntitiesOfClass(ItemEntity.class, this.sulfurCube.getBoundingBox().inflate(8.0, 8.0, 8.0), ALLOWED_ITEMS)) {
                    double distance = item.distanceToSqr(this.sulfurCube);
                    if (distance < bestDistance) {
                        this.targetItem = item;
                        bestDistance = distance;
                    }
                }
                return this.targetItem != null;
            }

            return false;
        }

        @Override
        public void tick() {
            SulfurCube.this.lookAt(this.targetItem, 10.0F, 10.0F);
            if (SulfurCube.this.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl<?> moveControl) {
                moveControl.setDirection(SulfurCube.this.getYRot(), true);
            }
        }
    }

    private static class SulfurCubeTemptGoal extends TemptGoal.ForNonPathfinders {
        public SulfurCubeTemptGoal(Mob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare, double stopDistance) {
            super(mob, speedModifier, items, canScare, stopDistance);
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        protected void stopNavigation() {
            if (this.mob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl<?> moveControl) {
                moveControl.setWantedMovement(0.0);
            }
        }

        @Override
        protected void navigateTowards(Player player) {
            this.mob.lookAt(player, 10.0F, 10.0F);
            if (this.mob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl<?> moveControl) {
                moveControl.setDirection(this.mob.getYRot(), true);
            }
        }
    }

    private static float wrapDegrees90(float degrees) {
        return Mth.wrapDegrees(degrees - Math.round(degrees / 90.0F) * 90.0F);
    }
}
