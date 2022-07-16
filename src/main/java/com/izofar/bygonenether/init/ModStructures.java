package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.world.structure.CatacombStructure;
import com.izofar.bygonenether.world.structure.CitadelStructure;
import com.izofar.bygonenether.world.structure.NetherFortressStructure;
import com.izofar.bygonenether.world.structure.PiglinManorStructure;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public abstract class ModStructures {

	public static final DeferredRegister<StructureType<?>> MODDED_STRUCTURES = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, BygoneNetherMod.MODID);

	public static final RegistryObject<StructureType<NetherFortressStructure>> NETHER_FORTRESS = MODDED_STRUCTURES.register("fortress", () -> () -> NetherFortressStructure.CODEC);
	public static final RegistryObject<StructureType<CatacombStructure>> CATACOMB = MODDED_STRUCTURES.register("catacomb", () -> () -> CatacombStructure.CODEC);
	public static final RegistryObject<StructureType<CitadelStructure>> CITADEL = MODDED_STRUCTURES.register("citadel", () -> () -> CitadelStructure.CODEC);
	public static final RegistryObject<StructureType<PiglinManorStructure>> PIGLIN_MANOR = MODDED_STRUCTURES.register("piglin_manor", () -> () -> PiglinManorStructure.CODEC);
	
	public static void register(IEventBus eventBus) { MODDED_STRUCTURES.register(eventBus); }
}
