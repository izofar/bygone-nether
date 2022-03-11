package com.izofar.bygonenether.entity;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;

public class WexEntity extends Vex {

	public WexEntity(EntityType<? extends Vex> entityType, Level level) { super(entityType, level); }

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) { }

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 14.0D)
			.add(Attributes.ATTACK_DAMAGE, 4.0D);
	}

}
