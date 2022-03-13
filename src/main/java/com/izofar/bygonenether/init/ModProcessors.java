package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.world.processors.DataBlockProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;

public abstract class ModProcessors {

    public static final IStructureProcessorType<DataBlockProcessor> DATA_BLOCK_PROCESSOR = () -> DataBlockProcessor.CODEC;

    public static void registerProcessors(){
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BygoneNetherMod.MODID, "data_block_processor"), DATA_BLOCK_PROCESSOR);
    }
}
