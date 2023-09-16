package com.izofar.bygonenether.entity.ai.behaviour;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.entity.PiglinPrisoner;
import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Items;

public class ModStopHoldingItemIfNoLongerAdmiring<E extends PiglinPrisoner> extends Behavior<E> {

    public ModStopHoldingItemIfNoLongerAdmiring() {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E owner) {
        return !owner.getOffhandItem().isEmpty() && !owner.getOffhandItem().is(Items.SHIELD);
    }

    @Override
    protected void start(ServerLevel serverLevel, E entity, long gameTime) {
        PiglinPrisonerAi.stopHoldingOffHandItem(entity, true);
    }
}
