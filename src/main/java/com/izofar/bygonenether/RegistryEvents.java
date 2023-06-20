package com.izofar.bygonenether;

import com.izofar.bygonenether.client.renderer.*;
import com.izofar.bygonenether.entity.*;
import com.izofar.bygonenether.event.ModBlockEvents;
import com.izofar.bygonenether.event.ModEntityEvents;
import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class RegistryEvents {

	static {
		MinecraftForge.EVENT_BUS.register(ModBlockEvents.class);
		MinecraftForge.EVENT_BUS.register(ModEntityEvents.class);
	}

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(ModEntityTypes.PIGLIN_PRISONER.get(), PiglinPrisoner.createAttributes().build());
		event.put(ModEntityTypes.PIGLIN_HUNTER.get(), PiglinBrute.createAttributes().build());
		event.put(ModEntityTypes.WEX.get(), Wex.createAttributes().build());
		event.put(ModEntityTypes.WARPED_ENDERMAN.get(), WarpedEnderMan.createAttributes().build());
		event.put(ModEntityTypes.WRAITHER.get(), Wraither.createAttributes().build());
		event.put(ModEntityTypes.WITHER_SKELETON_KNIGHT.get(), WitherSkeletonKnight.createAttributes().build());
		event.put(ModEntityTypes.CORPOR.get(), Corpor.createAttributes().build());
		event.put(ModEntityTypes.WITHER_SKELETON_HORSE.get(), WitherSkeletonHorse.createAttributes().build());
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityTypes.PIGLIN_PRISONER.get(), PiglinPrisonerRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PIGLIN_HUNTER.get(), PiglinHunterRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.WEX.get(), WexRenderer:: new);
		event.registerEntityRenderer(ModEntityTypes.WARPED_ENDERMAN.get(), WarpedEnderManRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.WRAITHER.get(), WraitherRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.WITHER_SKELETON_KNIGHT.get(), WitherSkeletonKnightRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.CORPOR.get(), CorporRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.WITHER_SKELETON_HORSE.get(), WitherSkeletonHorseRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.WARPED_ENDER_PEARL.get(), ThrownItemRenderer::new);
		event.registerBlockEntityRenderer(ModEntityTypes.NETHERITE_BELL.get(), NetheriteBellRenderer::new);
	}

}
