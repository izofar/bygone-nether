package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BygoneNetherMod.MODID);

    public static final RegistryObject<SoundEvent> WITHER_WALTZ = register("wither_waltz");

    public static final RegistryObject<SoundEvent> WEX_CHARGE = register("entity.wex.charge");
    public static final RegistryObject<SoundEvent> WEX_DEATH = register("entity.wex.death");
    public static final RegistryObject<SoundEvent> WEX_HURT = register("entity.wex.hurt");
    public static final RegistryObject<SoundEvent> WEX_AMBIENT = register("entity.wex.ambient");

    public static final RegistryObject<SoundEvent> WARPED_ENDERMAN_DEATH = register("entity.warped_enderman.death");
    public static final RegistryObject<SoundEvent> WARPED_ENDERMAN_HURT = register("entity.warped_enderman.hurt");
    public static final RegistryObject<SoundEvent> WARPED_ENDERMAN_AMBIENT = register("entity.warped_enderman.ambient");
    public static final RegistryObject<SoundEvent> WARPED_ENDERMAN_TELEPORT = register("entity.warped_enderman.teleport");
    public static final RegistryObject<SoundEvent> WARPED_ENDERMAN_SCREAM = register("entity.warped_enderman.scream");
    public static final RegistryObject<SoundEvent> WARPED_ENDERMAN_STARE = register("entity.warped_enderman.stare");

    private static RegistryObject<SoundEvent> register(String name){
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(BygoneNetherMod.MODID, name)));
    }

    public static void register(IEventBus eventBus) { SOUND_EVENTS.register(eventBus); }
}
