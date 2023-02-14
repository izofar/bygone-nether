package com.izofar.bygonenether.util;

import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

public class ForgeHelper {
    public static boolean isPiglinCurrency(ItemStack stack) {
        return stack.getItem() == PiglinAi.BARTERING_ITEM;
    }

    public static boolean canDisableShield(ItemStack stack) {
        return stack.getItem() instanceof AxeItem;
    }
}
