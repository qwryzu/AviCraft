package net.qwryzu.avicraft.entity.custom.shorebirds;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ShorebirdEntity extends AnimalEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flyingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public ShorebirdEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 4)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2)
                .add(EntityAttributes.FLYING_SPEED, 1.3F)
                .add(EntityAttributes.ATTACK_DAMAGE, 1)
                .add(EntityAttributes.TEMPT_RANGE, 10);
    }

    public boolean isInAir() {
        return !this.isOnGround();
    }

    private void setupAnimationStates() {
        // Check if the entity is on the ground
        if (isInAir()) {
            this.idleAnimationState.stop();

            if (!this.flyingAnimationState.isRunning()) {
                this.flyingAnimationState.startIfNotRunning(this.age);
            }
            // Reset idle timeout when it lands
        } else {
            this.flyingAnimationState.stop();

            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = 60;
                this.idleAnimationState.start(this.age);
            } else {
                --this.idleAnimationTimeout;
            }
        }
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }
}
