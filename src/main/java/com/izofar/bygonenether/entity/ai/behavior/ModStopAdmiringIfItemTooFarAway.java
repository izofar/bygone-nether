package com.izofar.bygonenether.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.entity.PiglinPrisoner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.Optional;

public class ModStopAdmiringIfItemTooFarAway<E extends PiglinPrisoner> extends Behavior<E> {
    private final int maxDistanceToItem;

    public ModStopAdmiringIfItemTooFarAway(int p_35212_) {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.REGISTERED));
        this.maxDistanceToItem = p_35212_;
    }

    protected boolean checkExtraStartConditions(ServerLevel p_35221_, E p_35222_) {
        if (!p_35222_.getOffhandItem().isEmpty()) {
            return false;
        } else {
            Optional<ItemEntity> optional = p_35222_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
            return optional.map(itemEntity -> !itemEntity.closerThan(p_35222_, this.maxDistanceToItem)).orElse(true);
        }
    }

    protected void start(ServerLevel p_35224_, E p_35225_, long p_35226_) {
        p_35225_.getBrain().eraseMemory(MemoryModuleType.ADMIRING_ITEM);
    }
}
