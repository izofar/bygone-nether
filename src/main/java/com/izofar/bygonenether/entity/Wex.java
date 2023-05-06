package com.izofar.bygonenether.entity;

import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;

import java.util.Map;

public class Wex extends Vex {

	private static final Map<SoundEvent, SoundEvent> SOUND_MAP = ImmutableMap.of(
			SoundEvents.VEX_AMBIENT, ModSounds.WEX_AMBIENT.get(),
			SoundEvents.VEX_DEATH, ModSounds.WEX_DEATH.get(),
			SoundEvents.VEX_HURT, ModSounds.WEX_HURT.get(),
			SoundEvents.VEX_CHARGE, ModSounds.WEX_CHARGE.get()
	);

	public Wex(EntityType<? extends Vex> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficultyInstance) { }

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 14.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D);
	}

	@Override
	public void playSound(SoundEvent event, float volume, float pitch){
		super.playSound(SOUND_MAP.getOrDefault(event, event), volume, pitch);
	}

}