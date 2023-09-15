package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.world.structure.CatacombStructure;
import com.izofar.bygonenether.world.structure.CitadelStructure;
import com.izofar.bygonenether.world.structure.PiglinManorStructure;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class ModStructures {

    public static final StructureFeature<JigsawConfiguration> CATACOMB = new CatacombStructure(JigsawConfiguration.CODEC);
    public static final StructureFeature<JigsawConfiguration> CITADEL = new CitadelStructure(JigsawConfiguration.CODEC);
    public static final StructureFeature<JigsawConfiguration> PIGLIN_MANOR = new PiglinManorStructure(JigsawConfiguration.CODEC);

    public static void registerStructures() {
        Registry.register(Registry.STRUCTURE_FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "catacomb"), CATACOMB);
        Registry.register(Registry.STRUCTURE_FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "citadel"), CITADEL);
        Registry.register(Registry.STRUCTURE_FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "piglin_manor"), PIGLIN_MANOR);
    }
}
