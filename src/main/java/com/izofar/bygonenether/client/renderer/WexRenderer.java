package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.WexModel;
import com.izofar.bygonenether.entity.WexEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WexRenderer extends BipedRenderer<WexEntity, WexModel> {

	private static final ResourceLocation WEX_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wex/wex.png");
	private static final ResourceLocation WEX_CHARGING_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/wex/wex_charging.png");

	public WexRenderer(EntityRendererManager entityRendererManager) {
		super(entityRendererManager, new WexModel(), 0.3F);
	}

	@Override
	protected int getBlockLightLevel(WexEntity wexEntity, BlockPos blockPos) {
		return 15;
	}

	@Override
	public ResourceLocation getTextureLocation(WexEntity wex) {
		return wex.isCharging() ? WEX_CHARGING_LOCATION : WEX_LOCATION;
	}

	@Override
	protected void scale(WexEntity wexEntity, MatrixStack matrixStack, float partialTicks) {
		matrixStack.scale(0.4F, 0.4F, 0.4F);
	}

}
