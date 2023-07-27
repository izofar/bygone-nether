package com.izofar.bygonenether.client.renderer.layers;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.entity.WraitherEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WitherGlowLayer<T extends AbstractSkeletonEntity, M extends SkeletonModel<T>> extends AbstractEyesLayer<T, M> {

    private static final RenderType WITHER_GLOW = RenderType.eyes(new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wraither_eyes.png"));

    public WitherGlowLayer(IEntityRenderer<T, M> layer) { super(layer); }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(livingEntity instanceof WraitherEntity && ((WraitherEntity)livingEntity).isPossessed()) {
            super.render(matrixStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @Override
    public RenderType renderType() {
        return WITHER_GLOW;
    }
}
