package com.izofar.bygonenether.entity.ai.behvaior;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.entity.PiglinPrisonerEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class ModStopReachingItemTask<E extends PiglinPrisonerEntity> extends Task<E> {
    private final int maxTimeToReachItem;
    private final int disableTime;

    public ModStopReachingItemTask(int pMaxTimeToReachItem, int pDisableTime) {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleStatus.REGISTERED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleStatus.REGISTERED));
        this.maxTimeToReachItem = pMaxTimeToReachItem;
        this.disableTime = pDisableTime;
    }

    protected boolean checkExtraStartConditions(ServerWorld pLevel, E pOwner) {
        return pOwner.getOffhandItem().isEmpty();
    }

    protected void start(ServerWorld pLevel, E pEntity, long pGameTime) {
        Brain<PiglinPrisonerEntity> brain = pEntity.getBrain();
        Optional<Integer> optional = brain.getMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
        if (!optional.isPresent()) {
            brain.setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, 0);
        } else {
            int i = optional.get();
            if (i > this.maxTimeToReachItem) {
                brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
                brain.eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
                brain.setMemoryWithExpiry(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, true, (long)this.disableTime);
            } else {
                brain.setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, i + 1);
            }
        }

    }
}
