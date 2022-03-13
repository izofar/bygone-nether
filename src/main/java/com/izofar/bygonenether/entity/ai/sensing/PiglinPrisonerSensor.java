package com.izofar.bygonenether.entity.ai.sensing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PiglinPrisonerSensor extends Sensor<LivingEntity> {

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return ImmutableSet.of(
				MemoryModuleType.VISIBLE_LIVING_ENTITIES,
				MemoryModuleType.NEAREST_VISIBLE_NEMESIS, 
				MemoryModuleType.NEARBY_ADULT_PIGLINS,
				MemoryModuleType.NEAREST_VISIBLE_PLAYER
			);
	}
	
	@Override
	protected void doTick(ServerWorld level, LivingEntity entity) {
		Brain<?> brain = entity.getBrain();
		brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(level, entity));
		List<AbstractPiglinEntity> list = Lists.newArrayList();
		Optional<MobEntity> optional1 = Optional.empty();

		for(LivingEntity livingentity : brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of())){
			if(livingentity instanceof WitherSkeletonEntity || livingentity instanceof WitherEntity){
				optional1 = Optional.of((MobEntity)livingentity);
				break;
			}
		}

		Optional<PlayerEntity> optional2 = Optional.empty();
		
		for (LivingEntity livingentity : brain.getMemory(MemoryModuleType.LIVING_ENTITIES).orElse(ImmutableList.of()))
			if (livingentity instanceof AbstractPiglinEntity piglin && piglin.isAdult())
				list.add((AbstractPiglinEntity) livingentity);
			else if(livingentity instanceof PlayerEntity player)
				optional2 = Optional.of(player);

		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional1);
		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, optional2);
		brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list);
	}
	
	private static Optional<BlockPos> findNearestRepellent(ServerWorld p_26735_, LivingEntity p_26736_) { return BlockPos.findClosestMatch(p_26736_.blockPosition(), 8, 4, (p_186160_) -> isValidRepellent(p_26735_, p_186160_)); }

	private static boolean isValidRepellent(ServerWorld serverlevel, BlockPos blockpos) {
		BlockState blockstate = serverlevel.getBlockState(blockpos);
		boolean flag = blockstate.is(BlockTags.PIGLIN_REPELLENTS);
		return flag && blockstate.is(Blocks.SOUL_CAMPFIRE) ? CampfireBlock.isLitCampfire(blockstate) : flag;
	}
}
