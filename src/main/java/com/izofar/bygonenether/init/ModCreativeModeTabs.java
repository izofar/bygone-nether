package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = BygoneNetherMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {

    public static CreativeModeTab MOD_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        MOD_TAB = event.registerCreativeModeTab(
                new ResourceLocation(BygoneNetherMod.MODID, "bygonenethertab"),
                builder -> builder.icon(() -> new ItemStack(ModItems.WITHERED_DEBRIS.get())).title(Component.translatable("creativemodetab.bygonenethertab"))
        );
    }

    @SubscribeEvent
    public static void addCreativeModeTab(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == MOD_TAB) {
            for(RegistryObject<Item> object : ModItems.MODDED_ITEMS.getEntries()) {
                event.accept(object.get());
            }
        }
    }

}
