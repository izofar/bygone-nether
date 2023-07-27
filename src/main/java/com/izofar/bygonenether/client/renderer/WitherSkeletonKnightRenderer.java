package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.WitherSkeletonKnightModel;
import com.izofar.bygonenether.entity.WitherSkeletonKnightEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WitherSkeletonKnightRenderer extends BipedRenderer<WitherSkeletonKnightEntity, WitherSkeletonKnightModel> {

    private static final ResourceLocation ARMORED_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wither_skeleton_knight.png");
    private static final ResourceLocation DISARMORED_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wither_skeleton_knight_disarmored.png");


    public WitherSkeletonKnightRenderer(EntityRendererManager manager) {
        super(manager, new WitherSkeletonKnightModel(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(WitherSkeletonKnightEntity witherSkeletonKnightEntity) {
        return witherSkeletonKnightEntity.isDisarmored() ? DISARMORED_LOCATION : ARMORED_LOCATION;
    }

    @Override
    protected void scale(WitherSkeletonKnightEntity witherSkeletonKnightEntity, MatrixStack matrixStack, float partialTicks) {
        matrixStack.scale(1.2F, 1.2F, 1.2F);
    }
}
