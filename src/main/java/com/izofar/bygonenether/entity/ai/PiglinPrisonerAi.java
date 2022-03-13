 package com.izofar.bygonenether.entity.ai;

 import com.google.common.collect.ImmutableList;
 import com.google.common.collect.ImmutableSet;
 import com.izofar.bygonenether.entity.PiglinPrisonerEntity;
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
 import net.minecraft.entity.monster.piglin.ForgetAdmiredItemTask;
 import net.minecraft.entity.monster.piglin.PiglinTasks;
 import net.minecraft.entity.monster.piglin.StopReachingItemTask;
 import net.minecraft.item.Item;
 import net.minecraft.tags.ItemTags;
 import net.minecraft.util.*;
 import net.minecraft.util.math.BlockPos;

 import java.util.Optional;

public class PiglinPrisonerAi {

	private static final RangedInteger AVOID_ZOMBIFIED_DURATION = TickRangeConverter.rangeOfSeconds(5, 7);

	public static Brain<?> makeBrain(PiglinPrisonerEntity piglin, Brain<PiglinPrisonerEntity> brain) {
		initCoreActivity(brain);
		initIdleActivity(brain);
		initAdmireItemActivity(brain);
		initFightActivity(piglin, brain);
		initRetreatActivity(brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	private static void initCoreActivity(Brain<PiglinPrisonerEntity> brain) {
		brain.addActivity(Activity.CORE, 0,
				ImmutableList.<Task<? super PiglinPrisonerEntity>>of(
						new LookTask(45, 90),
						new WalkToTargetTask(),
						new InteractWithDoorTask(),
						avoidZombified(),
						new GetAngryTask()
					)
				);
	}

	private static void initIdleActivity(Brain<PiglinPrisonerEntity> brain) {
		brain.addActivity(Activity.IDLE, 10,
				ImmutableList.<Task<? super PiglinPrisonerEntity>>of(
						new LookAtEntityTask(PiglinTasks::isPlayerHoldingLovedItem, 14.0F),
						new ForgetAttackTargetTask<>(AbstractPiglinEntity::isAdult, PiglinPrisonerAi::findNearestValidAttackTarget),
						avoidRepellent(),
						createIdleLookBehaviors(),
						createIdleMovementBehaviors(),
						new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)
					)
				);
	}

	private static void initFightActivity(PiglinPrisonerEntity piglin, Brain<PiglinPrisonerEntity> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
				ImmutableList.<Task<? super PiglinPrisonerEntity>>of(
						new FindNewAttackTargetTask<>((target) ->  !isNearestValidAttackTarget(piglin, target)),
						new SupplementedTask<>(PiglinPrisonerAi::hasCrossbow, new AttackStrafingTask<>(5, 0.75F)),
						new MoveToTargetTask(1.0F),
						new AttackTargetTask(20),
						new ShootTargetTask(),
						new PredicateTask<>(PiglinPrisonerAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)
					),
				MemoryModuleType.ATTACK_TARGET);
	}

