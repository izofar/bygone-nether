package com.izofar.bygonenether.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class WexEntity extends VexEntity {

	public WexEntity(EntityType<? extends VexEntity> entityType, World world) { super(entityType, world); }

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) { }

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MonsterEntity.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 14.0D)
			.add(Attributes.ATTACK_DAMAGE, 4.0D);
	}

}
