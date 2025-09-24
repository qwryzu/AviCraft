
package net.qwryzu.avicraft.ai;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

public class LargeBirdFlightMoveControl extends MoveControl {
    private final int maxPitchChange;
    private final boolean noGravity;
    private final float horizontalSpeedMultiplier;
    private final float verticalSpeedMultiplier;
    private final float minVerticalSpeed;
    private final float maxVerticalSpeed;

    // Smoothing for more realistic flight
    private float currentUpwardSpeed = 0.0F;
    private final float verticalAcceleration = 0.2F;

    public LargeBirdFlightMoveControl(MobEntity entity, int maxPitchChange, boolean noGravity) {
        this(entity, maxPitchChange, noGravity, 1.5F, 0.3F, 0.05F, 0.8F);
    }

    public LargeBirdFlightMoveControl(MobEntity entity, int maxPitchChange, boolean noGravity,
                                      float horizontalSpeedMultiplier, float verticalSpeedMultiplier,
                                      float minVerticalSpeed, float maxVerticalSpeed) {
        super(entity);
        this.maxPitchChange = maxPitchChange;
        this.noGravity = noGravity;
        this.horizontalSpeedMultiplier = horizontalSpeedMultiplier;
        this.verticalSpeedMultiplier = verticalSpeedMultiplier;
        this.minVerticalSpeed = minVerticalSpeed;
        this.maxVerticalSpeed = maxVerticalSpeed;
    }

    @Override
    public void tick() {
        if (this.state == MoveControl.State.MOVE_TO) {
            this.state = MoveControl.State.WAIT;
            this.entity.setNoGravity(true);

            double deltaX = this.targetX - this.entity.getX();
            double deltaY = this.targetY - this.entity.getY();
            double deltaZ = this.targetZ - this.entity.getZ();
            double totalDistance = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;

            if (totalDistance < 2.5000003E-7F) {
                this.entity.setUpwardSpeed(0.0F);
                this.entity.setForwardSpeed(0.0F);
                this.currentUpwardSpeed = 0.0F;
                return;
            }

            // Calculate yaw (horizontal rotation)
            float targetYaw = (float) (MathHelper.atan2(deltaZ, deltaX) * 180.0F / (float) Math.PI) - 90.0F;
            this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), targetYaw, 90.0F));

            // Get base speed
            float baseSpeed;
            if (this.entity.isOnGround()) {
                baseSpeed = (float) (this.speed * this.entity.getAttributeValue(EntityAttributes.MOVEMENT_SPEED));
            } else {
                baseSpeed = (float) (this.speed * this.entity.getAttributeValue(EntityAttributes.FLYING_SPEED));
            }

            // Set horizontal movement speed (faster for large birds)
            float horizontalSpeed = baseSpeed * this.horizontalSpeedMultiplier;
            this.entity.setMovementSpeed(horizontalSpeed);

            // Calculate horizontal distance for pitch calculation
            double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

            if (Math.abs(deltaY) > 1.0E-5F || Math.abs(horizontalDistance) > 1.0E-5F) {
                // Calculate pitch angle
                float targetPitch = (float) (-(MathHelper.atan2(deltaY, horizontalDistance) * 180.0F / (float) Math.PI));
                this.entity.setPitch(this.wrapDegrees(this.entity.getPitch(), targetPitch, this.maxPitchChange));

                // Smooth vertical movement - more realistic for large birds
                float targetVerticalSpeed = deltaY > 0.0 ?
                        baseSpeed * this.verticalSpeedMultiplier :
                        -baseSpeed * this.verticalSpeedMultiplier;

                // Clamp vertical speed to reasonable limits
                targetVerticalSpeed = MathHelper.clamp(targetVerticalSpeed, -this.maxVerticalSpeed, this.maxVerticalSpeed);

                // Apply minimum vertical speed when moving up/down
                if (Math.abs(deltaY) > 0.5) { // Only apply minimum when there's significant vertical movement
                    if (targetVerticalSpeed > 0 && targetVerticalSpeed < this.minVerticalSpeed) {
                        targetVerticalSpeed = this.minVerticalSpeed;
                    } else if (targetVerticalSpeed < 0 && targetVerticalSpeed > -this.minVerticalSpeed) {
                        targetVerticalSpeed = -this.minVerticalSpeed;
                    }
                }

                // Smooth acceleration towards target vertical speed
                if (Math.abs(targetVerticalSpeed - this.currentUpwardSpeed) < this.verticalAcceleration) {
                    this.currentUpwardSpeed = targetVerticalSpeed;
                } else if (targetVerticalSpeed > this.currentUpwardSpeed) {
                    this.currentUpwardSpeed += this.verticalAcceleration;
                } else {
                    this.currentUpwardSpeed -= this.verticalAcceleration;
                }

                this.entity.setUpwardSpeed(this.currentUpwardSpeed);
            } else {
                // Gradually reduce vertical speed when not moving vertically
                this.currentUpwardSpeed *= 0.9F;
                this.entity.setUpwardSpeed(this.currentUpwardSpeed);
            }
        } else {
            if (!this.noGravity) {
                this.entity.setNoGravity(false);
            }

            this.entity.setUpwardSpeed(0.0F);
            this.entity.setForwardSpeed(0.0F);
        }
    }
}