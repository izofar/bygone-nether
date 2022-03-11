package com.izofar.bygonenether.entity;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.entity.ai.PiglinBruteAi;
import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import com.izofar.bygonenether.init.ModSensorTypes;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ModPiglinBrute extends AbstractPiglin {
	
	protected static final ImmutableList<SensorType<? extends Sensor<? super ModPiglinBrute>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, ModSensorTypes.PIGLIN_BRUTE_SPECIFIC_SENSOR.get());
	protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
			MemoryModuleType.LOOK_TARGET, 
			MemoryModuleType.DOORS_TO_CLOSE, 
			MemoryModuleType.NEAREST_LIVING_ENTITIES,
			MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, 
			MemoryModuleType.NEAREST_VISIBLE_PLAYER,
			MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, 
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

	public ModPiglinBrute(EntityType<? extends ModPiglinBrute> entityType, Level world) {
		super(entityType, world);
		this.xpReward = 20;
	}

	public static AttributeSupplier.Builder createAttributes() { 
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 50.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35F)
				.add(Attributes.ATTACK_DAMAGE, 7.0D); 
		}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverlevel, DifficultyInstance difficultylevel, MobSpawnType spawntype, @Nullable SpawnGroupData spawndata, @Nullable CompoundTag tags) {
		PiglinBruteAi.initMemories(this);
		this.populateDefaultEquipmentSlots(difficultylevel);
		return super.finalizeSpawn(serverlevel, difficultylevel, spawntype, spawndata, tags);
	}

	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) { this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE)); }

	protected Brain.Provider<ModPiglinBrute> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }

	protected Brain<?> makeBrain(Dynamic<?> dynamic) { return PiglinBruteAi.makeBrain(this, this.brainProvider().makeBrain(dynamic)); }

	@SuppressWarnings("unchecked")
	public Brain<ModPiglinBrute> getBrain() { return (Brain<ModPiglinBrute>) super.getBrain(); }

	public boolean canHunt() { return false; }

	public boolean wantsToPickUp(ItemStack itemstack) { return itemstack.is(Items.GOLDEN_AXE) && super.wantsToPickUp(itemstack); }

	protected void customServerAiStep() {
		this.level.getProfiler().push("piglinBruteBrain");
		this.getBrain().tick((ServerLevel) this.level, this);
		this.level.getProfiler().pop();
		PiglinBruteAi.updateActivity(this);
		PiglinBruteAi.maybePlayActivitySound(this);
		super.customServerAiStep();
	}

	public PiglinArmPose getArmPose() { return this.isAggressive() && this.isHoldingMeleeWeapon() ? PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON : PiglinArmPose.DEFAULT; }

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
