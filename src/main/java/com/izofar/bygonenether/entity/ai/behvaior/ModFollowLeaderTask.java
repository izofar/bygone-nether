package com.izofar.bygonenether.entity.ai.behvaior;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.function.Predicate;

public class ModFollowLeaderTask<E extends CreatureEntity> extends Task<E> {

    private static final int TOO_FAR_DIST = 28;
    private static final int TOO_CLOSE_DIST = 3;
    private static final int CLOSE_ENOUGH_DIST = 6;

    private final Predicate<CreatureEntity> isDistracted;

    public ModFollowLeaderTask(Predicate<CreatureEntity> isDistracted) {
        super(ImmutableMap.of(
                MemoryModuleType.WALK_TARGET, MemoryModuleStatus.REGISTERED,
                ModMemoryModuleTypes.IS_TEMPTED.get(), MemoryModuleStatus.REGISTERED,
                ModMemoryModuleTypes.TEMPTING_PLAYER.get(), MemoryModuleStatus.VALUE_PRESENT
        ));
        this.isDistracted = isDistracted;
    }

    private Optional<PlayerEntity> getTemptingPlayer(CreatureEntity mob) {
        return mob.getBrain().hasMemoryValue(ModMemoryModuleTypes.IS_TEMPTED.get())
                ? mob.getBrain().getMemory(ModMemoryModuleTypes.TEMPTING_PLAYER.get())
                : Optional.empty();
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return false;
    }

    @Override
    protected boolean canStillUse(ServerWorld world, CreatureEntity mob, long gameTime) {
        return this.getTemptingPlayer(mob).isPresent();
    }

    @Override
    protected void stop(ServerWorld world, E mob, long gametime){
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getBrain().eraseMemory(ModMemoryModuleTypes.TEMPTING_PLAYER.get());
        mob.getBrain().eraseMemory(ModMemoryModuleTypes.IS_TEMPTED.get());
    }

    @Override
    protected void tick(ServerWorld world, E mob, long gameTime) {
        if (this.isDistracted.test(mob)) {
            return;
        }
        Brain<?> brain = mob.getBrain();
        PlayerEntity player = this.getTemptingPlayer(mob).get();
        double dist = mob.distanceToSqr(player);
        if (dist < TOO_CLOSE_DIST * TOO_CLOSE_DIST) {
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        } else if (dist > CLOSE_ENOUGH_DIST * CLOSE_ENOUGH_DIST && dist < TOO_FAR_DIST * TOO_FAR_DIST) {
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityPosWrapper(player, false), 1, 2));
        }
    }

}
