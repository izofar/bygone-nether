package com.izofar.bygonenether.event;

import com.izofar.bygonenether.entity.PiglinPrisoner;
import io.github.fabricators_of_create.porting_lib.event.common.EntityEvents;

public class ModEntityEvents {
    public static void preventPrisonersFromDespawning() {
        EntityEvents.ON_JOIN_WORLD.register((entity, level, b) -> {
            if(entity instanceof PiglinPrisoner prisoner) {
                prisoner.setPersistenceRequired();
            }
            return true;
        });
    }
}
