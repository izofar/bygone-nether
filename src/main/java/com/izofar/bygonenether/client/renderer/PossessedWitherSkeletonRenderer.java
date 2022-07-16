package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.renderer.layers.WitherGlowLayer;
import com.izofar.bygonenether.entity.PossessedWitherSkeleton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PossessedWitherSkeletonRenderer extends MobRenderer<PossessedWitherSkeleton, SkeletonModel<PossessedWitherSkeleton>> {

    private static final ResourceLocation WITHER_SKELETON_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/possessed_wither_skeleton.png");

    public PossessedWitherSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonModel<>(SkeletonModel.createBodyLayer().bakeRoot()), 0.5F);
        this.addLayer(new WitherGlowLayer<>(this));
        //this.addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel(context.bakeLayer(p_174384_)), new SkeletonModel(p_174382_.bakeLayer(p_174385_))));
    }

    @Override
    public ResourceLocation getTextureLocation(PossessedWitherSkeleton skeleton) {
        return WITHER_SKELETON_LOCATION;
    }

    @Override
    protected void scale(PossessedWitherSkeleton skeleton, PoseStack poseStack, float f){
        poseStack.scale(1.2F, 1.2F, 1.2F);
    }
}
