package net.qwryzu.avicraft.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.Component;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class WarblerEntity extends AnimalEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flyingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public WarblerEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.15D));

        // Temp goal
        this.goalSelector.add(2, new TemptGoal(this, 1.25D, stack -> stack.isOf(Items.WHEAT_SEEDS), false));

        // Perching goals
        this.goalSelector.add(3, new FlyOntoFenceGoal(this, 1D));
        //this.goalSelector.add(3, new FlyOntoTreeGoal(this, 1D));

        // Fallback, low priority goals
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 18)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2)
                .add(EntityAttributes.FLYING_SPEED, 0.6F)
                .add(EntityAttributes.ATTACK_DAMAGE, 1)
                .add(EntityAttributes.TEMPT_RANGE, 10);
    }

    public abstract Identifier getTextureLocation();

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanOpenDoors(false);
        birdNavigation.setCanSwim(true);
        return birdNavigation;
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
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.WHEAT_SEEDS);
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    public boolean isInAir() {
        return !this.isOnGround();
    }

    protected boolean canTeleportOntoLeaves() {
        return true;
    }

    static class FlyOntoFenceGoal extends FlyGoal {
        private int perchTime;
        private static final int MIN_PERCH_TIME = 100; // 5 seconds at 20 TPS
        private boolean isPerching = false;
        private Vec3d targetPos = null;
        private boolean isSnapping = false;

        public FlyOntoFenceGoal(PathAwareEntity pathAwareEntity, double d) {
            super(pathAwareEntity, d);
        }

        @Override
        public boolean shouldContinue() {
            // Don't allow the goal to end if we haven't perched for minimum time
            if (this.isPerching && this.perchTime < MIN_PERCH_TIME) {
                return true;
            }
            return super.shouldContinue();
        }

        @Override
        public void tick() {
            // Don't call super.tick() if we're in snapping mode
            if (!isSnapping) {
                super.tick();
            }

            // Check if bird is hovering over a fence and ready to snap
            if (!isSnapping && targetPos != null) {
                Vec3d mobPos = this.mob.getPos();
                double horizontalDist = Math.sqrt(
                        Math.pow(mobPos.x - targetPos.x, 2) +
                                Math.pow(mobPos.z - targetPos.z, 2)
                );

                // If close horizontally and above the target
                if (horizontalDist < 0.6 && mobPos.y > targetPos.y + 0.7) { //&&
                        //this.mob.getVelocity().lengthSquared() < 0.5) {

                    // Start snapping - disable pathfinding
                    isSnapping = true;

                    this.mob.getNavigation().stop();
                }
            }

            // Handle the snapping process
            if (isSnapping && targetPos != null) {
                Vec3d mobPos = this.mob.getPos();

                // Check if we're still above a fence at our target position
                BlockPos fencePos = new BlockPos((int)targetPos.x, (int)(targetPos.y - 1), (int)targetPos.z);
                BlockState fenceState = this.mob.getWorld().getBlockState(fencePos);

                if (fenceState.getBlock() instanceof FenceBlock) {
                    // Smoothly move to exact target position
                    Vec3d direction = targetPos.subtract(mobPos);
                    double distance = direction.length();

                    if (distance > 0.8) {
                        // Move towards target
                        direction = direction.normalize().multiply(0.15);
                        this.mob.setVelocity(direction.x, direction.y, direction.z);
                    } else {
                        // Close enough - snap to exact position
                        this.mob.setPosition(targetPos.x, targetPos.y, targetPos.z);
                        this.mob.setVelocity(0, 0, 0);

                        // We've landed - start perching
                        isSnapping = false;
                        isPerching = true;
                        perchTime = 0;
                        targetPos = null;
                    }
                } else {
                    // Fence is gone, abort snapping
                    isSnapping = false;
                    targetPos = null;
                }
            }

            // Regular perching logic (only when not snapping)
            if (!isSnapping && (this.mob.getNavigation().isIdle() || this.mob.getVelocity().lengthSquared() < 0.01)) {
                if (!this.isPerching) {
                    this.isPerching = true;
                    this.perchTime = 0;
                } else {
                    this.perchTime++;
                }
            }
        }


        @Nullable
        @Override
        protected Vec3d getWanderTarget() {
            Vec3d vec3d = null;
            if (this.mob.isTouchingWater()) {
                vec3d = FuzzyTargeting.find(this.mob, 15, 15);
            }
            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3d = this.locateFenceSegment();
            }
            return vec3d == null ? super.getWanderTarget() : vec3d;
        }

        @Nullable
        private Vec3d locateFenceSegment() {
            BlockPos blockPos = this.mob.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (BlockPos blockPos2 : BlockPos.iterate(
                    MathHelper.floor(this.mob.getX() - 8.0),
                    MathHelper.floor(this.mob.getY() - 4.0),
                    MathHelper.floor(this.mob.getZ() - 8.0),
                    MathHelper.floor(this.mob.getX() + 8.0),
                    MathHelper.floor(this.mob.getY() + 4.0),
                    MathHelper.floor(this.mob.getZ() + 8.0)
            )) {
                if (!blockPos.equals(blockPos2)) {
                    BlockState blockState = this.mob.getWorld().getBlockState(blockPos2);

                    if (blockState.getBlock() instanceof FenceBlock &&
                            this.mob.getWorld().isAir(mutable.set(blockPos2, Direction.UP))) {

                        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

                        for (Direction dir : directions) {
                            BlockPos adjacentPos = blockPos2.offset(dir);
                            BlockState adjacentState = this.mob.getWorld().getBlockState(adjacentPos);

                            if (adjacentState.getBlock() instanceof FenceBlock &&
                                    this.mob.getWorld().isAir(mutable.set(adjacentPos, Direction.UP))) {

                                if (canFencesConnect(blockState, adjacentState, blockPos2, adjacentPos, dir)) {
                                    // Calculate precise midpoint for X-Z coordinates
                                    double midX = (blockPos2.getX() + adjacentPos.getX()) / 2.0 - 0.5; // +0.5 for block center
                                    double midZ = (blockPos2.getZ() + adjacentPos.getZ()) / 2.0 - 0.5;
                                    double landingY = blockPos2.getY() + 1.0; // On top of fence

                                    // Store the exact landing position
                                    this.targetPos = new Vec3d(midX, landingY, midZ);

                                    // Reset states
                                    this.isPerching = false;
                                    this.perchTime = 0;
                                    this.isSnapping = false;

                                    // Return position above the fence for initial pathfinding
                                    return new Vec3d(midX, landingY + 2.0, midZ);
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }

        /**
         * Check if two fence blocks can connect to each other
         * This mimics Minecraft's fence connection logic
         */
        private boolean canFencesConnect(BlockState state1, BlockState state2, BlockPos pos1, BlockPos pos2, Direction direction) {
            // Basic check - both are fences
            if (!(state1.getBlock() instanceof FenceBlock) || !(state2.getBlock() instanceof FenceBlock)) {
                return false;
            }

            // Check if they're the same type of fence (optional - remove if you want different fence types to connect)
            if (!state1.getBlock().equals(state2.getBlock())) {
                return false;
            }

            // Ensure they're at the same Y level
            if (pos1.getY() != pos2.getY()) {
                return false;
            }

            // Check that there are no blocks blocking the connection path
            // (This is a simplified check - Minecraft's logic is more complex)
            return true;
        }
    }

    static class FlyOntoTreeGoal extends FlyGoal {
        public FlyOntoTreeGoal(PathAwareEntity pathAwareEntity, double d) {
            super(pathAwareEntity, d);
        }

        @Nullable
        @Override
        protected Vec3d getWanderTarget() {
            Vec3d vec3d = null;
            if (this.mob.isTouchingWater()) {
                vec3d = FuzzyTargeting.find(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3d = this.locateTree();
            }

            return vec3d == null ? super.getWanderTarget() : vec3d;
        }

        @Nullable
        private Vec3d locateTree() {
            BlockPos blockPos = this.mob.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            BlockPos.Mutable mutable2 = new BlockPos.Mutable();

            for (BlockPos blockPos2 : BlockPos.iterate(
                    MathHelper.floor(this.mob.getX() - 3.0),
                    MathHelper.floor(this.mob.getY() - 6.0),
                    MathHelper.floor(this.mob.getZ() - 3.0),
                    MathHelper.floor(this.mob.getX() + 3.0),
                    MathHelper.floor(this.mob.getY() + 6.0),
                    MathHelper.floor(this.mob.getZ() + 3.0)
            )) {
                if (!blockPos.equals(blockPos2)) {
                    BlockState blockState = this.mob.getWorld().getBlockState(mutable2.set(blockPos2, Direction.DOWN));
                    boolean bl = blockState.getBlock() instanceof LeavesBlock || blockState.isIn(BlockTags.LOGS);
                    if (bl && this.mob.getWorld().isAir(blockPos2) && this.mob.getWorld().isAir(mutable.set(blockPos2, Direction.UP))) {
                        return Vec3d.ofBottomCenter(blockPos2);
                    }
                }
            }

            return null;
        }
    }
}
