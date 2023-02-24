package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.renderer.layers.WitherGlowLayer;
import com.izofar.bygonenether.entity.Wraither;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WraitherRenderer extends MobRenderer<Wraither, SkeletonModel<Wraither>> {

    private static final ResourceLocation POSSESSED_SKELETON_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wraither.png");
    private static final ResourceLocation WITHER_SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    public WraitherRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonModel<>(SkeletonModel.createBodyLayer().bakeRoot()), 0.5F);
        this.addLayer(new WitherGlowLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Wraither skeleton) {
        return skeleton.isPossessed() ? POSSESSED_SKELETON_LOCATION : WITHER_SKELETON_LOCATION;
    }

    @Override
    protected void scale(Wraither skeleton, PoseStack poseStack, float f){
        poseStack.scale(1.2F, 1.2F, 1.2F);
    }
}
