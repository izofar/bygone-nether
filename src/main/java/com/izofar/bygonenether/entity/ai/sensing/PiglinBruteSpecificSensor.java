package com.izofar.bygonenether.entity.ai.sensing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.izofar.bygonenether.entity.ai.ModPiglinBruteAi;
import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PiglinBruteSpecificSensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
                MemoryModuleType.NEARBY_ADULT_PIGLINS,
                ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD
        );
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();
        List<AbstractPiglin> list = Lists.newArrayList();
        NearestVisibleLivingEntities nearestvisiblelivingentities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        Optional<Mob> optional = nearestvisiblelivingentities.findClosest((target) -> target instanceof WitherSkeleton || target instanceof WitherBoss).map(Mob.class::cast);

        Optional<Player> optional5 = Optional.empty();

        for(LivingEntity livingentity : nearestvisiblelivingentities.findAll((p_186157_) -> true) )
            if (livingentity instanceof Player player && optional5.isEmpty() && !ModPiglinBruteAi.isWearingGild(player) && entity.canAttack(livingentity))
                optional5 = Optional.of(player);

        for (LivingEntity livingentity : brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of()))
            if (livingentity instanceof AbstractPiglin && ((AbstractPiglin) livingentity).isAdult())
                list.add((AbstractPiglin) livingentity);

        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional);
        brain.setMemory(ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD, optional5);
        brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list);
    }
}
