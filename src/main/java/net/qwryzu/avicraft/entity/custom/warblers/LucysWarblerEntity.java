package net.qwryzu.avicraft.entity.custom.warblers;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.qwryzu.avicraft.AviCraft;
import net.qwryzu.avicraft.entity.AviCraftEntities;
import net.qwryzu.avicraft.sound.AviCraftSounds;
import org.jetbrains.annotations.Nullable;

public class LucysWarblerEntity extends WarblerEntity {
    private static final Identifier TEXTURE = Identifier.of(AviCraft.MOD_ID, "/textures/entity/warblers/lucyswarbler.png");

    public LucysWarblerEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public Identifier getTextureLocation() {
        return TEXTURE;
    }

    @Nullable
    @Override
    public LucysWarblerEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return AviCraftEntities.LUCYSWARBLER.create(serverWorld, SpawnReason.BREEDING);
    }

    protected SoundEvent getWarblerSong() {
        return null;
    }

    public static boolean canLucysWarblerSpawn(EntityType<? extends AnimalEntity> type,
                                               WorldAccess world,
                                               SpawnReason spawnReason,
                                               BlockPos pos,
                                               Random random) {
        // Custom logic for desert spawning
        BlockState blockBelow = world.getBlockState(pos.down());

        // Allow spawning on sand, sandstone, and similar desert blocks
        return (blockBelow.isOf(Blocks.SAND) ||
                blockBelow.isOf(Blocks.RED_SAND) ||
                blockBelow.isOf(Blocks.SANDSTONE) ||
                blockBelow.isOf(Blocks.RED_SANDSTONE) ||
                blockBelow.isIn(BlockTags.TERRACOTTA)) &&
                world.getBaseLightLevel(pos, 0) > 8; // Ensure it's not too dark
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.15D));

        // Tempt goal
        this.goalSelector.add(3, new TemptGoal(this, 1.25D, stack -> stack.isOf(Items.WHEAT_SEEDS), false));

        // Perching goals
        this.goalSelector.add(4, new FlyOntoCactusGoal(this, 1D));

        // Fallback, low priority goals
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    static class FlyOntoCactusGoal extends FlyGoal {
        public FlyOntoCactusGoal(PathAwareEntity pathAwareEntity, double d) {
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
                vec3d = this.locateCactus();
                if (vec3d != null) {
                    AviCraft.LOGGER.info("Bird trying to target cactus at {} {} {}", vec3d.getX(), vec3d.getY(), vec3d.getZ());
                }
            }

            return vec3d == null ? super.getWanderTarget() : vec3d;
        }

        @Nullable
        private Vec3d locateCactus() {
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
                    boolean bl = blockState.getBlock() instanceof CactusBlock;
                    if (bl && this.mob.getWorld().isAir(blockPos2) && this.mob.getWorld().isAir(mutable.set(blockPos2, Direction.UP))) {
                        return Vec3d.ofBottomCenter(blockPos2);
                    }
                }
            }

            return null;
        }
    }
}
