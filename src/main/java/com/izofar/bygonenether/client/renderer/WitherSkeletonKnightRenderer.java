package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.WitherSkeletonKnightModel;
import com.izofar.bygonenether.entity.WitherSkeletonKnight;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WitherSkeletonKnightRenderer extends HumanoidMobRenderer<WitherSkeletonKnight, WitherSkeletonKnightModel> {

    private static final ResourceLocation ARMORED_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wither_skeleton_knight.png");
    private static final ResourceLocation DISARMORED_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wither_skeleton_knight_disarmored.png");

    public WitherSkeletonKnightRenderer(EntityRendererProvider.Context context) {
        super(context, new WitherSkeletonKnightModel(WitherSkeletonKnightModel.createBodyLayer().bakeRoot()), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(WitherSkeletonKnight witherSkeletonKnight) {
        return witherSkeletonKnight.isDisarmored() ? DISARMORED_LOCATION : ARMORED_LOCATION;
    }

    @Override
    protected void scale(WitherSkeletonKnight witherSkeletonKnight, PoseStack poseStack, float partialTicks) {
        poseStack.scale(1.2F, 1.2F, 1.2F);
    }
}
