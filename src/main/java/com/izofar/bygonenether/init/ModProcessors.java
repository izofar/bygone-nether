package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.world.processors.DataBlockProcessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public abstract class ModProcessors {

    public static StructureProcessorType<DataBlockProcessor> DATA_BLOCK_PROCESSOR = () -> DataBlockProcessor.CODEC;

    public static void registerProcessors(){
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BygoneNetherMod.MODID, "data_block_processor"), DATA_BLOCK_PROCESSOR);
    }
}
