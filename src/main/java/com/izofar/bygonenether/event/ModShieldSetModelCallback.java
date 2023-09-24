package com.izofar.bygonenether.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.world.InteractionResult;

public interface ModShieldSetModelCallback {

    Event<ModShieldSetModelCallback> EVENT = EventFactory.createArrayBacked(ModShieldSetModelCallback.class,
            (listeners) -> (loader) -> {
                for (ModShieldSetModelCallback listener : listeners) {
                    InteractionResult result = listener.setModel(loader);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult setModel(EntityModelSet loader);
}
