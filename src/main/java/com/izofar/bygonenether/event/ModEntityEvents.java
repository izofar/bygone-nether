package com.izofar.bygonenether.event;

import com.izofar.bygonenether.entity.PiglinPrisonerEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ModEntityEvents {

	@SubscribeEvent
	public static void preventPiglinPrisonersFromDespawning(LivingSpawnEvent.AllowDespawn event) { if (event.getEntityLiving()instanceof PiglinPrisonerEntity entity) event.setResult(Event.Result.DENY); }

}
