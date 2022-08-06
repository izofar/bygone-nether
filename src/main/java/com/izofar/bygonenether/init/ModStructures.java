package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.world.structure.CatacombStructure;
import com.izofar.bygonenether.world.structure.CitadelStructure;
import com.izofar.bygonenether.world.structure.NetherFortressStructure;
import com.izofar.bygonenether.world.structure.PiglinManorStructure;
import com.izofar.bygonenether.world.structure.util.ModStructureUtils;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ModStructures {

	public static final DeferredRegister<Structure<?>> MODDED_STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BygoneNetherMod.MODID);

	public static final RegistryObject<Structure<NoFeatureConfig>> NETHER_FORTRESS = MODDED_STRUCTURES.register("fortress", NetherFortressStructure::new);
	public static final RegistryObject<Structure<NoFeatureConfig>> CATACOMB = MODDED_STRUCTURES.register("catacomb", CatacombStructure::new);
	public static final RegistryObject<Structure<NoFeatureConfig>> CITADEL = MODDED_STRUCTURES.register("citadel", CitadelStructure::new);
	public static final RegistryObject<Structure<NoFeatureConfig>> PIGLIN_MANOR = MODDED_STRUCTURES.register("piglin_manor", PiglinManorStructure::new);


	public static void setupStructures() {
		ModStructureUtils.setupMapSpacingAndLand(NETHER_FORTRESS.get(), new StructureSeparationSettings(27, 4, 1206458988), false);
		ModStructureUtils.setupMapSpacingAndLand(CATACOMB.get(), new StructureSeparationSettings(12, 4, 1163018812), false);
		ModStructureUtils.setupMapSpacingAndLand(CITADEL.get(), new StructureSeparationSettings(12, 4, 1621815507), true);
		ModStructureUtils.setupMapSpacingAndLand(PIGLIN_MANOR.get(), new StructureSeparationSettings(12, 4, 292421824), true);
	}
	
	public static void register(IEventBus eventBus) { MODDED_STRUCTURES.register(eventBus); }
}
