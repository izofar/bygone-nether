package com.izofar.bygonenether.client.renderer.layers;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.entity.Wraither;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class WitherGlowLayer<T extends AbstractSkeleton, M extends SkeletonModel<T>> extends EyesLayer<T, M> {
    private static final RenderType WITHER_GLOW = RenderType.eyes(new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wraither_eyes.png"));

    public WitherGlowLayer(RenderLayerParent<T, M> layer) { super(layer); }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch){
        if(entity instanceof Wraither wraither && wraither.isPossessed())
            super.render(stack, buffer, light, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public RenderType renderType() { return WITHER_GLOW; }
}
