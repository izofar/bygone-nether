package com.izofar.bygonenether.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.entity.PiglinPrisoner;
import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;

public class ModStartAdmiringItemIfSeen<E extends PiglinPrisoner> extends Behavior<E> {

    private final int admireDuration;

    public ModStartAdmiringItemIfSeen(int admireDuration) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT, MemoryModuleType.ADMIRING_DISABLED, MemoryStatus.VALUE_ABSENT, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryStatus.VALUE_ABSENT));
        this.admireDuration = admireDuration;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E piglinPrisoner) {
        ItemEntity itementity = piglinPrisoner.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).get();
        return PiglinPrisonerAi.isLovedItem(itementity.getItem());
    }

    protected void start(ServerLevel serverLevel, E piglinPrisoner, long gametime) {
        piglinPrisoner.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, (long)this.admireDuration);
    }
}