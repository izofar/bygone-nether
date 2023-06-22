package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.PiglinHunterModel;
import com.izofar.bygonenether.entity.PiglinHunter;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PiglinHunterRenderer extends HumanoidMobRenderer<PiglinHunter, PiglinHunterModel> {

    private static final ResourceLocation PIGLIN_HUNTER_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/piglin/piglin_hunter.png");

    public PiglinHunterRenderer(EntityRendererProvider.Context context) {
        super(context, new PiglinHunterModel(PiglinHunterModel.createBodyLayer().bakeRoot()), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
        this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PIGLIN_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PIGLIN_OUTER_ARMOR))));
    }

    @Override
    public ResourceLocation getTextureLocation(PiglinHunter piglinHunter) {
        return PIGLIN_HUNTER_LOCATION;
    }

    @Override
    protected boolean isShaking(PiglinHunter piglinHunter) {
        return super.isShaking(piglinHunter) || piglinHunter.isConverting();
    }
}
