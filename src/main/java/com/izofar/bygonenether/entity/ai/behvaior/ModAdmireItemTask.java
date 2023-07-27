package com.izofar.bygonenether.entity.ai.behvaior;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.entity.PiglinPrisonerEntity;
import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.server.ServerWorld;

public class ModAdmireItemTask<E extends PiglinPrisonerEntity> extends Task<E> {
    private final int admireDuration;

    public ModAdmireItemTask(int admireDuration) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleStatus.VALUE_ABSENT));
        this.admireDuration = admireDuration;
    }

    protected boolean checkExtraStartConditions(ServerWorld world, E piglingPrisonerEntity) {
        ItemEntity itementity = piglingPrisonerEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).get();
        return PiglinPrisonerAi.isLovedItem(itementity.getItem().getItem());
    }

    protected void start(ServerWorld world, E piglingPrisonerEntity, long gameTime) {
        piglingPrisonerEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, this.admireDuration);
    }
}
