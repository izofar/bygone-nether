package com.izofar.bygonenether.init;

import com.izofar.bygonenether.advancement.StartRidingTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class ModCriteriaTriggers {

    public static StartRidingTrigger START_RIDING_TRIGGER;

    public static void registerTriggers(){
        START_RIDING_TRIGGER = CriteriaTriggers.register(new StartRidingTrigger());
    }

}
