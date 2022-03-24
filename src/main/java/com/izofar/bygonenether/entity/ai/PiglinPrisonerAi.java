 package com.izofar.bygonenether.entity.ai;

 import com.google.common.collect.ImmutableList;
 import com.google.common.collect.ImmutableSet;
 import com.izofar.bygonenether.entity.PiglinPrisoner;
 import com.mojang.datafixers.util.Pair;
 import net.minecraft.core.BlockPos;
 import net.minecraft.sounds.SoundEvent;
 import net.minecraft.sounds.SoundEvents;
 import net.minecraft.util.TimeUtil;
 import net.minecraft.util.valueproviders.UniformInt;
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

 import java.util.List;
 import java.util.Optional;

 public class PiglinPrisonerAi {

	private static final UniformInt AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
	 private static final int CELEBRATION_TIME = 200;

	public static Brain<?> makeBrain(PiglinPrisoner piglin, Brain<PiglinPrisoner> brain) {
		initCoreActivity(brain);
		initIdleActivity(brain);
		initFightActivity(piglin, brain);
		initCelebrateActivity(brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	private static void initCoreActivity(Brain<PiglinPrisoner> brain) {
		brain.addActivity(Activity.CORE, 0,
				ImmutableList.of(
						new LookAtTargetSink(45, 90), 
						new MoveToTargetSink(), 
						new InteractWithDoor(),
						avoidZombified(),
						new StopBeingAngryIfTargetDead<>()
					)
				);
	}

	private static void initIdleActivity(Brain<PiglinPrisoner> brain) {
		brain.addActivity(Activity.IDLE, 10,
				ImmutableList.of(
	    				new StartAttacking<>(PiglinPrisonerAi::findNearestValidAttackTarget),
						avoidRepellent(),
						createIdleLookBehaviors(),
						createIdleMovementBehaviors(),
						new SetLookAndInteract(EntityType.PLAYER, 4)
					)
				);
	}

	private static void initFightActivity(PiglinPrisoner piglin, Brain<PiglinPrisoner> brain) {
	      brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, 
	    		  ImmutableList.<net.minecraft.world.entity.ai.behavior.Behavior<? super PiglinPrisoner>>of(
						  new StopAttackingIfTargetInvalid<>((target) -> !isNearestValidAttackTarget(piglin, target)),
	    				  new RunIf<Mob>(PiglinPrisonerAi::hasCrossbow, new BackUpIfTooClose<>(5, 0.75F)), 
	    				  new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0F), 
	    				  new MeleeAttack(20), 
	    				  new CrossbowAttack(),
						  new EraseMemoryIf<>(PiglinPrisonerAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)
    				  ), 
	    		  MemoryModuleType.ATTACK_TARGET);
	   }

	 private static void initCelebrateActivity(Brain<PiglinPrisoner> brain) {
		 brain.addActivityAndRemoveMemoryWhenStopped(
				 Activity.CELEBRATE,
				 10,
				 ImmutableList.of(
					 avoidRepellent(),
					 new StartAttacking<PiglinPrisoner>(AbstractPiglin::isAdult, PiglinPrisonerAi::findNearestValidAttackTarget),
					 new RunIf<PiglinPrisoner>((piglin) -> !piglin.isDancing(), new GoToCelebrateLocation<>(2, 1.0F)),
					 new RunIf<PiglinPrisoner>(PiglinPrisoner::isDancing, new GoToCelebrateLocation<>(4, 0.6F)),
					 new RunOne<>(ImmutableList.of(
							 Pair.of(new SetEntityLookTarget(EntityType.PIGLIN, 8.0F), 1),
							 Pair.of(new RandomStroll(0.6F, 2, 1), 1),
							 Pair.of(new DoNothing(10, 20), 1)))
				 	),
				 MemoryModuleType.CELEBRATE_LOCATION);
	 }
	
	private static RunOne<PiglinPrisoner> createIdleLookBehaviors() {
		return new RunOne<>(
				ImmutableList.of(
					Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 1),
					Pair.of(new SetEntityLookTarget(EntityType.PIGLIN, 8.0F), 1), 
					Pair.of(new SetEntityLookTarget(8.0F), 1),
					Pair.of(new DoNothing(30, 60), 1)
				)
			);
	}

	private static RunOne<PiglinPrisoner> createIdleMovementBehaviors() {
		return new RunOne<>(
				ImmutableList.of(
					Pair.of(new RandomStroll(0.6F), 2),
					Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
					Pair.of(new DoNothing(30, 60), 1)
				)
			);
	}
	
	public static void updateActivity(PiglinPrisoner piglin) {
		Brain<PiglinPrisoner> brain = piglin.getBrain();
		Activity activity = brain.getActiveNonCoreActivity().orElse(null);
		brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
		Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
		if (activity != activity1) getSoundForCurrentActivity(piglin).ifPresent(piglin::playSound);
		piglin.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));

		if (!brain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION)) {
			brain.eraseMemory(MemoryModuleType.DANCING);
		}

		piglin.setDancing(brain.hasMemoryValue(MemoryModuleType.DANCING));
	}

	private static boolean isNearestValidAttackTarget(PiglinPrisoner piglin, LivingEntity target) { return findNearestValidAttackTarget(piglin).filter((potentialTarget) -> potentialTarget == target).isPresent(); }
	
	private static Optional<? extends LivingEntity> findNearestValidAttackTarget(PiglinPrisoner piglin) {
		Brain<PiglinPrisoner> brain = piglin.getBrain();
		if (isNearZombified(piglin)) return Optional.empty();
		else {
			Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(piglin, MemoryModuleType.ANGRY_AT);
			if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(piglin, optional.get())) return optional;
			else {
				Optional<Mob> optional1 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
				if (optional1.isPresent()) return optional1;
			}
		}
		return Optional.empty();
	}
	
	private static boolean hasCrossbow(LivingEntity entity) { return entity.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem); }

	private static CopyMemoryWithExpiry<PiglinPrisoner, LivingEntity> avoidZombified() {
		return new CopyMemoryWithExpiry<>(
				PiglinPrisonerAi::isNearZombified,
				MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
				MemoryModuleType.AVOID_TARGET,
				AVOID_ZOMBIFIED_DURATION
		);
	}
	
	private static boolean isNearZombified(PiglinPrisoner entity) {
		Brain<PiglinPrisoner> brain = entity.getBrain();
		if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
			LivingEntity livingentity = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
			return entity.closerThan(livingentity, 6.0D);
		} else return false;
	}

	private static SetWalkTargetAwayFrom<BlockPos> avoidRepellent() { return SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false); }
	
	public static Optional<SoundEvent> getSoundForCurrentActivity(PiglinPrisoner piglin) { return piglin.getBrain().getActiveNonCoreActivity().map((activity) -> getSoundForActivity(piglin, activity)); }
	
	private static SoundEvent getSoundForActivity(PiglinPrisoner piglin, Activity activity) {
		if (activity == Activity.FIGHT) return SoundEvents.PIGLIN_ANGRY;
		else if (piglin.isConverting()) return SoundEvents.PIGLIN_RETREAT;
		else if (activity == Activity.AVOID && isNearAvoidTarget(piglin)) return SoundEvents.PIGLIN_RETREAT;
		else if (activity == Activity.ADMIRE_ITEM) return SoundEvents.PIGLIN_ADMIRING_ITEM;
		else if (activity == Activity.CELEBRATE) return SoundEvents.PIGLIN_CELEBRATE;
		else return isNearRepellent(piglin) ? SoundEvents.PIGLIN_RETREAT : SoundEvents.PIGLIN_AMBIENT;
	}
	
	private static boolean isNearAvoidTarget(PiglinPrisoner piglin) {
		Brain<PiglinPrisoner> brain = piglin.getBrain();
		return brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET) && brain.getMemory(MemoryModuleType.AVOID_TARGET).get().closerThan(piglin, 12.0D);
	}
	
	private static boolean isNearRepellent(PiglinPrisoner piglin) { return piglin.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT); }

	public static void exciteNearbyPiglins(Player player, boolean requireVisibility) {
		List<PiglinPrisoner> list = player.level.getEntitiesOfClass(PiglinPrisoner.class, player.getBoundingBox().inflate(16.0D));
		list.stream().filter(PiglinAi::isIdle).filter((piglin) -> !requireVisibility || BehaviorUtils.canSee(piglin, player)).forEach((piglin) -> startDancing(piglin));
	}

	private static void startDancing(PiglinPrisoner piglin){
		piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.DANCING, true, CELEBRATION_TIME);
		piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, piglin.blockPosition(), CELEBRATION_TIME);
	}

}
