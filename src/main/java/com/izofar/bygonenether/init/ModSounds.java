package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static SoundEvent WITHER_WALTZ = sound("wither_waltz");

    public static SoundEvent WEX_CHARGE = sound("entity.wex.charge");
    public static SoundEvent WEX_DEATH = sound("entity.wex.death");
    public static SoundEvent WEX_HURT = sound("entity.wex.hurt");
    public static SoundEvent WEX_AMBIENT = sound("entity.wex.ambient");

    public static SoundEvent WARPED_ENDERMAN_DEATH = sound("entity.warped_enderman.death");
    public static SoundEvent WARPED_ENDERMAN_HURT = sound("entity.warped_enderman.hurt");
    public static SoundEvent WARPED_ENDERMAN_AMBIENT = sound("entity.warped_enderman.ambient");
    public static SoundEvent WARPED_ENDERMAN_TELEPORT = sound("entity.warped_enderman.teleport");
    public static SoundEvent WARPED_ENDERMAN_SCREAM = sound("entity.warped_enderman.scream");
    public static SoundEvent WARPED_ENDERMAN_STARE = sound("entity.warped_enderman.stare");

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, id("wither_waltz"), WITHER_WALTZ);
        Registry.register(Registry.SOUND_EVENT, id("entity.wex.charge"), WEX_CHARGE);
        Registry.register(Registry.SOUND_EVENT, id("entity.wex.death"), WEX_DEATH);
        Registry.register(Registry.SOUND_EVENT, id("entity.wex.hurt"), WEX_HURT);
        Registry.register(Registry.SOUND_EVENT, id("entity.wex.ambient"), WEX_AMBIENT);
        Registry.register(Registry.SOUND_EVENT, id("entity.warped_enderman.death"), WARPED_ENDERMAN_DEATH);
        Registry.register(Registry.SOUND_EVENT, id("entity.warped_enderman.hurt"), WARPED_ENDERMAN_HURT);
        Registry.register(Registry.SOUND_EVENT, id("entity.warped_enderman.ambient"), WARPED_ENDERMAN_AMBIENT);
        Registry.register(Registry.SOUND_EVENT, id("entity.warped_enderman.teleport"), WARPED_ENDERMAN_TELEPORT);
        Registry.register(Registry.SOUND_EVENT, id("entity.warped_enderman.scream"), WARPED_ENDERMAN_SCREAM);
        Registry.register(Registry.SOUND_EVENT, id("entity.warped_enderman.stare"), WARPED_ENDERMAN_STARE);
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(BygoneNetherMod.MODID, id);
    }

    private static SoundEvent sound(String sound) {
        return new SoundEvent(id(sound));
    }
}
