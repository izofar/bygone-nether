package com.izofar.bygonenether.entity.ai.behvaior;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.entity.PiglinPrisonerEntity;
import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class ModStartAdmiringItemTask<E extends PiglinPrisonerEntity> extends Task<E> {

    public ModStartAdmiringItemTask() {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_ABSENT));
    }

    protected boolean checkExtraStartConditions(ServerWorld world, E piglingPrisonerEntity) {
        return !piglingPrisonerEntity.getOffhandItem().isEmpty() && !piglingPrisonerEntity.getOffhandItem().isShield(piglingPrisonerEntity);
    }

    protected void start(ServerWorld world, E piglingPrisonerEntity, long gameTime) {
        PiglinPrisonerAi.stopHoldingOffHandItem(piglingPrisonerEntity, true);
    }
}
