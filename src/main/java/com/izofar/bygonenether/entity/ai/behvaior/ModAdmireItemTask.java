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

    public ModAdmireItemTask(int pAdmireDuration) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleStatus.VALUE_ABSENT));
        this.admireDuration = pAdmireDuration;
    }

    protected boolean checkExtraStartConditions(ServerWorld pLevel, E pOwner) {
        ItemEntity itementity = pOwner.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).get();
        return PiglinPrisonerAi.isLovedItem(itementity.getItem().getItem());
    }

    protected void start(ServerWorld pLevel, E pEntity, long pGameTime) {
        pEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, this.admireDuration);
    }
}
