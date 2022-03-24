package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BygoneNetherMod.MODID);

    public static RegistryObject<SoundEvent> WITHER_WALTZ = SOUND_EVENTS.register("wither_waltz", () -> new SoundEvent(new ResourceLocation(BygoneNetherMod.MODID, "wither_waltz")));

    public static void register(IEventBus eventBus) { SOUND_EVENTS.register(eventBus); }
}
