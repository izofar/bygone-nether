package com.izofar.bygonenether.init;

import com.izofar.bygonenether.entity.ai.sensing.ModPiglinBruteSpecificSensor;
import com.izofar.bygonenether.entity.ai.sensing.PiglinPrisonerSensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ModSensorTypes {

	public static final DeferredRegister<SensorType<?>> VANILLA_SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, "minecraft");
	public static final DeferredRegister<SensorType<?>> MODDED_SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, "bygonenether");
	
	public static final RegistryObject<SensorType<ModPiglinBruteSpecificSensor>> PIGLIN_BRUTE_SPECIFIC_SENSOR = VANILLA_SENSOR_TYPES.register("piglin_brute_specific_sensor", () -> new SensorType<>(ModPiglinBruteSpecificSensor::new));
	public static final RegistryObject<SensorType<PiglinPrisonerSensor>> PIGLIN_PRISONER_SPECIFIC_SENSOR = MODDED_SENSOR_TYPES.register("piglin_prisoner_specific_sensor", () -> new SensorType<>(PiglinPrisonerSensor::new));
	
	public static void register(IEventBus eventBus){
		VANILLA_SENSOR_TYPES.register(eventBus);
		MODDED_SENSOR_TYPES.register(eventBus);
	}
	
}
