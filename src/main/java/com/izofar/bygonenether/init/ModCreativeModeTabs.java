package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {

    public static final ResourceKey<CreativeModeTab> MOD_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(BygoneNetherMod.MODID, "bygonenethertab"));

    private static final CreativeModeTab.Builder MOD_TAB_BUILDER = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.WITHERED_DEBRIS))
            .title(Component.translatable("itemGroup.bygonenether.bygonenethertab"));

    public static void registerCreativeModeTabs() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MOD_TAB, MOD_TAB_BUILDER.build());
    }

}
