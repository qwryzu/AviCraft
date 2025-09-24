package net.qwryzu.avicraft.ai.goals;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.qwryzu.avicraft.AviCraft;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Birds will fly in an arc to reach distant water sources, then spend time feeding.
 */
public class WaterFeedingGoal extends Goal {
    private static final int DEFAULT_FEEDING_DURATION = 600; // 10 seconds at water
    private static final double DEFAULT_ARRIVAL_DISTANCE = 1.2; // How close to consider "arrived"
    private static final double DEFAULT_ARC_HEIGHT = 7.0; // How high to fly in the arc
    private static final double DEFAULT_MIN_FLY_DISTANCE = 24.0; // Minimum distance to trigger flight vs walking
    private static final float DEFAULT_START_CHANCE = 0.01F; // Chance per tick to start behavior
    private static final int DEFAULT_SEARCH_RADIUS = 42;

    private final PathAwareEntity mob;
    private final double speed;
    private final int feedingDuration;
    private final double arrivalDistance;
    private final double arcHeight;
    private final double minFlyDistance;
    private final float startChance;
    private final int searchRadius;

    private boolean isActive = false;

    // State tracking
    private enum State {
        IDLE,           // Hanging out, not moving
        FLYING_ARC,     // Flying to elevated waypoint
        FLYING_DOWN,    // Flying from arc to ground target
        FEEDING         // At water, feeding
    }

    private State currentState = State.IDLE;
    private Vec3d groundTarget = null;      // Final destination on ground near water
    private Vec3d arcTarget = null;         // Elevated waypoint for flight arc
    private int stateTimer = 0;             // Generic timer for current state

    // Constructor with default values
    public WaterFeedingGoal(PathAwareEntity mob, double speed) {
        this(mob, speed, DEFAULT_FEEDING_DURATION, DEFAULT_ARRIVAL_DISTANCE,
                DEFAULT_ARC_HEIGHT, DEFAULT_MIN_FLY_DISTANCE, DEFAULT_START_CHANCE, DEFAULT_SEARCH_RADIUS);
    }

    // Constructor with customizable feeding duration
    public WaterFeedingGoal(PathAwareEntity mob, double speed, int feedingDuration) {
        this(mob, speed, feedingDuration, DEFAULT_ARRIVAL_DISTANCE,
                DEFAULT_ARC_HEIGHT, DEFAULT_MIN_FLY_DISTANCE, DEFAULT_START_CHANCE, DEFAULT_SEARCH_RADIUS);
    }

