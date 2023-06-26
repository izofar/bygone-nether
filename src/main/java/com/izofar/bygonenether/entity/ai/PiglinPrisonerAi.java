package com.izofar.bygonenether.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.izofar.bygonenether.entity.PiglinPrisoner;
import com.izofar.bygonenether.entity.ai.behavior.*;
import com.izofar.bygonenether.init.ModEntityTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class PiglinPrisonerAi {

	private static final UniformInt AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
	private static final int CELEBRATION_TIME = 200;

	private static final Predicate<PathfinderMob> isDistracted = (mob) -> {
		if (mob instanceof PiglinPrisoner piglinPrisoner) {
			Brain<PiglinPrisoner> brain = piglinPrisoner.getBrain();
			return brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)
					|| brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_NEMESIS)
					|| brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)
					|| brain.getMemory(MemoryModuleType.DANCING).orElse(false)
					|| !brain.getMemory(MemoryModuleType.IS_TEMPTED).orElse(false);
		}
		else {
			return false;
		}
	};

	public static Brain<?> makeBrain(PiglinPrisoner piglinPrisoner, Brain<PiglinPrisoner> brain) {
		initCoreActivity(brain);
		initIdleActivity(brain);
		initAdmireItemActivity(brain);
		initFightActivity(piglinPrisoner, brain);
		initCelebrateActivity(brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	private static void initCoreActivity(Brain<PiglinPrisoner> brain) {
		brain.addActivity(Activity.CORE, 0,
				ImmutableList.of(
						new ModFollowLeader(isDistracted),
						new LookAtTargetSink(45, 90),
						new MoveToTargetSink(),
						InteractWithDoor.create(),
						avoidZombified(),
						new ModStopHoldingItemIfNoLongerAdmiring<>(),
						new ModStartAdmiringItemIfSeen<>(120),
						StopBeingAngryIfTargetDead.create()
				)
		);
	}

	private static void initIdleActivity(Brain<PiglinPrisoner> brain) {
		brain.addActivity(Activity.IDLE, 10,
				ImmutableList.of(
						SetEntityLookTarget.create(PiglinPrisonerAi::isPlayerHoldingLovedItem, 14.0F),
						StartAttacking.create(AbstractPiglin::isAdult, PiglinPrisonerAi::findNearestValidAttackTarget),
						avoidRepellent(),
						createIdleLookBehaviors(),
						createIdleMovementBehaviors(),
						SetLookAndInteract.create(EntityType.PLAYER, 4)
				)
		);
	}

	private static void initFightActivity(PiglinPrisoner piglinPrisoner, Brain<PiglinPrisoner> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
				ImmutableList.<BehaviorControl<? super PiglinPrisoner>>of(
						StopAttackingIfTargetInvalid.create((target) -> !isNearestValidAttackTarget(piglinPrisoner, target)),
						BehaviorBuilder.triggerIf(PiglinPrisonerAi::hasCrossbow, BackUpIfTooClose.create(5, 0.75F)),
						SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
						MeleeAttack.create(20),
						new CrossbowAttack<>(),
						EraseMemoryIf.create(PiglinPrisonerAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)
				),
				MemoryModuleType.ATTACK_TARGET);
	}

	private static void initCelebrateActivity(Brain<PiglinPrisoner> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.CELEBRATE, 10,
				ImmutableList.of(
						avoidRepellent(),
						SetEntityLookTarget.create(PiglinPrisonerAi::isPlayerHoldingLovedItem, 14.0F),
						StartAttacking.create(AbstractPiglin::isAdult, PiglinPrisonerAi::findNearestValidAttackTarget),
						BehaviorBuilder.triggerIf(PiglinPrisoner::isDancing, GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 2, 1.0F)),
						BehaviorBuilder.triggerIf(PiglinPrisoner::isDancing, GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 4, 0.6F)),
						new RunOne<>(ImmutableList.of(
								Pair.of(SetEntityLookTarget.create(ModEntityTypes.PIGLIN_PRISONER.get(), 8.0F), 1),
								Pair.of(RandomStroll.stroll(0.6F, 2, 1), 1),
								Pair.of(new DoNothing(10, 20), 1)
						)
						)
				),
				MemoryModuleType.CELEBRATE_LOCATION);
	}

	private static void initAdmireItemActivity(Brain<PiglinPrisoner> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10,
				ImmutableList.of(
						GoToWantedItem.create(PiglinPrisonerAi::isNotHoldingLovedItemInOffHand, 1.0F, true, 9),
						new ModStopAdmiringIfItemTooFarAway<>(9),
						new ModStopAdmiringIfTiredOfTryingToReachItem<>(200, 200)
				),
				MemoryModuleType.ADMIRING_ITEM);
	}

	private static RunOne<LivingEntity> createIdleLookBehaviors() {
		return new RunOne<>(
				ImmutableList.of(
						Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 1),
						Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), 1),
						Pair.of(SetEntityLookTarget.create(ModEntityTypes.PIGLIN_PRISONER.get(), 8.0F), 1),
						Pair.of(SetEntityLookTarget.create(8.0F), 1),
						Pair.of(new DoNothing(30, 60), 1)
				)
		);
	}

	private static RunOne<PiglinPrisoner> createIdleMovementBehaviors() {
		return new RunOne<>(
				ImmutableList.of(
						Pair.of(RandomStroll.stroll(0.6F), 2),
						Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
						Pair.of(InteractWith.of(ModEntityTypes.PIGLIN_PRISONER.get(), 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
						Pair.of(new DoNothing(30, 60), 1)
				)
		);
	}

	private static BehaviorControl<PathfinderMob> avoidRepellent() {
		return SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false);
	}

	private static BehaviorControl<PiglinPrisoner> avoidZombified() {
		return CopyMemoryWithExpiry.create(
				PiglinPrisonerAi::isNearZombified,
				MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
				MemoryModuleType.AVOID_TARGET,
				AVOID_ZOMBIFIED_DURATION
		);
	}

	public static void updateActivity(PiglinPrisoner piglinPrisoner) {
		Brain<PiglinPrisoner> brain = piglinPrisoner.getBrain();
		Activity activity = brain.getActiveNonCoreActivity().orElse(null);
		brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.IDLE));

		Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
		if (activity != activity1) {
			getSoundForCurrentActivity(piglinPrisoner).ifPresent(piglinPrisoner::playSound);
		}
		piglinPrisoner.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
		if (!brain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION)) {
			brain.eraseMemory(MemoryModuleType.DANCING);
		}
		piglinPrisoner.setDancing(brain.hasMemoryValue(MemoryModuleType.DANCING));
	}

	public static void pickUpItem(PiglinPrisoner piglinPrisoner, ItemEntity itemEntity) {
		stopWalking(piglinPrisoner);
		ItemStack itemstack;

		if (itemEntity.getItem().is(Items.GOLD_NUGGET)) {
			piglinPrisoner.take(itemEntity, itemEntity.getItem().getCount());
			itemstack = itemEntity.getItem();
			itemEntity.discard();
		} else {
			piglinPrisoner.take(itemEntity, 1);
			itemstack = removeOneItemFromItemEntity(itemEntity);
		}

		if (isLovedItem(itemstack)) {
			piglinPrisoner.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
			holdInOffhand(piglinPrisoner, itemstack);
			admireGoldItem(piglinPrisoner);
		} else if (isFood(itemstack) && !hasEatenRecently(piglinPrisoner)) {
			eat(piglinPrisoner);
		} else if (piglinPrisoner.equipItemIfPossible(itemstack).isEmpty()) {
			putInInventory(piglinPrisoner, itemstack);
		}
	}

	public static void holdInOffhand(PiglinPrisoner piglinPrisoner, ItemStack stack) {
		if (isHoldingItemInOffHand(piglinPrisoner)) {
			piglinPrisoner.spawnAtLocation(piglinPrisoner.getItemInHand(InteractionHand.OFF_HAND));
		}
		piglinPrisoner.holdInOffHand(stack);
	}

	private static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
		ItemStack itemstack = itemEntity.getItem();
		ItemStack itemstack1 = itemstack.split(1);
		if (itemstack.isEmpty()) {
			itemEntity.discard();
		}
		else {
			itemEntity.setItem(itemstack);
		}
		return itemstack1;
	}

	public static void stopHoldingOffHandItem(PiglinPrisoner piglinPrisoner, boolean shouldThrowItems) {
		ItemStack itemstack = piglinPrisoner.getItemInHand(InteractionHand.OFF_HAND);
		piglinPrisoner.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		boolean flag = itemstack.isPiglinCurrency();
		if (shouldThrowItems && flag) {
			putInInventory(piglinPrisoner, itemstack);
		} else if (!flag) {
			boolean flag1 = !piglinPrisoner.equipItemIfPossible(itemstack).isEmpty();
			if (!flag1) {
				throwItems(piglinPrisoner, Collections.singletonList(itemstack));
			}
		}
	}

	public static void throwItems(PiglinPrisoner piglinPrisoner, List<ItemStack> stackList) {
		Player tempter = piglinPrisoner.getTempter();
		Optional<Player> optional = piglinPrisoner.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
		if (tempter != null) {
			throwItemsTowardPlayer(piglinPrisoner, tempter, stackList);
		}
		else if (optional.isPresent()) {
			throwItemsTowardPlayer(piglinPrisoner, optional.get(), stackList);
		}
		else {
			throwItemsTowardRandomPos(piglinPrisoner, stackList);
		}
	}

	private static void throwItemsTowardRandomPos(PiglinPrisoner piglinPrisoner, List<ItemStack> stackList) {
		throwItemsTowardPos(piglinPrisoner, stackList, getRandomNearbyPos(piglinPrisoner));
	}

	private static void throwItemsTowardPlayer(PiglinPrisoner piglinPrisoner, Player player, List<ItemStack> stackList) {
		throwItemsTowardPos(piglinPrisoner, stackList, player.position());
	}

	private static void throwItemsTowardPos(PiglinPrisoner piglinPrisoner, List<ItemStack> stackList, Vec3 vec) {
		if (!stackList.isEmpty()) {
			piglinPrisoner.swing(InteractionHand.OFF_HAND);
			for (ItemStack itemstack : stackList) {
				BehaviorUtils.throwItem(piglinPrisoner, itemstack, vec.add(0.0D, 1.0D, 0.0D));
			}
		}
	}

	private static Vec3 getRandomNearbyPos(PiglinPrisoner piglinPrisoner) {
		Vec3 vec3 = LandRandomPos.getPos(piglinPrisoner, 4, 2);
		return vec3 == null ? piglinPrisoner.position() : vec3;
	}

	public static void cancelAdmiring(PiglinPrisoner piglinPrisoner) {
		if (isAdmiringItem(piglinPrisoner) && !piglinPrisoner.getOffhandItem().isEmpty()) {
			piglinPrisoner.spawnAtLocation(piglinPrisoner.getOffhandItem());
			piglinPrisoner.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		}
	}

	public static boolean wantsToPickup(PiglinPrisoner piglinPrisoner, ItemStack stack) {
		if (stack.is(ItemTags.PIGLIN_REPELLENTS)) {
			return false;
		}
		else if (isAdmiringDisabled(piglinPrisoner) && piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
			return false;
		}
		else if (stack.isPiglinCurrency() || isLovedItem(stack)) {
			return isNotHoldingLovedItemInOffHand(piglinPrisoner);
		}
		else {
			boolean flag = piglinPrisoner.canAddToInventory(stack);
			if (stack.is(Items.GOLD_NUGGET)) {
				return flag;
			}
			else if (!isLovedItem(stack)) {
				return piglinPrisoner.canReplaceCurrentItem(stack);
			}
			else {
				return isNotHoldingLovedItemInOffHand(piglinPrisoner) && flag;
			}
		}
	}

	public static boolean isLovedItem(ItemStack stack) {
		return stack.is(ItemTags.PIGLIN_LOVED);
	}

	private static boolean isNearestValidAttackTarget(PiglinPrisoner piglinPrisoner, LivingEntity target) {
		return findNearestValidAttackTarget(piglinPrisoner).filter((potentialTarget) -> potentialTarget == target).isPresent();
	}


	private static boolean isNearZombified(PiglinPrisoner piglinPrisoner) {
		Brain<PiglinPrisoner> brain = piglinPrisoner.getBrain();
		if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
			LivingEntity livingentity = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
			return piglinPrisoner.closerThan(livingentity, 6.0D);
		} else {
			return false;
		}
	}

	private static Optional<? extends LivingEntity> findNearestValidAttackTarget(PiglinPrisoner piglinPrisoner) {
		Brain<PiglinPrisoner> brain = piglinPrisoner.getBrain();
		if (isNearZombified(piglinPrisoner)) return Optional.empty();
		else {
			Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(piglinPrisoner, MemoryModuleType.ANGRY_AT);
			if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(piglinPrisoner, optional.get())) {
				return optional;
			}
			else {
				Optional<Mob> optional1 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
				if (optional1.isPresent()) {
					return optional1;
				}
			}
		}
		return Optional.empty();
	}

	public static InteractionResult mobInteract(PiglinPrisoner piglinPrisoner, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (canAdmire(piglinPrisoner, itemstack)) {
			ItemStack itemstack1 = itemstack.split(1);
			holdInOffhand(piglinPrisoner, itemstack1);
			if (!player.equals(piglinPrisoner.getTempter())) {
				newTemptingPlayer(piglinPrisoner, player);
			}
			admireGoldItem(piglinPrisoner);
			stopWalking(piglinPrisoner);
			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.PASS;
		}
	}

	public static boolean canAdmire(PiglinPrisoner piglinPrisoner, ItemStack stack) {
		return !isAdmiringDisabled(piglinPrisoner)
				&& !isAdmiringItem(piglinPrisoner)
				&& (stack.isPiglinCurrency() || isLovedItem(stack));
	}

	public static void wasHurtBy(PiglinPrisoner piglin, LivingEntity attacker) {
		if (attacker instanceof Piglin) {
			return;
		}
		if (isHoldingItemInOffHand(piglin)) {
			stopHoldingOffHandItem(piglin, false);
		}
		Brain<PiglinPrisoner> brain = piglin.getBrain();
		brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
		brain.eraseMemory(MemoryModuleType.DANCING);
		brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
		if (attacker instanceof Player) {
			brain.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, true, 400L);
		}
		getAvoidTarget(piglin).ifPresent((target) -> {if (target.getType() != attacker.getType()) brain.eraseMemory(MemoryModuleType.AVOID_TARGET); });
		maybeRetaliate(piglin, attacker);
	}

	protected static void maybeRetaliate(AbstractPiglin piglin, LivingEntity target) {
		if (!piglin.getBrain().isActive(Activity.AVOID)
				&& Sensor.isEntityAttackableIgnoringLineOfSight(piglin, target)
				&& !BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(piglin, target, 4.0D)
				&& target.getType() != EntityType.PLAYER) {
			setAngerTarget(piglin, target);
			broadcastAngerTarget(piglin, target);
		}
	}

	public static Optional<SoundEvent> getSoundForCurrentActivity(PiglinPrisoner piglinPrisoner) {
		return piglinPrisoner.getBrain().getActiveNonCoreActivity().map((activity) -> getSoundForActivity(piglinPrisoner, activity));
	}

	private static SoundEvent getSoundForActivity(PiglinPrisoner piglinPrisoner, Activity activity) {
		if (activity == Activity.FIGHT) {
			return SoundEvents.PIGLIN_ANGRY;
		}
		else if (piglinPrisoner.isConverting()) {
			return SoundEvents.PIGLIN_RETREAT;
		}
		else if (activity == Activity.AVOID && isNearAvoidTarget(piglinPrisoner)) {
			return SoundEvents.PIGLIN_RETREAT;
		}
		else if (activity == Activity.ADMIRE_ITEM) {
			return SoundEvents.PIGLIN_ADMIRING_ITEM;
		}
		else if (activity == Activity.CELEBRATE) {
			return SoundEvents.PIGLIN_CELEBRATE;
		}
		else if (seesPlayerHoldingLovedItem(piglinPrisoner)) {
			return SoundEvents.PIGLIN_JEALOUS;
		}
		else {
			return isNearRepellent(piglinPrisoner) ? SoundEvents.PIGLIN_RETREAT : SoundEvents.PIGLIN_AMBIENT;
		}
	}

	private static boolean isNearAvoidTarget(PiglinPrisoner piglinPrisoner) {
		Brain<PiglinPrisoner> brain = piglinPrisoner.getBrain();
		return brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET) && brain.getMemory(MemoryModuleType.AVOID_TARGET).get().closerThan(piglinPrisoner, 12.0D);
	}

	private static void stopWalking(PiglinPrisoner piglinPrisoner) {
		piglinPrisoner.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
		piglinPrisoner.getNavigation().stop();
	}

	public static void setAngerTarget(AbstractPiglin piglin, LivingEntity target) {
		if (Sensor.isEntityAttackableIgnoringLineOfSight(piglin, target)) {
			piglin.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
			piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 600L);
		}
	}

	private static boolean isNearRepellent(PiglinPrisoner piglinPrisoner) {
		return piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
	}

	public static void exciteNearbyPiglins(Player player, boolean requireVisibility) {
		List<PiglinPrisoner> list = player.level().getEntitiesOfClass(PiglinPrisoner.class, player.getBoundingBox().inflate(16.0D));
		list.stream().filter(PiglinAi::isIdle).filter((piglin) -> !requireVisibility || BehaviorUtils.canSee(piglin, player)).forEach(PiglinPrisonerAi::startDancing);
	}

	public static void startDancing(PiglinPrisoner piglinPrisoner) {
		piglinPrisoner.getBrain().setMemoryWithExpiry(MemoryModuleType.DANCING, true, CELEBRATION_TIME);
		piglinPrisoner.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, piglinPrisoner.blockPosition(), CELEBRATION_TIME);
	}

	private static void startDancing(Piglin piglin) {
		piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.DANCING, true, CELEBRATION_TIME);
		piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, piglin.blockPosition(), CELEBRATION_TIME);
	}

	private static boolean seesPlayerHoldingLovedItem(PiglinPrisoner piglinPrisoner) {
		return piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
	}

	private static boolean isHoldingItemInOffHand(PiglinPrisoner piglinPrisoner) {
		return !piglinPrisoner.getOffhandItem().isEmpty();
	}

	private static void admireGoldItem(PiglinPrisoner piglinPrisoner) {
		piglinPrisoner.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120L);
	}

	private static void putInInventory(PiglinPrisoner piglinPrisoner, ItemStack stack) {
		piglinPrisoner.addToInventory(stack);
		giveGoldBuff(piglinPrisoner);
		pledgeAllegiance(piglinPrisoner);
	}

	private static boolean hasCrossbow(LivingEntity livingentity) {
		return livingentity.isHolding(is -> is.getItem() instanceof CrossbowItem);
	}

	protected static void broadcastAngerTarget(AbstractPiglin piglin, LivingEntity target) {
		getAdultAbstractPiglins(piglin).forEach((adultPiglin) -> setAngerTargetIfCloserThanCurrent(adultPiglin, target));
	}

	public static void broadcastBeingRescued(AbstractPiglin piglin) {
		getAdultPiglins(piglin).forEach(PiglinPrisonerAi::startDancing);
	}

	private static List<AbstractPiglin> getAdultAbstractPiglins(AbstractPiglin piglin) {
		return piglin.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
	}

	private static List<Piglin> getAdultPiglins(AbstractPiglin piglin) {
		return piglin.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of()).stream().filter((abstractPiglin) -> abstractPiglin instanceof Piglin).map((abstractPiglin -> (Piglin) abstractPiglin)).toList();
	}

	private static void setAngerTargetIfCloserThanCurrent(AbstractPiglin piglin, LivingEntity target) {
		Optional<LivingEntity> optional = getAngerTarget(piglin);
		LivingEntity livingentity = BehaviorUtils.getNearestTarget(piglin, optional, target);
		if (optional.isEmpty() || optional.get() != livingentity) {
			setAngerTarget(piglin, livingentity);
		}
	}

	private static Optional<LivingEntity> getAngerTarget(AbstractPiglin piglin) {
		return BehaviorUtils.getLivingEntityFromUUIDMemory(piglin, MemoryModuleType.ANGRY_AT);
	}

	private static boolean isNotHoldingLovedItemInOffHand(PiglinPrisoner piglinPrisoner) {
		return piglinPrisoner.getOffhandItem().isEmpty() || !isLovedItem(piglinPrisoner.getOffhandItem());
	}

	private static boolean isAdmiringItem(PiglinPrisoner piglinPrisoner) {
		return piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
	}

	private static boolean isAdmiringDisabled(PiglinPrisoner piglinPrisoner) {
		return piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
	}

	public static Optional<LivingEntity> getAvoidTarget(PiglinPrisoner piglinPrisoner) {
		return piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET) ? piglinPrisoner.getBrain().getMemory(MemoryModuleType.AVOID_TARGET) : Optional.empty();
	}

	public static boolean isPlayerHoldingLovedItem(LivingEntity player) {
		return player.getType() == EntityType.PLAYER && player.isHolding(PiglinPrisonerAi::isLovedItem);
	}

	private static boolean isFood(ItemStack stack) {
		return stack.is(ItemTags.PIGLIN_FOOD);
	}

	private static void eat(PiglinPrisoner piglinPrisoner) {
		piglinPrisoner.getBrain().setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, true, 200L);
	}

	private static boolean hasEatenRecently(PiglinPrisoner piglinPrisoner) {
		return piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY);
	}

	private static void giveGoldBuff(PiglinPrisoner piglinPrisoner) {
		piglinPrisoner.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 60 * 180, 3, false, true));
		piglinPrisoner.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60 * 120, 1, false, false));
	}

	private static void newTemptingPlayer(PiglinPrisoner piglinPrisoner, Player player) {
		piglinPrisoner.getBrain().setMemory(MemoryModuleType.TEMPTING_PLAYER, player);
		piglinPrisoner.getBrain().setMemory(MemoryModuleType.IS_TEMPTED, false);
		piglinPrisoner.setTempterUUID(player.getUUID());
	}

	protected static void pledgeAllegiance(PiglinPrisoner piglinPrisoner) {
		if (piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.TEMPTING_PLAYER)) {
			piglinPrisoner.getBrain().setMemory(MemoryModuleType.IS_TEMPTED, true);
		}
	}

	public static void reloadAllegiance(PiglinPrisoner piglinPrisoner, Player player) {
		piglinPrisoner.getBrain().setMemory(MemoryModuleType.TEMPTING_PLAYER, player);
		piglinPrisoner.getBrain().setMemory(MemoryModuleType.IS_TEMPTED, true);
	}

}
