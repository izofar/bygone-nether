package com.izofar.bygonenether.entity;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import com.izofar.bygonenether.init.ModSensorTypes;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class PiglinPrisoner extends AbstractPiglin implements CrossbowAttackMob, InventoryCarrier {

	private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);

	protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinPrisoner>>> SENSOR_TYPES =
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
				MemoryModuleType.NEAREST_LIVING_ENTITIES,
				MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, 
				MemoryModuleType.NEAREST_VISIBLE_PLAYER,
				MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, 
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

	private static final Predicate<Item> WANTS_TO_PICK_UP = (item) -> (item instanceof TieredItem tiereditem && tiereditem.getTier() == Tiers.GOLD) || (item instanceof ArmorItem armoritem && armoritem.getMaterial() == ArmorMaterials.GOLD);
	
	private final SimpleContainer inventory = new SimpleContainer(8); 

	public PiglinPrisoner(EntityType<? extends AbstractPiglin> entitytype, Level world) {
		super(entitytype, world);
		this.xpReward = 5;
	}

	public static AttributeSupplier.Builder createAttributes() { 
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 24.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35F)
				.add(Attributes.ATTACK_DAMAGE, 6.0D); 
		}
	
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelaccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
		this.populateDefaultEquipmentSlots(difficulty);
		return super.finalizeSpawn(levelaccessor, difficulty, spawnType, spawnData, tag);
	}

	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic) { return PiglinPrisonerAi.makeBrain(this, this.brainProvider().makeBrain(dynamic)); }

	@SuppressWarnings("unchecked")
	@Override
	public Brain<PiglinPrisoner> getBrain() { return  (Brain<PiglinPrisoner>) super.getBrain(); }

	@Override
	protected Brain.Provider<PiglinPrisoner> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }

	public boolean wantsToPickUp(ItemStack itemstack) { return WANTS_TO_PICK_UP.test(itemstack.getItem()); }

	protected void customServerAiStep() {
		this.level.getProfiler().push("piglinPrisonerBrain");
		this.getBrain().tick((ServerLevel) this.level, this);
		this.level.getProfiler().pop();
		PiglinPrisonerAi.updateActivity(this);
		super.customServerAiStep();
	}
	
	@Override
	protected boolean shouldDespawnInPeaceful() { return false; }

	@Override
	public void setChargingCrossbow(boolean bool) { this.entityData.set(DATA_IS_CHARGING_CROSSBOW, bool); }

	public boolean isChargingCrossbow() { return this.entityData.get(DATA_IS_CHARGING_CROSSBOW); }

	@Override
	public void shootCrossbowProjectile(LivingEntity piglin, ItemStack itemstack, Projectile projectile, float vel) { this.shootCrossbowProjectile(this, piglin, projectile, vel, 1.6F); }

	@Override
	public void onCrossbowAttackPerformed() { this.noActionTime = 0; }

	@Override
	public void performRangedAttack(LivingEntity entity, float vel) { this.performCrossbowAttack(this, 1.6F); }

	@Override
	public Container getInventory() { return this.inventory; }

	@Override
	protected boolean canHunt() { return false; }
	
	@Override
	public PiglinArmPose getArmPose() { return this.isAggressive() && this.isHoldingMeleeWeapon() ? PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON : PiglinArmPose.DEFAULT; }

	@Override
	protected void playConvertedSound() { this.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED); }

	public void playSound(SoundEvent sound) { this.playSound(sound, this.getSoundVolume(), this.getVoicePitch()); }

}
