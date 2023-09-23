package com.izofar.bygonenether.entity;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import com.izofar.bygonenether.init.ModItems;
import com.izofar.bygonenether.init.ModSensorTypes;
import com.izofar.bygonenether.util.ModLists;
import com.mojang.serialization.Dynamic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class PiglinPrisoner extends AbstractPiglin implements CrossbowAttackMob, InventoryCarrier {

    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(PiglinPrisoner.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(PiglinPrisoner.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(PiglinPrisoner.class, EntityDataSerializers.OPTIONAL_UUID);

    protected static final int RESCUE_TIME = 75;
    protected int timeBeingRescued;
    protected boolean isBeingRescued;

    protected boolean hasTempter;

    protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinPrisoner>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ITEMS,
            SensorType.HURT_BY,
            ModSensorTypes.PIGLIN_PRISONER_SPECIFIC_SENSOR
    );

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.DOORS_TO_CLOSE,
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
            MemoryModuleType.NEARBY_ADULT_PIGLINS,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.PATH,
            MemoryModuleType.ANGRY_AT,
            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.ADMIRING_ITEM,
            MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM,
            MemoryModuleType.ADMIRING_DISABLED,
            MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM,
            MemoryModuleType.CELEBRATE_LOCATION,
            MemoryModuleType.DANCING,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
            MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT,
            MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM,
            MemoryModuleType.ATE_RECENTLY,
            MemoryModuleType.NEAREST_REPELLENT,
            MemoryModuleType.TEMPTING_PLAYER,
            MemoryModuleType.IS_TEMPTED
    );

    private final SimpleContainer inventory = new SimpleContainer(8);

    public PiglinPrisoner(EntityType<? extends AbstractPiglin> entitytype, Level world) {
        super(entitytype, world);
        this.xpReward = 5;
        this.setPersistenceRequired();
    }

    @Override
    public void tick () {
        super.tick();
        if (this.level().isClientSide && !this.hasTempter && this.getTempter() != null) {
            this.hasTempter = true;
            this.spawnHeartParticles();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Inventory", this.inventory.createTag());
        tag.putInt("TimeBeingRescued", this.timeBeingRescued);
        tag.putBoolean("IsBeingRescued", this.isBeingRescued);
        if (this.getTempterUUID() != null) {
            tag.putUUID("Tempter", this.getTempterUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.inventory.fromTag(tag.getList("Inventory", 10));
        this.timeBeingRescued = tag.getInt("TimeBeingRescued");
        this.isBeingRescued = tag.getBoolean("IsBeingRescued");
        UUID uuid;
        if (tag.hasUUID("Tempter")) {
            uuid = tag.getUUID("Tempter");
            this.setTempterUUID(uuid);
            this.hasTempter = true;
            PiglinPrisonerAi.reloadAllegiance(this, this.getTempter());
        }
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("piglinBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        PiglinPrisonerAi.updateActivity(this);
        if (this.isBeingRescued) {
            this.timeBeingRescued ++;
        }
        else{
            this.timeBeingRescued = 0;
        }

        if (this.timeBeingRescued > RESCUE_TIME) {
            this.playConvertedSound();
            this.finishRescue();
        }
        super.customServerAiStep();
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int rand, boolean doDrop) {
        super.dropCustomDeathLoot(source, rand, doDrop);
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
    }

    public void addToInventory(ItemStack stack) {
        this.inventory.addItem(stack);
    }

    public boolean canAddToInventory(ItemStack stack) {
        return this.inventory.canAddItem(stack);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
        this.entityData.define(DATA_IS_DANCING, false);
        this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35F)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    @Override
    protected Brain.Provider<PiglinPrisoner> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return PiglinPrisonerAi.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public Brain<PiglinPrisoner> getBrain() {
        return  (Brain<PiglinPrisoner>) super.getBrain();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult interactionresult = super.mobInteract(player, hand);
        if (interactionresult.consumesAction()) {
            return interactionresult;
        } else if (!this.level().isClientSide) {
            return PiglinPrisonerAi.mobInteract(this, player, hand);
        } else {
            boolean flag = PiglinPrisonerAi.canAdmire(this, player.getItemInHand(hand)) && this.getArmPose() != PiglinArmPose.ADMIRING_ITEM;
            return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return this.isBaby() ? 0.93F : 1.74F;
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.92D;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public int getExperienceReward() {
        return this.xpReward;
    }

    @Override
    protected void finishConversion(ServerLevel serverLevel) {
        PiglinPrisonerAi.cancelAdmiring(this);
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
        super.finishConversion(serverLevel);
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
    }

    @Override
    public void setChargingCrossbow(boolean bool) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, bool);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float vel) {
        this.performCrossbowAttack(this, 1.6F);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity piglin, ItemStack itemstack, Projectile projectile, float vel) {
        this.shootCrossbowProjectile(this, piglin, projectile, vel, 1.6F);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem item) {
        return item == Items.CROSSBOW;
    }

    @VisibleForDebug
    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    protected boolean canHunt() {
        return false;
    }

    @Override
    public PiglinArmPose getArmPose() {
        if (this.isDancing()) {
            return PiglinArmPose.DANCING;
        } else if (PiglinPrisonerAi.isLovedItem(this.getOffhandItem())) {
            return PiglinArmPose.ADMIRING_ITEM;
        } else if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
            return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
        } else if (this.isChargingCrossbow()) {
            return PiglinArmPose.CROSSBOW_CHARGE;
        } else {
            return this.isAggressive() && this.isHolding(is -> is.getItem() instanceof CrossbowItem) ? PiglinArmPose.CROSSBOW_HOLD : PiglinArmPose.DEFAULT;
        }
    }

    public boolean isDancing() { return this.entityData.get(DATA_IS_DANCING); }

    public void setDancing(boolean isDancing) { this.entityData.set(DATA_IS_DANCING, isDancing); }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (flag && source.getEntity() instanceof LivingEntity) {
                PiglinPrisonerAi.wasHurtBy(this, (LivingEntity)source.getEntity());
            }
            return flag;
        }
    }

    public void holdInMainHand(ItemStack stack) {
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, stack);
    }

    public void holdInOffHand(ItemStack stack) {
        if (PiglinPrisonerAi.isPiglinCurrency(stack)) {
            this.setItemSlot(EquipmentSlot.OFFHAND, stack);
            this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        } else {
            this.setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, stack);
        }
    }

    @Override
    public boolean wantsToPickUp(ItemStack itemstack) {
        return level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.canPickUpLoot() && PiglinPrisonerAi.wantsToPickup(this, itemstack);
    }

    public boolean canReplaceCurrentItem(ItemStack replacementStack) {
        EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(replacementStack);
        ItemStack itemstack = this.getItemBySlot(equipmentslot);
        return this.canReplaceCurrentItem(replacementStack, itemstack);
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack newStack, ItemStack oldStack) {
        if (EnchantmentHelper.hasBindingCurse(oldStack)) {
            return false;
        } else {
            boolean flag = PiglinPrisonerAi.isLovedItem(newStack) || newStack.is(Items.CROSSBOW);
            boolean flag1 = PiglinPrisonerAi.isLovedItem(oldStack) || oldStack.is(Items.CROSSBOW);
            if (flag && !flag1) {
                return true;
            } else if (!flag && flag1) {
                return false;
            } else {
                return (!this.isAdult() || newStack.is(Items.CROSSBOW) || !oldStack.is(Items.CROSSBOW)) && super.canReplaceCurrentItem(newStack, oldStack);
            }
        }
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        this.onItemPickup(itemEntity);
        PiglinPrisonerAi.pickUpItem(this, itemEntity);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.level().isClientSide ? null : PiglinPrisonerAi.getSoundForCurrentActivity(this).orElse(null);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockpos, BlockState blockstate) {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }

    public void playSound(SoundEvent sound) {
        this.playSound(sound, this.getSoundVolume(), this.getVoicePitch());
    }

    @Override
    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }

    @Nullable
    public Player getTempter() {
        try {
            UUID uuid = this.getTempterUUID();
            return uuid == null ? null : this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getTempterUUID() {
        return this.entityData.get(DATA_OWNERUUID_ID).orElse(null);
    }

    public void setTempterUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(uuid));
    }

    @Environment(EnvType.CLIENT)
    public void spawnHeartParticles() {
        for (int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    public void rescue() {
        PiglinPrisonerAi.startDancing(this);
        PiglinPrisonerAi.broadcastBeingRescued(this);
        if (this.getTempter() instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, this);
        }
        this.isBeingRescued = true;
    }

    protected void finishRescue() {
        PiglinPrisonerAi.throwItems(this, Collections.singletonList(new ItemStack(ModItems.GILDED_NETHERITE_SHIELD)));
        AbstractPiglin piglin = this.convertTo(ModLists.PIGLIN_PRISONER_CONVERSIONS.getRandom(this.random).get().getData().get(), true);
        if (piglin != null) {
            piglin.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            if (this.getTempter() instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, this);
            }
        }

    }
}
