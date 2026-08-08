package net.rebel459.drops_backport.mixin.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.rebel459.drops_backport.entity.cube.SulfurCube;
import net.rebel459.drops_backport.registry.DBAttributes;
import net.rebel459.drops_backport.registry.DBGameEvents;
import net.rebel459.drops_backport.tag.DBBlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique
    private Vec3 movementBeforeCollision = Vec3.ZERO;

    @Unique
    private Vec3 positionBeforeMove = Vec3.ZERO;

    @Inject(
            method = "move",
            at = @At("HEAD")
    )
    private void captureMovementBeforeCollision(
            MoverType moverType,
            Vec3 delta,
            CallbackInfo ci
    ) {
        Entity entity = (Entity) (Object) this;

        this.movementBeforeCollision = entity.getDeltaMovement();
        this.positionBeforeMove = entity.position();
    }

    @Inject(
            method = "move",
            at = @At("TAIL")
    )
    private void applyBounciness(MoverType moverType, Vec3 delta, CallbackInfo ci) {

        if (!(Entity.class.cast(this) instanceof LivingEntity entity)) return;

        AttributeInstance bouncinessAttribute = entity.getAttribute(DBAttributes.BOUNCINESS);

        if (bouncinessAttribute == null) return;

        double restitution = bouncinessAttribute.getValue();

        if (restitution <= 0.0) return;

        if (entity.isShiftKeyDown()) {
            return;
        }

        Vec3 currentMovement = this.movementBeforeCollision;

        boolean xCollision = entity.horizontalCollision && !Mth.equal(delta.x, entity.getX() - this.positionBeforeMove.x);

        boolean zCollision =
                entity.horizontalCollision
                        && !Mth.equal(delta.z, entity.getZ() - this.positionBeforeMove.z);

        Vec3 movementAfterBounce = entity.getDeltaMovement();
        boolean bounced = false;

        if (xCollision) {
            movementAfterBounce = movementAfterBounce.with(Direction.Axis.X, -currentMovement.x * restitution);
            bounced = true;
        }
        if (zCollision) {
            movementAfterBounce = movementAfterBounce.with(Direction.Axis.Z, -currentMovement.z * restitution);
            bounced = true;
        }

        if (entity.verticalCollision) {
            double verticalRestitution = restitution;

            if (entity.verticalCollisionBelow) {
                BlockPos effectPos = entity.getOnPosLegacy();
                BlockState effectState = entity.level().getBlockState(effectPos);

                if (-currentMovement.y < entity.getGravity() || effectState.is(DBBlockTags.SUPPRESSES_BOUNCE)) {
                    verticalRestitution = 0.0;
                }
            }

            if (verticalRestitution > 0.0) {

                double actualYMovement = entity.getY() - this.positionBeforeMove.y;

                double portionWithMovement = currentMovement.y != 0.0 ? actualYMovement / currentMovement.y : 0.0;

                portionWithMovement = Mth.clamp(portionWithMovement, 0.0, 1.0);

                double gravityCompensation = portionWithMovement * entity.getGravity();

                float airDragModifier = 1.0F;
                AttributeInstance airDragAttribute = entity.getAttribute(DBAttributes.AIR_DRAG_MODIFIER);

                if (airDragAttribute != null) {
                    airDragModifier = (float) airDragAttribute.getValue();
                }

                float baseVerticalDrag = entity instanceof SulfurCube cube && cube.hasBodyItem() ? 0.91F : 0.98F;

                float airDrag = computeModifiedFriction(baseVerticalDrag, airDragModifier);

                double effectiveDrag = Mth.lerp(portionWithMovement, 1.0, airDrag);

                double bouncedY = (gravityCompensation - currentMovement.y) * effectiveDrag * verticalRestitution;

                double existingY = movementAfterBounce.y;

                if (Math.abs(bouncedY) > Math.abs(existingY)) {
                    movementAfterBounce = movementAfterBounce.with(Direction.Axis.Y, bouncedY);
                }

                bounced = true;
            }
        }

        if (bounced) {
            entity.setDeltaMovement(movementAfterBounce);
            entity.needsSync = true;
            entity.gameEvent(DBGameEvents.BOUNCE);
        }
    }

    @Unique
    private static float computeModifiedFriction(float friction, float modifier) {
        return Mth.clamp(1.0F - (1.0F - friction) * modifier, 0.0F, 1.0F);
    }
}