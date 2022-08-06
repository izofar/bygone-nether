package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.WitherSkeletonKnightModel;
import com.izofar.bygonenether.entity.WitherSkeletonKnight;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class WitherSkeletonKnightRenderer extends BipedRenderer<WitherSkeletonKnight, WitherSkeletonKnightModel> {

    private static final ResourceLocation ARMORED_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wither_skeleton_knight.png");
    private static final ResourceLocation DISARMORED_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wither_skeleton_knight_disarmored.png");


    public WitherSkeletonKnightRenderer(EntityRendererManager manager) {
        super(manager, new WitherSkeletonKnightModel(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(WitherSkeletonKnight witherSkeletonKnight) {
        return witherSkeletonKnight.isDisarmored() ? DISARMORED_LOCATION : ARMORED_LOCATION;
    }

    protected void scale(WitherSkeletonKnight witherSkeletonKnight, MatrixStack stack, float f) {
        stack.scale(1.2F, 1.2F, 1.2F);
    }
}
