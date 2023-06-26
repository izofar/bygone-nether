package com.izofar.bygonenether.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.entity.PiglinPrisoner;
import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class ModStopHoldingItemIfNoLongerAdmiring<E extends PiglinPrisoner> extends Behavior<E> {

    public ModStopHoldingItemIfNoLongerAdmiring() {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E piglinPrisoner) {
        return !piglinPrisoner.getOffhandItem().isEmpty() && !piglinPrisoner.getOffhandItem().canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK);
    }

    protected void start(ServerLevel serverLevel, E piglinPrisoner, long gameTime) {
        PiglinPrisonerAi.stopHoldingOffHandItem(piglinPrisoner, true);
    }
}