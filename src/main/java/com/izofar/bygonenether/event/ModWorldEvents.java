package com.izofar.bygonenether.event;

import com.izofar.bygonenether.init.ModConfiguredFeatures;
import com.izofar.bygonenether.init.ModConfiguredStructures;
import com.izofar.bygonenether.init.ModStructures;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.Map;

public abstract class ModWorldEvents {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void addFeaturesToBiomes(final BiomeLoadingEvent event){
		if(event.getCategory() == Biome.Category.NETHER){
			event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_NETHER_FORTRESS);
			event.getGeneration().getStructures().removeIf((supplier) -> supplier.get().feature == StructureFeatures.NETHER_BRIDGE.feature);
			if(event.getName().equals(Biomes.SOUL_SAND_VALLEY.location())) {
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CATACOMB);
				event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).add(() -> ModConfiguredFeatures.SOUL_STONE_BLOBS);
			}
			else if(event.getName().equals(Biomes.CRIMSON_FOREST.location()))
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_PIGLIN_MANOR);
			else if(event.getName().equals(Biomes.WARPED_FOREST.location()))
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CITADEL);

			if(event.getName().equals(Biomes.BASALT_DELTAS.location()))
				event.getGeneration().getStructures().add(() -> StructureFeatures.BASTION_REMNANT);
			else
				event.getGeneration().getStructures().removeIf((supplier) -> supplier.get().config instanceof VillageConfig && ((VillageConfig) supplier.get().config).startPool().get().getName().equals(StructureFeatures.BASTION_REMNANT.config.startPool().get().getName()));

			if(ModList.get().isLoaded("biomesoplenty")){
				if (event.getName().toString().equals("biomesoplenty:crystalline_chasm")
						|| event.getName().toString().equals("biomesoplenty:undergrowth")
						|| event.getName().toString().equals("biomesoplenty:visceral_heap"))
					;// Nether Fortress Only
				else if(event.getName().toString().equals("biomesoplenty:withered_abyss"))
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CATACOMB);
			}

			if(ModList.get().isLoaded("cinderscapes")){
				if (event.getName().toString().equals("cinderscapes:quartz_canyon")
						|| event.getName().toString().equals("cinderscapes:luminous_grove"))
					;// Nether Fortress Only
				else if(event.getName().toString().equals("cinderscapes:ashy_shoals")
						|| event.getName().toString().equals("cinderscapes:blackstone_shales")) {
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CATACOMB);
					event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).add(() -> ModConfiguredFeatures.SOUL_STONE_BLOBS);
				}
			}

			if(ModList.get().isLoaded("betternether")){
				if (event.getName().toString().equals("betternether:bone_reef")
					|| event.getName().toString().equals("betternether:nether_jungle")
					|| event.getName().toString().equals("betternether:old_warped_woods")
					|| event.getName().toString().equals("betternether:upside_down_forest"))
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CITADEL);
				else if (event.getName().toString().equals("betternether:crimson_glowing_woods")
						|| event.getName().toString().equals("betternether:crimson_pinewood"))
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_PIGLIN_MANOR);
				else if (event.getName().toString().equals("betternether:flooded_deltas"))
					event.getGeneration().getStructures().add(() -> StructureFeatures.BASTION_REMNANT);
				else if(event.getName().toString().equals("betternether:soul_plain")) {
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CATACOMB);
					event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).add(() -> ModConfiguredFeatures.SOUL_STONE_BLOBS);
				} else if(event.getName().toString().equals("betternether:nether_grasslands")
						|| event.getName().toString().equals("betternether:poor_nether_grasslands")
						|| event.getName().toString().equals("betternether:wart_forest")
						|| event.getName().toString().equals("betternether:wart_forest_edge"))
					event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).add(() -> ModConfiguredFeatures.SOUL_STONE_BLOBS);
				else if(event.getName().toString().equals("betternether:gravel_desert")
						|| event.getName().toString().equals("betternether:magma_land")
						|| event.getName().toString().equals("betternether:nether_mushroom_forest")
						|| event.getName().toString().equals("betternether:nether_mushroom_forest_edge")
						|| event.getName().toString().equals("betternether:nether_swampland")
						|| event.getName().toString().equals("betternether:nether_swampland_terraces")
						|| event.getName().toString().equals("betternether:old_fungiwoods")
						|| event.getName().toString().equals("betternether:old_swampland")
						|| event.getName().toString().equals("betternether:sulfuric_bone_reef"))
					;// Nether Fortress Only
			}

			if(ModList.get().isLoaded("infernalexp")){
				if (event.getName().toString().equals("infernalexp:glowstone_canyon"))
					;// Nether Fortress Only
			}

		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void addDimensionSpacing(final WorldEvent.Load event) {
		if (event.getWorld()instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld) event.getWorld();
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
