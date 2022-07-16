package com.izofar.bygonenether.init;

import com.izofar.bygonenether.entity.ai.sensing.PiglinBruteSpecificSensor;
import com.izofar.bygonenether.entity.ai.sensing.PiglinPrisonerSpecificSensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ModSensorTypes {

	public static final DeferredRegister<SensorType<?>> MODDED_SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, "bygonenether");
	
	public static final RegistryObject<SensorType<PiglinBruteSpecificSensor>> PIGLIN_BRUTE_SPECIFIC_SENSOR = MODDED_SENSOR_TYPES.register("piglin_brute_specific_sensor", () -> new SensorType<>(PiglinBruteSpecificSensor::new));
	public static final RegistryObject<SensorType<PiglinPrisonerSpecificSensor>> PIGLIN_PRISONER_SPECIFIC_SENSOR = MODDED_SENSOR_TYPES.register("piglin_prisoner_specific_sensor", () -> new SensorType<>(PiglinPrisonerSpecificSensor::new));

	public static void register(IEventBus eventBus){ MODDED_SENSOR_TYPES.register(eventBus); }
	
}
