package com.izofar.bygonenether.event;

import com.izofar.bygonenether.init.ModFeatures;
import com.izofar.bygonenether.init.ModStructures;
import com.izofar.bygonenether.world.structure.CatacombStructure;
import com.izofar.bygonenether.world.structure.CitadelStructure;
import com.izofar.bygonenether.world.structure.NetherFortressStructure;
import com.izofar.bygonenether.world.structure.PiglinManorStructure;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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

	@SubscribeEvent
	public static void setupPlacedFeatures(final BiomeLoadingEvent event){
		if(event.getName().equals(Biomes.SOUL_SAND_VALLEY.location()))
			event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModFeatures.SOUL_STONE_BLOBS_PLACED);
	}

}
