package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public abstract class ModConfiguredStructures {

	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_NETHER_FORTRESS = ModStructures.NETHER_FORTRESS.get().configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_CATACOMB = ModStructures.CATACOMB.get().configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_CITADEL = ModStructures.CITADEL.get().configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));
	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_PIGLIN_MANOR = ModStructures.PIGLIN_MANOR.get().configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));

	public static void registerConfiguredStructures() {
		Registry<ConfiguredStructureFeature<?, ?>> CONFIGURED_STRUCTURES = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;

		Registry.register(CONFIGURED_STRUCTURES, new ResourceLocation(BygoneNetherMod.MODID, "configured_fortress"), CONFIGURED_NETHER_FORTRESS);
		Registry.register(CONFIGURED_STRUCTURES, new ResourceLocation(BygoneNetherMod.MODID, "configured_catacomb"), CONFIGURED_CATACOMB);
		Registry.register(CONFIGURED_STRUCTURES, new ResourceLocation(BygoneNetherMod.MODID, "configured_citadel"), CONFIGURED_CITADEL);
		Registry.register(CONFIGURED_STRUCTURES, new ResourceLocation(BygoneNetherMod.MODID, "configured_piglin_manor"), CONFIGURED_PIGLIN_MANOR);
	}
}
