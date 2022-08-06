package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PiglinPrisonerRenderer extends PiglinRenderer {
	
	private static final ResourceLocation PIGLIN_PRISONER_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/piglin/piglin_prisoner.png");

	public PiglinPrisonerRenderer(EntityRendererManager entityRendererManager) {
		super(entityRendererManager, false);
	}

	@Override
	public ResourceLocation getTextureLocation(MobEntity entity) {
		return PIGLIN_PRISONER_LOCATION;
	}
}
