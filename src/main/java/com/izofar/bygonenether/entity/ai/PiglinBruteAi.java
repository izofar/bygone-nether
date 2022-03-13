package com.izofar.bygonenether.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.izofar.bygonenether.entity.ModPiglinBruteEntity;
import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import com.izofar.bygonenether.item.ModArmorMaterial;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.GlobalPos;

import java.util.Optional;

public class PiglinBruteAi {

	public static Brain<?> makeBrain(ModPiglinBruteEntity piglin, Brain<ModPiglinBruteEntity> brain) {
		initCoreActivity(piglin, brain);
		initIdleActivity(piglin, brain);
		initFightActivity(piglin, brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	public static void initMemories(ModPiglinBruteEntity piglin) {
		GlobalPos globalpos = GlobalPos.of(piglin.level.dimension(), piglin.blockPosition());
		piglin.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
	}

	private static void initCoreActivity(ModPiglinBruteEntity piglin, Brain<ModPiglinBruteEntity> brain) {
		brain.addActivity(Activity.CORE, 0, 
			ImmutableList.of(
				new LookTask(45, 90),
				new WalkToTargetTask(),
				new InteractWithDoorTask(),
				new GetAngryTask<>()
			)
		);
	}

	private static void initIdleActivity(ModPiglinBruteEntity piglin, Brain<ModPiglinBruteEntity> brain) {
		brain.addActivity(Activity.IDLE, 10,
			ImmutableList.of(
				new ForgetAttackTargetTask<>(PiglinBruteAi::findNearestValidAttackTarget),
				createIdleLookBehaviors(), 
				createIdleMovementBehaviors(),
				new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)
			)
		);
	}

	private static void initFightActivity(ModPiglinBruteEntity piglin, Brain<ModPiglinBruteEntity> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
			ImmutableList.of(
				new FindNewAttackTargetTask<>((target) -> !isNearestValidAttackTarget(piglin, target)),
				new MoveToTargetTask(1.0F),
				new AttackTargetTask(20)
			),
			MemoryModuleType.ATTACK_TARGET
		);
	}

	private static FirstShuffledTask<ModPiglinBruteEntity> createIdleLookBehaviors() {
		return new FirstShuffledTask<>(
			ImmutableList.of(
				Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 1),
				Pair.of(new LookAtEntityTask(EntityType.PIGLIN, 8.0F), 1),
				Pair.of(new LookAtEntityTask(EntityType.PIGLIN_BRUTE, 8.0F), 1),
				Pair.of(new LookAtEntityTask(8.0F), 1), Pair.of(new DummyTask(30, 60), 1)
			)
		);
	}

	private static FirstShuffledTask<ModPiglinBruteEntity> createIdleMovementBehaviors() {
		return new FirstShuffledTask<>(
			ImmutableList.of(
				Pair.of(new WalkRandomlyTask(0.6F), 2),
				Pair.of(InteractWithEntityTask.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
				Pair.of(InteractWithEntityTask.of(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
				Pair.of(new WalkTowardsPosTask(MemoryModuleType.HOME, 0.6F, 2, 100), 2),
				Pair.of(new WorkTask(MemoryModuleType.HOME, 0.6F, 5), 2), Pair.of(new DummyTask(30, 60), 1)
			)
		);
	}

	public static void updateActivity(ModPiglinBruteEntity piglin) {
		Brain<ModPiglinBruteEntity> brain = piglin.getBrain();
		Activity activity = brain.getActiveNonCoreActivity().orElse(null);
		brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
		Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
		if (activity != activity1) playActivitySound(piglin);
		piglin.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
	}

	private static boolean isNearestValidAttackTarget(ModPiglinBruteEntity piglin, LivingEntity target) { return findNearestValidAttackTarget(piglin).filter((potentialTarget) -> potentialTarget == target).isPresent(); }

	private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ModPiglinBruteEntity piglin) {
		Brain<ModPiglinBruteEntity> brain = piglin.getBrain();
		Optional<LivingEntity> optional = BrainUtil.getLivingEntityFromUUIDMemory(piglin, MemoryModuleType.ANGRY_AT);

		if (optional.isPresent() && isAttackAllowed(optional.get())) return optional;

		if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
			Optional<? extends LivingEntity> optional1 = getTargetIfWithinRange(piglin, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
			if (optional1.isPresent()) return optional1;
		}

		Optional<MobEntity> optional3 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
		if (optional3.isPresent()) return optional3;

		Optional<PlayerEntity> optional2 = brain.getMemory(ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD.get());
		return optional2.isPresent() && isAttackAllowed(optional2.get()) ? optional2 : Optional.empty();
	}

	private static boolean isAttackAllowed(LivingEntity livingEntity) { return EntityPredicates.ATTACK_ALLOWED.test(livingEntity); }

	private static Optional<? extends LivingEntity> getTargetIfWithinRange(AbstractPiglinEntity piglin, MemoryModuleType<? extends LivingEntity> memory) { return piglin.getBrain().getMemory(memory).filter((entity) ->entity.closerThan(piglin, 12.0D)); }

	public static void wasHurtBy(ModPiglinBruteEntity piglin, LivingEntity entity) { if (!(entity instanceof AbstractPiglinEntity)) PiglinTasks.maybeRetaliate(piglin, entity); }

	public static void maybePlayActivitySound(ModPiglinBruteEntity p_35115_) { if ((double) p_35115_.level.random.nextFloat() < 0.0125D) playActivitySound(p_35115_); }

	private static void playActivitySound(ModPiglinBruteEntity p_35123_) { p_35123_.getBrain().getActiveNonCoreActivity().ifPresent((p_35104_) -> { if (p_35104_ == Activity.FIGHT) p_35123_.playAngrySound(); } ); }

	public static boolean isWearingGild(LivingEntity p_234460_0_) {
		for (ItemStack itemstack : p_234460_0_.getArmorSlots())
			if (makesPiglinBrutesNeutral(itemstack, p_234460_0_)) 
				return true;
		return false;
	}

	private static boolean makesPiglinBrutesNeutral(ItemStack stack, LivingEntity wearer) { return stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() instanceof ModArmorMaterial; }
}
