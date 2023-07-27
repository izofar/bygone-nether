package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WitherSkeletonHorseRenderer extends UndeadHorseRenderer {

    private static final ResourceLocation WITHER_SKELETON_HORSE_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/wither_skeleton_horse.png");

    public WitherSkeletonHorseRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractHorseEntity horse) {
        return WITHER_SKELETON_HORSE_LOCATION;
    }

}
