package net.qwryzu.avicraft.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import net.qwryzu.avicraft.entity.AviCraftEntities;

public class AviCraftEntitySpawns {
    public static void addSpawns() {
        // Audubon's Warbler
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.MEADOW,
                        BiomeKeys.GROVE,
                        BiomeKeys.SNOWY_SLOPES,
                        BiomeKeys.JAGGED_PEAKS,
                        BiomeKeys.FROZEN_PEAKS,
                        BiomeKeys.STONY_PEAKS
                        ),
                SpawnGroup.CREATURE, AviCraftEntities.AUDUBONSWARBLER, 35, 2, 6);

        SpawnRestriction.register(AviCraftEntities.AUDUBONSWARBLER, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);

        // Red-faced Warbler
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.TAIGA,
                        BiomeKeys.WOODED_BADLANDS,
                        BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA
                        ),
                SpawnGroup.CREATURE, AviCraftEntities.REDFACEDWARBLER, 30, 1, 2);

        SpawnRestriction.register(AviCraftEntities.REDFACEDWARBLER, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);

        // Yellow Warbler
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.SWAMP
                        ),
                SpawnGroup.CREATURE, AviCraftEntities.YELLOWWARBLER, 30, 2, 6);

        SpawnRestriction.register(AviCraftEntities.YELLOWWARBLER, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);

        // Magnolia Warbler
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.FOREST,
                        BiomeKeys.BIRCH_FOREST,
                        BiomeKeys.JUNGLE,
                        BiomeKeys.PLAINS,
                        BiomeKeys.SUNFLOWER_PLAINS
                        ),
                SpawnGroup.CREATURE, AviCraftEntities.MAGNOLIAWARBLER, 40, 2, 6);

        SpawnRestriction.register(AviCraftEntities.MAGNOLIAWARBLER, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);

        // Black-throated Blue Warbler
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.SPARSE_JUNGLE,
                        BiomeKeys.JUNGLE
                        ),
                SpawnGroup.CREATURE, AviCraftEntities.BLACKTHROATEDBLUEWARBLER, 30, 1, 2);

        SpawnRestriction.register(AviCraftEntities.BLACKTHROATEDBLUEWARBLER, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);

        // Mangrove Warbler
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.MANGROVE_SWAMP
                ),
                SpawnGroup.CREATURE, AviCraftEntities.MANGROVEWARBLER, 30, 2, 4);

        SpawnRestriction.register(AviCraftEntities.MANGROVEWARBLER, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);

    }
}
