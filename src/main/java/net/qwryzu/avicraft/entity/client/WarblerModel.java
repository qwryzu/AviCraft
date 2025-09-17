package net.qwryzu.avicraft.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.qwryzu.avicraft.AviCraft;
import net.qwryzu.avicraft.render.entity.state.WarblerEntityRenderState;

public class WarblerModel extends EntityModel<WarblerEntityRenderState> {
    public static final EntityModelLayer WARBLER_LAYER = new EntityModelLayer(Identifier.of(AviCraft.MOD_ID, "warbler"), "main");
    private final ModelPart head;
    private final Animation idleAnimation;
    private final Animation flappingAnimation;

    public WarblerModel(ModelPart modelPart) {
        super(modelPart);
        this.head = modelPart.getChild(EntityModelPartNames.HEAD);
        this.idleAnimation = WarblerAnimations.WARBLER_IDLE.createAnimation(root);
        this.flappingAnimation = WarblerAnimations.WARBLER_FLAP.createAnimation(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        try {
            ModelPartData bill = modelPartData.addChild("bill",
                    ModelPartBuilder.create().uv(24, 0).cuboid(-0.5F, -0.65F, 1.6F, 1.0F, 1.0F, 2.0F,
                            new Dilation(0.0F)), ModelTransform.of(-0.5F, 19.0F, -4.0F, 0.0F, 3.1416F, 0.0F));

            ModelPartData head = modelPartData.addChild("head",
                    ModelPartBuilder.create().uv(20, 11).cuboid(-1.5F, -1.65F, -1.0F, 3.0F, 3.0F, 3.0F,
                            new Dilation(0.0F)), ModelTransform.of(-0.5F, 19.0F, -4.0F, 0.0F, 3.1416F, 0.0F));

            ModelPartData body = modelPartData.addChild("body",
                    ModelPartBuilder.create().uv(1, 1).cuboid(-2.0F, -5.65F, -1.0F, 5.0F, 3.0F, 6.0F,
                            new Dilation(0.0F)), ModelTransform.of(0.0F, 25.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

            ModelPartData tail = modelPartData.addChild("tail",
                    ModelPartBuilder.create(), ModelTransform.of(0.0F, 20.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

            ModelPartData tail_r1 = tail.addChild("tail_r1",
                    ModelPartBuilder.create().uv(4, 15).cuboid(-1.5F, -1.25F, -4.8F, 3.0F, 1.0F, 3.0F,
                            new Dilation(0.0F)), ModelTransform.of(0.5F, 1.0F, 1.0F, 0.1745F, 0.0F, 0.0F));

            ModelPartData left_leg = modelPartData.addChild("left_leg",
                    ModelPartBuilder.create().uv(2, 1).cuboid(-1.0F, -1.0F, -1.0F, 1.0F, 0.0F, 1.0F,
                            new Dilation(0.0F)), ModelTransform.of(-2.0F, 25.0F, -2.0F, 0.0F, 3.1416F, 0.0F));

            ModelPartData leftleg_r1 = left_leg.addChild("leftleg_r1",
                    ModelPartBuilder.create().uv(22, 17).cuboid(-1.0F, -1.35F, -1.0F, 1.0F, 2.0F, 0.0F,
                            new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

            ModelPartData right_leg = modelPartData.addChild("right_leg",
                    ModelPartBuilder.create().uv(2, 1).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 0.0F, 1.0F,
                            new Dilation(0.0F)), ModelTransform.of(0.0F, 25.0F, -1.0F, 0.0F, 3.1416F, 0.0F));

            ModelPartData rightleg_r1 = right_leg.addChild("rightleg_r1",
                    ModelPartBuilder.create().uv(20, 17).cuboid(-1.0F, -1.35F, -1.0F, 1.0F, 2.0F, 0.0F,
                            new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 1.0F, 0.3927F, 0.0F, 0.0F));

            ModelPartData left_wing = modelPartData.addChild("left_wing",
                    ModelPartBuilder.create().uv(12, 19).cuboid(-0.5F, -0.15F, 1.5F, 1.0F, 2.0F, 5.0F,
                            new Dilation(0.0F)), ModelTransform.of(-3.0F, 20.0F, 2.0F, 0.0F, 3.1416F, 0.0F));

            ModelPartData right_wing = modelPartData.addChild("right_wing",
                    ModelPartBuilder.create().uv(12, 19).cuboid(-0.5F, -0.15F, 1.5F, 1.0F, 2.0F, 5.0F,
                            new Dilation(0.0F)), ModelTransform.of(2.0F, 20.0F, 2.0F, 0.0F, 3.1416F, 0.0F));
            return TexturedModelData.of(modelData, 32, 32);

        } catch (Exception e) {
            System.err.println("=== ERROR CREATING MODEL DATA ===");
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public void setAngles(WarblerEntityRenderState warblerEntityRenderState) {
        super.setAngles(warblerEntityRenderState);
        this.idleAnimation.apply(warblerEntityRenderState.idleAnimationState, warblerEntityRenderState.age);
        this.flappingAnimation.apply(warblerEntityRenderState.flyingAnimationState, warblerEntityRenderState.age);
    }

    private void setHeadAngles(WarblerEntityRenderState state, float headYaw, float headPitch) {
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
        headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);
        this.head.yaw = headYaw * (float) (Math.PI / 180.0);
        this.head.pitch = headPitch * (float) (Math.PI / 180.0);
    }

    @Environment(EnvType.CLIENT)
    public static enum Pose {
        FLYING,
        STANDING,
    }
}
