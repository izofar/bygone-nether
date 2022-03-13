package com.izofar.bygonenether.entity;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.entity.ai.PiglinBruteAi;
import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import com.izofar.bygonenether.init.ModSensorTypes;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinAction;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class ModPiglinBruteEntity extends AbstractPiglinEntity {
	
	protected static final ImmutableList<SensorType<? extends Sensor<? super ModPiglinBruteEntity>>> SENSOR_TYPES = ImmutableList.of(
			SensorType.NEAREST_LIVING_ENTITIES,
			SensorType.NEAREST_PLAYERS,
			SensorType.NEAREST_ITEMS,
			SensorType.HURT_BY,
			ModSensorTypes.PIGLIN_BRUTE_SPECIFIC_SENSOR.get()
		);
	protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
			MemoryModuleType.LOOK_TARGET, 
			MemoryModuleType.DOORS_TO_CLOSE, 
			MemoryModuleType.LIVING_ENTITIES,
			MemoryModuleType.VISIBLE_LIVING_ENTITIES,
			MemoryModuleType.NEAREST_VISIBLE_PLAYER,
			MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
			MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
			MemoryModuleType.NEARBY_ADULT_PIGLINS, 
			MemoryModuleType.HURT_BY, 
			MemoryModuleType.HURT_BY_ENTITY,
			MemoryModuleType.WALK_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, 
			MemoryModuleType.ATTACK_TARGET,
			MemoryModuleType.ATTACK_COOLING_DOWN, 
			MemoryModuleType.INTERACTION_TARGET, 
			MemoryModuleType.PATH,
			MemoryModuleType.ANGRY_AT, 
			MemoryModuleType.NEAREST_VISIBLE_NEMESIS, 
			MemoryModuleType.HOME,
			ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD.get()
		);

	public ModPiglinBruteEntity(EntityType<? extends ModPiglinBruteEntity> entityType, World world) {
		super(entityType, world);
		this.xpReward = 20;
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MonsterEntity.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 50.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35F)
				.add(Attributes.ATTACK_DAMAGE, 7.0D); 
		}

	@Nullable
	public ILivingEntityData finalizeSpawn(IServerWorld serverlevel, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData spawndata, @Nullable CompoundNBT tags) {
		PiglinBruteAi.initMemories(this);
		this.populateDefaultEquipmentSlots(difficultyInstance);
		return super.finalizeSpawn(serverlevel, difficultyInstance, spawnReason, spawndata, tags);
	}

	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) { this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_AXE)); }

	protected Brain.BrainCodec<ModPiglinBruteEntity> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }

	protected Brain<?> makeBrain(Dynamic<?> dynamic) { return PiglinBruteAi.makeBrain(this, this.brainProvider().makeBrain(dynamic)); }

	@SuppressWarnings("unchecked")
	public Brain<ModPiglinBruteEntity> getBrain() { return (Brain<ModPiglinBruteEntity>) super.getBrain(); }

	public boolean canHunt() { return false; }

	public boolean wantsToPickUp(ItemStack itemstack) { return itemstack.getItem() == Items.GOLDEN_AXE && super.wantsToPickUp(itemstack); }

	protected void customServerAiStep() {
		this.level.getProfiler().push("piglinBruteBrain");
		this.getBrain().tick((ServerWorld) this.level, this);
		this.level.getProfiler().pop();
		PiglinBruteAi.updateActivity(this);
		PiglinBruteAi.maybePlayActivitySound(this);
		super.customServerAiStep();
	}

	public PiglinAction getArmPose() { return this.isAggressive() && this.isHoldingMeleeWeapon() ? PiglinAction.ATTACKING_WITH_MELEE_WEAPON : PiglinAction.DEFAULT; }

	public boolean hurt(DamageSource source, float amount) {
		boolean flag = super.hurt(source, amount);
		if (this.level.isClientSide) return false;
		else {
			if (flag && source.getEntity() instanceof LivingEntity) PiglinBruteAi.wasHurtBy(this, (LivingEntity) source.getEntity());
			return flag;
		}
	}

	protected SoundEvent getAmbientSound() { return SoundEvents.PIGLIN_BRUTE_AMBIENT; }

	protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.PIGLIN_BRUTE_HURT; }

	protected SoundEvent getDeathSound() { return SoundEvents.PIGLIN_BRUTE_DEATH; }

	protected void playStepSound(BlockPos blockpos, BlockState blockstate) { this.playSound(SoundEvents.PIGLIN_BRUTE_STEP, 0.15F, 1.0F); }

	public void playAngrySound() { this.playSound(SoundEvents.PIGLIN_BRUTE_ANGRY, 1.0F, this.getVoicePitch()); }

	protected void playConvertedSound() { this.playSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED, 1.0F, this.getVoicePitch()); }
}
