package com.izofar.bygonenether.advancement;

import com.google.gson.JsonObject;
import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class StartRidingTrigger extends AbstractCriterionTrigger<StartRidingTrigger.TriggerInstance> {
    static final ResourceLocation ID = new ResourceLocation(BygoneNetherMod.MODID, "started_riding");

    public ResourceLocation getId() {
        return ID;
    }

    public StartRidingTrigger.TriggerInstance createInstance(JsonObject pJson, EntityPredicate.AndPredicate player, ConditionArrayParser pConditionsParser) {
        return new StartRidingTrigger.TriggerInstance(player);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, (instance) -> true);
    }

    public static class TriggerInstance extends CriterionInstance {

        public TriggerInstance(EntityPredicate.AndPredicate player) { super(StartRidingTrigger.ID, player); }

    }
}
