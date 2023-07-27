package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.CorporModel;
import com.izofar.bygonenether.entity.CorporEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CorporRenderer extends BipedRenderer<CorporEntity, CorporModel> {

    private static final ResourceLocation CORPOR_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wither/corpor.png");

    public CorporRenderer(EntityRendererManager manager) {
        super(manager, new CorporModel(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(CorporEntity corporEntity){
        return CORPOR_LOCATION;
    }

    @Override
    protected void scale(CorporEntity corporEntity, MatrixStack stack, float partialTicks) {
        stack.scale(1.2F, 1.2F, 1.2F);
    }
}
