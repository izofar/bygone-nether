package com.izofar.bygonenether;

import com.izofar.bygonenether.event.ModBlockEvents;
import com.izofar.bygonenether.init.*;
import com.izofar.bygonenether.util.ModStructureUtils;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BygoneNetherMod implements ModInitializer {
	public static final String MODID = "bygonenether";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Override
	public void onInitialize() {
		ModItems.registerItems();
		ModBlocks.registerBlocks();
		ModEntityTypes.registerEntityTypes();
		ModStructures.registerStructures();
		ModSensorTypes.registerSensorTypes();
		ModMemoryModuleTypes.registerModMemoryModuleTypes();
		ModFeatures.registerFeatures();
		ModSounds.registerSounds();

		ModStructureUtils.addBasaltRestrictions();
		ModEntityTypes.modifyPiglinMemoryAndSensors();
		ModTags.registerTags();
		ModBlockEvents.enforceNetheriteToBreakWitheredStone();
		ModBlockEvents.onIronBarsBroken();
	}
}
