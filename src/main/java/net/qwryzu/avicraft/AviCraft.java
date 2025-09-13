package net.qwryzu.avicraft;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.qwryzu.avicraft.entity.AviCraftEntities;
import net.qwryzu.avicraft.entity.custom.*;
import net.qwryzu.avicraft.item.AviCraftItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AviCraft implements ModInitializer {
	public static final String MOD_ID = "avicraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        AviCraftItems.registerModItems();

        AviCraftEntities.registerModEntities();

        FabricDefaultAttributeRegistry.register(AviCraftEntities.AUDUBONSWARBLER, AudubonsWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.BLACKTHROATEDBLUEWARBLER, BlackThroatedBlueWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.MAGNOLIAWARBLER, MagnoliaWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.REDFACEDWARBLER, RedFacedWarblerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(AviCraftEntities.YELLOWWARBLER, YellowWarblerEntity.createAttributes());
	}
}