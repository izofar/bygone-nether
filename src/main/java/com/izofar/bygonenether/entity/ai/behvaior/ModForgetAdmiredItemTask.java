package com.izofar.bygonenether.entity.ai.behvaior;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.entity.PiglinPrisonerEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class ModForgetAdmiredItemTask<E extends PiglinPrisonerEntity> extends Task<E> {

    private final int maxDistanceToItem;

    public ModForgetAdmiredItemTask(int pMaxDistanceToItem) {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleStatus.REGISTERED));
        this.maxDistanceToItem = pMaxDistanceToItem;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld world, E piglingPrisonerEntity) {
        if (!piglingPrisonerEntity.getOffhandItem().isEmpty()) {
            return false;
        } else {
            Optional<ItemEntity> optional = piglingPrisonerEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
            return optional.map(itemEntity -> !itemEntity.closerThan(piglingPrisonerEntity, this.maxDistanceToItem)).orElse(true);
        }
    }

    @Override
    protected void start(ServerWorld world, E entity, long gameTime) {
        entity.getBrain().eraseMemory(MemoryModuleType.ADMIRING_ITEM);
    }
}
