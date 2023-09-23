package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.WexModel;
import com.izofar.bygonenether.entity.Wex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class WexRenderer extends HumanoidMobRenderer<Wex, WexModel> {

    private static final ResourceLocation WEX_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wex/wex.png");
    private static final ResourceLocation WEX_CHARGING_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wex/wex_charging.png");

    public WexRenderer(EntityRendererProvider.Context context) {
        super(context, new WexModel(WexModel.createBodyLayer().bakeRoot()), 0.3F);
    }

    @Override
    protected int getBlockLightLevel(Wex wex, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(Wex wex) {
        return wex.isCharging() ? WEX_CHARGING_LOCATION : WEX_LOCATION;
    }

    @Override
    protected void scale(Wex wex, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(0.4F, 0.4F, 0.4F);
    }

}
