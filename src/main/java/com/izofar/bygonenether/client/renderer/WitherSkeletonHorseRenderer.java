package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class WitherSkeletonHorseRenderer extends UndeadHorseRenderer {

    private static final ResourceLocation WITHER_SKELETON_HORSE_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither_skeleton_horse.png");

    public WitherSkeletonHorseRenderer(EntityRendererProvider.Context context) { super(context, ModelLayers.SKELETON_HORSE); }

    @Override
    public ResourceLocation getTextureLocation(AbstractHorse horse) { return WITHER_SKELETON_HORSE_LOCATION; }

}
