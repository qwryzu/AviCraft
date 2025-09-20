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
import net.qwryzu.avicraft.AviCraft;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ShorebirdEntity extends AnimalEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState idleAnimationState2 = new AnimationState();
    public final AnimationState flyingAnimationState = new AnimationState();
    public final AnimationState walkingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int movingCoolDown = 0;

    private boolean wasMoving = false;
    private static final double MOVEMENT_THRESHOLD = 0.02; // Slightly higher threshold
    private static final int IDLE_DELAY = 10; // Ticks to wait before starting idle

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
                .add(EntityAttributes.MOVEMENT_SPEED, 0.15F)
                .add(EntityAttributes.FLYING_SPEED, 0.6F)
                .add(EntityAttributes.ATTACK_DAMAGE, 1)
                .add(EntityAttributes.TEMPT_RANGE, 10);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.15D));
        this.goalSelector.add(2, new TemptGoal(this, 1.25D, stack -> stack.isOf(Items.COD), false));
        //this.goalSelector.add(2, new largeShorebirdFeedingWanderGoal(this, 1.0));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
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

        // Update movement tracking
        this.wasMoving = currentlyMoving;
    }

    @Override
    public void tick() {
        super.tick();
        this.movingCoolDown--;
        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        }
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    public class largeShorebirdFeedingWanderGoal extends WanderAroundFarGoal {
        public largeShorebirdFeedingWanderGoal(PathAwareEntity pathAwareEntity, double d) {
            super(pathAwareEntity, d);
        }

        protected Vec3d getWanderTarget() {
            Vec3d vec3d = null;
            if (movingCoolDown > 0) {
                // Even if we request a render target, return null if not enough time has passed since cooldown
                return null;
            }
            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3d = this.locateWaterAdjacentBlock();
                if (vec3d != null) {
                    AviCraft.LOGGER.info("Heading to Destination at {} {} {}",
                            vec3d.getX(),
                            vec3d.getY(),
                            vec3d.getZ());
                    movingCoolDown = 120;
                }
            }
            return vec3d == null ? super.getWanderTarget() : vec3d;
        }

        @Nullable
        private Vec3d locateWaterAdjacentBlock() {
            BlockPos mobPos = this.mob.getBlockPos();
            World world = this.mob.getWorld();
            List<BlockPos> validPositions = new ArrayList<>();

            // Search in a radius around the mob (starting from 16 blocks away)
            int searchRadius = 32; // Maximum search distance
            int minDistance = 16;  // Minimum distance from mob

            // Search in a cube around the mob
            for (int x = -searchRadius; x <= searchRadius; x++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    for (int y = -searchRadius; y <= searchRadius; y++) {
                        BlockPos checkPos = mobPos.add(x, y, z);

                        // Skip if too close to mob
                        double distance = Math.sqrt(x * x + y * y + z * z);
                        if (distance < minDistance) {
                            continue;
                        }

                        // Check if this position meets our criteria
                        if (isValidWaterAdjacentPosition(checkPos, world)) {
                            validPositions.add(checkPos);
                        }
                    }
                }
            }

            // Return random valid position, or null if none found
            if (validPositions.isEmpty()) {
                return null;
            }

            BlockPos selectedPos = validPositions.get(this.mob.getRandom().nextInt(validPositions.size()));
            return Vec3d.ofCenter(selectedPos.up());
        }

        private boolean isValidWaterAdjacentPosition(BlockPos pos, World world) {
            // Check if the position itself is not water
            if (world.getBlockState(pos).getBlock() == Blocks.WATER) {
                return false;
            }

            // Check if the position is a solid, non-water block
            if (!world.getBlockState(pos).isSolidBlock(world, pos)) {
                return false;
            }

            // Find adjacent water blocks
            List<BlockPos> adjacentWaterBlocks = new ArrayList<>();

            // Check all 8 horizontal directions and 2 vertical directions (10 total adjacent positions)
            BlockPos[] adjacentPositions = {
                    pos.north(),     // North
                    pos.south(),     // South
                    pos.east(),      // East
                    pos.west(),      // West
                    pos.up(),        // Up
                    pos.down(),      // Down
                    pos.north().east(),  // Northeast
                    pos.north().west(),  // Northwest
                    pos.south().east(),  // Southeast
                    pos.south().west()   // Southwest
            };

            for (BlockPos adjacentPos : adjacentPositions) {
                if (world.getBlockState(adjacentPos).getBlock() == Blocks.WATER) {
                    adjacentWaterBlocks.add(adjacentPos);
                }
            }

            // Must have at least one adjacent water block to be "water-adjacent"
            if (adjacentWaterBlocks.isEmpty()) {
                return false;
            }

            // Check each adjacent water block to see if it meets our criteria
            for (BlockPos waterPos : adjacentWaterBlocks) {
                if (isValidWaterBlock(waterPos, world)) {
                    return true; // Found at least one valid water block adjacent
                }
            }

            return false;
        }

        private boolean isValidWaterBlock(BlockPos waterPos, World world) {
            // Check if this water block has another water block directly below it
            // If yes, it's not "only 1 deep" so it's invalid
            if (world.getBlockState(waterPos.down()).getBlock() == Blocks.WATER) {
                return false;
            }

            // If the block above isn't air, that's wrong
            if (world.getBlockState(waterPos.up()).getBlock() != Blocks.AIR) {
                return false;
            }

            // Count adjacent water blocks (including diagonals)
            int adjacentWaterCount = 0;

            // Check all 8 horizontal directions plus up and down
            BlockPos[] adjacentPositions = {
                    waterPos.north(),
                    waterPos.south(),
                    waterPos.east(),
                    waterPos.west(),
                    waterPos.up(),
                    waterPos.down(),
                    waterPos.north().east(),
                    waterPos.north().west(),
                    waterPos.south().east(),
                    waterPos.south().west()
            };

            for (BlockPos adjacentPos : adjacentPositions) {
                if (world.getBlockState(adjacentPos).getBlock() == Blocks.WATER) {
                    adjacentWaterCount++;
                }
            }

            // Must have at least 3 adjacent water blocks
            return adjacentWaterCount >= 3;
        }
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
