package com.izofar.bygonenether.event;

import com.izofar.bygonenether.init.ModConfiguredFeatures;
import com.izofar.bygonenether.init.ModConfiguredStructures;
import com.izofar.bygonenether.init.ModStructures;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class ModWorldEvents {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void addFeaturesToBiomes(final BiomeLoadingEvent event){
		if(event.getCategory() == Biome.Category.NETHER){
			event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_NETHER_FORTRESS);

			if(!event.getName().toString().equals("minecraft:basalt_deltas")) event.getGeneration().getStructures().removeIf((supplier) -> supplier.get() == StructureFeatures.BASTION_REMNANT);

			if(event.getName().toString().equals("minecraft:soul_sand_valley")) {
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CATACOMB);
				event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).add(() -> ModConfiguredFeatures.SOUL_STONE_BLOBS);
			}
			else if(event.getName().toString().equals("minecraft:crimson_forest"))
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_PIGLIN_MANOR);
			else if(event.getName().toString().equals("minecraft:warped_forest"))
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CITADEL);
			else if(event.getName().toString().equals("minecraft:basalt_deltas"))
				event.getGeneration().getStructures().add(() -> StructureFeatures.BASTION_REMNANT);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void addDimensionSpacing(final WorldEvent.Load event) {
		if (event.getWorld()instanceof ServerWorld serverWorld) {
			ChunkGenerator chunkGenerator = serverWorld.getChunkSource().getGenerator();

			if (chunkGenerator instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD)) return;

			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
			tempMap.putIfAbsent(ModStructures.NETHER_FORTRESS.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.NETHER_FORTRESS.get()));
			tempMap.putIfAbsent(ModStructures.CATACOMB.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.CATACOMB.get()));
			tempMap.putIfAbsent(ModStructures.CITADEL.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.CITADEL.get()));
			tempMap.putIfAbsent(ModStructures.PIGLIN_MANOR.get(), DimensionStructuresSettings.DEFAULTS.get(ModStructures.PIGLIN_MANOR.get()));
			chunkGenerator.getSettings().structureConfig = tempMap;
		}
	}
}
