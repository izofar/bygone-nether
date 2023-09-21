package com.izofar.bygonenether.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.WarpedEndermanModel;
import com.izofar.bygonenether.client.renderer.layers.ModCarriedBlockLayer;
import com.izofar.bygonenether.client.renderer.layers.ModEnderEyesLayer;
import com.izofar.bygonenether.entity.WarpedEnderMan;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class WarpedEnderManRenderer extends MobRenderer<WarpedEnderMan, WarpedEndermanModel> {

    private static final Map<WarpedEnderMan.WarpedEnderManVariant, ResourceLocation> WARPED_ENDERMAN_LOCATION_MAP = ImmutableMap.of(
            WarpedEnderMan.WarpedEnderManVariant.FRESH, new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_fresh.png"),
            WarpedEnderMan.WarpedEnderManVariant.SHORT_VINE, new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_short_vine.png"),
            WarpedEnderMan.WarpedEnderManVariant.LONG_VINE, new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_long_vine.png")
    );

    public WarpedEnderManRenderer(EntityRendererProvider.Context context) {
        super(context, new WarpedEndermanModel(WarpedEndermanModel.createBodyLayer().bakeRoot()), 0.5F);
        this.addLayer(new ModEnderEyesLayer<>(this));
        this.addLayer(new ModCarriedBlockLayer<>(this));
    }

    @Override
    public void render(WarpedEnderMan warpedEnderMan, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        BlockState blockstate = warpedEnderMan.getCarriedBlock();
        WarpedEndermanModel endermanmodel = this.getModel();
        endermanmodel.carrying = blockstate != null;
        endermanmodel.creepy = warpedEnderMan.isCreepy();
        super.render(warpedEnderMan, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public Vec3 getRenderOffset(WarpedEnderMan warpedEnderMan, float partialTicks) {
        return warpedEnderMan.isCreepy() ? new Vec3(warpedEnderMan.getRandom().nextGaussian() * 0.02D, 0.0D, warpedEnderMan.getRandom().nextGaussian() * 0.02D) : super.getRenderOffset(warpedEnderMan, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(WarpedEnderMan warpedEnderMan) {
        return WARPED_ENDERMAN_LOCATION_MAP.get(warpedEnderMan.getVariant());
    }
}
