package com.izofar.bygonenether.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class ModShieldItem extends ShieldItem {

    public ModShieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return false;
    }
}