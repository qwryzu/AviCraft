package net.qwryzu.avicraft.entity.custom.warblers;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.qwryzu.avicraft.AviCraft;
import net.qwryzu.avicraft.entity.AviCraftEntities;
import net.qwryzu.avicraft.sound.AviCraftSounds;
import org.jetbrains.annotations.Nullable;

public class AmericanRedstartEntity extends WarblerEntity {
    private static final Identifier TEXTURE = Identifier.of(AviCraft.MOD_ID, "textures/entity/americanredstart/americanredstart.png");

    public AmericanRedstartEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public Identifier getTextureLocation() {
        return TEXTURE;
    }

    @Nullable
    @Override
    public AmericanRedstartEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return AviCraftEntities.AMERICANREDSTART.create(serverWorld, SpawnReason.BREEDING);
    }

    protected SoundEvent getWarblerSong() {
        return AviCraftSounds.AMERICAN_REDSTART_SONG;
    }

}
