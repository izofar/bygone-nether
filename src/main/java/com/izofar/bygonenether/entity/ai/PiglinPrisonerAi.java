package com.izofar.bygonenether.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.izofar.bygonenether.entity.PiglinPrisoner;
import com.izofar.bygonenether.entity.ai.behavior.*;
import com.izofar.bygonenether.init.ModEntityTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.RememberIfHoglinWasKilled;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
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
		if(mob instanceof PiglinPrisoner piglin) {
			Brain<PiglinPrisoner> brain = piglin.getBrain();
			return brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)
					|| brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_NEMESIS)
					|| brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)
					|| brain.getMemory(MemoryModuleType.DANCING).orElse(false)
					|| !brain.getMemory(MemoryModuleType.IS_TEMPTED).orElse(false);
		}
		else return false;
	};

	public static Brain<?> makeBrain(PiglinPrisoner piglin, Brain<PiglinPrisoner> brain) {
		initCoreActivity(brain);
		initIdleActivity(brain);
		initAdmireItemActivity(brain);
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
						new ModFollowLeader(isDistracted),
						new LookAtTargetSink(45, 90),
						new MoveToTargetSink(),
						new InteractWithDoor(),
						avoidZombified(),
						new ModStopHoldingItemIfNoLongerAdmiring<>(),
						new ModStartAdmiringItemIfSeen<>(120),
						new StopBeingAngryIfTargetDead<>()));

	}

	private static void initIdleActivity(Brain<PiglinPrisoner> brain) {
		brain.addActivity(Activity.IDLE, 10,
				ImmutableList.of(
						new SetEntityLookTarget(PiglinPrisonerAi::isPlayerHoldingLovedItem, 14.0F),
						new StartAttacking<>(AbstractPiglin::isAdult, PiglinPrisonerAi::findNearestValidAttackTarget),
						avoidRepellent(),
						createIdleLookBehaviors(),
						createIdleMovementBehaviors(),
						new SetLookAndInteract(EntityType.PLAYER, 4))
		);
	}

	private static void initFightActivity(PiglinPrisoner piglin, Brain<PiglinPrisoner> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
				ImmutableList.<Behavior<? super PiglinPrisoner>>of(
						new StopAttackingIfTargetInvalid<>((target) -> !isNearestValidAttackTarget(piglin, target)),
						new RunIf<>(PiglinPrisonerAi::hasCrossbow, new BackUpIfTooClose<>(5, 0.75F)),
						new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0F),
						new MeleeAttack(20),
						new CrossbowAttack(),
						new RememberIfHoglinWasKilled(),
						new EraseMemoryIf<>(PiglinPrisonerAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)),
				MemoryModuleType.ATTACK_TARGET);
	}

	private static void initCelebrateActivity(Brain<PiglinPrisoner> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.CELEBRATE, 10, ImmutableList.of(
						avoidRepellent(),
						new SetEntityLookTarget(PiglinPrisonerAi::isPlayerHoldingLovedItem, 14.0F),
						new StartAttacking<>(AbstractPiglin::isAdult, PiglinPrisonerAi::findNearestValidAttackTarget),
						new RunIf<>((piglinPrisoner) -> !piglinPrisoner.isDancing(), new GoToCelebrateLocation<>(2, 1.0F)),
						new RunIf<>(PiglinPrisoner::isDancing, new GoToCelebrateLocation<>(4, 0.6F)),
						new RunOne<>(ImmutableList.of(
								Pair.of(new SetEntityLookTarget(ModEntityTypes.PIGLIN_PRISONER.get(), 8.0F), 1),
								Pair.of(new RandomStroll(0.6F, 2, 1), 1),
								Pair.of(new DoNothing(10, 20), 1)))
				),
				MemoryModuleType.CELEBRATE_LOCATION);
	}

	private static void initAdmireItemActivity(Brain<PiglinPrisoner> brain) {
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10,
				ImmutableList.of(
						new GoToWantedItem<>(PiglinPrisonerAi::isNotHoldingLovedItemInOffHand, 1.0F, true, 9),
						new ModStopAdmiringIfItemTooFarAway<>(9),
						new ModStopAdmiringIfTiredOfTryingToReachItem<>(200, 200)
				),
				MemoryModuleType.ADMIRING_ITEM);
	}

	private static RunOne<PiglinPrisoner> createIdleLookBehaviors() {
		return new RunOne<>(
				ImmutableList.of(
						Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 1),
						Pair.of(new SetEntityLookTarget(EntityType.PIGLIN, 8.0F), 1),
						Pair.of(new SetEntityLookTarget(ModEntityTypes.PIGLIN_PRISONER.get(), 8.0F), 1),
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
						Pair.of(InteractWith.of(ModEntityTypes.PIGLIN_PRISONER.get(), 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
						Pair.of(new DoNothing(30, 60), 1)
				)
		);
	}

	private static SetWalkTargetAwayFrom<BlockPos> avoidRepellent() { return SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false); }

	private static CopyMemoryWithExpiry<PiglinPrisoner, LivingEntity> avoidZombified() {
		return new CopyMemoryWithExpiry<>(
				PiglinPrisonerAi::isNearZombified,
				MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
				MemoryModuleType.AVOID_TARGET,
				AVOID_ZOMBIFIED_DURATION
		);
	}

	public static void updateActivity(PiglinPrisoner piglin) {
		Brain<PiglinPrisoner> brain = piglin.getBrain();
		Activity activity = brain.getActiveNonCoreActivity().orElse(null);
		brain.setActiveActivityToFirstValid(
				ImmutableList.of(
						Activity.ADMIRE_ITEM,
						Activity.FIGHT,
						Activity.AVOID,
						Activity.CELEBRATE,
						Activity.IDLE
				)
		);
		Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
		if (activity != activity1)
			getSoundForCurrentActivity(piglin).ifPresent(piglin::playSound);
		piglin.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));

		if (!brain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION))
			brain.eraseMemory(MemoryModuleType.DANCING);

		piglin.setDancing(brain.hasMemoryValue(MemoryModuleType.DANCING));
	}

	public static void pickUpItem(PiglinPrisoner piglin, ItemEntity itemEntity) {
		stopWalking(piglin);
		ItemStack itemstack;

		if (itemEntity.getItem().is(Items.GOLD_NUGGET)) {
			piglin.take(itemEntity, itemEntity.getItem().getCount());
			itemstack = itemEntity.getItem();
			itemEntity.discard();
		} else {
			piglin.take(itemEntity, 1);
			itemstack = removeOneItemFromItemEntity(itemEntity);
		}

		if (isLovedItem(itemstack)) {
			piglin.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
			holdInOffhand(piglin, itemstack);
			admireGoldItem(piglin);
		} else if (isFood(itemstack) && !hasEatenRecently(piglin)) {
			eat(piglin);
		} else if (!piglin.equipItemIfPossible(itemstack)){
			putInInventory(piglin, itemstack);
		}
	}

	private static void holdInOffhand(PiglinPrisoner piglin, ItemStack stack) {
		if (isHoldingItemInOffHand(piglin))
			piglin.spawnAtLocation(piglin.getItemInHand(InteractionHand.OFF_HAND));
		piglin.holdInOffHand(stack);
	}

	private static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
		ItemStack itemstack = itemEntity.getItem();
		ItemStack itemstack1 = itemstack.split(1);
		if (itemstack.isEmpty())
			itemEntity.discard();
		else
			itemEntity.setItem(itemstack);
		return itemstack1;
	}

	public static void stopHoldingOffHandItem(PiglinPrisoner piglin, boolean shouldThrowItems) {
		ItemStack itemstack = piglin.getItemInHand(InteractionHand.OFF_HAND);
		piglin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		if (piglin.isAdult()) {
			boolean flag = itemstack.isPiglinCurrency();
			if (shouldThrowItems && flag) {
				putInInventory(piglin, itemstack);
			} else if (!flag) {
				boolean flag1 = piglin.equipItemIfPossible(itemstack);
				if (!flag1) {
					throwItems(piglin, Collections.singletonList(itemstack));
				}
			}
		} else {
			boolean flag2 = piglin.equipItemIfPossible(itemstack);
			if (!flag2) {
				ItemStack itemstack1 = piglin.getMainHandItem();
				if (isLovedItem(itemstack1)) {
					putInInventory(piglin, itemstack1);
				} else {
					throwItems(piglin, Collections.singletonList(itemstack1));
				}
				piglin.holdInMainHand(itemstack);
			}
		}
	}

	private static void throwItems(PiglinPrisoner p_34861_, List<ItemStack> p_34862_) {
		Optional<Player> optional = p_34861_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
		if (optional.isPresent())
			throwItemsTowardPlayer(p_34861_, optional.get(), p_34862_);
		else
			throwItemsTowardRandomPos(p_34861_, p_34862_);
	}

	private static void throwItemsTowardRandomPos(PiglinPrisoner piglin, List<ItemStack> stackList) {
		throwItemsTowardPos(piglin, stackList, getRandomNearbyPos(piglin));
	}

	private static void throwItemsTowardPlayer(PiglinPrisoner piglin, Player player, List<ItemStack> stackList) {
		throwItemsTowardPos(piglin, stackList, player.position());
	}

	private static void throwItemsTowardPos(PiglinPrisoner piglin, List<ItemStack> stackList, Vec3 vec) {
		if (!stackList.isEmpty()) {
			piglin.swing(InteractionHand.OFF_HAND);

			for(ItemStack itemstack : stackList)
				BehaviorUtils.throwItem(piglin, itemstack, vec.add(0.0D, 1.0D, 0.0D));
		}
	}

	private static Vec3 getRandomNearbyPos(PiglinPrisoner piglin) {
		Vec3 vec3 = LandRandomPos.getPos(piglin, 4, 2);
		return vec3 == null ? piglin.position() : vec3;
	}

	public static void cancelAdmiring(PiglinPrisoner piglin) {
		if (isAdmiringItem(piglin) && !piglin.getOffhandItem().isEmpty()) {
			piglin.spawnAtLocation(piglin.getOffhandItem());
			piglin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		}
	}

	public static boolean wantsToPickup(PiglinPrisoner piglin, ItemStack stack) {
		if (stack.is(ItemTags.PIGLIN_REPELLENTS))
			return false;
		else if (isAdmiringDisabled(piglin) && piglin.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
			return false;
		else if (stack.isPiglinCurrency() || isLovedItem(stack))
			return isNotHoldingLovedItemInOffHand(piglin);
		else {
			boolean flag = piglin.canAddToInventory(stack);
			if (stack.is(Items.GOLD_NUGGET))
				return flag;
			else if (!isLovedItem(stack))
				return piglin.canReplaceCurrentItem(stack);
			else
				return isNotHoldingLovedItemInOffHand(piglin) && flag;
		}
	}

	public static boolean isLovedItem(ItemStack stack) {  return stack.is(ItemTags.PIGLIN_LOVED); }

	private static boolean isNearestValidAttackTarget(PiglinPrisoner piglin, LivingEntity target) { return findNearestValidAttackTarget(piglin).filter((potentialTarget) -> potentialTarget == target).isPresent(); }


	private static boolean isNearZombified(PiglinPrisoner entity) {
		Brain<PiglinPrisoner> brain = entity.getBrain();
		if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
			LivingEntity livingentity = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
			return entity.closerThan(livingentity, 6.0D);
		} else return false;
	}

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

	public static InteractionResult mobInteract(PiglinPrisoner piglin, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (canAdmire(piglin, itemstack)) {
			ItemStack itemstack1 = itemstack.split(1);
			holdInOffhand(piglin, itemstack1);
			if(!player.equals(piglin.getTempter())) 
				newTemptingPlayer(piglin, player);
			admireGoldItem(piglin);
			stopWalking(piglin);
			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.PASS;
		}
	}

	public static boolean canAdmire(PiglinPrisoner piglin, ItemStack stack) {
		return !isAdmiringDisabled(piglin) && !isAdmiringItem(piglin) && piglin.isAdult() && (stack.isPiglinCurrency() || isLovedItem(stack));
	}

	public static void wasHurtBy(PiglinPrisoner piglin, LivingEntity attacker) {
		if (attacker instanceof Piglin) return;

		if (isHoldingItemInOffHand(piglin))
			stopHoldingOffHandItem(piglin, false);

		Brain<PiglinPrisoner> brain = piglin.getBrain();
		brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
		brain.eraseMemory(MemoryModuleType.DANCING);
		brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
		if (attacker instanceof Player)
			brain.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, true, 400L);

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

	public static Optional<SoundEvent> getSoundForCurrentActivity(PiglinPrisoner piglin) { return piglin.getBrain().getActiveNonCoreActivity().map((activity) -> getSoundForActivity(piglin, activity)); }

	private static SoundEvent getSoundForActivity(PiglinPrisoner piglin, Activity activity) {
		if (activity == Activity.FIGHT) return SoundEvents.PIGLIN_ANGRY;
		else if (piglin.isConverting()) return SoundEvents.PIGLIN_RETREAT;
		else if (activity == Activity.AVOID && isNearAvoidTarget(piglin)) return SoundEvents.PIGLIN_RETREAT;
		else if (activity == Activity.ADMIRE_ITEM) return SoundEvents.PIGLIN_ADMIRING_ITEM;
		else if (activity == Activity.CELEBRATE) return SoundEvents.PIGLIN_CELEBRATE;
		else if (seesPlayerHoldingLovedItem(piglin)) return SoundEvents.PIGLIN_JEALOUS;
		else return isNearRepellent(piglin) ? SoundEvents.PIGLIN_RETREAT : SoundEvents.PIGLIN_AMBIENT;
	}

	private static boolean isNearAvoidTarget(PiglinPrisoner piglin) {
		Brain<PiglinPrisoner> brain = piglin.getBrain();
		return brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET) && brain.getMemory(MemoryModuleType.AVOID_TARGET).get().closerThan(piglin, 12.0D);
	}

	private static void stopWalking(PiglinPrisoner piglin) {
		piglin.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
		piglin.getNavigation().stop();
	}

	public static void setAngerTarget(AbstractPiglin piglin, LivingEntity target) {
		if (Sensor.isEntityAttackableIgnoringLineOfSight(piglin, target)) {
			piglin.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
			piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 600L);
		}
	}

	private static boolean isNearRepellent(PiglinPrisoner piglin) { return piglin.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT); }

	public static void exciteNearbyPiglins(Player player, boolean requireVisibility) {
		List<PiglinPrisoner> list = player.level.getEntitiesOfClass(PiglinPrisoner.class, player.getBoundingBox().inflate(16.0D));
		list.stream().filter(PiglinAi::isIdle).filter((piglin) -> !requireVisibility || BehaviorUtils.canSee(piglin, player)).forEach(PiglinPrisonerAi::startDancing);
	}

	private static void startDancing(PiglinPrisoner piglin){
		piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.DANCING, true, CELEBRATION_TIME);
		piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, piglin.blockPosition(), CELEBRATION_TIME);
	}

	private static boolean seesPlayerHoldingLovedItem(LivingEntity piglin) {
		return piglin.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
	}

	private static boolean isHoldingItemInOffHand(PiglinPrisoner piglin) {
		return !piglin.getOffhandItem().isEmpty();
	}

	private static void admireGoldItem(LivingEntity piglin) {
		piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120L);
	}

	private static void putInInventory(PiglinPrisoner piglin, ItemStack stack) {
		piglin.addToInventory(stack);
		giveGoldBuff(piglin);
		pledgeAllegiance(piglin);
	}

	private static boolean hasCrossbow(LivingEntity entity) { return entity.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem); }

	protected static void broadcastAngerTarget(AbstractPiglin piglin, LivingEntity target) {
		getAdultPiglins(piglin).forEach((adultPiglin) -> setAngerTargetIfCloserThanCurrent(adultPiglin, target));
	}

	private static List<AbstractPiglin> getAdultPiglins(AbstractPiglin piglin) {
		return piglin.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
	}

	private static void setAngerTargetIfCloserThanCurrent(AbstractPiglin piglin, LivingEntity target) {
		Optional<LivingEntity> optional = getAngerTarget(piglin);
		LivingEntity livingentity = BehaviorUtils.getNearestTarget(piglin, optional, target);
		if (optional.isEmpty() || optional.get() != livingentity)
			setAngerTarget(piglin, livingentity);
	}

	private static Optional<LivingEntity> getAngerTarget(AbstractPiglin piglin) {
		return BehaviorUtils.getLivingEntityFromUUIDMemory(piglin, MemoryModuleType.ANGRY_AT);
	}

	private static boolean isNotHoldingLovedItemInOffHand(PiglinPrisoner piglin) {
		return piglin.getOffhandItem().isEmpty() || !isLovedItem(piglin.getOffhandItem());
	}

	private static boolean isAdmiringItem(PiglinPrisoner piglin) {
		return piglin.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
	}

	private static boolean isAdmiringDisabled(PiglinPrisoner piglin) {
		return piglin.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
	}

	public static Optional<LivingEntity> getAvoidTarget(PiglinPrisoner piglin) {
		return piglin.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET) ? piglin.getBrain().getMemory(MemoryModuleType.AVOID_TARGET) : Optional.empty();
	}

	public static boolean isPlayerHoldingLovedItem(LivingEntity player) {
		return player.getType() == EntityType.PLAYER && player.isHolding(PiglinPrisonerAi::isLovedItem);
	}

	private static boolean isFood(ItemStack stack) {
		return stack.is(ItemTags.PIGLIN_FOOD);
	}

	private static void eat(PiglinPrisoner piglin) {
		piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, true, 200L);
	}

	private static boolean hasEatenRecently(PiglinPrisoner piglin) {
		return piglin.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY);
	}

	private static void giveGoldBuff(PiglinPrisoner piglin){
		piglin.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 60 * 180, 3, false, true));
		piglin.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60 * 120, 1, false, false));
	}

	private static void newTemptingPlayer(PiglinPrisoner piglin, Player player){
		piglin.getBrain().setMemory(MemoryModuleType.TEMPTING_PLAYER, player);
		piglin.getBrain().setMemory(MemoryModuleType.IS_TEMPTED, false);
		piglin.setTempterUUID(player.getUUID());
	}

	protected static void pledgeAllegiance(PiglinPrisoner piglin){
		if(piglin.getBrain().hasMemoryValue(MemoryModuleType.TEMPTING_PLAYER))
			piglin.getBrain().setMemory(MemoryModuleType.IS_TEMPTED, true);
	}

	public static void reloadAllegiance(PiglinPrisoner piglin, Player player){
		piglin.getBrain().setMemory(MemoryModuleType.TEMPTING_PLAYER, player);
		piglin.getBrain().setMemory(MemoryModuleType.IS_TEMPTED, true);
	}

}
