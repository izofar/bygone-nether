package com.izofar.bygonenether.entity;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class WarpedEnderManEntity extends EndermanEntity {

	public WarpedEnderManEntity(EntityType<? extends EndermanEntity> entityType, World world) { super(entityType, world); }

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
}
