package com.izofar.bygonenether.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.izofar.bygonenether.entity.Wex;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class WexModel extends HumanoidModel<Wex> {
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public WexModel(ModelPart part) {
        super(part);
        this.leftLeg.visible = false;
        this.hat.visible = false;
        this.rightWing = part.getChild("right_wing");
        this.leftWing = part.getChild("left_wing");
    }

    protected Iterable<ModelPart> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.rightWing, this.leftWing));
    }

    public void setupAnim(Wex wex, float f0, float f1, float f2, float f3, float f4) {
        super.setupAnim(wex, f0, f1, f2, f3, f4);
        if (wex.isCharging()) {
            if (wex.getMainHandItem().isEmpty()) {
                this.rightArm.xRot = ((float)Math.PI * 1.5F);
                this.leftArm.xRot = ((float)Math.PI * 1.5F);
            } else if (wex.getMainArm() == HumanoidArm.RIGHT) {
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
        this.rightWing.yRot = 0.47123894F + Mth.cos(f2 * 45.836624F * ((float)Math.PI / 180F)) * (float)Math.PI * 0.05F;
        this.leftWing.yRot = -this.rightWing.yRot;
        this.leftWing.zRot = -0.47123894F;
        this.leftWing.xRot = 0.47123894F;
        this.rightWing.xRot = 0.47123894F;
        this.rightWing.zRot = 0.47123894F;
    }
}
