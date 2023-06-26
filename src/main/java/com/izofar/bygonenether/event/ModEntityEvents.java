package com.izofar.bygonenether.event;

import com.izofar.bygonenether.entity.PiglinPrisoner;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ModEntityEvents {

	@SubscribeEvent
	public static void preventPiglinPrisonersFromDespawning(MobSpawnEvent.AllowDespawn event) {
		if (event.getEntity() instanceof PiglinPrisoner) {
			event.setResult(Event.Result.DENY);
		}
	}

}
