package com.izofar.bygonenether.event;

import com.izofar.bygonenether.init.ModConfiguredFeatures;
import com.izofar.bygonenether.init.ModConfiguredStructures;
import com.izofar.bygonenether.init.ModStructures;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public abstract class ModWorldEvents {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void addFeaturesToBiomes(final BiomeLoadingEvent event){
		if(event.getCategory() == Biome.Category.NETHER){
			event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_NETHER_FORTRESS);
			event.getGeneration().getStructures().removeIf((supplier) -> supplier.get().feature == StructureFeatures.NETHER_BRIDGE.feature);
			if(isBiome(event, Biomes.SOUL_SAND_VALLEY.location())) {
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CATACOMB);
				event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).add(() -> ModConfiguredFeatures.SOUL_STONE_BLOBS);
			}
			else if(isBiome(event, Biomes.CRIMSON_FOREST.location()))
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_PIGLIN_MANOR);
			else if(isBiome(event, Biomes.WARPED_FOREST.location()))
				event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CITADEL);

			if(isBiome(event, Biomes.BASALT_DELTAS.location()))
				event.getGeneration().getStructures().add(() -> StructureFeatures.BASTION_REMNANT);

			if(isLoaded("biomesoplenty")){
				if (isBiome(event, "biomesoplenty:crystalline_chasm")
						|| isBiome(event, "biomesoplenty:undergrowth")
						|| isBiome(event, "biomesoplenty:visceral_heap"))
					;// Nether Fortress Only
				else if(isBiome(event, "biomesoplenty:withered_abyss"))
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CATACOMB);
			}

			if(isLoaded("cinderscapes")){
				if (isBiome(event, "cinderscapes:quartz_canyon")
						|| isBiome(event, "cinderscapes:luminous_grove"))
					;// Nether Fortress Only
				else if(isBiome(event, "cinderscapes:ashy_shoals")
						|| isBiome(event, "cinderscapes:blackstone_shales")) {
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CATACOMB);
					event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).add(() -> ModConfiguredFeatures.SOUL_STONE_BLOBS);
				}
			}

			if(isLoaded("betternether")){
				if (isBiome(event, "betternether:bone_reef")
					|| isBiome(event, "betternether:nether_jungle")
					|| isBiome(event, "betternether:old_warped_woods")
					|| isBiome(event, "betternether:upside_down_forest"))
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CITADEL);
				else if (isBiome(event, "betternether:crimson_glowing_woods")
						|| isBiome(event, "betternether:crimson_pinewood"))
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_PIGLIN_MANOR);
				else if (isBiome(event, "betternether:flooded_deltas"))
					event.getGeneration().getStructures().add(() -> StructureFeatures.BASTION_REMNANT);
				else if(isBiome(event, "betternether:soul_plain")) {
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CATACOMB);
					event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).add(() -> ModConfiguredFeatures.SOUL_STONE_BLOBS);
				} else if(isBiome(event, "betternether:nether_grasslands")
						|| isBiome(event, "betternether:poor_nether_grasslands")
						|| isBiome(event, "betternether:wart_forest")
						|| isBiome(event, "betternether:wart_forest_edge"))
					event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).add(() -> ModConfiguredFeatures.SOUL_STONE_BLOBS);
				else if(isBiome(event, "betternether:gravel_desert")
						|| isBiome(event, "betternether:magma_land")
						|| isBiome(event, "betternether:nether_mushroom_forest")
						|| isBiome(event, "betternether:nether_mushroom_forest_edge")
						|| isBiome(event, "betternether:nether_swampland")
						|| isBiome(event, "betternether:nether_swampland_terraces")
						|| isBiome(event, "betternether:old_fungiwoods")
						|| isBiome(event, "betternether:old_swampland")
						|| isBiome(event, "betternether:sulfuric_bone_reef"))
					;// Nether Fortress Only
			}

			if(isLoaded("infernalexp")){
				if (isBiome(event, "infernalexp:glowstone_canyon"))
					;// Nether Fortress Only
			}

			if(isLoaded("byg")){
				if (isBiome(event, "byg:waiting_garth"))
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CATACOMB);
				else if(isBiome(event, "byg:crimson_gardens"))
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_PIGLIN_MANOR);
				else if(isBiome(event, "byg:glowstone_gardens"))
					event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_CITADEL);
			}

		}
	}

	private static boolean isLoaded(String modid){ return ModList.get().isLoaded(modid); }
	private static boolean isBiome(final BiomeLoadingEvent event, String key){ return event.getName().toString().equals(key); }
	private static boolean isBiome(final BiomeLoadingEvent event, ResourceLocation location){ return event.getName().equals(location); }

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void addDimensionSpacing(final WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerWorld) {
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
