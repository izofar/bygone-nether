package com.izofar.bygonenether;

import com.izofar.bygonenether.client.renderer.ModPiglinRenderer;
import com.izofar.bygonenether.client.renderer.PiglinHunterRenderer;
import com.izofar.bygonenether.client.renderer.WarpedEnderManRenderer;
import com.izofar.bygonenether.client.renderer.WexRenderer;
import com.izofar.bygonenether.init.*;
import com.izofar.bygonenether.world.feature.ModFeatureUtils;
import com.izofar.bygonenether.world.structure.util.ModStructureUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
        eventBus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModProcessors.registerProcessors();
            ModStructures.setupStructures();
            ModConfiguredStructures.registerConfiguredStructures();
            ModConfiguredFeatures.registerConfiguredFeatures();
            ModStructureUtils.addBasaltRestrictions();
            ModFeatureUtils.replaceBlackstoneBlobs();
            ModFeatureUtils.replaceBlackstoneInBastion();
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PIGLIN_PRISONER.get(), (entityRendererManager) -> new ModPiglinRenderer(entityRendererManager,false));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PIGLIN_HUNTER.get(), (entityRendererManager) -> new PiglinHunterRenderer(entityRendererManager, false));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PIGLIN_BRUTE.get(), (entityRendererManager) -> new ModPiglinRenderer(entityRendererManager, false));
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WEX.get(), WexRenderer:: new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WARPED_ENDERMAN.get(), WarpedEnderManRenderer::new);
    }
}
