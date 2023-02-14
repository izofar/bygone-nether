package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.CorporModel;
import com.izofar.bygonenether.entity.Corpor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CorporRenderer extends HumanoidMobRenderer<Corpor, CorporModel> {
    private static final ResourceLocation BRUTE_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/corpor.png");

    public CorporRenderer(EntityRendererProvider.Context context) { super(context, new CorporModel(CorporModel.createBodyLayer().bakeRoot()), 0.5F); }

    @Override
    public ResourceLocation getTextureLocation(Corpor wither){ return BRUTE_LOCATION; }

    protected void scale(Corpor corpor, PoseStack stack, float f) {
        stack.scale(1.2F, 1.2F, 1.2F);
    }
}
