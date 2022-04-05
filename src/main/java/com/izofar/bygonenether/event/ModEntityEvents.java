package com.izofar.bygonenether.event;

import com.izofar.bygonenether.entity.PiglinPrisoner;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ModEntityEvents {

	@SubscribeEvent
	public static void preventPiglinPrisonersFromDespawning(LivingSpawnEvent.AllowDespawn event) { if (event.getEntityLiving() instanceof PiglinPrisoner) event.setResult(Event.Result.DENY); }

}
