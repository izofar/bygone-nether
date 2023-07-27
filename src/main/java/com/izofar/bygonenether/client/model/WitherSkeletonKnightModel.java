package com.izofar.bygonenether.client.model;

import com.izofar.bygonenether.entity.WitherSkeletonKnightEntity;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WitherSkeletonKnightModel extends BipedModel<WitherSkeletonKnightEntity> {

    public WitherSkeletonKnightModel() {
        super(RenderType::entityCutoutNoCull, 0.0F, 0.0F, 64, 64);
        this.body = new ModelRenderer(this, 16, 16);

        this.rightArm = new ModelRenderer(this, 40, 16);
        this.leftArm = new ModelRenderer(this, 40, 16);
        this.rightLeg = new ModelRenderer(this, 0, 16);
        this.leftLeg = new ModelRenderer(this, 0, 16);

        ModelRenderer bodywear = new ModelRenderer(this);
        ModelRenderer leftArmwear = new ModelRenderer(this);
        ModelRenderer rightArmwear = new ModelRenderer(this);
        ModelRenderer leftLegwear = new ModelRenderer(this);
        ModelRenderer rightLegwear = new ModelRenderer(this);

        this.rightArm.texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F);
        this.leftArm.texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, true);
        this.rightLeg.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F);
        this.leftLeg.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, true);

        this.hat.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F);
        bodywear.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.2F);
        leftArmwear.texOffs(40, 32).addBox(3.0F, -4.0F, -1.5F, 4.0F, 12.0F, 4.0F, 0.4F);
        rightArmwear.texOffs(40, 32).addBox(-9.0F, -4.0F, -1.5F, 4.0F, 12.0F, 4.0F, 0.4F);
        leftLegwear.texOffs(0, 32).addBox(0.0F, -12.0F, -2.1F, 4.0F, 12.0F, 4.0F, 0.1F);
        rightLegwear.texOffs(0, 32).addBox(-4.0F, -12.0F, -2.1F, 4.0F, 12.0F, 4.0F, 0.1F);

        this.leftArm.mirror = true;
        this.leftLeg.mirror = true;
        leftArmwear.mirror = true;
        leftLegwear.mirror = true;

        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.leftArm.setPos(5.0F, 2.0F, 0.0F);
        this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
        this.leftLeg.setPos(2.0F, 12.0F, 0.0F);

        this.hat.setPos(0.0F, 0.0F, 0.0F);
        this.body.setPos(0.0F, 0.0F, 0.0F);
        bodywear.setPos(0.0F, 0.0F, 0.0F);
        leftArmwear.setPos(-3.0F, 2.0F, 0.0F);
        rightArmwear.setPos(5.0F, 2.0F, 0.0F);
        leftLegwear.setPos(-2.0F, 12.0F, 0.1F);
        rightLegwear.setPos(2.0F, 12.0F, 0.1F);

        this.body.addChild(bodywear);
        this.leftArm.addChild(leftArmwear);
        this.rightArm.addChild(rightArmwear);
        this.leftLeg.addChild(leftLegwear);
        this.rightLeg.addChild(rightLegwear);
    }

    @Override
    public void prepareMobModel(WitherSkeletonKnightEntity witherSkeletonKnightEntity, float pitch, float yaw, float roll) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        ItemStack itemstack = witherSkeletonKnightEntity.getItemInHand(Hand.MAIN_HAND);
        if (itemstack.getItem() == Items.BOW && witherSkeletonKnightEntity.isAggressive()) {
            if (witherSkeletonKnightEntity.getMainArm() == HandSide.RIGHT) {
                this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            }
        }
        super.prepareMobModel(witherSkeletonKnightEntity, pitch, yaw, roll);
    }

    @Override
    public void setupAnim(WitherSkeletonKnightEntity witherSkeletonKnightEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(witherSkeletonKnightEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ItemStack itemstack = witherSkeletonKnightEntity.getMainHandItem();
        if (witherSkeletonKnightEntity.isAggressive() && (itemstack.isEmpty() || itemstack.getItem() != Items.BOW)) {
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

        if(witherSkeletonKnightEntity.isAlive() && witherSkeletonKnightEntity.isUsingShield()) {
            boolean flag = witherSkeletonKnightEntity.getMainArm() == HandSide.RIGHT;
            if ((witherSkeletonKnightEntity.getShieldHand() == Hand.MAIN_HAND) == flag) {
                this.poseRightArmShield();
            } else if ((witherSkeletonKnightEntity.getShieldHand() == Hand.OFF_HAND) == flag) {
                this.poseLeftArmShield();
            }
        }
    }

    private void poseRightArmShield() {
        this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F + ((float)Math.PI / 4F);
        this.rightArm.yRot = (-(float)Math.PI / 6F);
    }

    private void poseLeftArmShield() {
        this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F + ((float)Math.PI / 4F);
        this.leftArm.yRot = ((float)Math.PI / 6F);
    }

    @Override
    public void translateToHand(HandSide arm, MatrixStack pose) {
        float f = arm == HandSide.RIGHT ? 1.0F : -1.0F;
        ModelRenderer modelpart = this.getArm(arm);
        modelpart.x += f;
        modelpart.translateAndRotate(pose);
        modelpart.x -= f;
    }
}
