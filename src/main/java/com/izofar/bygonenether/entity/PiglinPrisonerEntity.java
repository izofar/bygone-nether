package com.izofar.bygonenether.entity;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import com.izofar.bygonenether.init.ModSensorTypes;
import com.mojang.serialization.Dynamic;
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
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class PiglinPrisonerEntity extends AbstractPiglinEntity implements ICrossbowUser {

	private static final DataParameter<Boolean> DATA_IS_CHARGING_CROSSBOW = EntityDataManager.defineId(PiglinPrisonerEntity.class, DataSerializers.BOOLEAN);
	private final Inventory inventory = new Inventory(8);
	private static final Predicate<Item> WANTS_TO_PICK_UP = (item) -> (item instanceof TieredItem tiereditem && tiereditem.getTier() == ItemTier.GOLD) || (item instanceof ArmorItem armoritem && armoritem.getMaterial() == ArmorMaterial.GOLD);

	protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinPrisonerEntity>>> SENSOR_TYPES =
			ImmutableList.of(
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
				MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
				MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
				MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, 
				MemoryModuleType.HURT_BY_ENTITY,
				MemoryModuleType.WALK_TARGET,
				MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, 
				MemoryModuleType.ATTACK_TARGET,
				MemoryModuleType.ATTACK_COOLING_DOWN, 
				MemoryModuleType.INTERACTION_TARGET, 
				MemoryModuleType.PATH,
				MemoryModuleType.ANGRY_AT, 
				MemoryModuleType.NEAREST_VISIBLE_NEMESIS
			);

	public PiglinPrisonerEntity(EntityType<? extends AbstractPiglinEntity> entitytype, World world) {
		super(entitytype, world);
		this.xpReward = 5;
	}

	public void addAdditionalSaveData(CompoundNBT tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Inventory", this.inventory.createTag());
	}

	public void readAdditionalSaveData(CompoundNBT tag) {
		super.readAdditionalSaveData(tag);
		this.inventory.fromTag(tag.getList("Inventory", 10));
	}

	protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
		super.dropCustomDeathLoot(source, looting, recentlyHit);
		this.inventory.removeAllItems().forEach(this::spawnAtLocation);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MonsterEntity.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 24.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35F)
				.add(Attributes.ATTACK_DAMAGE, 6.0D); 
		}

	@Nullable
	public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
		if (pReason != SpawnReason.STRUCTURE) {
			if (pLevel.getRandom().nextFloat() < 0.2F) {
				this.setBaby(true);
			} else if (this.isAdult()) {
				this.setItemSlot(EquipmentSlotType.MAINHAND, this.createSpawnWeapon());
			}
		}

		this.populateDefaultEquipmentSlots(pDifficulty);
		this.populateDefaultEquipmentEnchantments(pDifficulty);
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	protected boolean shouldDespawnInPeaceful() {
		return false;
	}

	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
		return !this.isPersistenceRequired();
	}

	protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
		if (this.isAdult()) {
			this.maybeWearArmor(EquipmentSlotType.HEAD, new ItemStack(Items.GOLDEN_HELMET));
			this.maybeWearArmor(EquipmentSlotType.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
			this.maybeWearArmor(EquipmentSlotType.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
			this.maybeWearArmor(EquipmentSlotType.FEET, new ItemStack(Items.GOLDEN_BOOTS));
		}
	}

	private void maybeWearArmor(EquipmentSlotType pSlot, ItemStack pStack) {
		if (this.level.random.nextFloat() < 0.1F) {
			this.setItemSlot(pSlot, pStack);
		}
	}

	protected Brain.BrainCodec<PiglinPrisonerEntity> brainProvider() {
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}

	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic) { return PiglinPrisonerAi.makeBrain(this, this.brainProvider().makeBrain(dynamic)); }

	public Brain<PiglinPrisonerEntity> getBrain() { return (Brain<PiglinPrisonerEntity>) super.getBrain(); }

	protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
		return 1.74F;
	}

	public double getPassengersRidingOffset() {
		return (double)this.getBbHeight() * 0.92D;
	}

	public boolean isBaby() {
		return false;
	}

	protected boolean canHunt() {
		return false;
	}

	protected void customServerAiStep() {
		this.level.getProfiler().push("piglinPrisonerBrain");
		this.getBrain().tick((ServerWorld) this.level, this);
		this.level.getProfiler().pop();
		PiglinPrisonerAi.updateActivity(this);
		super.customServerAiStep();
	}

	protected int getExperienceReward(PlayerEntity pPlayer) {
		return this.xpReward;
	}

	protected void finishConversion(ServerWorld pServerLevel) {
		this.inventory.removeAllItems().forEach(this::spawnAtLocation);
		super.finishConversion(pServerLevel);
	}

	private ItemStack createSpawnWeapon() {
		return (double)this.random.nextFloat() < 0.5D ? new ItemStack(Items.CROSSBOW) : new ItemStack(Items.GOLDEN_SWORD);
	}

	private boolean isChargingCrossbow() {
		return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
	}

	public void setChargingCrossbow(boolean pIsCharging) {
		this.entityData.set(DATA_IS_CHARGING_CROSSBOW, pIsCharging);
	}

	public void onCrossbowAttackPerformed() {
		this.noActionTime = 0;
	}

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
			return this.isAggressive() && this.isHolding(item -> item instanceof net.minecraft.item.CrossbowItem) ? PiglinAction.CROSSBOW_HOLD : PiglinAction.DEFAULT;
		}
	}

	public boolean isDancing() {
		return false;
	}

	public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
		this.performCrossbowAttack(this, 1.6F);
	}

	public void shootCrossbowProjectile(LivingEntity pTarget, ItemStack pCrossbowStack, ProjectileEntity pProjectile, float pProjectileAngle) {
		this.shootCrossbowProjectile(this, pTarget, pProjectile, pProjectileAngle, 1.6F);
	}

	public boolean canFireProjectileWeapon(ShootableItem pProjectileWeapon) {
		return pProjectileWeapon == Items.CROSSBOW;
	}

	public boolean wantsToPickUp(ItemStack pStack) {
		return net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) && this.canPickUpLoot() && WANTS_TO_PICK_UP.test(pStack.getItem());
	}

	protected boolean canReplaceCurrentItem(ItemStack pCandidate) {
		EquipmentSlotType equipmentslottype = MobEntity.getEquipmentSlotForItem(pCandidate);
		ItemStack itemstack = this.getItemBySlot(equipmentslottype);
		return this.canReplaceCurrentItem(pCandidate, itemstack);
	}

	protected boolean canReplaceCurrentItem(ItemStack pCandidate, ItemStack pExisting) {
		if (EnchantmentHelper.hasBindingCurse(pExisting)) {
			return false;
		} else {
			boolean flag = PiglinPrisonerAi.isLovedItem(pCandidate.getItem()) || pCandidate.getItem() == Items.CROSSBOW;
			boolean flag1 = PiglinPrisonerAi.isLovedItem(pExisting.getItem()) || pExisting.getItem() == Items.CROSSBOW;
			if (flag && !flag1) {
				return true;
			} else if (!flag && flag1) {
				return false;
			} else {
				return this.isAdult() && pCandidate.getItem() != Items.CROSSBOW && pExisting.getItem() == Items.CROSSBOW ? false : super.canReplaceCurrentItem(pCandidate, pExisting);
			}
		}
	}

	protected void pickUpItem(ItemEntity pItemEntity) {
		this.onItemPickup(pItemEntity);
	}

	public boolean startRiding(Entity pEntity, boolean pForce) {
		if (this.isBaby() && pEntity.getType() == EntityType.HOGLIN) {
			pEntity = this.getTopPassenger(pEntity, 3);
		}

		return super.startRiding(pEntity, pForce);
	}

	private Entity getTopPassenger(Entity pVehicle, int pMaxPosition) {
		List<Entity> list = pVehicle.getPassengers();
		return pMaxPosition != 1 && !list.isEmpty() ? this.getTopPassenger(list.get(0), pMaxPosition - 1) : pVehicle;
	}

	protected SoundEvent getAmbientSound() {
		return this.level.isClientSide ? null : PiglinPrisonerAi.getSoundForCurrentActivity(this).orElse((SoundEvent)null);
	}

	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return SoundEvents.PIGLIN_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.PIGLIN_DEATH;
	}

	protected void playStepSound(BlockPos pPos, BlockState pBlock) {
		this.playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
	}

	public void playSound(SoundEvent pSound) {
		this.playSound(pSound, this.getSoundVolume(), this.getVoicePitch());
	}

	protected void playConvertedSound() {
		this.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
	}

}
