package com.izofar.bygonenether.entity;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import com.izofar.bygonenether.init.ModItems;
import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import com.izofar.bygonenether.init.ModSensorTypes;
import com.izofar.bygonenether.util.ModLists;
import com.mojang.serialization.Dynamic;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class PiglinPrisonerEntity extends AbstractPiglinEntity implements ICrossbowUser {

	private static final DataParameter<Boolean> DATA_IS_CHARGING_CROSSBOW = EntityDataManager.defineId(PiglinPrisonerEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> DATA_IS_DANCING = EntityDataManager.defineId(PiglinPrisonerEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Optional<UUID>> DATA_OWNERUUID_ID  = EntityDataManager.defineId(PiglinPrisonerEntity.class, DataSerializers.OPTIONAL_UUID);

	protected static final int RESCUE_TIME = 75;
	protected int timeBeingRescued;
	protected boolean isBeingRescued;

	protected boolean hasTempter;

	private final Inventory inventory = new Inventory(8);

	protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinPrisonerEntity>>> SENSOR_TYPES = ImmutableList.of(
			SensorType.NEAREST_LIVING_ENTITIES,
			SensorType.NEAREST_PLAYERS,
			SensorType.NEAREST_ITEMS,
			SensorType.HURT_BY,
			ModSensorTypes.PIGLIN_PRISONER_SPECIFIC_SENSOR.get()
		);

	protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
			MemoryModuleType.LOOK_TARGET,
			MemoryModuleType.DOORS_TO_CLOSE,
			MemoryModuleType.LIVING_ENTITIES,
			MemoryModuleType.VISIBLE_LIVING_ENTITIES,
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
			MemoryModuleType.NEAREST_REPELLENT,
			ModMemoryModuleTypes.TEMPTING_PLAYER.get(),
			ModMemoryModuleTypes.IS_TEMPTED.get()
		);

	public PiglinPrisonerEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
		super(entityType, world);
		this.xpReward = 5;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level.isClientSide && !this.hasTempter && this.getTempter() != null) {
			this.hasTempter = true;
			this.spawnHeartParticles();
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Inventory", this.inventory.createTag());
		tag.putInt("TimeBeingRescued", this.timeBeingRescued);
		tag.putBoolean("IsBeingRescued", this.isBeingRescued);
		if (this.getTempterUUID() != null) {
			tag.putUUID("Tempter", this.getTempterUUID());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT tag) {
		super.readAdditionalSaveData(tag);
		this.inventory.fromTag(tag.getList("Inventory", 10));
		this.timeBeingRescued = tag.getInt("TimeBeingRescued");
		this.isBeingRescued = tag.getBoolean("IsBeingRescued");
		if (tag.hasUUID("Tempter")) {
			UUID uuid = tag.getUUID("Tempter");
			this.setTempterUUID(uuid);
			this.hasTempter = true;
			PiglinPrisonerAi.reloadAllegiance(this, this.getTempter());
		}
	}

	@Override
	protected void customServerAiStep() {
		this.level.getProfiler().push("piglinBrain");
		this.getBrain().tick((ServerWorld)this.level, this);
		this.level.getProfiler().pop();
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
	protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
		super.dropCustomDeathLoot(source, looting, recentlyHit);
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

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MonsterEntity.createMonsterAttributes()
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
	protected Brain.BrainCodec<PiglinPrisonerEntity> brainProvider() {
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}

	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic) {
		return PiglinPrisonerAi.makeBrain(this, this.brainProvider().makeBrain(dynamic));
	}

	@Override
	public Brain<PiglinPrisonerEntity> getBrain() {
		return (Brain<PiglinPrisonerEntity>) super.getBrain();
	}

	@Override
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ActionResultType actionresulttype = super.mobInteract(player, hand);
		if (actionresulttype.consumesAction()) {
			return actionresulttype;
		} else if (!this.level.isClientSide) {
			return PiglinPrisonerAi.mobInteract(this, player, hand);
		} else {
			boolean flag = PiglinPrisonerAi.canAdmire(this, player.getItemInHand(hand)) && this.getArmPose() != PiglinAction.ADMIRING_ITEM;
			return flag ? ActionResultType.SUCCESS : ActionResultType.PASS;
		}
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntitySize entitySize) {
		return 1.74F;
	}

	@Override
	public double getPassengersRidingOffset() {
		return (double)this.getBbHeight() * 0.92D;
	}

	@Override
	public boolean isBaby() {
		return false;
	}

	@Override
	protected boolean canHunt() {
		return false;
	}

	@Override
	protected int getExperienceReward(PlayerEntity player) {
		return this.xpReward;
	}

	@Override
	protected void finishConversion(ServerWorld serverWorld) {
		PiglinPrisonerAi.cancelAdmiring(this);
		this.inventory.removeAllItems().forEach(this::spawnAtLocation);
		super.finishConversion(serverWorld);
	}

	private boolean isChargingCrossbow() {
		return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
	}

	@Override
	public void setChargingCrossbow(boolean isCharging) {
		this.entityData.set(DATA_IS_CHARGING_CROSSBOW, isCharging);
	}

	@Override
	public void onCrossbowAttackPerformed() {
		this.noActionTime = 0;
	}

	@Override
	public PiglinAction getArmPose() {
		if (this.isDancing()) {
			return PiglinAction.DANCING;
		} else if (PiglinPrisonerAi.isLovedItem(this.getOffhandItem().getItem())) {
			return PiglinAction.ADMIRING_ITEM;
		} else if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
			return PiglinAction.ATTACKING_WITH_MELEE_WEAPON;
		} else if (this.isChargingCrossbow()) {
			return PiglinAction.CROSSBOW_CHARGE;
		} else {
			return this.isAggressive() && this.isHolding(item -> item instanceof CrossbowItem) ? PiglinAction.CROSSBOW_HOLD : PiglinAction.DEFAULT;
		}
	}

	public boolean isDancing() {
		return this.entityData.get(DATA_IS_DANCING);
	}

	public void setDancing(boolean isDancing) {
		this.entityData.set(DATA_IS_DANCING, isDancing);
	}

	@Override
	public boolean hurt(DamageSource damageSource, float amount) {
		boolean flag = super.hurt(damageSource, amount);
		if (this.level.isClientSide) {
			return false;
		} else {
			if (flag && damageSource.getEntity() instanceof LivingEntity) {
				PiglinPrisonerAi.wasHurtBy(this, (LivingEntity)damageSource.getEntity());
			}
			return flag;
		}
	}

	@Override
	public void performRangedAttack(LivingEntity target, float velocity) {
		this.performCrossbowAttack(this, 1.6F);
	}

	@Override
	public void shootCrossbowProjectile(LivingEntity taget, ItemStack crossbowStack, ProjectileEntity projectileEntity, float projectileAngle) {
		this.shootCrossbowProjectile(this, taget, projectileEntity, projectileAngle, 1.6F);
	}

	@Override
	public boolean canFireProjectileWeapon(ShootableItem projectileWeapon) {
		return projectileWeapon == Items.CROSSBOW;
	}

	public void holdInMainHand(ItemStack stack) {
		this.setItemSlotAndDropWhenKilled(EquipmentSlotType.MAINHAND, stack);
	}

	public void holdInOffHand(ItemStack stack) {
		if (stack.isPiglinCurrency()) {
			this.setItemSlot(EquipmentSlotType.OFFHAND, stack);
			this.setGuaranteedDrop(EquipmentSlotType.OFFHAND);
		} else {
			this.setItemSlotAndDropWhenKilled(EquipmentSlotType.OFFHAND, stack);
		}
	}

	@Override
	public boolean wantsToPickUp(ItemStack stack) {
		return net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) && this.canPickUpLoot() && PiglinPrisonerAi.wantsToPickup(this, stack);
	}

	public boolean canReplaceCurrentItem(ItemStack candidateStack) {
		EquipmentSlotType equipmentslottype = MobEntity.getEquipmentSlotForItem(candidateStack);
		ItemStack itemstack = this.getItemBySlot(equipmentslottype);
		return this.canReplaceCurrentItem(candidateStack, itemstack);
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack candidateStack, ItemStack existingStack) {
		if (EnchantmentHelper.hasBindingCurse(existingStack)) {
			return false;
		} else {
			boolean flag = PiglinPrisonerAi.isLovedItem(candidateStack.getItem()) || candidateStack.getItem() == Items.CROSSBOW;
			boolean flag1 = PiglinPrisonerAi.isLovedItem(existingStack.getItem()) || existingStack.getItem() == Items.CROSSBOW;
			if (flag && !flag1) {
				return true;
			} else if (!flag && flag1) {
				return false;
			} else {
				return (!this.isAdult() || candidateStack.getItem() == Items.CROSSBOW || existingStack.getItem() != Items.CROSSBOW) && super.canReplaceCurrentItem(candidateStack, existingStack);
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
		return this.level.isClientSide ? null : PiglinPrisonerAi.getSoundForCurrentActivity(this).orElse(null);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.PIGLIN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.PIGLIN_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos blockPos, BlockState blockState) {
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
	public PlayerEntity getTempter() {
		try {
			UUID uuid = this.getTempterUUID();
			return uuid == null ? null : this.level.getPlayerByUUID(uuid);
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

	@OnlyIn(Dist.CLIENT)
	public void spawnHeartParticles() {
		for (int i = 0; i < 5; ++i) {
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			double d2 = this.random.nextGaussian() * 0.02D;
			this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
		}
	}

	public void rescue() {
		PiglinPrisonerAi.startDancing(this);
		PiglinPrisonerAi.broadcastBeingRescued(this);
		CriteriaTriggers.SUMMONED_ENTITY.trigger((ServerPlayerEntity) this.getTempter(), this);
		this.isBeingRescued = true;
	}

	protected void finishRescue() {
		PiglinPrisonerAi.throwItems(this, Collections.singletonList(new ItemStack(ModItems.GILDED_NETHERITE_SHIELD.get())));
		AbstractPiglinEntity piglin = this.convertTo(ModLists.PIGLIN_PRISONER_CONVERSIONS.getRandom(this.random).get(), true);
		if (piglin != null) {
			piglin.addEffect(new EffectInstance(Effects.CONFUSION, 200, 0));
			ForgeEventFactory.onLivingConvert(this, piglin);
		}

	}

}