	private static void initAdmireItemActivity(Brain<PiglinPrisonerEntity> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10,
				ImmutableList.<Task<? super PiglinPrisonerEntity>>of(
						new PickupWantedItemTask<>(PiglinPrisonerAi::isNotHoldingLovedItemInOffHand, 1.0F, true, 9),
						new ForgetAdmiredItemTask(9),
						new StopReachingItemTask(200, 200)
					),
				MemoryModuleType.ADMIRING_ITEM);
	}

	private static void initRetreatActivity(Brain<PiglinPrisonerEntity> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10,
				ImmutableList.of(
						RunAwayTask.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true),
						createIdleLookBehaviors(), createIdleMovementBehaviors(),
						new PredicateTask<PiglinPrisonerEntity>(PiglinPrisonerAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)),
				MemoryModuleType.AVOID_TARGET);
	}

	private static FirstShuffledTask<PiglinPrisonerEntity> createIdleLookBehaviors() {
		return new FirstShuffledTask<>(
				ImmutableList.of(
					Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 1),
					Pair.of(new LookAtEntityTask(EntityType.PIGLIN, 8.0F), 1),
					Pair.of(new LookAtEntityTask(8.0F), 1),
					Pair.of(new DummyTask(30, 60), 1)
				)
			);
	}

	private static FirstShuffledTask<PiglinPrisonerEntity> createIdleMovementBehaviors() {
		return new FirstShuffledTask<>(
				ImmutableList.of(
						Pair.of(new WalkRandomlyTask(0.6F), 2),
						Pair.of(InteractWithEntityTask.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
						Pair.of(new SupplementedTask<>(PiglinPrisonerAi::doesntSeeAnyPlayerHoldingLovedItem, new WalkTowardsLookTargetTask(0.6F, 3)), 2),
						Pair.of(new DummyTask(30, 60), 1)
				)
			);
	}
	
	public static void updateActivity(PiglinPrisonerEntity piglin) {
		Brain<PiglinPrisonerEntity> brain = piglin.getBrain();
		Activity activity = brain.getActiveNonCoreActivity().orElse(null);
		brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
		Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
		if (activity != activity1) getSoundForCurrentActivity(piglin).ifPresent(piglin::playSound);
		piglin.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
	}

	private static boolean isNearestValidAttackTarget(PiglinPrisonerEntity piglin, LivingEntity target) { return findNearestValidAttackTarget(piglin).filter((potentialTarget) -> potentialTarget == target).isPresent(); }
	
	private static Optional<? extends LivingEntity> findNearestValidAttackTarget(PiglinPrisonerEntity piglin) {
		Brain<PiglinPrisonerEntity> brain = piglin.getBrain();
		if (isNearZombified(piglin)) return Optional.empty();
		else {
			Optional<LivingEntity> optional = BrainUtil.getLivingEntityFromUUIDMemory(piglin, MemoryModuleType.ANGRY_AT);
			if (optional.isPresent() && isAttackAllowed(optional.get())) return optional;
			else {
				Optional<MobEntity> optional1 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
				if (optional1.isPresent()) return optional1;
			}
		}
		return Optional.empty();
	}
	
	private static boolean hasCrossbow(LivingEntity entity) { return entity.isHolding(is -> is.getItem() instanceof net.minecraft.item.CrossbowItem); }

	private static PiglinIdleActivityTask<PiglinPrisonerEntity, LivingEntity> avoidZombified() {
		return new PiglinIdleActivityTask<>(
				PiglinPrisonerAi::isNearZombified,
				MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
				MemoryModuleType.AVOID_TARGET,
				AVOID_ZOMBIFIED_DURATION
		);
	}
	
	private static boolean isNearZombified(PiglinPrisonerEntity entity) {
		Brain<PiglinPrisonerEntity> brain = entity.getBrain();
		if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
			LivingEntity livingentity = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
			return entity.closerThan(livingentity, 6.0D);
		} else return false;
	}

	private static RunAwayTask<BlockPos> avoidRepellent() { return RunAwayTask.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false); }

	public static Optional<SoundEvent> getSoundForCurrentActivity(PiglinPrisonerEntity piglin) { return piglin.getBrain().getActiveNonCoreActivity().map((activity) -> getSoundForActivity(piglin, activity)); }
	
	private static SoundEvent getSoundForActivity(PiglinPrisonerEntity piglin, Activity activity) {
		if (activity == Activity.FIGHT) return SoundEvents.PIGLIN_ANGRY;
		else if (piglin.isConverting()) return SoundEvents.PIGLIN_RETREAT;
		else if (activity == Activity.AVOID && isNearAvoidTarget(piglin)) return SoundEvents.PIGLIN_RETREAT;
		else if (activity == Activity.ADMIRE_ITEM) return SoundEvents.PIGLIN_ADMIRING_ITEM;
		else if (activity == Activity.CELEBRATE) return SoundEvents.PIGLIN_CELEBRATE;
		else return isNearRepellent(piglin) ? SoundEvents.PIGLIN_RETREAT : SoundEvents.PIGLIN_AMBIENT;
	}

	private static boolean wantsToStopFleeing(PiglinPrisonerEntity piglin) {
		Brain<PiglinPrisonerEntity> brain = piglin.getBrain();
		if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
			return true;
		} else {
			LivingEntity livingentity = brain.getMemory(MemoryModuleType.AVOID_TARGET).get();
			EntityType<?> entitytype = livingentity.getType();
			if (isZombified(entitytype)) {
				return !brain.isMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, livingentity);
			} else {
				return false;
			}
		}
	}
	
	private static boolean isNearAvoidTarget(PiglinPrisonerEntity piglin) {
		Brain<PiglinPrisonerEntity> brain = piglin.getBrain();
		return brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET) && brain.getMemory(MemoryModuleType.AVOID_TARGET).get().closerThan(piglin, 12.0D);
	}
	
	private static boolean isNearRepellent(PiglinPrisonerEntity piglin) { return piglin.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT); }

	private static boolean isAttackAllowed(LivingEntity pTarget) { return EntityPredicates.ATTACK_ALLOWED.test(pTarget); }

	private static boolean doesntSeeAnyPlayerHoldingLovedItem(LivingEntity player) { return !seesPlayerHoldingLovedItem(player); }

	private static boolean seesPlayerHoldingLovedItem(LivingEntity pPiglin) { return pPiglin.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM); }

	private static boolean isNotHoldingLovedItemInOffHand(PiglinPrisonerEntity piglin) { return piglin.getOffhandItem().isEmpty() || !isLovedItem(piglin.getOffhandItem().getItem()); }

	public static boolean isLovedItem(Item item) { return item.is(ItemTags.PIGLIN_LOVED); }

	public static boolean isZombified(EntityType pEntityType) { return pEntityType == EntityType.ZOMBIFIED_PIGLIN || pEntityType == EntityType.ZOGLIN; }

}
