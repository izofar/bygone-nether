package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.PiglinHunterModel;
import com.izofar.bygonenether.entity.PiglinHunter;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PiglinHunterRenderer extends HumanoidMobRenderer<PiglinHunter, PiglinHunterModel> {
    private static final ResourceLocation PIGLIN_HUNTER_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/piglin_hunter.png");

    public PiglinHunterRenderer(EntityRendererProvider.Context context, ModelLayerLocation layer1, ModelLayerLocation layer3, boolean wearingArmor) {
        super(context, new PiglinHunterModel(PiglinHunterModel.createBodyLayer().bakeRoot()), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
        this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel<>(context.bakeLayer(layer1)), new HumanoidModel<>(context.bakeLayer(layer3))));
    }

    private static PiglinModel<PiglinHunter> createModel(EntityModelSet modelset, ModelLayerLocation layer, boolean wearingArmor) {
        PiglinModel<PiglinHunter> piglinmodel = new PiglinModel<>(modelset.bakeLayer(layer));
        if (wearingArmor) piglinmodel.rightEar.visible = false;
        return piglinmodel;
    }

    public ResourceLocation getTextureLocation(PiglinHunter hunter) throws IllegalArgumentException {
        return PIGLIN_HUNTER_LOCATION;
    }

    protected boolean isShaking(PiglinHunter hunter) { return super.isShaking(hunter) || hunter instanceof AbstractPiglin && hunter.isConverting(); }
}
