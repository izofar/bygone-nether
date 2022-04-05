package com.izofar.bygonenether.item;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public enum ModArmorMaterial implements ArmorMaterial {
	
	GILDED_NETHERITE(BygoneNetherMod.MODID + ":gilded_netherite", 8/*37 /*8*/, new int[]{3, 6, 8, 3}, 20, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> Ingredient.of(Items.NETHERITE_INGOT));

	private static final int[] HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final int durabilityMultiplier;
	private final int[] slotProtections;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	ModArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.slotProtections = slotProtections;
		this.enchantmentValue = enchantmentValue;
		this.sound = sound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
	}

	public int getDurabilityForSlot(EquipmentSlot slot) { return HEALTH_PER_SLOT[slot.getIndex()] * this.durabilityMultiplier; }

	public int getDefenseForSlot(EquipmentSlot slot) { return this.slotProtections[slot.getIndex()]; }

	public int getEnchantmentValue() { return this.enchantmentValue; }

	public SoundEvent getEquipSound() { return this.sound; }

	public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }

	@OnlyIn(Dist.CLIENT)
	public String getName() { return this.name; }

	public float getToughness() { return this.toughness; }

	public float getKnockbackResistance() { return this.knockbackResistance; }
}
