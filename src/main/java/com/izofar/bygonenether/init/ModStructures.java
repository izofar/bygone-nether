package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.world.structure.CatacombStructure;
import com.izofar.bygonenether.world.structure.CitadelStructure;
import com.izofar.bygonenether.world.structure.PiglinManorStructure;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class ModStructures {

    public static final StructureType<CatacombStructure> CATACOMB = () -> CatacombStructure.CODEC;
    public static final StructureType<CitadelStructure> CITADEL = () -> CitadelStructure.CODEC;
    public static final StructureType<PiglinManorStructure> PIGLIN_MANOR = () -> PiglinManorStructure.CODEC;

    public static void registerStructures() {
        Registry.register(BuiltInRegistries.STRUCTURE_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "catacomb"), CATACOMB);
        Registry.register(BuiltInRegistries.STRUCTURE_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "citadel"), CITADEL);
        Registry.register(BuiltInRegistries.STRUCTURE_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "piglin_manor"), PIGLIN_MANOR);
    }
}
