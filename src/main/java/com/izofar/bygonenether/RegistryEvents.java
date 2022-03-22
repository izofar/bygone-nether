package com.izofar.bygonenether;

import com.izofar.bygonenether.client.renderer.ModPiglinRenderer;
import com.izofar.bygonenether.client.renderer.PiglinHunterRenderer;
import com.izofar.bygonenether.client.renderer.WarpedEnderManRenderer;
import com.izofar.bygonenether.client.renderer.WexRenderer;
import com.izofar.bygonenether.entity.PiglinPrisoner;
import com.izofar.bygonenether.entity.WarpedEnderMan;
import com.izofar.bygonenether.entity.WexEntity;
import com.izofar.bygonenether.event.ModBlockEvents;
import com.izofar.bygonenether.event.ModEntityEvents;
import com.izofar.bygonenether.event.ModWorldEvents;
import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class RegistryEvents {

	static {
		MinecraftForge.EVENT_BUS.register(ModBlockEvents.class);
		MinecraftForge.EVENT_BUS.register(ModEntityEvents.class);
		MinecraftForge.EVENT_BUS.register(ModWorldEvents.class);
	}

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(ModEntityTypes.PIGLIN_PRISONER.get(), PiglinPrisoner.createAttributes().build());
		event.put(ModEntityTypes.PIGLIN_HUNTER.get(), PiglinBrute.createAttributes().build());
		event.put(ModEntityTypes.WEX.get(), WexEntity.createAttributes().build());
		event.put(ModEntityTypes.WARPED_ENDERMAN.get(), WarpedEnderMan.createAttributes().build());
	}

	@SubscribeEvent
	public static void onRegisterRenderers(RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityTypes.PIGLIN_PRISONER.get(), (context) -> new ModPiglinRenderer(context, ModelLayers.PIGLIN, ModelLayers.PIGLIN_INNER_ARMOR, ModelLayers.PIGLIN_OUTER_ARMOR, false));
		event.registerEntityRenderer(ModEntityTypes.PIGLIN_HUNTER.get(), (context) -> new PiglinHunterRenderer(context, ModelLayers.PIGLIN_INNER_ARMOR, ModelLayers.PIGLIN_OUTER_ARMOR, false));
		event.registerEntityRenderer(ModEntityTypes.WEX.get(), WexRenderer:: new);
		event.registerEntityRenderer(ModEntityTypes.WARPED_ENDERMAN.get(), WarpedEnderManRenderer::new);
	}

}
