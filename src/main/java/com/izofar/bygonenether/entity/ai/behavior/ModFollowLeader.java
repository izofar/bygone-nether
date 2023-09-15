package com.izofar.bygonenether.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.function.Predicate;

public class ModFollowLeader extends Behavior<PathfinderMob> {

    private static final int TOO_FAR_DIST = 28;
    private static final int TOO_CLOSE_DIST = 3;
    private static final int CLOSE_ENOUGH_DIST = 6;

    private final Predicate<PathfinderMob> isDistracted;

    public ModFollowLeader(Predicate<PathfinderMob> isDistracted) {
        super(Util.make(() -> {
            ImmutableMap.Builder<MemoryModuleType<?>, MemoryStatus> builder = ImmutableMap.builder();
            builder.put(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED);
            builder.put(MemoryModuleType.IS_TEMPTED, MemoryStatus.REGISTERED);
            builder.put(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_PRESENT);
            return builder.build();
        }));
        this.isDistracted = isDistracted;
    }

    private Optional<Player> getTemptingPlayer(PathfinderMob mob) {
        return mob.getBrain().hasMemoryValue(MemoryModuleType.IS_TEMPTED)
                ? mob.getBrain().getMemory(MemoryModuleType.TEMPTING_PLAYER)
                : Optional.empty();
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return false;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, PathfinderMob mob, long gameTime) {
        return this.getTemptingPlayer(mob).isPresent();
    }

    @Override
    protected void stop(ServerLevel level, PathfinderMob mob, long gameTime) {
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getBrain().eraseMemory(MemoryModuleType.TEMPTING_PLAYER);
        mob.getBrain().eraseMemory(MemoryModuleType.IS_TEMPTED);
    }

    @Override
    protected void tick(ServerLevel level, PathfinderMob mob, long gameTime) {
        if (this.isDistracted.test(mob)) {
            return;
        }

        Brain<?> brain = mob.getBrain();
        Player player = this.getTemptingPlayer(mob).get();
        double dist = mob.distanceToSqr(player);
        if (dist < TOO_CLOSE_DIST * TOO_CLOSE_DIST) {
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        } else if (dist > CLOSE_ENOUGH_DIST * CLOSE_ENOUGH_DIST && dist < TOO_FAR_DIST * TOO_FAR_DIST) {
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(player, false), 1, 2));
        }
    }
}