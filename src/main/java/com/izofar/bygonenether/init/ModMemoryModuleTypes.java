package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public abstract class ModMemoryModuleTypes {

public static final DeferredRegister<MemoryModuleType<?>> MODDED_MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, BygoneNetherMod.MODID);
	
	public static final RegistryObject<MemoryModuleType<PlayerEntity>> NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD = MODDED_MEMORY_MODULE_TYPES.register("nearest_targetable_player_not_wearing_gild", () -> new MemoryModuleType<>(Optional.empty()));
	public static final RegistryObject<MemoryModuleType<PlayerEntity>> TEMPTING_PLAYER = MODDED_MEMORY_MODULE_TYPES.register("tempting_player", () -> new MemoryModuleType<>(Optional.empty()));
	public static final RegistryObject<MemoryModuleType<Boolean>> IS_TEMPTED = MODDED_MEMORY_MODULE_TYPES.register("is_tempted", () -> new MemoryModuleType<>(Optional.empty()));
	
	public static void register(IEventBus eventBus) { MODDED_MEMORY_MODULE_TYPES.register(eventBus); }
	
}
