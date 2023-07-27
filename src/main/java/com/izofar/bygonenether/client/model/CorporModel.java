package com.izofar.bygonenether.client.model;

import com.izofar.bygonenether.entity.CorporEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
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
public class CorporModel extends BipedModel<CorporEntity> {
    
    public CorporModel() { 
        super(0.0F);

        this.head = new ModelRenderer(this);
        this.rightArm = new ModelRenderer(this);
        this.leftArm = new ModelRenderer(this);
        this.rightLeg = new ModelRenderer(this);
        this.leftLeg = new ModelRenderer(this);
        
        this.head.texOffs(0, 0).addBox(-4.5F, -9.0F, -4.0F, 9.0F, 10.0F, 6.0F, 0.0F);
        this.rightArm.texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.2F);
        this.leftArm.texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.2F, true);
        this.rightLeg.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.2F);
        this.leftLeg.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.2F, true);

        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.leftArm.setPos(5.0F, 2.0F, 0.0F);
        this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
        this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
    }

    @Override
    public void prepareMobModel(CorporEntity corporEntity, float pitch, float yaw, float roll) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        ItemStack itemstack = corporEntity.getItemInHand(Hand.MAIN_HAND);
        if (itemstack.getItem() == Items.BOW && corporEntity.isAggressive()) {
            if (corporEntity.getMainArm() == HandSide.RIGHT) {
                this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            }
        }
        super.prepareMobModel(corporEntity, pitch, yaw, roll);
    }

    @Override
    public void setupAnim(CorporEntity corporEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(corporEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ItemStack itemstack = corporEntity.getMainHandItem();
        if (corporEntity.isAggressive() && (itemstack.isEmpty() || itemstack.getItem() != Items.BOW)) {
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

    @Override
    public void translateToHand(HandSide arm, MatrixStack pose) {
        float f = arm == HandSide.RIGHT ? 1.0F : -1.0F;
        ModelRenderer modelpart = this.getArm(arm);
        modelpart.x += f;
        modelpart.translateAndRotate(pose);
        modelpart.x -= f;
    }
}
