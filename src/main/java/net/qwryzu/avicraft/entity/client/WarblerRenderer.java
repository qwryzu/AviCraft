package net.qwryzu.avicraft.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.qwryzu.avicraft.entity.custom.warblers.WarblerEntity;
import net.qwryzu.avicraft.render.entity.state.WarblerEntityRenderState;

@Environment(EnvType.CLIENT)
public class WarblerRenderer<T extends WarblerEntity> extends MobEntityRenderer<T, WarblerEntityRenderState, WarblerModel> {

    public WarblerRenderer(EntityRendererFactory.Context context) {
        super(context, createModel(context), 0.25f);
    }

    private static WarblerModel createModel(EntityRendererFactory.Context context) {
        try {
            ModelPart modelPart = context.getPart(WarblerModel.WARBLER_LAYER);
            WarblerModel model = new WarblerModel(modelPart);
            return model;
        } catch (Exception e) {
            System.err.println("Error type: " + e.getClass().getSimpleName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Identifier getTexture(WarblerEntityRenderState warblerEntityRenderState) {
        // This will be overridden to get texture from entity in updateRenderState
        return warblerEntityRenderState.texture;
    }

    public WarblerEntityRenderState createRenderState() {
        return new WarblerEntityRenderState();
    }

    public void updateRenderState(T warblerEntity, WarblerEntityRenderState warblerEntityRenderState, float f) {
        super.updateRenderState(warblerEntity, warblerEntityRenderState, f);

        // Store the texture from the entity in the render state
        warblerEntityRenderState.texture = warblerEntity.getTextureLocation();

        // Update animation states
        warblerEntityRenderState.idleAnimationState.copyFrom(warblerEntity.idleAnimationState);
        warblerEntityRenderState.flyingAnimationState.copyFrom(warblerEntity.flyingAnimationState);
    }
}