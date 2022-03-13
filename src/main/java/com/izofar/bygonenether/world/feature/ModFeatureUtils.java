package com.izofar.bygonenether.world.feature;

import com.izofar.bygonenether.init.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.BlobReplacementConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraft.world.gen.feature.template.RandomBlockMatchRuleTest;

public class ModFeatureUtils {

	public static void replaceBlackstoneBlobs() {
		((BlobReplacementConfig) Features.BLACKSTONE_BLOBS.config).replaceState = ModBlocks.COBBLED_BLACKSTONE.get().defaultBlockState();
	}
	
	public static void replaceBlackstoneInBastion() {
		ProcessorLists.REMOVE_GILDED_BLACKSTONE.inputPredicate = new RandomBlockMatchRuleTest(Blocks.BLACKSTONE, 1.0F);
		ProcessorLists.REMOVE_GILDED_BLACKSTONE.outputState = ModBlocks.COBBLED_BLACKSTONE.get().defaultBlockState();
	}
	
}
