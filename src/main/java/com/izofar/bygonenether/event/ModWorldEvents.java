package com.izofar.bygonenether.event;

import com.izofar.bygonenether.init.ModFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public abstract class ModWorldEvents {

	@SubscribeEvent
	public static void setupPlacedFeatures(final BiomeLoadingEvent event) {
		if (isBiome(event, Biomes.SOUL_SAND_VALLEY)
			|| (isLoaded("biomesoplenty")
				&& isBiome(event, "biomesoplenty:withered_abyss"))
			|| (isLoaded("incendium")
				&& isBiome(event, "incendium:weeping_valley")
				&& isBiome(event, "incendium:withered_forest"))
			|| (isLoaded("byg")
				&& isBiome(event, "byg:wailing_garth"))
			|| (isLoaded("cinderscapes")
				&& isBiome(event, "cinderscapes:ashy_shoals")
				&& isBiome(event, "cinderscapes:blackstone_shales"))
			|| (isLoaded("betternether")
				&& isBiome(event, "betternether:soul_plain"))
		) {
			event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModFeatures.SOUL_STONE_BLOBS_PLACED);
		}
	}

	private static boolean isLoaded(String modid) { return ModList.get().isLoaded(modid); }
	private static boolean isBiome(final BiomeLoadingEvent event, ResourceKey<Biome> biomeKey) { return event.getName().equals(biomeKey.location()); }
	private static boolean isBiome(final BiomeLoadingEvent event, String location) { return event.getName().equals(location); }

}
