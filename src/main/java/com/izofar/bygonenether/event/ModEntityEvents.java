package com.izofar.bygonenether.event;

import com.izofar.bygonenether.entity.PiglinPrisonerEntity;
import com.izofar.bygonenether.entity.WarpedEnderManEntity;
import com.izofar.bygonenether.entity.WexEntity;
import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ModEntityEvents {

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(ModEntityTypes.PIGLIN_PRISONER.get(), PiglinPrisonerEntity.createAttributes().build());
		event.put(ModEntityTypes.PIGLIN_HUNTER.get(), PiglinBruteEntity.createAttributes().build());
		event.put(ModEntityTypes.WEX.get(), WexEntity.createAttributes().build());
		event.put(ModEntityTypes.WARPED_ENDERMAN.get(), WarpedEnderManEntity.createAttributes().build());
	}

	@SubscribeEvent
	public static void preventPiglinPrisonersFromDespawning(LivingSpawnEvent.AllowDespawn event) { if (event.getEntityLiving()instanceof PiglinPrisonerEntity) event.setResult(Event.Result.DENY); }

}
