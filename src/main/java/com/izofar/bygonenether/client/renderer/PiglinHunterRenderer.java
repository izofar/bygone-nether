package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.PiglinHunterModel;
import com.izofar.bygonenether.entity.PiglinHunterEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PiglinHunterRenderer extends BipedRenderer<PiglinHunterEntity, PiglinHunterModel> {

    private static final ResourceLocation PIGLIN_HUNTER_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/piglin_hunter.png");

    public PiglinHunterRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, createModel(false), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
        this.addLayer(new BipedArmorLayer(this, new BipedModel(0.5F), new BipedModel(1.02F)));
    }

    private static PiglinHunterModel createModel(boolean wearingArmor) {
        PiglinHunterModel piglinHunterModel = new PiglinHunterModel(0.0F, 128, 64);
        if (wearingArmor) piglinHunterModel.earLeft.visible = false;
        return piglinHunterModel;
    }

    public ResourceLocation getTextureLocation(PiglinHunterEntity hunter) throws IllegalArgumentException { return PIGLIN_HUNTER_LOCATION; }

    protected boolean isShaking(PiglinHunterEntity hunter) { return super.isShaking(hunter) || hunter instanceof AbstractPiglinEntity && hunter.isConverting(); }
}
