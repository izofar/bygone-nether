package com.izofar.bygonenether.client.model;

import com.izofar.bygonenether.entity.WarpedEnderMan;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class WarpedEndermanModel extends EndermanModel<WarpedEnderMan> {

    private final ModelPart stemBody;

    public WarpedEndermanModel(ModelPart root) {
        super(root);
        stemBody = body.getChild("stem_body");
    }

    public static LayerDefinition createBodyLayer(){
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, -14.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartPose partpose = PartPose.offset(0.0F, -13.0F, 0.0F);
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), partpose);
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), partpose);
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.offset(0.0F, -14.0F, 0.0F));
        PartDefinition stemBody = body.addOrReplaceChild("stem_body", CubeListBuilder.create(), PartPose.offset(0.0F, 39.0F, 0.0F));
        stemBody.addOrReplaceChild("stem_body_r1", CubeListBuilder.create().texOffs(42, 0).addBox(-6.0F, -14.0F, -0.5F, 6.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -40.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(-5.0F, -12.0F, 0.0F));
        PartDefinition leftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(5.0F, -12.0F, 0.0F));
        leftArm.addOrReplaceChild("stem_arm", CubeListBuilder.create().texOffs(35, 0).addBox(-4.0F, -38.0F, -0.5F, 6.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 37.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(-2.0F, -5.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(2.0F, -5.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(WarpedEnderMan pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch){
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        float f3 = pAgeInTicks / 60.0F;
        this.stemBody.yRot = ((float)Math.PI / 180F) * Mth.sin(f3 * 3.5F) * 5.0F;
    }
}
