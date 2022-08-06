package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.renderer.layers.WitherGlowLayer;
import com.izofar.bygonenether.entity.Wraither;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WraitherRenderer extends MobRenderer<Wraither, SkeletonModel<Wraither>> {

    private static final ResourceLocation WITHER_SKELETON_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wraither.png");

    public WraitherRenderer(EntityRendererManager context) {
        super(context, new SkeletonModel<>(), 0.5F);
        this.addLayer(new WitherGlowLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Wraither skeleton) {
        return WITHER_SKELETON_LOCATION;
    }

    @Override
    protected void scale(Wraither skeleton, MatrixStack poseStack, float f){
        poseStack.scale(1.2F, 1.2F, 1.2F);
    }
}
