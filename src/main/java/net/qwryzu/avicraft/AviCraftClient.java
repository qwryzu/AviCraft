package net.qwryzu.avicraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.qwryzu.avicraft.entity.AviCraftEntities;
import net.qwryzu.avicraft.entity.client.LongBilledCurlewModel;
import net.qwryzu.avicraft.entity.client.LongBilledCurlewRenderer;
import net.qwryzu.avicraft.entity.client.WarblerModel;
import net.qwryzu.avicraft.entity.client.WarblerRenderer;

import javax.swing.text.html.parser.Entity;

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
        EntityRendererRegistry.register(AviCraftEntities.MANGROVEWARBLER, WarblerRenderer::new);
        EntityRendererRegistry.register(AviCraftEntities.MYRTLEWARBLER, WarblerRenderer::new);
        EntityRendererRegistry.register(AviCraftEntities.AMERICANREDSTART, WarblerRenderer::new);
        EntityRendererRegistry.register(AviCraftEntities.PAINTEDREDSTART, WarblerRenderer::new);

        // Register the long-billed curlew model
        EntityModelLayerRegistry.registerModelLayer(LongBilledCurlewModel.LONG_BILLED_CURLEW_LAYER, LongBilledCurlewModel::getTexturedModelData);
        EntityRendererRegistry.register(AviCraftEntities.LONGBILLEDCURLEW, LongBilledCurlewRenderer::new);
    }
}
