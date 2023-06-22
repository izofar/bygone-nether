package com.izofar.bygonenether.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

@OnlyIn(Dist.CLIENT)
public class ModCarriedBlockLayer<T extends EnderMan, M extends EndermanModel<T>> extends RenderLayer<T, M> {

    private final BlockRenderDispatcher blockRenderer;

    public ModCarriedBlockLayer(RenderLayerParent<T, M> layer, BlockRenderDispatcher blockRenderer) {
        super(layer);
        this.blockRenderer = blockRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        BlockState blockstate = livingEntity.getCarriedBlock();
        if (blockstate != null) {
            poseStack.pushPose();
            poseStack.translate(0.0D, 0.6875D, -0.75D);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0F));
            poseStack.translate(0.25D, 0.1875D, 0.25D);
            poseStack.scale(-0.5F, -0.5F, 0.5F);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            this.blockRenderer.renderSingleBlock(blockstate, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
            poseStack.popPose();
        }
    }
}
