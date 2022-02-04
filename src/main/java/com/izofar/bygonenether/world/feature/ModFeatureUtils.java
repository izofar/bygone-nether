package com.izofar.bygonenether.world.feature;

import com.izofar.bygonenether.init.ModBlocks;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;

public class ModFeatureUtils {

	public static void replaceBlackstoneBlobs() {
		NetherFeatures.BLACKSTONE_BLOBS.config.replaceState = ModBlocks.COBBLED_BLACKSTONE.get().defaultBlockState();
	}
	
	public static void replaceBlackstoneInBastion() {
		ProcessorLists.REMOVE_GILDED_BLACKSTONE.inputPredicate = new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0F);
		ProcessorLists.REMOVE_GILDED_BLACKSTONE.outputState = ModBlocks.COBBLED_BLACKSTONE.get().defaultBlockState();
	}
	
}
