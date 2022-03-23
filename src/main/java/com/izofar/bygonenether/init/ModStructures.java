package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.world.structure.CatacombStructure;
import com.izofar.bygonenether.world.structure.CitadelStructure;
import com.izofar.bygonenether.world.structure.NetherFortressStructure;
import com.izofar.bygonenether.world.structure.PiglinManorStructure;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ModStructures {

	public static final DeferredRegister<StructureFeature<?>> MODDED_STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BygoneNetherMod.MODID);

	public static final RegistryObject<StructureFeature<JigsawConfiguration>> NETHER_FORTRESS = MODDED_STRUCTURES.register("fortress", () -> new NetherFortressStructure(JigsawConfiguration.CODEC));
	public static final RegistryObject<StructureFeature<JigsawConfiguration>> CATACOMB = MODDED_STRUCTURES.register("catacomb", () -> new CatacombStructure(JigsawConfiguration.CODEC));
	public static final RegistryObject<StructureFeature<JigsawConfiguration>> CITADEL = MODDED_STRUCTURES.register("citadel", () -> new CitadelStructure(JigsawConfiguration.CODEC));
	public static final RegistryObject<StructureFeature<JigsawConfiguration>> PIGLIN_MANOR = MODDED_STRUCTURES.register("piglin_manor", () -> new PiglinManorStructure(JigsawConfiguration.CODEC));
	
	public static void register(IEventBus eventBus) { MODDED_STRUCTURES.register(eventBus); }
}
