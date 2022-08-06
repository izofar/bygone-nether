package com.izofar.bygonenether.client.renderer.layers;

import com.izofar.bygonenether.BygoneNetherMod;
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
    public RenderType renderType() { return WITHER_GLOW; }
}
