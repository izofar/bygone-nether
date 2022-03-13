package com.izofar.bygonenether.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.izofar.bygonenether.entity.WexEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WexModel extends BipedModel<WexEntity> {
   private final ModelRenderer leftWing;
   private final ModelRenderer rightWing;

   public WexModel() {
      super(0.0F, 0.0F, 64, 64);
      this.leftLeg.visible = false;
      this.hat.visible = false;
      this.rightLeg = new ModelRenderer(this, 32, 0);
      this.rightLeg.addBox(-1.0F, -1.0F, -2.0F, 6.0F, 10.0F, 4.0F, 0.0F);
      this.rightLeg.setPos(-1.9F, 12.0F, 0.0F);
      this.rightWing = new ModelRenderer(this, 0, 32);
      this.rightWing.addBox(-20.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F);
      this.leftWing = new ModelRenderer(this, 0, 32);
      this.leftWing.mirror = true;
      this.leftWing.addBox(0.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F);
   }

   protected Iterable<ModelRenderer> bodyParts() {
      return Iterables.concat(super.bodyParts(), ImmutableList.of(this.rightWing, this.leftWing));
   }

   public void setupAnim(WexEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
      super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
      if (pEntity.isCharging()) {
         if (pEntity.getMainHandItem().isEmpty()) {
            this.rightArm.xRot = ((float)Math.PI * 1.5F);
            this.leftArm.xRot = ((float)Math.PI * 1.5F);
         } else if (pEntity.getMainArm() == HandSide.RIGHT) {
            this.rightArm.xRot = 3.7699115F;
         } else {
            this.leftArm.xRot = 3.7699115F;
         }
      }

      this.rightLeg.xRot += ((float)Math.PI / 5F);
      this.rightWing.z = 2.0F;
      this.leftWing.z = 2.0F;
      this.rightWing.y = 1.0F;
      this.leftWing.y = 1.0F;
      this.rightWing.yRot = 0.47123894F + MathHelper.cos(pAgeInTicks * 0.8F) * (float)Math.PI * 0.05F;
      this.leftWing.yRot = -this.rightWing.yRot;
      this.leftWing.zRot = -0.47123894F;
      this.leftWing.xRot = 0.47123894F;
      this.rightWing.xRot = 0.47123894F;
      this.rightWing.zRot = 0.47123894F;
   }
}
