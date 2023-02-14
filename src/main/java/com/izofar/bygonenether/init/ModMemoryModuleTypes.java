package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class ModMemoryModuleTypes {
    public static final MemoryModuleType<Player> NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD = new MemoryModuleType<>(Optional.empty());

    public static void registerModMemoryModuleTypes() {
        Registry.register(Registry.MEMORY_MODULE_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "nearest_targetable_player_not_wearing_gild"), NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD);
    }
}
