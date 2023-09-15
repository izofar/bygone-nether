package com.izofar.bygonenether.entity.ai.sensing;

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
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PiglinPrisonerSpecificSensor extends Sensor<LivingEntity> {

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return ImmutableSet.of(
				MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
				MemoryModuleType.NEAREST_LIVING_ENTITIES,
				MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
				MemoryModuleType.NEAREST_VISIBLE_PLAYER,
				MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM,
				MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
				MemoryModuleType.NEARBY_ADULT_PIGLINS,
				MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT,
				MemoryModuleType.NEAREST_REPELLENT
		);
	}

	@Override
	protected void doTick(ServerLevel level, LivingEntity piglinPrisoner) {
		Brain<?> brain = piglinPrisoner.getBrain();
		brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(level, piglinPrisoner));
		Optional<Mob> optional = Optional.empty();
		Optional<Piglin> optional3 = Optional.empty();
		Optional<LivingEntity> optional4 = Optional.empty();
		Optional<Player> optional5 = Optional.empty();
		Optional<Player> optional6 = Optional.empty();
		List<AbstractPiglin> list = Lists.newArrayList();
		List<AbstractPiglin> list1 = Lists.newArrayList();
		NearestVisibleLivingEntities nearestvisiblelivingentities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());

		for (LivingEntity livingentity : nearestvisiblelivingentities.findAll((entity) -> true)) {
			if (livingentity instanceof PiglinBrute piglinbrute) {
				list.add(piglinbrute);
			} else if (livingentity instanceof Piglin piglin) {
				if (piglin.isBaby() && optional3.isEmpty()) {
					optional3 = Optional.of(piglin);
				} else if (piglin.isAdult()) {
					list.add(piglin);
				}
			} else if (livingentity instanceof Player player) {
				if (optional5.isEmpty()) {
					optional5 = Optional.of(player);
				}
				if (optional6.isEmpty() && !player.isSpectator() && PiglinAi.isPlayerHoldingLovedItem(player)) {
					optional6 = Optional.of(player);
				}
			} else if (optional.isPresent() || !(livingentity instanceof WitherSkeleton) && !(livingentity instanceof WitherBoss) && !(livingentity instanceof Hoglin hoglin && hoglin.isAdult())) {
				if (optional4.isEmpty() && PiglinAi.isZombified(livingentity.getType())) {
					optional4 = Optional.of(livingentity);
				}
			} else {
				optional = Optional.of((Mob)livingentity);
			}
		}

		for (LivingEntity livingentity1 : brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of())) {
			if (livingentity1 instanceof AbstractPiglin abstractpiglin) {
				if (abstractpiglin.isAdult()) {
					list1.add(abstractpiglin);
				}
			}
		}

		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional);
		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, optional4);
		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, optional5);
		brain.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, optional6);
		brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list1);
		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, list);
		brain.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, list.size());

	}

	private static Optional<BlockPos> findNearestRepellent(ServerLevel serverLevel, LivingEntity livingEntity) {
		return BlockPos.findClosestMatch(livingEntity.blockPosition(), 8, 4, (p_186160_) -> isValidRepellent(serverLevel, p_186160_));
	}

	private static boolean isValidRepellent(ServerLevel serverlevel, BlockPos blockpos) {
		BlockState blockstate = serverlevel.getBlockState(blockpos);
		boolean flag = blockstate.is(BlockTags.PIGLIN_REPELLENTS);
		return flag && blockstate.is(Blocks.SOUL_CAMPFIRE) ? CampfireBlock.isLitCampfire(blockstate) : flag;
	}
}
