package com.izofar.bygonenether.item;

import com.izofar.bygonenether.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.ListNBT;

import java.util.function.Consumer;

public class ModArmorItem extends ArmorItem {

	public ModArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties properties) { super(material, slot, properties); }
	
	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		if (!entity.level.isClientSide && entity instanceof ServerPlayerEntity player && !player.abilities.instabuild) {
			if (stack.hurt(amount, player.getRandom(), player)) {
				onBroken.accept(entity);
				replaceArmor(stack, player);
			}
		}
		return 0;
	}
	
	private void replaceArmor(ItemStack stack, PlayerEntity player) {
		ListNBT list = stack.getEnchantmentTags();
		Item item;
		int slot;
		stack.shrink(1);
		if (this == ModItems.GILDED_NETHERITE_HELMET.get()) {
			item = Items.NETHERITE_HELMET;
			slot = 3;
		} else if (this == ModItems.GILDED_NETHERITE_CHESTPLATE.get()) {
			item = Items.NETHERITE_CHESTPLATE;
			slot = 2;
		} else if (this == ModItems.GILDED_NETHERITE_LEGGINGS.get()) {
			item = Items.NETHERITE_LEGGINGS;
			slot = 1;
		} else if (this == ModItems.GILDED_NETHERITE_BOOTS.get()) {
			item = Items.NETHERITE_BOOTS;
			slot = 0;
		}else return;
		
		ItemStack newStack = new ItemStack(item, 1);
		newStack.addTagElement("Enchantments", list);
		newStack.setDamageValue(stack.getTag().getInt("NetheriteDamage"));
		player.inventory.armor.set(slot, newStack);
	}

	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) { return stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == ModArmorMaterial.GILDED_NETHERITE; }

}
