package com.izofar.bygonenether.event;

import com.izofar.bygonenether.init.ModFeatures;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ModWorldEvents {

	@SubscribeEvent
	public static void setupPlacedFeatures(final BiomeLoadingEvent event) {
		if (event.getName().equals(Biomes.SOUL_SAND_VALLEY.location())) {
			event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModFeatures.SOUL_STONE_BLOBS_PLACED);
		}
	}

}
