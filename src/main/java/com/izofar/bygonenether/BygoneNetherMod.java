package com.izofar.bygonenether;

import com.izofar.bygonenether.client.renderer.*;
import com.izofar.bygonenether.init.*;
import com.izofar.bygonenether.world.feature.ModFeatureUtils;
import com.izofar.bygonenether.util.ModStructureUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
        ModSounds.register(eventBus);
        ModLootModifers.register(eventBus);

        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModCriteriaTriggers.registerTriggers();
        event.enqueueWork(() -> {
            ModStructures.setupStructures();
            ModConfiguredStructures.registerConfiguredStructures();
            ModConfiguredFeatures.registerConfiguredFeatures();
            ModStructureUtils.addBasaltRestrictions();
            ModFeatureUtils.replaceBlackstoneBlobs();
            ModFeatureUtils.replaceBlackstoneInBastion();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
           ModShieldTileEntityRenderer.addShieldPropertyOverrides();
        });

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PIGLIN_PRISONER.get(), PiglinPrisonerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PIGLIN_HUNTER.get(), PiglinHunterRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WEX.get(), WexRenderer:: new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WARPED_ENDERMAN.get(), WarpedEnderManRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WITHER_SKELETON_HORSE.get(), WitherSkeletonHorseRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CORPOR.get(), CorporRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WITHER_SKELETON_KNIGHT.get(), WitherSkeletonKnightRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WRAITHER.get(), WraitherRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WARPED_ENDER_PEARL.get(), WarpedEnderpearlRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModEntityTypes.NETHERITE_BELL.get(), NetheriteBellTileEntityRenderer::new);
    }
}
