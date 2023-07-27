package com.izofar.bygonenether.client.model;

import com.izofar.bygonenether.entity.PiglinHunterEntity;
import net.minecraft.client.renderer.entity.model.PiglinModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PiglinHunterModel extends PiglinModel<PiglinHunterEntity> {

    public final ModelRenderer hoglinSkull;

    public PiglinHunterModel(float scale, int texWidth, int texHeight) {
        super(scale, texWidth, texHeight);
        hoglinSkull = new ModelRenderer(this);
        hoglinSkull.setPos(0.0F, 15.5F, 25.0F);
        body.addChild(hoglinSkull);
        setRotationAngle(hoglinSkull, -1.6581F, 0.0F, 3.1416F);
        hoglinSkull.texOffs(61, 1).addBox(-7.0F, 16.5F, 0.0F, 14.0F, 6.0F, 19.0F, -0.9F, false);
        hoglinSkull.texOffs(61, 3).addBox(5.0F, 11.5F, 0.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);
        hoglinSkull.texOffs(70, 3).addBox(-7.0F, 11.5F, 0.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(PiglinHunterEntity piglinHunterEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(piglinHunterEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (piglinHunterEntity.isPassenger()) {
            this.hoglinSkull.y = 10.5F;
        } else {
            this.hoglinSkull.y = 15.5F;
        }

        if (piglinHunterEntity.isAlive() && piglinHunterEntity.isUsingShield()) {
            boolean flag = piglinHunterEntity.getMainArm() == HandSide.RIGHT;
            if ((piglinHunterEntity.getShieldHand() == Hand.MAIN_HAND) == flag) {
                this.poseRightArmShield();
            } else if ((piglinHunterEntity.getShieldHand() == Hand.OFF_HAND) == flag) {
                this.poseLeftArmShield();
            }
        }

        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
    }

    @Override
    protected void setupAttackAnimation(PiglinHunterEntity piglinHunterEntity, float ageInTicks) {
        if (this.attackTime > 0.0F && piglinHunterEntity.getArmPose() == PiglinAction.ATTACKING_WITH_MELEE_WEAPON) {
            ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, piglinHunterEntity, this.attackTime, ageInTicks);
        } else {
            super.setupAttackAnimation(piglinHunterEntity, ageInTicks);
        }
    }

    private void poseRightArmShield() {
        this.rightArm.xRot = (-(float)Math.PI / 2F) * 0.5F - 0.9424779F + ((float)Math.PI / 4F);
        this.rightArm.yRot = (-(float)Math.PI / 6F);
    }

    private void poseLeftArmShield() {
        this.leftArm.xRot = (-(float)Math.PI / 2F) * 0.5F - 0.9424779F + ((float)Math.PI / 4F);
        this.leftArm.yRot = ((float)Math.PI / 6F);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
