package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WarpedEnderManRenderer extends EndermanRenderer{

	private static final ResourceLocation WARPED_ENDERMAN_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman.png");
	
	public WarpedEnderManRenderer(EntityRendererManager entityRendererManager) { super(entityRendererManager); }

	@Override
	public ResourceLocation getTextureLocation(EndermanEntity entity) { return WARPED_ENDERMAN_LOCATION; }

}
