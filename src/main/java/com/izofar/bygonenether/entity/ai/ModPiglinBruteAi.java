package com.izofar.bygonenether.entity.ai;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import com.izofar.bygonenether.init.ModSensorTypes;
import com.izofar.bygonenether.item.ModArmorMaterial;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.GameRules;

import java.util.Optional;

public class ModPiglinBruteAi {

	public static final ImmutableList<SensorType<? extends Sensor<? super PiglinBruteEntity>>> SENSOR_TYPES = ImmutableList.of(
			SensorType.NEAREST_LIVING_ENTITIES,
			SensorType.NEAREST_PLAYERS,
			SensorType.NEAREST_ITEMS,
			SensorType.HURT_BY,
			ModSensorTypes.PIGLIN_BRUTE_SPECIFIC_SENSOR.get()
		);

	public static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
			MemoryModuleType.LOOK_TARGET,
			MemoryModuleType.DOORS_TO_CLOSE,
			MemoryModuleType.LIVING_ENTITIES,
			MemoryModuleType.VISIBLE_LIVING_ENTITIES,
			MemoryModuleType.NEAREST_VISIBLE_PLAYER,
			MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
			MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
			MemoryModuleType.NEARBY_ADULT_PIGLINS,
			MemoryModuleType.HURT_BY,
			MemoryModuleType.HURT_BY_ENTITY,
			MemoryModuleType.WALK_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
			MemoryModuleType.ATTACK_TARGET,
			MemoryModuleType.ATTACK_COOLING_DOWN,
			MemoryModuleType.INTERACTION_TARGET,
			MemoryModuleType.PATH,
			MemoryModuleType.ANGRY_AT,
			MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
			MemoryModuleType.HOME,
			MemoryModuleType.UNIVERSAL_ANGER,
			ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD.get()
		);

	public static void setAngerTargetToNearestTargetablePlayerIfFound(PiglinBruteEntity piglinBrute, LivingEntity defaultEntity) {
		Optional<PlayerEntity> optional = piglinBrute.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER) ? piglinBrute.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER) : Optional.empty();
		PiglinTasks.setAngerTarget(piglinBrute, optional.isPresent()? optional.get() : defaultEntity);
	}

	public static void setAngerTarget(PiglinBruteEntity piglinBrute, LivingEntity entity) {
		if (EntityPredicates.ATTACK_ALLOWED.test(entity)) {
			piglinBrute.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
			piglinBrute.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, entity.getUUID(), 600L);
			if (entity.getType() == EntityType.PLAYER && piglinBrute.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
				piglinBrute.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
			}
		}
	}

	public static boolean isWearingGild(LivingEntity entity) {
		for (ItemStack itemstack : entity.getArmorSlots()) {
			if (makesPiglinBrutesNeutral(itemstack)) {
				return true;
			}
		}
		return false;
	}

	private static boolean makesPiglinBrutesNeutral(ItemStack stack) {
		return stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() instanceof ModArmorMaterial;
	}
}
