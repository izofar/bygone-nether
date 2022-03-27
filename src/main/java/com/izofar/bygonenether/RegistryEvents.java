package com.izofar.bygonenether;

import com.izofar.bygonenether.event.ModBlockEvents;
import com.izofar.bygonenether.event.ModEntityEvents;
import com.izofar.bygonenether.event.ModWorldEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class RegistryEvents {

	static {
		MinecraftForge.EVENT_BUS.register(ModBlockEvents.class);
		MinecraftForge.EVENT_BUS.register(ModEntityEvents.class);
		MinecraftForge.EVENT_BUS.register(ModWorldEvents.class);
	}
}
