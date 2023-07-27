package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public abstract class ModConfiguredStructures {

	public static final StructureFeature<?, ?> CONFIGURED_CATACOMB = ModStructures.CATACOMB.get().configured(IFeatureConfig.NONE);
	public static final StructureFeature<?, ?> CONFIGURED_CITADEL = ModStructures.CITADEL.get().configured(IFeatureConfig.NONE);
	public static final StructureFeature<?, ?> CONFIGURED_PIGLIN_MANOR = ModStructures.PIGLIN_MANOR.get().configured(IFeatureConfig.NONE);

	public static void registerConfiguredStructures() {
		Registry<StructureFeature<?, ?>> CONFIGURED_STRUCTURES = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;

		Registry.register(CONFIGURED_STRUCTURES, new ResourceLocation(BygoneNetherMod.MODID, "configured_catacomb"), CONFIGURED_CATACOMB);
		Registry.register(CONFIGURED_STRUCTURES, new ResourceLocation(BygoneNetherMod.MODID, "configured_citadel"), CONFIGURED_CITADEL);
		Registry.register(CONFIGURED_STRUCTURES, new ResourceLocation(BygoneNetherMod.MODID, "configured_piglin_manor"), CONFIGURED_PIGLIN_MANOR);
	}
}
