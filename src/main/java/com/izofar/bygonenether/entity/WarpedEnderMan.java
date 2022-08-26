package com.izofar.bygonenether.entity;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.init.ModSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class WarpedEnderMan extends EnderMan{

	private static final int SHEAR_COOLDOWN = 20;
	private static final WarpedEnderManVariant[] VARIANTS = WarpedEnderManVariant.values();
	private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(WarpedEnderMan.class, EntityDataSerializers.INT);

	private WarpedEnderManVariant variant;
	private int shearCooldownCounter = 0;
	private boolean toConvertToEnderman = false;

	private static final Map<SoundEvent, SoundEvent> SOUND_MAP = ImmutableMap.of(
			SoundEvents.ENDERMAN_AMBIENT, ModSounds.WARPED_ENDERMAN_AMBIENT.get(),
			SoundEvents.ENDERMAN_DEATH, ModSounds.WARPED_ENDERMAN_DEATH.get(),
			SoundEvents.ENDERMAN_HURT, ModSounds.WARPED_ENDERMAN_HURT.get(),
			SoundEvents.ENDERMAN_SCREAM, ModSounds.WARPED_ENDERMAN_SCREAM.get(),
			SoundEvents.ENDERMAN_STARE, ModSounds.WARPED_ENDERMAN_STARE.get(),
			SoundEvents.ENDERMAN_TELEPORT, ModSounds.WARPED_ENDERMAN_TELEPORT.get()
	);

	public WarpedEnderMan(EntityType<? extends EnderMan> entityType, Level world) {
		super(entityType, world);
		this.setVariant(randomVariant(this.getRandom()));
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Endermite.class, true, false));
		this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 55.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.ATTACK_DAMAGE, 8.5D)
				.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	@Override
	public void tick(){
		super.tick();
		if(!this.level.isClientSide) {
			if (shearCooldownCounter > 0) shearCooldownCounter--;
			else if (shearCooldownCounter < 0) shearCooldownCounter = 0;
			if(this.toConvertToEnderman){
				EnderMan enderman = this.convertTo(EntityType.ENDERMAN, false);
				this.playShearSound(enderman);
			}
		}
	}

	@Override
	public void playSound(SoundEvent event, float f1, float f2){ super.playSound(SOUND_MAP.getOrDefault(event, event), f1, f2); }

	private void playShearSound(EnderMan enderman){ this.level.playSound(null, enderman, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F); }

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT_ID, 2);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", this.variant.ordinal());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
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
	public InteractionResult mobInteract(Player player, InteractionHand hand){
		ItemStack stack = player.getItemInHand(hand);
		if(stack.is(Items.SHEARS)) {
			if(this.isReadyForShearing() && !this.level.isClientSide) {
				boolean flag = this.toConvertToEnderman;
				this.shearWarp();
				this.gameEvent(GameEvent.SHEAR, player);
				stack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(hand));
				if(this.toConvertToEnderman && !flag && player instanceof ServerPlayer serverPlayer)
					CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, this);
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		}
		return super.mobInteract(player, hand);
	}

	private boolean isReadyForShearing() { return this.shearCooldownCounter == 0; }

	private void shearWarp(){
		ItemStack itemstack = new ItemStack(Items.TWISTING_VINES, this.getRandom().nextInt(2) + 1);
		BehaviorUtils.throwItem(this, itemstack, Vec3.ZERO.add(0.0D, 1.0D, 0.0D));
		this.shearCooldownCounter = SHEAR_COOLDOWN;
		switch (this.variant) {
			case FRESH -> this.toConvertToEnderman = true;
			case SHORT_VINE -> {
				this.setVariant(WarpedEnderManVariant.FRESH);
				this.playShearSound(this);
			}
			case LONG_VINE -> {
				this.setVariant(WarpedEnderManVariant.SHORT_VINE);
				this.playShearSound(this);
			}
		}
	}

	private static WarpedEnderManVariant randomVariant(RandomSource random){ return VARIANTS[random.nextInt(VARIANTS.length)]; }

	public enum WarpedEnderManVariant {
		FRESH, SHORT_VINE, LONG_VINE
	}

}