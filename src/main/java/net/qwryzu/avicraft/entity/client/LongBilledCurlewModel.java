package net.qwryzu.avicraft.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.qwryzu.avicraft.AviCraft;
import net.qwryzu.avicraft.render.entity.state.LongBilledCurlewEntityRenderState;

public class LongBilledCurlewModel extends EntityModel<LongBilledCurlewEntityRenderState> {
    public static final EntityModelLayer LONG_BILLED_CURLEW_LAYER = new EntityModelLayer(Identifier.of(AviCraft.MOD_ID, "longbilledcurlew"), "main");
    private final Animation idleAnimation;
    private final Animation flappingAnimation;
    private final Animation takeoffAnimation;

    public LongBilledCurlewModel(ModelPart modelPart) {
        super(modelPart);
        this.idleAnimation = LongBilledCurlewAnimations.IDLE.createAnimation(root);
        this.flappingAnimation = LongBilledCurlewAnimations.FLAP.createAnimation(root);
        this.takeoffAnimation = LongBilledCurlewAnimations.TAKEOFF.createAnimation(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(8, 32).cuboid(0.0F, -2.72F, -1.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, 22.0F, 3.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData leg_lower_r1 = right_leg.addChild("leg_lower_r1", ModelPartBuilder.create().uv(18, 23).cuboid(0.0F, -2.6F, -0.95F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -1.0F, -0.3491F, 0.0F, 0.0F));

        ModelPartData right_foot = right_leg.addChild("right_foot", ModelPartBuilder.create().uv(28, 12).cuboid(0.0F, 0.0F, -1.88F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(8, 29).cuboid(-2.0F, 0.0F, -1.88F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 30).cuboid(-1.0F, 0.0F, -0.88F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 30).cuboid(-1.0F, 0.0F, 0.12F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 2.0F, -2.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(10, 32).cuboid(0.0F, -2.72F, -1.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 22.0F, 3.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData leg_lower_r2 = left_leg.addChild("leg_lower_r2", ModelPartBuilder.create().uv(26, 30).cuboid(0.0F, -2.6F, -0.95F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -1.0F, -0.3491F, 0.0F, 0.0F));

        ModelPartData left_foot = left_leg.addChild("left_foot", ModelPartBuilder.create().uv(28, 30).cuboid(1.0F, 0.0F, -1.88F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 31).cuboid(-1.0F, 0.0F, -1.88F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 31).cuboid(0.0F, 0.0F, -0.88F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 31).cuboid(0.0F, 0.0F, 0.12F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -2.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -10.72F, -3.0F, 7.0F, 6.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData tail = modelPartData.addChild("tail", ModelPartBuilder.create().uv(20, 21).cuboid(-5.5F, -8.72F, 4.0F, 6.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 0).cuboid(-5.0F, -7.72F, 5.0F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 4).cuboid(-4.5F, -6.0F, 6.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData neck = modelPartData.addChild("neck", ModelPartBuilder.create(), ModelTransform.of(-2.0F, 14.0F, -3.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData neck_r1 = neck.addChild("neck_r1", ModelPartBuilder.create().uv(0, 29).cuboid(-1.5F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 1.0F, 0.1745F, 0.0F, 0.0F));

        ModelPartData head = neck.addChild("head", ModelPartBuilder.create().uv(0, 23).cuboid(-4.0F, -16.0F, -5.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(12, 23).cuboid(-3.5F, -15.0F, -6.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 10.0F, 3.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData bill = head.addChild("bill", ModelPartBuilder.create().uv(20, 13).cuboid(-3.0F, -14.5F, -12.0F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData bill3_r1 = bill.addChild("bill3_r1", ModelPartBuilder.create().uv(14, 30).cuboid(0.0F, -1.26F, 0.4F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -12.0F, -14.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData bill2_r1 = bill.addChild("bill2_r1", ModelPartBuilder.create().uv(28, 7).cuboid(0.0F, -1.1F, -0.6F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -13.0F, -13.0F, 0.3054F, 0.0F, 0.0F));

        ModelPartData right_wing = modelPartData.addChild("right_wing", ModelPartBuilder.create().uv(0, 13).cuboid(-5.9981F, -0.72F, -2.0872F, 6.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, 16.0F, -2.0F, 0.0F, 1.5708F, -1.5708F));

        ModelPartData right_wing_tip = right_wing.addChild("right_wing_tip", ModelPartBuilder.create().uv(12, 26).cuboid(-4.0F, -0.72F, -2.0F, 4.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(28, 10).cuboid(-6.0F, -0.72F, -2.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-5.9981F, 0.0F, -0.0872F, 0.0F, 0.0F, 0.0F));

        ModelPartData left_wing = modelPartData.addChild("left_wing", ModelPartBuilder.create().uv(0, 18).cuboid(-6.0F, -0.28F, -2.0F, 6.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 16.0F, -2.0F, 0.0F, 1.5708F, -1.5708F));

        ModelPartData left_wing_tip = left_wing.addChild("left_wing_tip", ModelPartBuilder.create().uv(26, 26).cuboid(-4.0F, -0.28F, -2.0F, 4.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(8, 30).cuboid(-6.0F, -0.28F, -2.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(LongBilledCurlewEntityRenderState longBilledCurlewEntityRenderState) {
        super.setAngles(longBilledCurlewEntityRenderState);
        this.idleAnimation.apply(longBilledCurlewEntityRenderState.idleAnimationState, longBilledCurlewEntityRenderState.age);
        this.flappingAnimation.apply(longBilledCurlewEntityRenderState.flyingAnimationState, longBilledCurlewEntityRenderState.age);
    }


}
