package com.izofar.bygonenether.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public abstract class ModItemGroups {

    public static final ItemGroup MOD_TAB = new ItemGroup("bygonenethertab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.WITHERED_DEBRIS.get());
        }
    };

}
