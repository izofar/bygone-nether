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
public class PiglinHunterModel extends ModPlayerModel<PiglinHunterEntity> {

    public final ModelRenderer hoglinSkull;
    public final ModelRenderer earRight;
    public final ModelRenderer earLeft;
    public final ModelRenderer bodyDefault;
    private final ModelRenderer headDefault;
    public final ModelRenderer leftArmDefault;
    public final ModelRenderer rightArmDefault;

    public PiglinHunterModel(float scale, int texWidth, int texHeight) {
        super(scale, texWidth, texHeight);
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.body = new ModelRenderer(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, scale);
        hoglinSkull = new ModelRenderer(this);
        hoglinSkull.setPos(0.0F, 15.5F, 25.0F);
        body.addChild(hoglinSkull);
        setRotationAngle(hoglinSkull, -1.6581F, 0.0F, 3.1416F);
        hoglinSkull.texOffs(61, 1).addBox(-7.0F, 16.5F, 0.0F, 14.0F, 6.0F, 19.0F, -0.9F, false);
        hoglinSkull.texOffs(61, 3).addBox(5.0F, 11.5F, 0.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);
        hoglinSkull.texOffs(70, 3).addBox(-7.0F, 11.5F, 0.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);
        this.head = new ModelRenderer(this);
        this.head.texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, scale);
        this.head.texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, scale);
        this.head.texOffs(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, scale);
        this.head.texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, scale);
        this.earRight = new ModelRenderer(this);
        this.earRight.setPos(4.5F, -6.0F, 0.0F);
        this.earRight.texOffs(51, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, scale);
        this.head.addChild(this.earRight);
        this.earLeft = new ModelRenderer(this);
        this.earLeft.setPos(-4.5F, -6.0F, 0.0F);
        this.earLeft.texOffs(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, scale);
        this.head.addChild(this.earLeft);
        this.hat = new ModelRenderer(this);
        this.bodyDefault = this.body.createShallowCopy();
        this.headDefault = this.head.createShallowCopy();
        this.leftArmDefault = this.leftArm.createShallowCopy();
        this.rightArmDefault = this.leftArm.createShallowCopy();
    }

    @Override
    public void setupAnim(PiglinHunterEntity piglinHunterEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.body.copyFrom(this.bodyDefault);
        this.head.copyFrom(this.headDefault);
        this.leftArm.copyFrom(this.leftArmDefault);
        this.rightArm.copyFrom(this.rightArmDefault);
        super.setupAnim(piglinHunterEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float f1 = ageInTicks * 0.1F + limbSwing * 0.5F;
        float f2 = 0.08F + limbSwingAmount * 0.4F;
        this.earRight.zRot = (-(float)Math.PI / 6F) - MathHelper.cos(f1 * 1.2F) * f2;
        this.earLeft.zRot = ((float)Math.PI / 6F) + MathHelper.cos(f1) * f2;
        PiglinAction piglinaction = ((AbstractPiglinEntity)piglinHunterEntity).getArmPose();
        if (piglinaction == PiglinAction.DANCING) {
            float f3 = ageInTicks / 60.0F;
            this.earLeft.zRot = ((float)Math.PI / 6F) + ((float)Math.PI / 180F) * MathHelper.sin(f3 * 30.0F) * 10.0F;
            this.earRight.zRot = (-(float)Math.PI / 6F) - ((float)Math.PI / 180F) * MathHelper.cos(f3 * 30.0F) * 10.0F;
            this.head.x = MathHelper.sin(f3 * 10.0F);
            this.head.y = MathHelper.sin(f3 * 40.0F) + 0.4F;
            this.rightArm.zRot = ((float)Math.PI / 180F) * (70.0F + MathHelper.cos(f3 * 40.0F) * 10.0F);
            this.leftArm.zRot = this.rightArm.zRot * -1.0F;
            this.rightArm.y = MathHelper.sin(f3 * 40.0F) * 0.5F + 1.5F;
            this.leftArm.y = MathHelper.sin(f3 * 40.0F) * 0.5F + 1.5F;
            this.body.y = MathHelper.sin(f3 * 40.0F) * 0.35F;
        } else if (piglinaction == PiglinAction.ATTACKING_WITH_MELEE_WEAPON && this.attackTime == 0.0F) {
            this.holdWeaponHigh(piglinHunterEntity);
        } else if (piglinaction == PiglinAction.CROSSBOW_HOLD) {
            ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, !piglinHunterEntity.isLeftHanded());
        } else if (piglinaction == PiglinAction.CROSSBOW_CHARGE) {
            ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, piglinHunterEntity, !piglinHunterEntity.isLeftHanded());
        } else if (piglinaction == PiglinAction.ADMIRING_ITEM) {
            this.head.xRot = 0.5F;
            this.head.yRot = 0.0F;
            if (piglinHunterEntity.isLeftHanded()) {
                this.rightArm.yRot = -0.5F;
                this.rightArm.xRot = -0.9F;
            } else {
                this.leftArm.yRot = 0.5F;
                this.leftArm.xRot = -0.9F;
            }
        }
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

    private void holdWeaponHigh(PiglinHunterEntity entity) {
        if (entity.isLeftHanded()) {
            this.leftArm.xRot = -1.8F;
        } else {
            this.rightArm.xRot = -1.8F;
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
