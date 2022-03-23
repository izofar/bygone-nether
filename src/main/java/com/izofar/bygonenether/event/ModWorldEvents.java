package com.izofar.bygonenether.event;

import com.izofar.bygonenether.init.ModStructures;
import com.izofar.bygonenether.world.structure.CatacombStructure;
import com.izofar.bygonenether.world.structure.CitadelStructure;
import com.izofar.bygonenether.world.structure.NetherFortressStructure;
import com.izofar.bygonenether.world.structure.PiglinManorStructure;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ModWorldEvents {
	
	@SubscribeEvent
	public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
		if (event.getStructure() == ModStructures.NETHER_FORTRESS.get())
			event.addEntitySpawns(MobCategory.MONSTER, NetherFortressStructure.FORTRESS_ENEMIES);
		else if (event.getStructure() == ModStructures.CATACOMB.get())
			event.addEntitySpawns(MobCategory.MONSTER, CatacombStructure.FORTRESS_ENEMIES);
		else if (event.getStructure() == ModStructures.CITADEL.get())
			event.addEntitySpawns(MobCategory.MONSTER, CitadelStructure.CITADEL_ENEMIES);
		else if (event.getStructure() == ModStructures.PIGLIN_MANOR.get())
			event.addEntitySpawns(MobCategory.MONSTER, PiglinManorStructure.MANOR_ENEMIES);
	}
