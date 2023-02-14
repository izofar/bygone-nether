package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.entity.ai.sensing.PiglinBruteSpecificSensor;
import com.izofar.bygonenether.entity.ai.sensing.PiglinPrisonerSpecificSensor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.sensing.SensorType;

public class ModSensorTypes {
    public static final SensorType<PiglinBruteSpecificSensor> PIGLIN_BRUTE_SPECIFIC_SENSOR = new SensorType<>(PiglinBruteSpecificSensor::new);
    public static final SensorType<PiglinPrisonerSpecificSensor> PIGLIN_PRISONER_SPECIFIC_SENSOR = new SensorType<>(PiglinPrisonerSpecificSensor::new);

    public static void registerSensorTypes() {
        Registry.register(Registry.SENSOR_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "piglin_brute_specific_sensor"), PIGLIN_BRUTE_SPECIFIC_SENSOR);
        Registry.register(Registry.SENSOR_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "piglin_prisoner_specific_sensor"), PIGLIN_PRISONER_SPECIFIC_SENSOR);
    }
}
