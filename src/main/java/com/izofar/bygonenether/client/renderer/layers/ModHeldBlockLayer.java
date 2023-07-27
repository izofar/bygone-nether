package com.izofar.bygonenether.client.renderer.layers;

import com.izofar.bygonenether.client.model.WarpedEndermanModel;
import com.izofar.bygonenether.entity.WarpedEndermanEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModHeldBlockLayer extends LayerRenderer<WarpedEndermanEntity, WarpedEndermanModel> {

    public ModHeldBlockLayer(IEntityRenderer<WarpedEndermanEntity, WarpedEndermanModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, WarpedEndermanEntity warpedEndermanEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        BlockState blockstate = warpedEndermanEntity.getCarriedBlock();
        if (blockstate != null) {
            matrixStack.pushPose();
            matrixStack.translate(0.0D, 0.6875D, -0.75D);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(45.0F));
            matrixStack.translate(0.25D, 0.1875D, 0.25D);
            matrixStack.scale(-0.5F, -0.5F, 0.5F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
            matrixStack.popPose();
        }
    }
}
