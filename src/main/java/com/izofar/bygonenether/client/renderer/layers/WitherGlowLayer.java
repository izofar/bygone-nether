package com.izofar.bygonenether.client.renderer.layers;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WitherGlowLayer<T extends AbstractSkeleton, M extends SkeletonModel<T>> extends EyesLayer<T, M> {

    private static final RenderType WITHER_GLOW = RenderType.eyes(new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/possessed_wither_skeleton_eyes.png"));

    public WitherGlowLayer(RenderLayerParent<T, M> layer) { super(layer); }

    @Override
    public RenderType renderType() { return WITHER_GLOW; }
}
