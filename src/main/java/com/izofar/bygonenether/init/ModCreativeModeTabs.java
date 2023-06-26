package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public abstract class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> MODDED_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BygoneNetherMod.MODID);

    public static final RegistryObject<CreativeModeTab> MOD_TAB = MODDED_TABS.register("bygonenether",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.bygonenether"))
                    .icon(ModItems.WITHERED_DEBRIS.get()::getDefaultInstance)
                    .displayItems((displayParams, output) -> ModItems.MODDED_ITEMS.getEntries().forEach(itemLike -> output.accept(itemLike.get())))
                    .build()
    );

    public static void register(IEventBus eventBus) {
        MODDED_TABS.register(eventBus);
    }

}
