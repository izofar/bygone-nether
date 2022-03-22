package com.izofar.bygonenether.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PiglinModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ModPiglinRenderer extends BipedRenderer<MobEntity, PiglinModel<MobEntity>> {
	
	private static final Map<EntityType<?>, ResourceLocation> TEXTURES = ImmutableMap.of(ModEntityTypes.PIGLIN_PRISONER.get(), new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/piglin_prisoner.png"));

	public ModPiglinRenderer(EntityRendererManager entityRendererManager, boolean wearingArmor) {
		super(entityRendererManager, createModel(wearingArmor), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
		this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.02F)));
	}

	private static PiglinModel<MobEntity> createModel(boolean wearingArmor) {
		PiglinModel<MobEntity> piglinmodel = new PiglinModel<>(0.0F, 64, 64);
		if (wearingArmor) piglinmodel.earLeft.visible = false;
		return piglinmodel;
	}

	public ResourceLocation getTextureLocation(MobEntity p_115708_) throws IllegalArgumentException {
		ResourceLocation resourcelocation = TEXTURES.get(p_115708_.getType());
		if (resourcelocation == null) throw new IllegalArgumentException("Missing Texture for Layer: " + p_115708_.getType());
		else return resourcelocation;
	}

	protected boolean isShaking(MobEntity p_115712_) { return super.isShaking(p_115712_) || p_115712_ instanceof AbstractPiglinEntity && ((AbstractPiglinEntity) p_115712_).isConverting(); }
}
