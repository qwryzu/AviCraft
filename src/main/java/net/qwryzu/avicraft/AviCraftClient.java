package net.qwryzu.avicraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.qwryzu.avicraft.entity.AviCraftEntities;
import net.qwryzu.avicraft.entity.client.WarblerModel;
import net.qwryzu.avicraft.entity.client.WarblerRenderer;

public class AviCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register the shared warbler model layer
        EntityModelLayerRegistry.registerModelLayer(WarblerModel.WARBLER_LAYER, WarblerModel::getTexturedModelData);

        // Register the generic warbler renderer for each warbler species
        EntityRendererRegistry.register(AviCraftEntities.AUDUBONSWARBLER, WarblerRenderer::new);
        EntityRendererRegistry.register(AviCraftEntities.BLACKTHROATEDBLUEWARBLER, WarblerRenderer::new);
        EntityRendererRegistry.register(AviCraftEntities.MAGNOLIAWARBLER, WarblerRenderer::new);
        EntityRendererRegistry.register(AviCraftEntities.REDFACEDWARBLER, WarblerRenderer::new);
        EntityRendererRegistry.register(AviCraftEntities.YELLOWWARBLER, WarblerRenderer::new);

    }
}
