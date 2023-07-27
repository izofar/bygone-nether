package com.izofar.bygonenether.entity;


import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.init.ModSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Random;

public class WarpedEndermanEntity extends EndermanEntity {

	private static final int SHEAR_COOLDOWN = 20;
	private static final WarpedEnderManVariant[] VARIANTS = WarpedEnderManVariant.values();
	private static final DataParameter<Integer> VARIANT_ID = EntityDataManager.defineId(WarpedEndermanEntity.class, DataSerializers.INT);

	private WarpedEnderManVariant variant;
	private int shearCooldownCounter = 0;
	private boolean toConvertToEnderman = false;

	private static final Map<SoundEvent, SoundEvent> SOUND_MAP = ImmutableMap.<SoundEvent, SoundEvent>builder()
		.put(SoundEvents.ENDERMAN_AMBIENT, ModSounds.WARPED_ENDERMAN_AMBIENT.get())
		.put(SoundEvents.ENDERMAN_DEATH, ModSounds.WARPED_ENDERMAN_DEATH.get())
		.put(SoundEvents.ENDERMAN_HURT, ModSounds.WARPED_ENDERMAN_HURT.get())
		.put(SoundEvents.ENDERMAN_SCREAM, ModSounds.WARPED_ENDERMAN_SCREAM.get())
		.put(SoundEvents.ENDERMAN_STARE, ModSounds.WARPED_ENDERMAN_STARE.get())
		.put(SoundEvents.ENDERMAN_TELEPORT, ModSounds.WARPED_ENDERMAN_TELEPORT.get())
		.build();

	public WarpedEndermanEntity(EntityType<? extends EndermanEntity> entityType, World world) {
		super(entityType, world);
		this.setVariant(randomVariant(world.getRandom()));
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EndermiteEntity.class, true, false));
		this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
	}
	
	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MonsterEntity.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 55.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.3D)
			.add(Attributes.ATTACK_DAMAGE, 8.5D)
			.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level.isClientSide) {
			if (shearCooldownCounter > 0) {
				shearCooldownCounter--;
			}
			else if (shearCooldownCounter < 0) {
				shearCooldownCounter = 0;
			}
			if (this.toConvertToEnderman) {
				EndermanEntity enderman = this.convertTo(EntityType.ENDERMAN, false);
				this.playShearSound(enderman);
			}
		}
	}

	@Override
	public void playSound(SoundEvent event, float f1, float f2) {
		super.playSound(SOUND_MAP.getOrDefault(event, event), f1, f2);
	}

	private void playShearSound(EndermanEntity enderman) {
		this.level.playSound(null, enderman, SoundEvents.SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT_ID, 2);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", this.variant.ordinal());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT tag) {
		super.readAdditionalSaveData(tag);
		this.setVariant(VARIANTS[tag.getInt("Variant")]);
	}

	public WarpedEnderManVariant getVariant() {
		WarpedEnderManVariant ret = VARIANTS[this.entityData.get(VARIANT_ID)];
		this.variant = ret;
		return ret;
	}

	public void setVariant(WarpedEnderManVariant variant) {
		this.variant = variant;
		this.entityData.set(VARIANT_ID, variant.ordinal());
	}

	@Override
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() == Items.SHEARS) {
			if (this.isReadyForShearing() && !this.level.isClientSide) {
				boolean flag = this.toConvertToEnderman;
				this.shearWarp();
				stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(hand));
				if (this.toConvertToEnderman && !flag && player instanceof ServerPlayerEntity)
					CriteriaTriggers.SUMMONED_ENTITY.trigger((ServerPlayerEntity) player, this);
				return ActionResultType.SUCCESS;
			} else {
				return ActionResultType.CONSUME;
			}
		}
		return super.mobInteract(player, hand);
	}

	private boolean isReadyForShearing() {
		return this.shearCooldownCounter == 0;
	}

	private void shearWarp() {
		ItemStack itemstack = new ItemStack(Items.TWISTING_VINES, this.getRandom().nextInt(2) + 1);
		ItemEntity itementity = this.spawnAtLocation(itemstack);
		itementity.setDeltaMovement(itementity.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F));
		this.shearCooldownCounter = SHEAR_COOLDOWN;
		switch (this.variant) {
			case FRESH:
				this.toConvertToEnderman = true;
				break;
			case SHORT_VINE:
				this.setVariant(WarpedEnderManVariant.FRESH);
				this.playShearSound(this);
				break;
			case LONG_VINE:
				this.setVariant(WarpedEnderManVariant.SHORT_VINE);
				this.playShearSound(this);
				break;
		}
	}

	private static WarpedEnderManVariant randomVariant(Random random) {
		return VARIANTS[random.nextInt(VARIANTS.length)];
	}

	public enum WarpedEnderManVariant {
		FRESH, SHORT_VINE, LONG_VINE
	}

}