    // Full constructor for maximum customization
    public WaterFeedingGoal(PathAwareEntity mob, double speed, int feedingDuration,
                            double arrivalDistance, double arcHeight, double minFlyDistance,
                            float startChance, int searchRadius) {
        this.mob = mob;
        this.speed = speed;
        this.feedingDuration = feedingDuration;
        this.arrivalDistance = arrivalDistance;
        this.arcHeight = arcHeight;
        this.minFlyDistance = minFlyDistance;
        this.startChance = startChance;
        this.searchRadius = searchRadius;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (isActive) {
            //AviCraft.LOGGER.info("Water feeding goal already active, preventing duplicate start");
            return false;
        }

        // Only start if we're currently idle
        if (currentState != State.IDLE) {
            return false;
        }

        // Random chance to start new behavior
        if (this.mob.getRandom().nextFloat() >= this.startChance) {
            return false;
        }

        //AviCraft.LOGGER.info("Water feeding goal passed basic checks, looking for water destination");

        // Try to find a water spot to visit
        Vec3d target = findWaterSpot();
        if (target != null) {
            this.groundTarget = target;
            // Calculate if we need to fly (distance-based decision)
            double distance = this.mob.getPos().distanceTo(groundTarget);
            AviCraft.LOGGER.info("Aiming for block {} blocks away", distance);

            if (distance > minFlyDistance) {
                //AviCraft.LOGGER.info("Distance to water spot requires flight: {}", distance);
                Vec3d midpoint = this.mob.getPos().add(groundTarget).multiply(0.5);
                this.arcTarget = midpoint.add(0, arcHeight, 0);
                this.currentState = State.FLYING_ARC;
            } else {
                // Close enough to walk/hop directly
                this.arcTarget = null;
                this.currentState = State.FLYING_DOWN; // Skip arc, go straight to target
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldContinue() {
        return currentState != State.IDLE;
    }

    @Override
    public void start() {
        isActive = true;
        //AviCraft.LOGGER.info("Water feeding goal starting");

        // Set initial movement target based on our starting state
        if (currentState == State.FLYING_ARC && arcTarget != null) {
            // Force the bird to recognize it needs to fly by setting noGravity temporarily
            this.mob.setNoGravity(true);
            this.mob.getMoveControl().moveTo(arcTarget.x, arcTarget.y, arcTarget.z, this.speed);
            //AviCraft.LOGGER.info("Starting flight to arc point: {}", arcTarget);
        } else if (currentState == State.FLYING_DOWN && groundTarget != null) {
            this.mob.getMoveControl().moveTo(groundTarget.x, groundTarget.y, groundTarget.z, this.speed);
        }
    }

    @Override
    public void tick() {
        switch (currentState) {
            case FLYING_ARC:
                // Check if we've reached the arc waypoint
                if (this.mob.squaredDistanceTo(arcTarget) < arrivalDistance * arrivalDistance) {
                    // Transition to flying down to ground target
                    AviCraft.LOGGER.info("Reached arc waypoint, transitioning to ground target");
                    currentState = State.FLYING_DOWN;
                    this.mob.getMoveControl().moveTo(groundTarget.x, groundTarget.y, groundTarget.z, this.speed);
                } else {
                    // Keep flying to arc point
                    this.mob.getMoveControl().moveTo(arcTarget.x, arcTarget.y, arcTarget.z, this.speed);
                }
                break;

            case FLYING_DOWN:
                double groundDistance = this.mob.squaredDistanceTo(groundTarget);
                this.mob.setNoGravity(false);
                // Check if we've reached the ground target
                if (groundDistance < arrivalDistance * arrivalDistance) {
                    AviCraft.LOGGER.info("Reached water source - starting feeding");
                    currentState = State.FEEDING;
                    stateTimer = feedingDuration;
                    this.mob.getMoveControl().moveTo(this.mob.getX(), this.mob.getY(), this.mob.getZ(), 0);
                } else {
                    // Keep flying to target
                    this.mob.getMoveControl().moveTo(groundTarget.x, groundTarget.y, groundTarget.z, this.speed);
                }
                break;

            case FEEDING:
                // Re-enable gravity so bird naturally descends to ground
                this.mob.setNoGravity(false);

                // Count down feeding timer
                stateTimer--;
                if (stateTimer <= 0) {
                    currentState = State.IDLE;
                }
                break;

            case IDLE:
                // Keep gravity enabled when idle
                this.mob.setNoGravity(false);
                break;
        }
    }

    @Override
    public void stop() {
        //AviCraft.LOGGER.info("Water feeding goal stopped - was in state: {}", currentState);
        isActive = false;

        // Reset everything to idle state
        this.currentState = State.IDLE;
        this.groundTarget = null;
        this.arcTarget = null;
        this.stateTimer = 0;

        // Ensure gravity is restored
        this.mob.setNoGravity(false);
    }

    @Nullable
    protected Vec3d findWaterSpot() {
        BlockPos mobPos = this.mob.getBlockPos();
        World world = this.mob.getWorld();
        List<BlockPos> candidates = new ArrayList<>();

        // Search in the configured radius
        for (int x = -searchRadius; x <= searchRadius; x++) {
            for (int z = -searchRadius; z <= searchRadius; z++) {
                for (int y = -12; y <= 3; y++) { // Limited Y search for performance
                    BlockPos checkPos = mobPos.add(x, y, z);
                    double xDist = mobPos.getX() - checkPos.getX();
                    double yDist = mobPos.getY() - checkPos.getY();
                    double zDist = mobPos.getZ() - checkPos.getZ();
                    double dist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2) + Math.pow(zDist, 2));
                    if (dist < this.minFlyDistance) {
                        continue;
                    }
                    // Simple check: solid block with air above, next to water
                    if (isValidWaterSpot(checkPos, world) && noNearbyTrees(checkPos, world)) {
                        candidates.add(checkPos);
                    }
                }
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        // Pick random candidate and return position above it (where bird will stand)
        BlockPos chosen = candidates.get(this.mob.getRandom().nextInt(candidates.size()));
        return Vec3d.ofCenter(chosen.up());
    }

    protected boolean isValidWaterSpot(BlockPos pos, World world) {
        // Must be solid block (for bird to land on)
        if (!world.getBlockState(pos).isSolidBlock(world, pos)) {
            return false;
        }

        // Must have air above (for bird to stand)
        if (!world.getBlockState(pos.up()).isAir()) {
            return false;
        }

        // Must be adjacent to water
        BlockPos[] adjacent = {pos.north(), pos.south(), pos.east(), pos.west()};
        for (BlockPos adj : adjacent) {
            if (world.getBlockState(adj).getBlock() == Blocks.WATER) {
                return true; // Found adjacent water, this is good
            }
        }

        return false; // No adjacent water found
    }

    protected boolean noNearbyTrees(BlockPos pos, World world) {
        // We want to remove candidates where there are too many trees and logs nearby
        return true;
    }

    // Getter methods for accessing configuration values
    public State getCurrentState() { return currentState; }
    public boolean isActive() { return isActive; }
    public Vec3d getGroundTarget() { return groundTarget; }
    public Vec3d getArcTarget() { return arcTarget; }
}