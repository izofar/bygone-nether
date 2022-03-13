package com.izofar.bygonenether.client.model;

import com.izofar.bygonenether.entity.PiglinHunterEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.PiglinModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// Made with Blockbench 4.1.4
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports

@OnlyIn(Dist.CLIENT)
public class PiglinHunterModel extends PiglinModel<PiglinHunterEntity> {

    private final ModelRenderer hoglin_skull;

    public PiglinHunterModel(float scale, int texWidth, int texHeight) {
        super(scale, texWidth, texHeight);
        hoglin_skull = new ModelRenderer(this);
        hoglin_skull.setPos(0.0F, 15.5F, 25.0F);
        body.addChild(hoglin_skull);
        setRotationAngle(hoglin_skull, -1.6581F, 0.0F, 3.1416F);
        hoglin_skull.texOffs(61, 1).addBox(-7.0F, 16.5F, 0.0F, 14.0F, 6.0F, 19.0F, -0.9F, false);
        hoglin_skull.texOffs(61, 3).addBox(5.0F, 11.5F, 0.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);
        hoglin_skull.texOffs(70, 3).addBox(-7.0F, 11.5F, 0.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
       super.renderToBuffer(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        hoglin_skull.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
