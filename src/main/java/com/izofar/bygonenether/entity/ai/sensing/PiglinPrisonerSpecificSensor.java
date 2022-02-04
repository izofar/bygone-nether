package com.izofar.bygonenether.entity.ai.sensing;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PiglinPrisonerSpecificSensor extends Sensor<LivingEntity>{

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return ImmutableSet.of(
				MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
				MemoryModuleType.NEAREST_VISIBLE_NEMESIS, 
				MemoryModuleType.NEARBY_ADULT_PIGLINS,
				MemoryModuleType.NEAREST_VISIBLE_PLAYER
			);
	}
	
	@Override
	protected void doTick(ServerLevel level, LivingEntity entity) {
		Brain<?> brain = entity.getBrain();
		brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(level, entity));
		List<AbstractPiglin> list = Lists.newArrayList();
		NearestVisibleLivingEntities nearestvisiblelivingentities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
		Optional<Mob> optional = nearestvisiblelivingentities.findClosest((target) -> target instanceof WitherSkeleton || target instanceof WitherBoss).map(Mob.class::cast);
		
		Optional<Player> optional1 = Optional.empty();
		
		for (LivingEntity livingentity : brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of()))
			if (livingentity instanceof AbstractPiglin piglin && piglin.isAdult()) list.add((AbstractPiglin) livingentity);
			else if(livingentity instanceof Player player) optional1 = Optional.of(player);

		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional);
		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, optional1);
		brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list);
	}
	
	private static Optional<BlockPos> findNearestRepellent(ServerLevel p_26735_, LivingEntity p_26736_) { return BlockPos.findClosestMatch(p_26736_.blockPosition(), 8, 4, (p_186160_) -> isValidRepellent(p_26735_, p_186160_)); }

	private static boolean isValidRepellent(ServerLevel serverlevel, BlockPos blockpos) {
		BlockState blockstate = serverlevel.getBlockState(blockpos);
		boolean flag = blockstate.is(BlockTags.PIGLIN_REPELLENTS);
		return flag && blockstate.is(Blocks.SOUL_CAMPFIRE) ? CampfireBlock.isLitCampfire(blockstate) : flag;
	}
}
