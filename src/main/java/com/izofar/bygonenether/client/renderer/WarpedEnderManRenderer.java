package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;

import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WarpedEnderManRenderer extends EndermanRenderer{

	private static final ResourceLocation WARPED_ENDERMAN_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman.png");
	
	public WarpedEnderManRenderer(Context context) { super(context); }

	@Override
	public ResourceLocation getTextureLocation(EnderMan entity) { return WARPED_ENDERMAN_LOCATION; }

}
