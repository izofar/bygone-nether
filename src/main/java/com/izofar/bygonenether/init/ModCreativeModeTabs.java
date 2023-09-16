package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {

    public static final CreativeModeTab MOD_TAB = FabricItemGroupBuilder.build(new ResourceLocation(BygoneNetherMod.MODID, "bygonenethertab"),
            () -> new ItemStack(ModItems.WITHERED_DEBRIS));

}