/*
	@SubscribeEvent
	public static void setupPlacedFeatures(final BiomeLoadingEvent event){
		if(event.getCategory() != BiomeCategory.NETHER) return;

		if(event.getName().equals(Biomes.SOUL_SAND_VALLEY.location()))
			event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModFeatures.SOUL_STONE_BLOBS.get());

		if(event.getName().equals(Biomes.SOUL_SAND_VALLEY.location()))
			event.getGeneration().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).add(Holder.direct(ModConfiguredFeatures.CATACOMB_PLACED));
		if(event.getName().equals(Biomes.CRIMSON_FOREST.location()))
			event.getGeneration().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).add(Holder.direct(ModConfiguredFeatures.PIGLIN_MANOR_PLACED));
		if(event.getName().equals(Biomes.WARPED_FOREST.location()))
			event.getGeneration().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).add(Holder.direct(ModConfiguredFeatures.CITADEL_PLACED));
		if(event.getName().equals(Biomes.BASALT_DELTAS.location()))
			event.getGeneration().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).add(Holder.direct(ModConfiguredFeatures.BASTION_PLACED));
	}*/
	/*
	@SubscribeEvent
	public static void addDimensionSpacing(final WorldEvent.Load event) {
		if (event.getWorld()instanceof ServerLevel serverLevel) {
			ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();

			if (chunkGenerator instanceof FlatLevelSource && serverLevel.dimension().equals(Level.OVERWORLD)) return;

			HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> multimap = new HashMap<>();
			HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> blacklist_multimap = new HashMap<>();

			Set<Entry<ResourceKey<Biome>, Biome>> entrySet = serverLevel.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).entrySet();

			for (Entry<ResourceKey<Biome>, Biome> biomeEntry : entrySet) {
				Biome biome = biomeEntry.getValue();
				ResourceKey<Biome> biomeKey = biomeEntry.getKey();
				Biome.BiomeCategory biomeCategory = biome.getBiomeCategory();

				if (biomeCategory == BiomeCategory.NETHER) {
					
					associateBiomeToConfiguredStructure(multimap, ModConfiguredStructures.CONFIGURED_NETHER_FORTRESS, biomeKey);

					if (biome == getBiomeFromEntrySet(entrySet, Biomes.BASALT_DELTAS))
						associateBiomeToConfiguredStructure(multimap, StructureFeatures.BASTION_REMNANT, biomeKey);
					else
						disassociateBiomeToConfiguredStructure(blacklist_multimap, StructureFeatures.BASTION_REMNANT, biomeKey);
					
					if(biome == getBiomeFromEntrySet(entrySet, Biomes.WARPED_FOREST))
						associateBiomeToConfiguredStructure(multimap, ModConfiguredStructures.CONFIGURED_CITADEL, biomeKey);
					else if(biome == getBiomeFromEntrySet(entrySet, Biomes.CRIMSON_FOREST))
						associateBiomeToConfiguredStructure(multimap, ModConfiguredStructures.CONFIGURED_PIGLIN_MANOR, biomeKey);
					else if(biome == getBiomeFromEntrySet(entrySet, Biomes.SOUL_SAND_VALLEY))
						associateBiomeToConfiguredStructure(multimap, ModConfiguredStructures.CONFIGURED_CATACOMB, biomeKey);

					if(biome != getBiomeFromEntrySet(entrySet, Biomes.NETHER_WASTES)) disassociateBiomeToConfiguredStructure(blacklist_multimap, StructureFeatures.NETHER_BRIDGE, biomeKey);

					if(ModList.get().isLoaded("biomesoplenty")){
						if (biome.getRegistryName().toString().equals("biomesoplenty:crystalline_chasm")
								|| biome.getRegistryName().toString().equals("biomesoplenty:erupting_inferno")
								|| biome.getRegistryName().toString().equals("biomesoplenty:undergrowth")
								|| biome.getRegistryName().toString().equals("biomesoplenty:visceral_heap"))
							associateBiomeToConfiguredStructure(multimap, ModConfiguredStructures.CONFIGURED_NETHER_FORTRESS, biomeKey);
						else if(biome.getRegistryName().toString().equals("biomesoplenty:withered_abyss"))
							associateBiomeToConfiguredStructure(multimap, ModConfiguredStructures.CONFIGURED_CATACOMB, biomeKey);
					}

				}
				
			}
			
			ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();

			StructureSettings structureSettings = chunkGenerator.getSettings();
			
			structureSettings.configuredStructures.entrySet().stream().filter(entry -> !multimap.containsKey(entry.getKey()) && !blacklist_multimap.containsKey(entry.getKey())).forEach(tempStructureToMultiMap::put);
			multimap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));
			structureSettings.configuredStructures = tempStructureToMultiMap.build();

			Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureSettings.structureConfig());
			tempMap.putIfAbsent(ModStructures.NETHER_FORTRESS.get(), StructureSettings.DEFAULTS.get(ModStructures.NETHER_FORTRESS.get()));
			tempMap.putIfAbsent(ModStructures.CATACOMB.get(), StructureSettings.DEFAULTS.get(ModStructures.CATACOMB.get()));
			tempMap.putIfAbsent(ModStructures.CITADEL.get(), StructureSettings.DEFAULTS.get(ModStructures.CITADEL.get()));
			tempMap.putIfAbsent(ModStructures.PIGLIN_MANOR.get(), StructureSettings.DEFAULTS.get(ModStructures.PIGLIN_MANOR.get()));
			structureSettings.structureConfig = tempMap;
		}
	}
	
	private static void disassociateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> multimap, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
		multimap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
		HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = multimap.get(configuredStructureFeature.feature);
		configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
	}

	private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> multimap, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
		multimap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
		HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = multimap.get(configuredStructureFeature.feature);

		if (configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey))
			BygoneNetherMod.LOGGER.error(
					"""
					    2 ConfiguredStructureFeatures sharing a StructureFeature were be added to the same biome. 
					    One will be prevented from spawning.
					    The two conflicting ConfiguredStructures are: {}, {}
					    The biome that is attempting to be shared: {}
					""",
					BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature),
					BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap
							.entries()
							.stream()
							.filter(e -> e.getValue() == biomeRegistryKey)
							.findFirst()
							.get()
							.getKey()
						),
					biomeRegistryKey);
		else 
			configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
	}

	private static Biome getBiomeFromEntrySet(Set<Entry<ResourceKey<Biome>, Biome>> entrySet, ResourceKey<Biome> biome) {
		for (Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : entrySet)
			if (biomeEntry.getKey().equals(biome))
				return biomeEntry.getValue();
		return null;
	}*/

}
