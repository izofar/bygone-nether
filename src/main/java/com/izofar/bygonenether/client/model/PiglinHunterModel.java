package com.izofar.bygonenether.client.model;

import com.izofar.bygonenether.entity.PiglinHunter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PiglinHunterModel extends PiglinModel<PiglinHunter> {

    private final ModelPart hoglin_skull;

    public PiglinHunterModel(ModelPart root) {
        super(root);
        this.hoglin_skull = root.getChild("hoglin_skull");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = PiglinModel.createMesh(CubeDeformation.NONE);
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("hoglin_skull", CubeListBuilder.create().texOffs(61, 1).addBox(-7.0F, 16.5F, 0.0F, 14.0F, 6.0F, 19.0F, new CubeDeformation(-0.9F)).texOffs(61, 3).addBox(5.0F, 11.5F, 0.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(70, 3).addBox(-7.0F, 11.5F, 0.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.5F, 25.0F, -1.6581F, 0.0F, 3.1416F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(PiglinHunter piglin, float p_103367_, float p_103368_, float p_103369_, float p_103370_, float p_103371_){
        super.setupAnim(piglin, p_103367_, p_103368_, p_103369_, p_103370_, p_103371_);
        if(piglin.isPassenger()){
            this.hoglin_skull.y = 10.5F;
        }else{
            this.hoglin_skull.y = 15.5F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        hoglin_skull.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
