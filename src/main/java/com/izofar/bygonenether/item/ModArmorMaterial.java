package com.izofar.bygonenether.item;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumMap;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public enum ModArmorMaterial implements ArmorMaterial {
	
	GILDED_NETHERITE(BygoneNetherMod.MODID + ":gilded_netherite", 8, Util.make(new EnumMap<>(ArmorItem.Type.class), (armorMap) -> {
		armorMap.put(ArmorItem.Type.BOOTS, 3);
		armorMap.put(ArmorItem.Type.LEGGINGS, 6);
		armorMap.put(ArmorItem.Type.CHESTPLATE, 8);
		armorMap.put(ArmorItem.Type.HELMET, 3);
	}), 20, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> Ingredient.of(Items.NETHERITE_INGOT));

	private final String name;
	private final int durabilityMultiplier;
	private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (armorMap) -> {
		armorMap.put(ArmorItem.Type.BOOTS, 13);
		armorMap.put(ArmorItem.Type.LEGGINGS, 15);
		armorMap.put(ArmorItem.Type.CHESTPLATE, 16);
		armorMap.put(ArmorItem.Type.HELMET, 11);
	});

	ModArmorMaterial(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionFunctionForType, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionFunctionForType = protectionFunctionForType;
		this.enchantmentValue = enchantmentValue;
		this.sound = sound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
	}

	public int getDurabilityForType(ArmorItem.Type armorType) {
		return HEALTH_FUNCTION_FOR_TYPE.get(armorType) * this.durabilityMultiplier;
	}

	public int getDefenseForType(ArmorItem.Type armorType) {
		return this.protectionFunctionForType.get(armorType);
	}

	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	public SoundEvent getEquipSound() {
		return this.sound;
	}

	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@OnlyIn(Dist.CLIENT)
	public String getName() {
		return this.name;
	}

	public float getToughness() {
		return this.toughness;
	}

	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}
