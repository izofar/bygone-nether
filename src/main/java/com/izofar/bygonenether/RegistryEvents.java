package com.izofar.bygonenether;

import com.izofar.bygonenether.entity.*;
import com.izofar.bygonenether.event.ModBlockEvents;
import com.izofar.bygonenether.event.ModEntityEvents;
import com.izofar.bygonenether.event.ModWorldEvents;
import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
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
		event.put(ModEntityTypes.PIGLIN_PRISONER.get(), PiglinPrisonerEntity.createAttributes().build());
		event.put(ModEntityTypes.PIGLIN_HUNTER.get(), PiglinBruteEntity.createAttributes().build());
		event.put(ModEntityTypes.WEX.get(), WexEntity.createAttributes().build());
		event.put(ModEntityTypes.WARPED_ENDERMAN.get(), WarpedEndermanEntity.createAttributes().build());
		event.put(ModEntityTypes.CORPOR.get(), CorporEntity.createAttributes().build());
		event.put(ModEntityTypes.WITHER_SKELETON_KNIGHT.get(), WitherSkeletonKnightEntity.createAttributes().build());
		event.put(ModEntityTypes.WRAITHER.get(), WraitherEntity.createAttributes().build());
		event.put(ModEntityTypes.WITHER_SKELETON_HORSE.get(), WitherSkeletonHorseEntity.createAttributes().build());
	}
}
