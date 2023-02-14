package com.izofar.bygonenether.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.WarpedEndermanModel;
import com.izofar.bygonenether.client.renderer.layers.ModCarriedBlockLayer;
import com.izofar.bygonenether.client.renderer.layers.ModEnderEyesLayer;
import com.izofar.bygonenether.entity.WarpedEnderMan;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Random;

public class WarpedEnderManRenderer extends MobRenderer<WarpedEnderMan, WarpedEndermanModel> {
    private static final Map<WarpedEnderMan.WarpedEnderManVariant, ResourceLocation> WARPED_ENDERMAN_LOCATION_MAP = ImmutableMap.of(
            WarpedEnderMan.WarpedEnderManVariant.FRESH, new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_fresh.png"),
            WarpedEnderMan.WarpedEnderManVariant.SHORT_VINE, new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_short_vine.png"),
            WarpedEnderMan.WarpedEnderManVariant.LONG_VINE, new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_long_vine.png")
    );

    private final Random random = new Random();

    public WarpedEnderManRenderer(EntityRendererProvider.Context context) {
        super(context, new WarpedEndermanModel(WarpedEndermanModel.createBodyLayer().bakeRoot()), 0.5F);
        this.addLayer(new ModEnderEyesLayer<>(this));
        this.addLayer(new ModCarriedBlockLayer<>(this));
    }

    @Override
    public void render(WarpedEnderMan enderman, float p_114340_, float p_114341_, PoseStack p_114342_, MultiBufferSource p_114343_, int p_114344_) {
        BlockState blockstate = enderman.getCarriedBlock();
        WarpedEndermanModel endermanmodel = this.getModel();
        endermanmodel.carrying = blockstate != null;
        endermanmodel.creepy = enderman.isCreepy();
        super.render(enderman, p_114340_, p_114341_, p_114342_, p_114343_, p_114344_);
    }

    @Override
    public Vec3 getRenderOffset(WarpedEnderMan enderman, float offset) {
        return enderman.isCreepy() ? new Vec3(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D) : super.getRenderOffset(enderman, offset);
    }

    @Override
    public ResourceLocation getTextureLocation(WarpedEnderMan enderman) { return WARPED_ENDERMAN_LOCATION_MAP.get(enderman.getVariant()); }
}
