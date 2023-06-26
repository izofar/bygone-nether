package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.WexModel;
import com.izofar.bygonenether.entity.Wex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WexRenderer extends MobRenderer<Wex, WexModel> {

	private static final ResourceLocation WEX_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wex/wex.png");
	private static final ResourceLocation WEX_CHARGING_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wex/wex_charging.png");

	public WexRenderer(Context context) {
		super(context, new WexModel(WexModel.createBodyLayer().bakeRoot()), 0.3F);
	}

	@Override
	protected int getBlockLightLevel(Wex wex, BlockPos blockPos) {
		return 15;
	}

	@Override
	public ResourceLocation getTextureLocation(Wex wex) {
		return wex.isCharging() ? WEX_CHARGING_LOCATION : WEX_LOCATION;
	}

	@Override
	protected void scale(Wex wex, PoseStack poseStack, float partialTicks) {
		poseStack.scale(0.4F, 0.4F, 0.4F);
	}
}
