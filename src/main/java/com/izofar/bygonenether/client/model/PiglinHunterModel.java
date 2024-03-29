package com.izofar.bygonenether.client.model;

import com.izofar.bygonenether.entity.PiglinHunter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PiglinHunterModel extends PiglinModel<PiglinHunter> {

    private final ModelPart hoglinSkull;

    public PiglinHunterModel(ModelPart root) {
        super(root);
        this.hoglinSkull = root.getChild("hoglin_skull");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = PiglinModel.createMesh(CubeDeformation.NONE);
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("hoglin_skull", CubeListBuilder.create().texOffs(61, 1).addBox(-7.0F, 16.5F, 0.0F, 14.0F, 6.0F, 19.0F, new CubeDeformation(-0.9F)).texOffs(61, 3).addBox(5.0F, 11.5F, 0.0F, 2.0F, 11.0F, 2.0F, CubeDeformation.NONE).texOffs(70, 3).addBox(-7.0F, 11.5F, 0.0F, 2.0F, 11.0F, 2.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 15.5F, 25.0F, -1.6581F, 0.0F, 3.1416F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(PiglinHunter piglinHunter, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        super.setupAnim(piglinHunter, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if(piglinHunter.isPassenger()){
            this.hoglinSkull.y = 10.5F;
        }else{
            this.hoglinSkull.y = 15.5F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        hoglinSkull.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
