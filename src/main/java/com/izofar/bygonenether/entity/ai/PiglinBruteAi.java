package com.izofar.bygonenether.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.izofar.bygonenether.entity.ModPiglinBrute;
import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import com.izofar.bygonenether.item.ModArmorMaterial;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;

import java.util.Optional;

public class PiglinBruteAi {

	public static Brain<?> makeBrain(ModPiglinBrute piglin, Brain<ModPiglinBrute> brain) {
		initCoreActivity(piglin, brain);
		initIdleActivity(piglin, brain);
		initFightActivity(piglin, brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	public static void initMemories(ModPiglinBrute piglin) {
		GlobalPos globalpos = GlobalPos.of(piglin.level.dimension(), piglin.blockPosition());
		piglin.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
	}

	private static void initCoreActivity(ModPiglinBrute piglin, Brain<ModPiglinBrute> brain) {
		brain.addActivity(Activity.CORE, 0, 
			ImmutableList.of(
				new LookAtTargetSink(45, 90), 
				new MoveToTargetSink(),
				new InteractWithDoor(), 
				new StopBeingAngryIfTargetDead<>()
			)
		);
	}

	private static void initIdleActivity(ModPiglinBrute piglin, Brain<ModPiglinBrute> brain) {
		brain.addActivity(Activity.IDLE, 10,
			ImmutableList.of(
				new StartAttacking<>(PiglinBruteAi::findNearestValidAttackTarget),
				createIdleLookBehaviors(), 
				createIdleMovementBehaviors(),
				new SetLookAndInteract(EntityType.PLAYER, 4)
			)
		);
	}

	private static void initFightActivity(ModPiglinBrute piglin, Brain<ModPiglinBrute> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
			ImmutableList.of(
				new StopAttackingIfTargetInvalid<>((target) -> !isNearestValidAttackTarget(piglin, target)), 
				new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0F), 
				new MeleeAttack(20)
			),
			MemoryModuleType.ATTACK_TARGET
		);
	}

	private static RunOne<ModPiglinBrute> createIdleLookBehaviors() {
		return new RunOne<>(
			ImmutableList.of(
				Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 1),
				Pair.of(new SetEntityLookTarget(EntityType.PIGLIN, 8.0F), 1),
				Pair.of(new SetEntityLookTarget(EntityType.PIGLIN_BRUTE, 8.0F), 1),
				Pair.of(new SetEntityLookTarget(8.0F), 1), Pair.of(new DoNothing(30, 60), 1)
			)
		);
	}

	private static RunOne<ModPiglinBrute> createIdleMovementBehaviors() {
		return new RunOne<>(
			ImmutableList.of(
				Pair.of(new RandomStroll(0.6F), 2),
				Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
				Pair.of(InteractWith.of(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
				Pair.of(new StrollToPoi(MemoryModuleType.HOME, 0.6F, 2, 100), 2),
				Pair.of(new StrollAroundPoi(MemoryModuleType.HOME, 0.6F, 5), 2), Pair.of(new DoNothing(30, 60), 1)
			)
		);
	}

	public static void updateActivity(ModPiglinBrute piglin) {
		Brain<ModPiglinBrute> brain = piglin.getBrain();
		Activity activity = brain.getActiveNonCoreActivity().orElse(null);
		brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
		Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
		if (activity != activity1) playActivitySound(piglin);
		piglin.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
	}

	private static boolean isNearestValidAttackTarget(ModPiglinBrute piglin, LivingEntity target) { return findNearestValidAttackTarget(piglin).filter((potentialTarget) -> potentialTarget == target).isPresent(); }

	private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ModPiglinBrute piglin) {
		Brain<ModPiglinBrute> brain = piglin.getBrain();
		Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(piglin, MemoryModuleType.ANGRY_AT);

		if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(piglin, optional.get())) return optional;

		if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
			Optional<? extends LivingEntity> optional1 = getTargetIfWithinRange(piglin, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
			if (optional1.isPresent()) return optional1;
		}

		Optional<Mob> optional3 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
		if (optional3.isPresent()) return optional3;

		Optional<Player> optional2 = brain.getMemory(ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD.get());
		return optional2.isPresent() && Sensor.isEntityAttackable(piglin, optional2.get()) ? optional2 : Optional.empty();
	}

	private static Optional<? extends LivingEntity> getTargetIfWithinRange(AbstractPiglin piglin, MemoryModuleType<? extends LivingEntity> memory) { return piglin.getBrain().getMemory(memory).filter((entity) ->entity.closerThan(piglin, 12.0D)); }

	public static void wasHurtBy(ModPiglinBrute piglin, LivingEntity entity) { if (!(entity instanceof AbstractPiglin)) PiglinAi.maybeRetaliate(piglin, entity); }

	protected static void setAngerTarget(ModPiglinBrute piglin, LivingEntity entity) {
		piglin.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
		piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, entity.getUUID(), 600L);
		if (entity.getType() == EntityType.PLAYER && piglin.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
	}

	public static void maybePlayActivitySound(ModPiglinBrute p_35115_) { if ((double) p_35115_.level.random.nextFloat() < 0.0125D) playActivitySound(p_35115_); }

	private static void playActivitySound(ModPiglinBrute p_35123_) { p_35123_.getBrain().getActiveNonCoreActivity().ifPresent((p_35104_) -> { if (p_35104_ == Activity.FIGHT) p_35123_.playAngrySound(); } ); }

	public static boolean isWearingGild(LivingEntity p_234460_0_) {
		for (ItemStack itemstack : p_234460_0_.getArmorSlots())
			if (makesPiglinBrutesNeutral(itemstack, p_234460_0_)) 
				return true;
		return false;
	}

	private static boolean makesPiglinBrutesNeutral(ItemStack stack, LivingEntity wearer) { return stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() instanceof ModArmorMaterial; }
}
