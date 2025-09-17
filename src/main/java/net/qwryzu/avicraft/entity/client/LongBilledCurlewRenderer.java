package net.qwryzu.avicraft.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.qwryzu.avicraft.entity.custom.shorebirds.LongBilledCurlewEntity;
import net.qwryzu.avicraft.render.entity.state.LongBilledCurlewEntityRenderState;

@Environment(EnvType.CLIENT)
public class LongBilledCurlewRenderer<T extends LongBilledCurlewEntity> extends MobEntityRenderer<T, LongBilledCurlewEntityRenderState, LongBilledCurlewModel> {

    public LongBilledCurlewRenderer(EntityRendererFactory.Context context) {
        super(context, createModel(context), 0.25f);
    }

    private static LongBilledCurlewModel createModel(EntityRendererFactory.Context context) {
        try {
            ModelPart modelPart = context.getPart(LongBilledCurlewModel.LONG_BILLED_CURLEW_LAYER);
            LongBilledCurlewModel model = new LongBilledCurlewModel(modelPart);
            return model;
        } catch (Exception e) {
            System.err.println("Error type: " + e.getClass().getSimpleName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Identifier getTexture(LongBilledCurlewEntityRenderState longBilledCurlewEntityRenderState) {
        // This will be overridden to get texture from entity in updateRenderState
        return longBilledCurlewEntityRenderState.texture;
    }

    public LongBilledCurlewEntityRenderState createRenderState() {
        return new LongBilledCurlewEntityRenderState();
    }

    public void updateRenderState(T longBilledCurlewEntity, LongBilledCurlewEntityRenderState longBilledCurlewEntityRenderState, float f) {
        super.updateRenderState(longBilledCurlewEntity, longBilledCurlewEntityRenderState, f);

        // Store the texture from the entity in the render state
        longBilledCurlewEntityRenderState.texture = longBilledCurlewEntity.getTextureLocation();

        // Update animation states
        longBilledCurlewEntityRenderState.idleAnimationState.copyFrom(longBilledCurlewEntity.idleAnimationState);
        longBilledCurlewEntityRenderState.flyingAnimationState.copyFrom(longBilledCurlewEntity.flyingAnimationState);
    }
}
