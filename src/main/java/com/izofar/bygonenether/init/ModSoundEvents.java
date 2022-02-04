package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ModSoundEvents {

	public static DeferredRegister<SoundEvent> MODDED_SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BygoneNetherMod.MODID);
	
	/*public static final RegistryObject<SoundEvent> CLAY_GOLEM_ATTACK = registerSoundEvent("attack");
	public static final RegistryObject<SoundEvent> CLAY_GOLEM_DAMAGE = registerSoundEvent("damage");
	public static final RegistryObject<SoundEvent> CLAY_GOLEM_DEATH = registerSoundEvent("death");
	public static final RegistryObject<SoundEvent> CLAY_GOLEM_HURT = registerSoundEvent("hurt");
	public static final RegistryObject<SoundEvent> CLAY_GOLEM_REPAIR = registerSoundEvent("repair");
	public static final RegistryObject<SoundEvent> CLAY_GOLEM_STEP = registerSoundEvent("step");*/
	
	private static RegistryObject<SoundEvent> registerSoundEvent(String name){ return MODDED_SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(BygoneNetherMod.MODID, name))); }
	
	public static void register(IEventBus eventBus) { MODDED_SOUND_EVENTS.register(eventBus); }
	
}
