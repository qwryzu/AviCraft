package net.qwryzu.avicraft.entity.custom.shorebirds;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.qwryzu.avicraft.AviCraft;
import net.qwryzu.avicraft.entity.AviCraftEntities;
import org.jetbrains.annotations.Nullable;

public class LongBilledCurlewEntity extends ShorebirdEntity {
    private static final Identifier TEXTURE = Identifier.of(AviCraft.MOD_ID, "/textures/entity/shorebirds/longbilledcurlew.png");

    public LongBilledCurlewEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public Identifier getTextureLocation() {
        return TEXTURE;
    }

    @Nullable
    @Override
    public LongBilledCurlewEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return AviCraftEntities.LONGBILLEDCURLEW.create(serverWorld, SpawnReason.BREEDING);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.COD);
    }


}
