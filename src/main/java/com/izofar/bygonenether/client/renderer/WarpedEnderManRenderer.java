package com.izofar.bygonenether.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.WarpedEndermanModel;
import com.izofar.bygonenether.client.renderer.layers.ModEndermanEyesLayer;
import com.izofar.bygonenether.client.renderer.layers.ModHeldBlockLayer;
import com.izofar.bygonenether.entity.WarpedEndermanEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class WarpedEnderManRenderer extends MobRenderer<WarpedEndermanEntity, WarpedEndermanModel> {

	private static final Map<WarpedEndermanEntity.WarpedEnderManVariant, ResourceLocation> WARPED_ENDERMAN_LOCATION_MAP = ImmutableMap.of(
			WarpedEndermanEntity.WarpedEnderManVariant.FRESH, new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_fresh.png"),
			WarpedEndermanEntity.WarpedEnderManVariant.SHORT_VINE, new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_short_vine.png"),
			WarpedEndermanEntity.WarpedEnderManVariant.LONG_VINE, new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_long_vine.png")
	);

	private final Random random = new Random();

	public WarpedEnderManRenderer(EntityRendererManager manager) {
		super(manager, new WarpedEndermanModel(0.0F), 0.5F);
		this.addLayer(new ModEndermanEyesLayer(this));
		this.addLayer(new ModHeldBlockLayer(this));
	}

	@Override
	public void render(WarpedEndermanEntity warpedEndermanEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		BlockState blockstate = warpedEndermanEntity.getCarriedBlock();
		WarpedEndermanModel warpedEndermanModel = this.getModel();
		warpedEndermanModel.carrying = blockstate != null;
		warpedEndermanModel.creepy = warpedEndermanEntity.isCreepy();
		super.render(warpedEndermanEntity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

	@Override
	public Vector3d getRenderOffset(WarpedEndermanEntity warpedEndermanEntity, float partialTicks) {
		if (warpedEndermanEntity.isCreepy())
			return new Vector3d(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D);
		else return super.getRenderOffset(warpedEndermanEntity, partialTicks);
	}

	@Override
	public ResourceLocation getTextureLocation(WarpedEndermanEntity warpedEndermanEntity) {
		return WARPED_ENDERMAN_LOCATION_MAP.get(warpedEndermanEntity.getVariant());
	}

}
