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

public class PiglinPrisonerAi {

    private static final RangedInteger AVOID_ZOMBIFIED_DURATION = TickRangeConverter.rangeOfSeconds(5, 7);
    private static final int CELEBRATION_TIME = 200;

    private static final Predicate<CreatureEntity> isDistracted = (mob) -> {
        if(mob instanceof PiglinPrisonerEntity) {
            PiglinPrisonerEntity piglin = (PiglinPrisonerEntity) mob;
            Brain<PiglinPrisonerEntity> brain = piglin.getBrain();
            return brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM)
                    || brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_NEMESIS)
                    || brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)
                    || brain.getMemory(MemoryModuleType.DANCING).orElse(false)
                    || !brain.getMemory(ModMemoryModuleTypes.IS_TEMPTED.get()).orElse(false);
        }
        else return false;
    };

    public static Brain<?> makeBrain(PiglinPrisonerEntity piglin, Brain<PiglinPrisonerEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initAdmireItemActivity(brain);
        initFightActivity(piglin, brain);
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

    private static void initFightActivity(PiglinPrisonerEntity piglin, Brain<PiglinPrisonerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
                ImmutableList.<Task<? super PiglinPrisonerEntity>>of(
                        new FindNewAttackTargetTask<>((target) -> !isNearestValidAttackTarget(piglin, target)),
                        new SupplementedTask<>(PiglinPrisonerAi::hasCrossbow, new AttackStrafingTask<>(5, 0.75F)),
                        new MoveToTargetTask(1.0F),
                        new AttackTargetTask(20),
                        new ShootTargetTask<>(),
                        new PredicateTask<>(PiglinPrisonerAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)
                ),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static void initCelebrateActivity(Brain<PiglinPrisonerEntity> pBrain) {
        pBrain.addActivityAndRemoveMemoryWhenStopped(Activity.CELEBRATE, 10,
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
                MemoryModuleType.CELEBRATE_LOCATION);
    }

    private static void initAdmireItemActivity(Brain<PiglinPrisonerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10,
                ImmutableList.<Task<? super PiglinPrisonerEntity>>of(
                        new PickupWantedItemTask<>(PiglinPrisonerAi::isNotHoldingLovedItemInOffHand, 1.0F, true, 9),
                        new ModForgetAdmiredItemTask<>(9),
                        new ModStopReachingItemTask<>(200, 200)
                ),
                MemoryModuleType.ADMIRING_ITEM);
    }

    private static void initRetreatActivity(Brain<PiglinPrisonerEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10,
                ImmutableList.of(
                        RunAwayTask.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true),
                        createIdleLookBehaviors(), createIdleMovementBehaviors(),
                        new PredicateTask<>(PiglinPrisonerAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)),
                MemoryModuleType.AVOID_TARGET);
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

    public static void updateActivity(PiglinPrisonerEntity piglin) {
        Brain<PiglinPrisonerEntity> brain = piglin.getBrain();
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
        if (activity != activity1) getSoundForCurrentActivity(piglin).ifPresent(piglin::playSound);
        piglin.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));

        if (!brain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION))
            brain.eraseMemory(MemoryModuleType.DANCING);

        piglin.setDancing(brain.hasMemoryValue(MemoryModuleType.DANCING));
    }

    public static void pickUpItem(PiglinPrisonerEntity pPiglin, ItemEntity pItemEntity) {
        stopWalking(pPiglin);
        ItemStack itemstack;
        if (pItemEntity.getItem().getItem() == Items.GOLD_NUGGET) {
            pPiglin.take(pItemEntity, pItemEntity.getItem().getCount());
            itemstack = pItemEntity.getItem();
            pItemEntity.remove();
        } else {
            pPiglin.take(pItemEntity, 1);
            itemstack = removeOneItemFromItemEntity(pItemEntity);
        }

        Item item = itemstack.getItem();
        if (isLovedItem(item)) {
            pPiglin.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(pPiglin, itemstack);
            admireGoldItem(pPiglin);
        } else if (!pPiglin.equipItemIfPossible(itemstack)) {
            putInInventory(pPiglin, itemstack);
        }
    }

    private static void holdInOffhand(PiglinPrisonerEntity pPiglin, ItemStack pStack) {
        if (isHoldingItemInOffHand(pPiglin))
            pPiglin.spawnAtLocation(pPiglin.getItemInHand(Hand.OFF_HAND));
        pPiglin.holdInOffHand(pStack);
    }

    private static ItemStack removeOneItemFromItemEntity(ItemEntity pItemEntity) {
        ItemStack itemstack = pItemEntity.getItem();
        ItemStack itemstack1 = itemstack.split(1);
        if (itemstack.isEmpty())
            pItemEntity.remove();
        else
            pItemEntity.setItem(itemstack);
        return itemstack1;
    }

    public static void stopHoldingOffHandItem(PiglinPrisonerEntity pPiglin, boolean pShouldBarter) {
        ItemStack itemstack = pPiglin.getItemInHand(Hand.OFF_HAND);
        pPiglin.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        if (pPiglin.isAdult()) {
            boolean flag = itemstack.isPiglinCurrency();
            if (pShouldBarter && flag) {
                putInInventory(pPiglin, itemstack);
            } else if (!flag) {
                boolean flag1 = pPiglin.equipItemIfPossible(itemstack);
                if (!flag1) {
                    throwItems(pPiglin, Collections.singletonList(itemstack));
                }
            }
        } else {
            boolean flag2 = pPiglin.equipItemIfPossible(itemstack);
            if (!flag2) {
                ItemStack itemstack1 = pPiglin.getMainHandItem();
                if (isLovedItem(itemstack1.getItem())) {
                    putInInventory(pPiglin, itemstack1);
                } else {
                    throwItems(pPiglin, Collections.singletonList(itemstack1));
                }
                pPiglin.holdInMainHand(itemstack);
            }
        }
    }

    public static void cancelAdmiring(PiglinPrisonerEntity pPiglin) {
        if (isAdmiringItem(pPiglin) && !pPiglin.getOffhandItem().isEmpty()) {
            pPiglin.spawnAtLocation(pPiglin.getOffhandItem());
            pPiglin.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    private static void putInInventory(PiglinPrisonerEntity pPiglin, ItemStack pStack) {
        pPiglin.addToInventory(pStack);
        giveGoldBuff(pPiglin);
        pledgeAllegiance(pPiglin);
    }

    private static void throwItems(PiglinPrisonerEntity pPilgin, List<ItemStack> pStacks) {
        Optional<PlayerEntity> optional = pPilgin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        if (optional.isPresent())
            throwItemsTowardPlayer(pPilgin, optional.get(), pStacks);
        else
            throwItemsTowardRandomPos(pPilgin, pStacks);
    }

    private static void throwItemsTowardRandomPos(PiglinPrisonerEntity pPiglin, List<ItemStack> pStacks) {
        throwItemsTowardPos(pPiglin, pStacks, getRandomNearbyPos(pPiglin));
    }

    private static void throwItemsTowardPlayer(PiglinPrisonerEntity pPiglin, PlayerEntity pPlayer, List<ItemStack> pStacks) {
        throwItemsTowardPos(pPiglin, pStacks, pPlayer.position());
    }

    private static void throwItemsTowardPos(PiglinPrisonerEntity pPiglin, List<ItemStack> pStacks, Vector3d pPos) {
        if (!pStacks.isEmpty()) {
            pPiglin.swing(Hand.OFF_HAND);
            for (ItemStack itemstack : pStacks)
                BrainUtil.throwItem(pPiglin, itemstack, pPos.add(0.0D, 1.0D, 0.0D));
        }
    }

    public static boolean wantsToPickup(PiglinPrisonerEntity pPiglin, ItemStack pStack) {
        Item item = pStack.getItem();
        if (item.is(ItemTags.PIGLIN_REPELLENTS))
            return false;
        else if (isAdmiringDisabled(pPiglin) && pPiglin.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
            return false;
        else if (pStack.isPiglinCurrency() || isLovedItem(item))
            return isNotHoldingLovedItemInOffHand(pPiglin);
        else {
            boolean flag = pPiglin.canAddToInventory(pStack);
            if (item == Items.GOLD_NUGGET)
                return flag;
            else if (!isLovedItem(item))
                return pPiglin.canReplaceCurrentItem(pStack);
            else
                return isNotHoldingLovedItemInOffHand(pPiglin) && flag;
        }
    }

    public static boolean isLovedItem(Item item) {
        return item.is(ItemTags.PIGLIN_LOVED);
    }

    private static boolean isNearestValidAttackTarget(PiglinPrisonerEntity piglin, LivingEntity target) {
        return findNearestValidAttackTarget(piglin).filter((potentialTarget) -> potentialTarget == target).isPresent();
    }

    private static boolean isNearZombified(PiglinPrisonerEntity piglin) {
        Brain<PiglinPrisonerEntity> brain = piglin.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED))
            return piglin.closerThan(brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get(), 6.0D);
        else
            return false;
    }

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

    public static void exciteNearbyPiglins(PlayerEntity player, boolean requireVisibility) {
        List<PiglinPrisonerEntity> list = player.level.getEntitiesOfClass(PiglinPrisonerEntity.class, player.getBoundingBox().inflate(16.0D));
        list.stream().filter(PiglinTasks::isIdle).filter((piglin) -> !requireVisibility || BrainUtil.canSee(piglin, player)).forEach(PiglinPrisonerAi::startDancing);
    }

    private static void startDancing(PiglinPrisonerEntity piglin){
        piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.DANCING, true, CELEBRATION_TIME);
        piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, piglin.blockPosition(), CELEBRATION_TIME);
    }

    public static ActionResultType mobInteract(PiglinPrisonerEntity pPiglin, PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (canAdmire(pPiglin, itemstack)) {
            ItemStack itemstack1 = itemstack.split(1);
            holdInOffhand(pPiglin, itemstack1);
            if(!pPlayer.equals(pPiglin.getTempter()))
                newTemptingPlayer(pPiglin, pPlayer);
            admireGoldItem(pPiglin);
            stopWalking(pPiglin);
            return ActionResultType.CONSUME;
        } else {
            return ActionResultType.PASS;
        }
    }

    public static boolean canAdmire(PiglinPrisonerEntity pPiglin, ItemStack pStack) {
        return !isAdmiringDisabled(pPiglin) && !isAdmiringItem(pPiglin) && pPiglin.isAdult() && (pStack.isPiglinCurrency() || isLovedItem(pStack.getItem()));
    }

    public static void wasHurtBy(PiglinPrisonerEntity pPiglin, LivingEntity pTarget) {
        if (pTarget instanceof AbstractPiglinEntity) return;

        if (isHoldingItemInOffHand(pPiglin)) {
            stopHoldingOffHandItem(pPiglin, false);
        }

        Brain<PiglinPrisonerEntity> brain = pPiglin.getBrain();
        brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
        brain.eraseMemory(MemoryModuleType.DANCING);
        brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
        if (pTarget instanceof PlayerEntity) {
            brain.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, true, 400L);
        }

        getAvoidTarget(pPiglin).ifPresent((target) -> {
            if (target.getType() != pTarget.getType())
                brain.eraseMemory(MemoryModuleType.AVOID_TARGET);
        });

        maybeRetaliate(pPiglin, pTarget);
    }

    protected static void maybeRetaliate(AbstractPiglinEntity pPiglin, LivingEntity pTarget) {
        if (!pPiglin.getBrain().isActive(Activity.AVOID)
                && isAttackAllowed(pTarget)
                && !BrainUtil.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(pPiglin, pTarget, 4.0D)) {
            setAngerTarget(pPiglin, pTarget);
            broadcastAngerTarget(pPiglin, pTarget);
        }
    }

    public static Optional<SoundEvent> getSoundForCurrentActivity(PiglinPrisonerEntity piglin) {
        return piglin.getBrain().getActiveNonCoreActivity().map((activity) -> getSoundForActivity(piglin, activity));
    }

    private static SoundEvent getSoundForActivity(PiglinPrisonerEntity pPiglin, Activity pActivity) {
        if (pActivity == Activity.FIGHT) return SoundEvents.PIGLIN_ANGRY;
        else if (pPiglin.isConverting()) return SoundEvents.PIGLIN_RETREAT;
        else if (pActivity == Activity.AVOID && isNearAvoidTarget(pPiglin)) return SoundEvents.PIGLIN_RETREAT;
        else if (pActivity == Activity.ADMIRE_ITEM) return SoundEvents.PIGLIN_ADMIRING_ITEM;
        else if (pActivity == Activity.CELEBRATE) return SoundEvents.PIGLIN_CELEBRATE;
        else if (seesPlayerHoldingLovedItem(pPiglin)) return SoundEvents.PIGLIN_JEALOUS;
        else return isNearRepellent(pPiglin) ? SoundEvents.PIGLIN_RETREAT : SoundEvents.PIGLIN_AMBIENT;
    }

    private static boolean isNearAvoidTarget(PiglinPrisonerEntity pPiglin) {
        Brain<PiglinPrisonerEntity> brain = pPiglin.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET) && brain.getMemory(MemoryModuleType.AVOID_TARGET).get().closerThan(pPiglin, 12.0D);
    }

    private static List<AbstractPiglinEntity> getAdultPiglins(AbstractPiglinEntity pPiglin) {
        return pPiglin.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    private static void stopWalking(PiglinPrisonerEntity pPiglin) {
        pPiglin.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        pPiglin.getNavigation().stop();
    }

    protected static void broadcastAngerTarget(AbstractPiglinEntity pPiglin, LivingEntity pTarget) {
        getAdultPiglins(pPiglin).forEach((abstractPiglin) -> setAngerTargetIfCloserThanCurrent(abstractPiglin, pTarget));
    }

    public static void setAngerTarget(AbstractPiglinEntity pPiglin, LivingEntity pTarget) {
        if (isAttackAllowed(pTarget)) {
            pPiglin.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            pPiglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, pTarget.getUUID(), 600L);
        }
    }

    private static void setAngerTargetIfCloserThanCurrent(AbstractPiglinEntity pPiglin, LivingEntity pCurrentTarget) {
        Optional<LivingEntity> optional = getAngerTarget(pPiglin);
        LivingEntity livingentity = BrainUtil.getNearestTarget(pPiglin, optional, pCurrentTarget);
        if (!optional.isPresent() || optional.get() != livingentity) {
            setAngerTarget(pPiglin, livingentity);
        }
    }

    private static Optional<LivingEntity> getAngerTarget(AbstractPiglinEntity pPiglin) {
        return BrainUtil.getLivingEntityFromUUIDMemory(pPiglin, MemoryModuleType.ANGRY_AT);
    }

    public static Optional<LivingEntity> getAvoidTarget(PiglinPrisonerEntity pPiglin) {
        return pPiglin.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET) ? pPiglin.getBrain().getMemory(MemoryModuleType.AVOID_TARGET) : Optional.empty();
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

    private static Vector3d getRandomNearbyPos(PiglinPrisonerEntity pPiglin) {
        Vector3d vector3d = RandomPositionGenerator.getLandPos(pPiglin, 4, 2);
        return vector3d == null ? pPiglin.position() : vector3d;
    }

    private static boolean hasCrossbow(LivingEntity entity) {
        return entity.isHolding(itemStack -> itemStack.getItem() instanceof CrossbowItem);
    }

    private static void admireGoldItem(LivingEntity pPiglin) {
        pPiglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120L);
    }

    private static boolean isAdmiringItem(PiglinPrisonerEntity pPiglin) {
        return pPiglin.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }

    private static boolean isAttackAllowed(LivingEntity pTarget) {
        return EntityPredicates.ATTACK_ALLOWED.test(pTarget);
    }

    private static boolean isNearRepellent(PiglinPrisonerEntity piglin) {
        return piglin.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
    }

    private static boolean doesntSeeAnyPlayerHoldingLovedItem(LivingEntity player) {
        return !seesPlayerHoldingLovedItem(player);
    }

    private static boolean seesPlayerHoldingLovedItem(LivingEntity pPiglin) {
        return pPiglin.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
    }

    private static boolean isAdmiringDisabled(PiglinPrisonerEntity pPiglin) {
        return pPiglin.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
    }

    private static boolean isHoldingItemInOffHand(PiglinPrisonerEntity pPiglin) {
        return !pPiglin.getOffhandItem().isEmpty();
    }

    private static boolean isNotHoldingLovedItemInOffHand(PiglinPrisonerEntity piglin) {
        return piglin.getOffhandItem().isEmpty() || !isLovedItem(piglin.getOffhandItem().getItem());
    }

    public static boolean isZombified(EntityType<?> pEntityType) {
        return pEntityType == EntityType.ZOMBIFIED_PIGLIN || pEntityType == EntityType.ZOGLIN;
    }

    private static void giveGoldBuff(PiglinPrisonerEntity piglin){
        piglin.addEffect(new EffectInstance(Effects.ABSORPTION, 60 * 180, 3, false, true));
        piglin.addEffect(new EffectInstance(Effects.REGENERATION, 60 * 120, 1, false, false));
    }

    private static void newTemptingPlayer(PiglinPrisonerEntity piglin, PlayerEntity player){
        piglin.getBrain().setMemory(ModMemoryModuleTypes.TEMPTING_PLAYER.get(), player);
        piglin.getBrain().setMemory(ModMemoryModuleTypes.IS_TEMPTED.get(), false);
        piglin.setTempterUUID(player.getUUID());
    }

    protected static void pledgeAllegiance(PiglinPrisonerEntity piglin){
        if(piglin.getBrain().hasMemoryValue(ModMemoryModuleTypes.TEMPTING_PLAYER.get()))
            piglin.getBrain().setMemory(ModMemoryModuleTypes.IS_TEMPTED.get(), true);
    }

    public static void reloadAllegiance(PiglinPrisonerEntity piglin, PlayerEntity player){
        piglin.getBrain().setMemory(ModMemoryModuleTypes.TEMPTING_PLAYER.get(), player);
        piglin.getBrain().setMemory(ModMemoryModuleTypes.IS_TEMPTED.get(), true);
    }

}
