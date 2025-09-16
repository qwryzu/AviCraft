package net.qwryzu.avicraft;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.item.FlintAndSteelItem;
import net.qwryzu.avicraft.entity.AviCraftEntities;
import net.qwryzu.avicraft.entity.custom.*;
import net.qwryzu.avicraft.item.AviCraftItems;
import net.qwryzu.avicraft.sound.AviCraftSounds;
import net.qwryzu.avicraft.world.gen.AviCraftWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AviCraft implements ModInitializer {
	public static final String MOD_ID = "avicraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        // Register items
        AviCraftItems.registerModItems();
        // Register entities
        AviCraftEntities.registerModEntities();
        // Register world gen
        AviCraftWorldGeneration.generateModWorldGen();
        // Register sounds
        AviCraftSounds.registerSounds();

        FabricDefaultAttributeRegistry.register(AviCraftEntities.AUDUBONSWARBLER, AudubonsWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.BLACKTHROATEDBLUEWARBLER, BlackThroatedBlueWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.MAGNOLIAWARBLER, MagnoliaWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.REDFACEDWARBLER, RedFacedWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.YELLOWWARBLER, YellowWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.MANGROVEWARBLER, MangroveWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.MYRTLESWARBLER, MangroveWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.AMERICANREDSTART, MangroveWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.PAINTEDREDSTART, MangroveWarblerEntity.createAttributes());
	}
}