package net.qwryzu.avicraft.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.qwryzu.avicraft.AviCraft;
import net.qwryzu.avicraft.entity.custom.shorebirds.LongBilledCurlewEntity;
import net.qwryzu.avicraft.entity.custom.warblers.*;

public class AviCraftEntities {
    public static final EntityType<AudubonsWarblerEntity> AUDUBONSWARBLER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AviCraft.MOD_ID, "audubons_warbler"),
            EntityType.Builder.create((EntityType<AudubonsWarblerEntity> entityType, World world) -> new AudubonsWarblerEntity(entityType, world), SpawnGroup.CREATURE)
                    .dimensions(0.6f, 0.3f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(AviCraft.MOD_ID, "audubons_warbler"))));

    public static final EntityType<BlackThroatedBlueWarblerEntity> BLACKTHROATEDBLUEWARBLER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AviCraft.MOD_ID, "black_throated_blue_warbler"),
            EntityType.Builder.create((EntityType<BlackThroatedBlueWarblerEntity> entityType, World world) -> new BlackThroatedBlueWarblerEntity(entityType, world), SpawnGroup.CREATURE)
                    .dimensions(1.0f, 0.6f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(AviCraft.MOD_ID, "black_throated_blue_warbler"))));

    public static final EntityType<MagnoliaWarblerEntity> MAGNOLIAWARBLER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AviCraft.MOD_ID, "magnolia_warbler"),
            EntityType.Builder.create((EntityType<MagnoliaWarblerEntity> entityType, World world) -> new MagnoliaWarblerEntity(entityType, world), SpawnGroup.CREATURE)
                    .dimensions(1.0f, 0.6f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(AviCraft.MOD_ID, "magnolia_warbler"))));

    public static final EntityType<RedFacedWarblerEntity> REDFACEDWARBLER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AviCraft.MOD_ID, "red_faced_warbler"),
            EntityType.Builder.create((EntityType<RedFacedWarblerEntity> entityType, World world) -> new RedFacedWarblerEntity(entityType, world), SpawnGroup.CREATURE)
                    .dimensions(1.0f, 0.6f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(AviCraft.MOD_ID, "red_faced_warbler"))));

    public static final EntityType<YellowWarblerEntity> YELLOWWARBLER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AviCraft.MOD_ID, "yellow_warbler"),
            EntityType.Builder.create((EntityType<YellowWarblerEntity> entityType, World world) -> new YellowWarblerEntity(entityType, world), SpawnGroup.CREATURE)
                    .dimensions(1.0f, 0.6f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(AviCraft.MOD_ID, "yellow_warbler"))));

    public static final EntityType<MangroveWarblerEntity> MANGROVEWARBLER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AviCraft.MOD_ID, "mangrove_warbler"),
            EntityType.Builder.create((EntityType<MangroveWarblerEntity> entityType, World world) -> new MangroveWarblerEntity(entityType, world), SpawnGroup.CREATURE)
                    .dimensions(1.0f, 0.6f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(AviCraft.MOD_ID, "mangrove_warbler"))));

    public static final EntityType<MyrtleWarblerEntity> MYRTLEWARBLER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AviCraft.MOD_ID, "myrtle_warbler"),
            EntityType.Builder.create((EntityType<MyrtleWarblerEntity> entityType, World world) -> new MyrtleWarblerEntity(entityType, world), SpawnGroup.CREATURE)
                    .dimensions(1.0f, 0.6f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(AviCraft.MOD_ID, "myrtle_warbler"))));

    public static final EntityType<AmericanRedstartEntity> AMERICANREDSTART = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AviCraft.MOD_ID, "american_redstart"),
            EntityType.Builder.create((EntityType<AmericanRedstartEntity> entityType, World world) -> new AmericanRedstartEntity(entityType, world), SpawnGroup.CREATURE)
                    .dimensions(1.0f, 0.6f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(AviCraft.MOD_ID, "american_redstart"))));

    public static final EntityType<PaintedRedstartEntity> PAINTEDREDSTART = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AviCraft.MOD_ID, "painted_redstart"),
            EntityType.Builder.create((EntityType<PaintedRedstartEntity> entityType, World world) -> new PaintedRedstartEntity(entityType, world), SpawnGroup.CREATURE)
                    .dimensions(1.0f, 0.6f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(AviCraft.MOD_ID, "painted_redstart"))));

    public static final EntityType<LongBilledCurlewEntity> LONGBILLEDCURLEW = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(AviCraft.MOD_ID, "long_billed_curlew"),
            EntityType.Builder.create((EntityType<LongBilledCurlewEntity> entityType, World world) -> new LongBilledCurlewEntity(entityType, world), SpawnGroup.CREATURE)
                .dimensions(1.5F, 2F)
                .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(AviCraft.MOD_ID, "long_billed_curlew"))));

    public static void registerModEntities() {
        AviCraft.LOGGER.info("Registering Mod Entities for " + AviCraft.MOD_ID);
    }
}