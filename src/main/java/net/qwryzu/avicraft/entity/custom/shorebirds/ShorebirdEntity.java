package net.qwryzu.avicraft.entity.custom.shorebirds;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.qwryzu.avicraft.ai.LargeBirdFlightMoveControl;
import net.qwryzu.avicraft.ai.goals.WaterFeedingGoal;



public abstract class ShorebirdEntity extends AnimalEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState idleAnimationState2 = new AnimationState();
    public final AnimationState flyingAnimationState = new AnimationState();
    public final AnimationState walkingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    private static final double MOVEMENT_THRESHOLD = 0.02; // Slightly higher threshold
    private static final int IDLE_DELAY = 10; // Ticks to wait before starting idle

    public ShorebirdEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = setFlightControlSpeed();
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
    }

    public abstract LargeBirdFlightMoveControl setFlightControlSpeed();

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 4)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.15F)
                .add(EntityAttributes.FLYING_SPEED, 2.0F)
                .add(EntityAttributes.ATTACK_DAMAGE, 1)
                .add(EntityAttributes.TEMPT_RANGE, 10);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.15D));
        this.goalSelector.add(2, new TemptGoal(this, 1.25D, stack -> stack.isOf(Items.COD), false));
        this.goalSelector.add(3, new WaterFeedingGoal(this, 1.0, 400));
    }

    public boolean isInAir() {
        return !this.isOnGround();
    }

    private boolean isMoving() {
        Vec3d velocity = this.getVelocity();
        // Use horizontal movement for ground movement, total movement for air movement
        if (isInAir()) {
            return velocity.lengthSquared() > MOVEMENT_THRESHOLD * MOVEMENT_THRESHOLD;
        } else {
            // Only consider horizontal movement when on ground
            return (velocity.x * velocity.x + velocity.z * velocity.z) > MOVEMENT_THRESHOLD * MOVEMENT_THRESHOLD;
        }
    }

    private void setupAnimationStates() {
        boolean currentlyMoving = isMoving();

        if (isInAir()) {
            // Flying state - stop ground animations
            this.idleAnimationState.stop();
            this.walkingAnimationState.stop();
            this.idleAnimationTimeout = 0; // Reset idle timeout

            if (currentlyMoving) {
                // Flying and moving
                if (!this.flyingAnimationState.isRunning()) {
                    this.flyingAnimationState.startIfNotRunning(this.age);
                }
            } else {
                // Flying but not moving (hovering) - could still use flying animation or add hover animation
                if (!this.flyingAnimationState.isRunning()) {
                    this.flyingAnimationState.startIfNotRunning(this.age);
                }
            }
        } else {
            // On ground - stop flying animation
            this.flyingAnimationState.stop();

            if (currentlyMoving) {
                // Moving on ground - use walking animation
                this.idleAnimationState.stop();
                this.idleAnimationTimeout = IDLE_DELAY; // Reset idle timeout with delay

                if (!this.walkingAnimationState.isRunning()) {
                    this.walkingAnimationState.startIfNotRunning(this.age);
                }
            } else {
                // Not moving on ground
                this.walkingAnimationState.stop();

                // Only start idle animation after a short delay and if we were previously moving
                // or if enough time has passed
                if (this.idleAnimationTimeout > 0) {
                    this.idleAnimationTimeout--;
                } else {
                    if (!this.idleAnimationState.isRunning()) {
                        this.idleAnimationState.start(this.age);
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        }
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    public static boolean canShorebirdSpawn(EntityType<? extends AnimalEntity> type,
                                            WorldAccess world,
                                            SpawnReason spawnReason,
                                            BlockPos pos,
                                            Random random) {

        // Custom logic for desert spawning
        BlockState blockBelow = world.getBlockState(pos.down());

        // Allow spawning on sand, sandstone, and similar desert blocks
        return (blockBelow.isOf(Blocks.SAND) ||
                blockBelow.isOf(Blocks.GRAVEL) ||
                blockBelow.isOf(Blocks.GRASS_BLOCK) ||
                blockBelow.isOf(Blocks.MUD) ||
                blockBelow.isIn(BlockTags.DIRT)) &&
                world.getBaseLightLevel(pos, 0) > 8; // Ensure it's not too dark
    }
}
