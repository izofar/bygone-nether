package com.izofar.bygonenether.client.model;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.entity.WitherSkeletonKnight;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class WitherSkeletonKnightModel extends BipedModel<WitherSkeletonKnight> {

    private final List<ModelRenderer> armor;

    private final ModelRenderer bodywear;
    private final ModelRenderer leftArmwear;
    private final ModelRenderer rightArmwear;
    private final ModelRenderer leftLegwear;
    private final ModelRenderer rightLegwear;

    public WitherSkeletonKnightModel() {
        super(RenderType::entityCutoutNoCull, 0.0F, 0.0F, 64, 64);
        this.body = new ModelRenderer(this, 16, 16);
        this.bodywear = new ModelRenderer(this);
        this.leftArmwear = new ModelRenderer(this);
        this.rightArmwear = new ModelRenderer(this);
        this.leftLegwear = new ModelRenderer(this);
        this.rightLegwear = new ModelRenderer(this);

        this.armor = ImmutableList.of(
                this.hat,
                this.bodywear,
                this.leftArmwear,
                this.rightArmwear,
                this.leftLegwear,
                this.rightLegwear
        );

        this.rightArm.texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F);
        this.leftArm.texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, true);
        this.rightLeg.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F);
        this.leftLeg.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, true);

        this.hat.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F);
        this.bodywear.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.2F);
        this.leftArmwear.texOffs(40, 32).addBox(3.0F, -4.0F, -1.5F, 4.0F, 12.0F, 4.0F, 0.4F);
        this.rightArmwear.texOffs(40, 32).addBox(-9.0F, -4.0F, -1.5F, 4.0F, 12.0F, 4.0F, 0.4F);
        this.leftLegwear.texOffs(0, 32).addBox(0.0F, -12.0F, -2.1F, 4.0F, 12.0F, 4.0F, 0.1F);
        this.rightLegwear.texOffs(0, 32).addBox(-4.0F, -12.0F, -2.1F, 4.0F, 12.0F, 4.0F, 0.1F);

        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.leftArm.setPos(5.0F, 2.0F, 0.0F);
        this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
        this.leftLeg.setPos(2.0F, 12.0F, 0.0F);

        this.hat.setPos(0.0F, 0.0F, 0.0F);
        this.body.setPos(-5.0F, 2.0F, 0.0F);
        this.bodywear.setPos(0.0F, 0.0F, 0.0F);
        this.leftArmwear.setPos(-3.0F, 2.0F, 0.0F);
        this.rightArmwear.setPos(5.0F, 2.0F, 0.0F);
        this.leftLegwear.setPos(-2.0F, 12.0F, 0.1F);
        this.rightLegwear.setPos(2.0F, 12.0F, 0.1F);

    }

    public void prepareMobModel(WitherSkeletonKnight witherSkeletonKnight, float pitch, float yaw, float roll) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        ItemStack itemstack = witherSkeletonKnight.getItemInHand(Hand.MAIN_HAND);
        if (itemstack.getItem() == Items.BOW && witherSkeletonKnight.isAggressive()) {
            if (witherSkeletonKnight.getMainArm() == HandSide.RIGHT) {
                this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            }
        }
        super.prepareMobModel(witherSkeletonKnight, pitch, yaw, roll);
    }

    public void setupAnim(WitherSkeletonKnight witherSkeletonKnight, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(witherSkeletonKnight, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ItemStack itemstack = witherSkeletonKnight.getMainHandItem();
        if (witherSkeletonKnight.isAggressive() && (itemstack.isEmpty() || itemstack.getItem() != Items.BOW)) {
            float f = MathHelper.sin(this.attackTime * (float)Math.PI);
            float f1 = MathHelper.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - f * 0.6F);
            this.leftArm.yRot = 0.1F - f * 0.6F;
            this.rightArm.xRot = (-(float)Math.PI / 2F);
            this.leftArm.xRot = (-(float)Math.PI / 2F);
            this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
            this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
            ModelHelper.bobArms(this.rightArm, this.leftArm, ageInTicks);
        }

    }

    public void translateToHand(HandSide arm, MatrixStack pose) {
        float f = arm == HandSide.RIGHT ? 1.0F : -1.0F;
        ModelRenderer modelpart = this.getArm(arm);
        modelpart.x += f;
        modelpart.translateAndRotate(pose);
        modelpart.x -= f;
    }
}
