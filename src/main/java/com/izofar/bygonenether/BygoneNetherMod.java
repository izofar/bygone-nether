package com.izofar.bygonenether;

import com.izofar.bygonenether.init.*;
import com.izofar.bygonenether.world.feature.ModFeatureUtils;
import com.izofar.bygonenether.world.structure.ModStructureUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BygoneNetherMod.MODID)
public class BygoneNetherMod
{
    public static final String MODID = "bygonenether";
    public static final Logger LOGGER = LogManager.getLogger();

    public BygoneNetherMod() {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModEntityTypes.register(eventBus);
        ModStructures.register(eventBus);
        ModSensorTypes.register(eventBus);
        ModMemoryModuleTypes.register(eventBus);
        ModFeatures.register(eventBus);

        eventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModProcessors.registerProcessors();
            ModStructures.setupStructures();
            ModConfiguredStructures.registerConfiguredStructures();
            ModConfiguredFeatures.registerConfiguredFeatures();
            ModFeatures.registerPlacedFeatures();
            ModStructureUtils.addBasaltRestrictions();
            ModFeatureUtils.replaceBlackstoneBlobs();
            ModFeatureUtils.replaceBlackstoneInBastion();
        });
    }
}
