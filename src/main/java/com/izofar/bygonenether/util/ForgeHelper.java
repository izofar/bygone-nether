package com.izofar.bygonenether.util;

import com.izofar.bygonenether.item.ModArmorItem;
import com.izofar.bygonenether.item.ModArmorMaterial;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

public class ForgeHelper {
    public static boolean isPiglinCurrency(ItemStack stack) {
        return stack.getItem() == PiglinAi.BARTERING_ITEM;
    }
    public static boolean makesPiglinsNeutral(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == ModArmorMaterial.GILDED_NETHERITE;
    }
    public static boolean canDisableShield(ItemStack stack) {
        return stack.getItem() instanceof AxeItem;
    }
}
