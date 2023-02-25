package com.izofar.bygonenether.item;

import com.izofar.bygonenether.init.ModItems;
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

import java.util.function.Consumer;

public class ModArmorItem extends ArmorItem {
    public ModArmorItem(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, FabricItemSettings properties) {
        super(armorMaterial, equipmentSlot, properties.customDamage(new ModArmorItemDamageHandler()));
    }

    private static class ModArmorItemDamageHandler implements CustomDamageHandler {

        @Override
        public int damage(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> onBroken) {
            if (!entity.level.isClientSide && entity instanceof ServerPlayer player && !player.getAbilities().instabuild) {
                if (stack.hurt(amount, player.getRandom(), player)) {
                    onBroken.accept(entity);
                    replaceArmor(stack, player);
                }
            }
            return 0;
        }

        private static void replaceArmor(ItemStack stack, Player player) {
            ListTag list = stack.getEnchantmentTags();
            Item brokenItem = stack.getItem();
            Item item;
            int slot;
            stack.shrink(1);
            if (brokenItem == ModItems.GILDED_NETHERITE_HELMET) {
                item = Items.NETHERITE_HELMET;
                slot = 3;
            } else if (brokenItem == ModItems.GILDED_NETHERITE_CHESTPLATE) {
                item = Items.NETHERITE_CHESTPLATE;
                slot = 2;
            } else if (brokenItem == ModItems.GILDED_NETHERITE_LEGGINGS) {
                item = Items.NETHERITE_LEGGINGS;
                slot = 1;
            } else if (brokenItem == ModItems.GILDED_NETHERITE_BOOTS) {
                item = Items.NETHERITE_BOOTS;
                slot = 0;
            }else return;

            ItemStack newStack = new ItemStack(item, 1);
            newStack.addTagElement("Enchantments", list);
            newStack.setDamageValue(stack.getTag().getInt("NetheriteDamage"));
            player.getInventory().armor.set(slot, newStack);
        }

    }
}
