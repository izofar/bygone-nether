package com.izofar.bygonenether.entity.ai.sensing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.izofar.bygonenether.entity.ai.PiglinBruteAi;
import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ModPiglinBruteSpecificSensor extends Sensor<LivingEntity> {
	
	@Override
	public Set<MemoryModuleType<?>> requires() {
		return ImmutableSet.of(
				MemoryModuleType.VISIBLE_LIVING_ENTITIES,
				MemoryModuleType.NEAREST_VISIBLE_NEMESIS, 
				MemoryModuleType.NEARBY_ADULT_PIGLINS,
				MemoryModuleType.UNIVERSAL_ANGER,
				ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD.get()
			);
	}

	@Override
	protected void doTick(ServerWorld level, LivingEntity entity) {
		Brain<?> brain = entity.getBrain();
		List<AbstractPiglinEntity> list = Lists.newArrayList();
		Optional<MobEntity> optional1 = Optional.empty();

		for(LivingEntity livingentity : brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of())){
			if(livingentity instanceof WitherSkeletonEntity || livingentity instanceof WitherEntity){
				optional1 = Optional.of((MobEntity)livingentity);
				break;
			}
		}

		Optional<PlayerEntity> optional2 = Optional.empty();
		
		for (LivingEntity livingentity : brain.getMemory(MemoryModuleType.LIVING_ENTITIES).orElse(ImmutableList.of())) {
			if (livingentity instanceof PlayerEntity player && optional2.isEmpty() && !PiglinBruteAi.isWearingGild(player) && entity.canAttack(livingentity))
				optional2 = Optional.of(player);
			else if (livingentity instanceof AbstractPiglinEntity && ((AbstractPiglinEntity) livingentity).isAdult())
				list.add((AbstractPiglinEntity) livingentity);
		}

		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional1);
		brain.setMemory(ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD.get(), optional2);
		brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list);
	}
}
