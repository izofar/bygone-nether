package com.izofar.bygonenether.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ModPiglinRenderer extends HumanoidMobRenderer<Mob, PiglinModel<Mob>> {
	
	private static final Map<EntityType<?>, ResourceLocation> TEXTURES = ImmutableMap.of(ModEntityTypes.PIGLIN_PRISONER.get(), new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/piglin_prisoner.png"));

	public ModPiglinRenderer(EntityRendererProvider.Context context, ModelLayerLocation layer0, ModelLayerLocation layer1, ModelLayerLocation layer3, boolean wearingArmor) {
		super(context, createModel(context.getModelSet(), layer0, wearingArmor), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
		this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(layer1)), new HumanoidModel<>(context.bakeLayer(layer3))));
	}

	private static PiglinModel<Mob> createModel(EntityModelSet modelset, ModelLayerLocation layer, boolean wearingArmor) {
		PiglinModel<Mob> piglinmodel = new PiglinModel<>(modelset.bakeLayer(layer));
		if (wearingArmor) piglinmodel.rightEar.visible = false;
		return piglinmodel;
	}

	public ResourceLocation getTextureLocation(Mob p_115708_) throws IllegalArgumentException {
		ResourceLocation resourcelocation = TEXTURES.get(p_115708_.getType());
		if (resourcelocation == null) throw new IllegalArgumentException("Missing Texture for Layer: " + p_115708_.getType());
		else return resourcelocation;
	}

	protected boolean isShaking(Mob p_115712_) { return super.isShaking(p_115712_) || p_115712_ instanceof AbstractPiglin && ((AbstractPiglin) p_115712_).isConverting(); }
}
