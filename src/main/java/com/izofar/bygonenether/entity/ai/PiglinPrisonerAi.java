package com.izofar.bygonenether.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.izofar.bygonenether.entity.PiglinPrisonerEntity;
import com.izofar.bygonenether.entity.ai.behvaior.*;
import com.izofar.bygonenether.init.ModEntityTypes;
import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PiglinPrisonerAi {

    private static final RangedInteger AVOID_ZOMBIFIED_DURATION = TickRangeConverter.rangeOfSeconds(5, 7);
    private static final int CELEBRATION_TIME = 200;

    private static final Predicate<CreatureEntity> isDistracted = (mob) -> {
        if (mob instanceof PiglinPrisonerEntity) {
            PiglinPrisonerEntity piglinPrisoner = (PiglinPrisonerEntity) mob;
            Brain<PiglinPrisonerEntity> brain = piglinPrisoner.getBrain();
            return brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)
                    || brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_NEMESIS)
                    || brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)
                    || brain.getMemory(MemoryModuleType.DANCING).orElse(false)
                    || !brain.getMemory(ModMemoryModuleTypes.IS_TEMPTED.get()).orElse(false);
        }
        else {
            return false;
        }
    };

    public static Brain<?> makeBrain(PiglinPrisonerEntity piglinPrisoner, Brain<PiglinPrisonerEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initAdmireItemActivity(brain);
        initFightActivity(piglinPrisoner, brain);
        initCelebrateActivity(brain);
        initRetreatActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<PiglinPrisonerEntity> brain) {
        brain.addActivity(Activity.CORE, 0,
                ImmutableList.<Task<? super PiglinPrisonerEntity>>of(
                        new ModFollowLeaderTask<>(isDistracted),
                        new LookTask(45, 90),
                        new WalkToTargetTask(),
                        new InteractWithDoorTask(),
                        avoidZombified(),
                        new ModStartAdmiringItemTask<>(),
                        new ModAdmireItemTask<>(120),
                        new GetAngryTask<>()
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

    private static void initFightActivity(PiglinPrisonerEntity piglinPrisoner, Brain<PiglinPrisonerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
                ImmutableList.<Task<? super PiglinPrisonerEntity>>of(
                        new FindNewAttackTargetTask<>((target) -> !isNearestValidAttackTarget(piglinPrisoner, target)),
                        new SupplementedTask<>(PiglinPrisonerAi::hasCrossbow, new AttackStrafingTask<>(5, 0.75F)),
                        new MoveToTargetTask(1.0F),
                        new AttackTargetTask(20),
                        new ShootTargetTask<>(),
                        new PredicateTask<>(PiglinPrisonerAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static void initCelebrateActivity(Brain<PiglinPrisonerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.CELEBRATE, 10,
                ImmutableList.<Task<? super PiglinPrisonerEntity>>of(
                        avoidRepellent(),
                        new LookAtEntityTask(PiglinTasks::isPlayerHoldingLovedItem, 14.0F),
                        new ForgetAttackTargetTask<>(AbstractPiglinEntity::isAdult, PiglinPrisonerAi::findNearestValidAttackTarget),
                        new SupplementedTask<>((mob) -> !mob.isDancing(), new HuntCelebrationTask<>(2, 1.0F)),
                        new SupplementedTask<>(PiglinPrisonerEntity::isDancing, new HuntCelebrationTask<>(4, 0.6F)),
                        new FirstShuffledTask<>(ImmutableList.of(
                                Pair.of(new LookAtEntityTask(EntityType.PIGLIN, 8.0F), 1),
                                Pair.of(new WalkRandomlyTask(0.6F, 2, 1), 1),
                                Pair.of(new DummyTask(10, 20), 1))
                        )
                ),
                MemoryModuleType.CELEBRATE_LOCATION
        );
    }

    private static void initAdmireItemActivity(Brain<PiglinPrisonerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10,
                ImmutableList.<Task<? super PiglinPrisonerEntity>>of(
                        new PickupWantedItemTask<>(PiglinPrisonerAi::isNotHoldingLovedItemInOffHand, 1.0F, true, 9),
                        new ModForgetAdmiredItemTask<>(9),
                        new ModStopReachingItemTask<>(200, 200)
                ),
                MemoryModuleType.ADMIRING_ITEM
        );
    }

    private static void initRetreatActivity(Brain<PiglinPrisonerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10,
                ImmutableList.of(
                        RunAwayTask.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true),
                        createIdleLookBehaviors(), createIdleMovementBehaviors(),
                        new PredicateTask<>(PiglinPrisonerAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)),
                MemoryModuleType.AVOID_TARGET
        );
    }

    private static FirstShuffledTask<PiglinPrisonerEntity> createIdleLookBehaviors() {
        return new FirstShuffledTask<>(
                ImmutableList.of(
                        Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 1),
                        Pair.of(new LookAtEntityTask(EntityType.PIGLIN, 8.0F), 1),
                        Pair.of(new LookAtEntityTask(ModEntityTypes.PIGLIN_PRISONER.get(), 8.0F), 1),
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
                        Pair.of(InteractWithEntityTask.of(ModEntityTypes.PIGLIN_PRISONER.get(), 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(new SupplementedTask<>(PiglinPrisonerAi::doesntSeeAnyPlayerHoldingLovedItem, new WalkTowardsLookTargetTask(0.6F, 3)), 2),
                        Pair.of(new DummyTask(30, 60), 1)
                )
        );
    }

    private static RunAwayTask<BlockPos> avoidRepellent() {
        return RunAwayTask.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false);
    }

    private static PiglinIdleActivityTask<PiglinPrisonerEntity, LivingEntity> avoidZombified() {
        return new PiglinIdleActivityTask<>(
                PiglinPrisonerAi::isNearZombified,
                MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
                MemoryModuleType.AVOID_TARGET,
                AVOID_ZOMBIFIED_DURATION
        );
    }

    public static void updateActivity(PiglinPrisonerEntity piglinPrisoner) {
        Brain<PiglinPrisonerEntity> brain = piglinPrisoner.getBrain();
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
        if (activity != activity1) getSoundForCurrentActivity(piglinPrisoner).ifPresent(piglinPrisoner::playSound);
        piglinPrisoner.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));

        if (!brain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION))
            brain.eraseMemory(MemoryModuleType.DANCING);

        piglinPrisoner.setDancing(brain.hasMemoryValue(MemoryModuleType.DANCING));
    }

    public static void pickUpItem(PiglinPrisonerEntity piglinPrisoner, ItemEntity itemEntity) {
        stopWalking(piglinPrisoner);
        ItemStack itemstack;
        if (itemEntity.getItem().getItem() == Items.GOLD_NUGGET) {
            piglinPrisoner.take(itemEntity, itemEntity.getItem().getCount());
            itemstack = itemEntity.getItem();
            itemEntity.remove();
        } else {
            piglinPrisoner.take(itemEntity, 1);
            itemstack = removeOneItemFromItemEntity(itemEntity);
        }

        Item item = itemstack.getItem();
        if (isLovedItem(item)) {
            piglinPrisoner.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(piglinPrisoner, itemstack);
            admireGoldItem(piglinPrisoner);
        } else if (!piglinPrisoner.equipItemIfPossible(itemstack)) {
            putInInventory(piglinPrisoner, itemstack);
        }
    }

    private static void holdInOffhand(PiglinPrisonerEntity piglinPrisoner, ItemStack itemStack) {
        if (isHoldingItemInOffHand(piglinPrisoner))
            piglinPrisoner.spawnAtLocation(piglinPrisoner.getItemInHand(Hand.OFF_HAND));
        piglinPrisoner.holdInOffHand(itemStack);
    }

    private static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        ItemStack itemstack1 = itemstack.split(1);
        if (itemstack.isEmpty()) {
            itemEntity.remove();
        }
        else {
            itemEntity.setItem(itemstack);
        }
        return itemstack1;
    }

    public static void stopHoldingOffHandItem(PiglinPrisonerEntity piglinPrisoner, boolean shouldBarter) {
        ItemStack itemstack = piglinPrisoner.getItemInHand(Hand.OFF_HAND);
        piglinPrisoner.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        boolean flag = itemstack.isPiglinCurrency();
        if (shouldBarter && flag) {
            putInInventory(piglinPrisoner, itemstack);
        } else if (!flag) {
            boolean flag1 = piglinPrisoner.equipItemIfPossible(itemstack);
            if (!flag1) {
                throwItems(piglinPrisoner, Collections.singletonList(itemstack));
            }
        }
    }

    public static void cancelAdmiring(PiglinPrisonerEntity piglinPrisoner) {
        if (isAdmiringItem(piglinPrisoner) && !piglinPrisoner.getOffhandItem().isEmpty()) {
            piglinPrisoner.spawnAtLocation(piglinPrisoner.getOffhandItem());
            piglinPrisoner.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    private static void putInInventory(PiglinPrisonerEntity piglinPrisoner, ItemStack itemStack) {
        piglinPrisoner.addToInventory(itemStack);
        giveGoldBuff(piglinPrisoner);
        pledgeAllegiance(piglinPrisoner);
    }

    public static void throwItems(PiglinPrisonerEntity piglinPrisoner, List<ItemStack> stackList) {
        PlayerEntity tempter = piglinPrisoner.getTempter();
        Optional<PlayerEntity> optional = piglinPrisoner.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        if (tempter != null) {
            throwItemsTowardPlayer(piglinPrisoner, tempter, stackList);
        } else if (optional.isPresent()) {
            throwItemsTowardPlayer(piglinPrisoner, optional.get(), stackList);
        } else {
            throwItemsTowardRandomPos(piglinPrisoner, stackList);
        }
    }

    private static void throwItemsTowardRandomPos(PiglinPrisonerEntity piglinPrisoner, List<ItemStack> stackList) {
        throwItemsTowardPos(piglinPrisoner, stackList, getRandomNearbyPos(piglinPrisoner));
    }

    private static void throwItemsTowardPlayer(PiglinPrisonerEntity pPiglin, PlayerEntity pPlayer, List<ItemStack> pStacks) {
        throwItemsTowardPos(pPiglin, pStacks, pPlayer.position());
    }

    private static void throwItemsTowardPos(PiglinPrisonerEntity piglinPrisoner, List<ItemStack> stackList, Vector3d pos) {
        if (!stackList.isEmpty()) {
            piglinPrisoner.swing(Hand.OFF_HAND);
            for (ItemStack itemstack : stackList) {
                BrainUtil.throwItem(piglinPrisoner, itemstack, pos.add(0.0D, 1.0D, 0.0D));
            }
        }
    }

    public static boolean wantsToPickup(PiglinPrisonerEntity piglinPrisoner, ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item.is(ItemTags.PIGLIN_REPELLENTS)) {
            return false;
        } else if (isAdmiringDisabled(piglinPrisoner) && piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        } else if (itemStack.isPiglinCurrency() || isLovedItem(item)) {
            return isNotHoldingLovedItemInOffHand(piglinPrisoner);
        } else {
            boolean flag = piglinPrisoner.canAddToInventory(itemStack);
            if (item == Items.GOLD_NUGGET) {
                return flag;
            } else if (!isLovedItem(item)) {
                return piglinPrisoner.canReplaceCurrentItem(itemStack);
            } else {
                return isNotHoldingLovedItemInOffHand(piglinPrisoner) && flag;
            }
        }
    }

    public static boolean isLovedItem(Item item) {
        return item.is(ItemTags.PIGLIN_LOVED);
    }

    private static boolean isNearestValidAttackTarget(PiglinPrisonerEntity piglinPrisoner, LivingEntity target) {
        return findNearestValidAttackTarget(piglinPrisoner).filter((potentialTarget) -> potentialTarget == target).isPresent();
    }

    private static boolean isNearZombified(PiglinPrisonerEntity piglinPrisoner) {
        Brain<PiglinPrisonerEntity> brain = piglinPrisoner.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
            return piglinPrisoner.closerThan(brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get(), 6.0D);
        } else {
            return false;
        }
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(PiglinPrisonerEntity piglinPrisoner) {
        Brain<PiglinPrisonerEntity> brain = piglinPrisoner.getBrain();
        if (isNearZombified(piglinPrisoner)) {
            return Optional.empty();
        } else {
            Optional<LivingEntity> optional = BrainUtil.getLivingEntityFromUUIDMemory(piglinPrisoner, MemoryModuleType.ANGRY_AT);
            if (optional.isPresent() && isAttackAllowed(optional.get())) {
                return optional;
            } else {
                Optional<MobEntity> optional1 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
                if (optional1.isPresent()) {
                    return optional1;
                }
            }
        }
        return Optional.empty();
    }

    public static void exciteNearbyPiglins(PlayerEntity player, boolean requireVisibility) {
        List<PiglinPrisonerEntity> list = player.level.getEntitiesOfClass(PiglinPrisonerEntity.class, player.getBoundingBox().inflate(16.0D));
        list.stream().filter(PiglinTasks::isIdle).filter((piglin) -> !requireVisibility || BrainUtil.canSee(piglin, player)).forEach(PiglinPrisonerAi::startDancing);
    }

    private static void startDancing(PiglinEntity piglin) {
        piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.DANCING, true, CELEBRATION_TIME);
        piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, piglin.blockPosition(), CELEBRATION_TIME);
    }

    public static void startDancing(PiglinPrisonerEntity piglinPrisoner) {
        piglinPrisoner.getBrain().setMemoryWithExpiry(MemoryModuleType.DANCING, true, CELEBRATION_TIME);
        piglinPrisoner.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, piglinPrisoner.blockPosition(), CELEBRATION_TIME);
    }

    public static ActionResultType mobInteract(PiglinPrisonerEntity piglinPrisoner, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (canAdmire(piglinPrisoner, itemstack)) {
            ItemStack itemstack1 = itemstack.split(1);
            holdInOffhand(piglinPrisoner, itemstack1);
            if (!player.equals(piglinPrisoner.getTempter())) {
                newTemptingPlayer(piglinPrisoner, player);
            }
            admireGoldItem(piglinPrisoner);
            stopWalking(piglinPrisoner);
            return ActionResultType.CONSUME;
        } else {
            return ActionResultType.PASS;
        }
    }

    public static boolean canAdmire(PiglinPrisonerEntity piglinPrisoner, ItemStack itemStack) {
        return !isAdmiringDisabled(piglinPrisoner) && !isAdmiringItem(piglinPrisoner) && piglinPrisoner.isAdult() && (itemStack.isPiglinCurrency() || isLovedItem(itemStack.getItem()));
    }

    public static void wasHurtBy(PiglinPrisonerEntity piglinPrisoner, LivingEntity entity) {
        if (entity instanceof AbstractPiglinEntity) {
            return;
        }

        if (isHoldingItemInOffHand(piglinPrisoner)) {
            stopHoldingOffHandItem(piglinPrisoner, false);
        }

        Brain<PiglinPrisonerEntity> brain = piglinPrisoner.getBrain();
        brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
        brain.eraseMemory(MemoryModuleType.DANCING);
        brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);

        if (entity instanceof PlayerEntity) {
            brain.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, true, 400L);
        }

        getAvoidTarget(piglinPrisoner).ifPresent((target) -> {
            if (target.getType() != entity.getType())
                brain.eraseMemory(MemoryModuleType.AVOID_TARGET);
        });

        maybeRetaliate(piglinPrisoner, entity);
    }

    protected static void maybeRetaliate(AbstractPiglinEntity abstractPiglin, LivingEntity targetEntity) {
        if (!abstractPiglin.getBrain().isActive(Activity.AVOID)
                && isAttackAllowed(targetEntity)
                && !BrainUtil.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(abstractPiglin, targetEntity, 4.0D)) {
            setAngerTarget(abstractPiglin, targetEntity);
            broadcastAngerTarget(abstractPiglin, targetEntity);
        }
    }

    public static Optional<SoundEvent> getSoundForCurrentActivity(PiglinPrisonerEntity piglinPrisoner) {
        return piglinPrisoner.getBrain().getActiveNonCoreActivity().map((activity) -> getSoundForActivity(piglinPrisoner, activity));
    }

    private static SoundEvent getSoundForActivity(PiglinPrisonerEntity piglinPrisoner, Activity activity) {
        if (activity == Activity.FIGHT) {
            return SoundEvents.PIGLIN_ANGRY;
        } else if (piglinPrisoner.isConverting()) {
            return SoundEvents.PIGLIN_RETREAT;
        } else if (activity == Activity.AVOID && isNearAvoidTarget(piglinPrisoner)) {
            return SoundEvents.PIGLIN_RETREAT;
        } else if (activity == Activity.ADMIRE_ITEM) {
            return SoundEvents.PIGLIN_ADMIRING_ITEM;
        } else if (activity == Activity.CELEBRATE) {
            return SoundEvents.PIGLIN_CELEBRATE;
        } else if (seesPlayerHoldingLovedItem(piglinPrisoner)) {
            return SoundEvents.PIGLIN_JEALOUS;
        } else {
            return isNearRepellent(piglinPrisoner) ? SoundEvents.PIGLIN_RETREAT : SoundEvents.PIGLIN_AMBIENT;
        }
    }

    private static boolean isNearAvoidTarget(PiglinPrisonerEntity piglinPrisoner) {
        Brain<PiglinPrisonerEntity> brain = piglinPrisoner.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET) && brain.getMemory(MemoryModuleType.AVOID_TARGET).get().closerThan(piglinPrisoner, 12.0D);
    }

    private static List<AbstractPiglinEntity> getAdultAbstractPiglins(AbstractPiglinEntity abstractPiglin) {
        return abstractPiglin.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    private static List<PiglinEntity> getAdultPiglins(AbstractPiglinEntity abstractPiglin) {
        return getAdultAbstractPiglins(abstractPiglin).stream().filter(PiglinEntity.class::isInstance).map(PiglinEntity.class::cast).collect(Collectors.toList());
    }

    private static void stopWalking(PiglinPrisonerEntity piglinPrisoner) {
        piglinPrisoner.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        piglinPrisoner.getNavigation().stop();
    }

    protected static void broadcastAngerTarget(AbstractPiglinEntity piglin, LivingEntity target) {
        getAdultAbstractPiglins(piglin).forEach((abstractPiglin) -> setAngerTargetIfCloserThanCurrent(abstractPiglin, target));
    }

    public static void broadcastBeingRescued(AbstractPiglinEntity abstractPiglin) {
        getAdultPiglins(abstractPiglin).forEach(PiglinPrisonerAi::startDancing);
    }

    public static void setAngerTarget(AbstractPiglinEntity abstractPiglin, LivingEntity target) {
        if (isAttackAllowed(target)) {
            abstractPiglin.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            abstractPiglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 600L);
        }
    }

    private static void setAngerTargetIfCloserThanCurrent(AbstractPiglinEntity abstractPiglin, LivingEntity currentTarget) {
        Optional<LivingEntity> optional = getAngerTarget(abstractPiglin);
        LivingEntity livingentity = BrainUtil.getNearestTarget(abstractPiglin, optional, currentTarget);
        if (!optional.isPresent() || optional.get() != livingentity) {
            setAngerTarget(abstractPiglin, livingentity);
        }
    }

    private static Optional<LivingEntity> getAngerTarget(AbstractPiglinEntity abstractPiglin) {
        return BrainUtil.getLivingEntityFromUUIDMemory(abstractPiglin, MemoryModuleType.ANGRY_AT);
    }

    public static Optional<LivingEntity> getAvoidTarget(PiglinPrisonerEntity piglinPrisoner) {
        return piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET) ? piglinPrisoner.getBrain().getMemory(MemoryModuleType.AVOID_TARGET) : Optional.empty();
    }

    private static boolean wantsToStopFleeing(PiglinPrisonerEntity piglinPrisoner) {
        Brain<PiglinPrisonerEntity> brain = piglinPrisoner.getBrain();
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

    private static Vector3d getRandomNearbyPos(PiglinPrisonerEntity piglinPrisoner) {
        Vector3d vector3d = RandomPositionGenerator.getLandPos(piglinPrisoner, 4, 2);
        return vector3d == null ? piglinPrisoner.position() : vector3d;
    }

    private static boolean hasCrossbow(LivingEntity entity) {
        return entity.isHolding(itemStack -> itemStack.getItem() instanceof CrossbowItem);
    }

    private static void admireGoldItem(LivingEntity livingEntity) {
        livingEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120L);
    }

    private static boolean isAdmiringItem(PiglinPrisonerEntity piglinPrisoner) {
        return piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }

    private static boolean isAttackAllowed(LivingEntity target) {
        return EntityPredicates.ATTACK_ALLOWED.test(target);
    }

    private static boolean isNearRepellent(PiglinPrisonerEntity piglinPrisoner) {
        return piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
    }

    private static boolean doesntSeeAnyPlayerHoldingLovedItem(LivingEntity livingEntity) {
        return !seesPlayerHoldingLovedItem(livingEntity);
    }

    private static boolean seesPlayerHoldingLovedItem(LivingEntity livingEntity) {
        return livingEntity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
    }

    private static boolean isAdmiringDisabled(PiglinPrisonerEntity piglinPrisoner) {
        return piglinPrisoner.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
    }

    private static boolean isHoldingItemInOffHand(PiglinPrisonerEntity piglinPrisoner) {
        return !piglinPrisoner.getOffhandItem().isEmpty();
    }

    private static boolean isNotHoldingLovedItemInOffHand(PiglinPrisonerEntity piglinPrisoner) {
        return piglinPrisoner.getOffhandItem().isEmpty() || !isLovedItem(piglinPrisoner.getOffhandItem().getItem());
    }

    public static boolean isZombified(EntityType<?> entityType) {
        return entityType == EntityType.ZOMBIFIED_PIGLIN || entityType == EntityType.ZOGLIN;
    }

    private static void giveGoldBuff(PiglinPrisonerEntity piglinPrisoner) {
        piglinPrisoner.addEffect(new EffectInstance(Effects.ABSORPTION, 60 * 180, 3, false, true));
        piglinPrisoner.addEffect(new EffectInstance(Effects.REGENERATION, 60 * 120, 1, false, false));
    }

    private static void newTemptingPlayer(PiglinPrisonerEntity piglinPrisoner, PlayerEntity player) {
        piglinPrisoner.getBrain().setMemory(ModMemoryModuleTypes.TEMPTING_PLAYER.get(), player);
        piglinPrisoner.getBrain().setMemory(ModMemoryModuleTypes.IS_TEMPTED.get(), false);
        piglinPrisoner.setTempterUUID(player.getUUID());
    }

    protected static void pledgeAllegiance(PiglinPrisonerEntity piglinPrisoner) {
        if (piglinPrisoner.getBrain().hasMemoryValue(ModMemoryModuleTypes.TEMPTING_PLAYER.get()))
            piglinPrisoner.getBrain().setMemory(ModMemoryModuleTypes.IS_TEMPTED.get(), true);
    }

    public static void reloadAllegiance(PiglinPrisonerEntity piglinPrisoner, PlayerEntity player) {
        piglinPrisoner.getBrain().setMemory(ModMemoryModuleTypes.TEMPTING_PLAYER.get(), player);
        piglinPrisoner.getBrain().setMemory(ModMemoryModuleTypes.IS_TEMPTED.get(), true);
    }

}
