package com.izofar.bygonenether.init;

import java.util.Optional;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ModMemoryModuleTypes {

public static final DeferredRegister<MemoryModuleType<?>> MODDED_MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, BygoneNetherMod.MODID);
	
	public static final RegistryObject<MemoryModuleType<Player>> NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD = MODDED_MEMORY_MODULE_TYPES.register("nearest_targetable_player_not_wearing_gild", () -> new MemoryModuleType<>(Optional.empty())); 
	
	public static void register(IEventBus eventBus){ MODDED_MEMORY_MODULE_TYPES.register(eventBus); }
	
}
