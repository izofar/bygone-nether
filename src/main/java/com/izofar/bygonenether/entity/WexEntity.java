package com.izofar.bygonenether.entity;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.init.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import java.util.Map;

public class WexEntity extends VexEntity {

	private static final Map<SoundEvent, SoundEvent> SOUND_MAP = ImmutableMap.of(
			SoundEvents.VEX_AMBIENT, ModSounds.WEX_AMBIENT.get(),
			SoundEvents.VEX_DEATH, ModSounds.WEX_DEATH.get(),
			SoundEvents.VEX_HURT, ModSounds.WEX_HURT.get(),
			SoundEvents.VEX_CHARGE, ModSounds.WEX_CHARGE.get()
		);

	public WexEntity(EntityType<? extends VexEntity> entityType, World world) { super(entityType, world); }

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) { }

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MonsterEntity.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 14.0D)
			.add(Attributes.ATTACK_DAMAGE, 4.0D);
	}

	@Override
	public void playSound(SoundEvent event, float f1, float f2){ super.playSound(SOUND_MAP.getOrDefault(event, event), f1, f2); }

}
