package com.izofar.bygonenether.entity;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Random;

public class WarpedEnderMan extends EnderMan{

	private final WarpedEnderManVariant variant;

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
		this.variant = randomVarient(world.getRandom());
	}

	public WarpedEnderManVariant getVariant(){ return this.variant; }

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
	public void playSound(SoundEvent event, float f1, float f2){ super.playSound(SOUND_MAP.getOrDefault(event, event), f1, f2); }

	private static WarpedEnderManVariant randomVarient(Random random){
		final WarpedEnderManVariant[] values = WarpedEnderManVariant.values();
		return values[random.nextInt(values.length)];
	}

	public enum WarpedEnderManVariant {
		FRESH, SHORT_VINE, LONG_VINE
	}

}
