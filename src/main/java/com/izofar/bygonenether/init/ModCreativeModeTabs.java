package com.izofar.bygonenether.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public abstract class ModCreativeModeTabs {

    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("bygonenethertab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.WITHERED_DEBRIS.get());
        }
    };

}
