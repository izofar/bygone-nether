package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.CorporModel;
import com.izofar.bygonenether.entity.Corpor;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class CorporRenderer extends BipedRenderer<Corpor, CorporModel> {

    private static final ResourceLocation BRUTE_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/corpor.png");

    public CorporRenderer(EntityRendererManager manager) { super(manager, new CorporModel(), 0.5F); }

    @Override
    public ResourceLocation getTextureLocation(Corpor wither){ return BRUTE_LOCATION; }

    protected void scale(Corpor corpor, MatrixStack stack, float f) {
        stack.scale(1.2F, 1.2F, 1.2F);
    }
}
