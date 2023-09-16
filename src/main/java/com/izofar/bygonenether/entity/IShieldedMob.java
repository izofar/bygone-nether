package com.izofar.bygonenether.entity;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

public interface IShieldedMob {
    boolean isShieldDisabled();
    void startUsingShield();
    void stopUsingShield();

    static boolean canDisableShield(ItemStack stack) {
        return stack.getItem() instanceof AxeItem;
    }
}
