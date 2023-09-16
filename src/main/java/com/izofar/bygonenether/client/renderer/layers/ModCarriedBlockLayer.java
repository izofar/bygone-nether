package com.izofar.bygonenether.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class ModCarriedBlockLayer<T extends EnderMan, M extends EndermanModel<T>> extends RenderLayer<T, M> {

    public ModCarriedBlockLayer(RenderLayerParent<T, M> layer) {
        super(layer);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        BlockState blockstate = livingEntity.getCarriedBlock();
        if (blockstate != null) {
            stack.pushPose();
            stack.translate(0.0D, 0.6875D, -0.75D);
            stack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
            stack.mulPose(Vector3f.YP.rotationDegrees(45.0F));
            stack.translate(0.25D, 0.1875D, 0.25D);
            stack.scale(-0.5F, -0.5F, 0.5F);
            stack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, stack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
            stack.popPose();
        }
    }
}
