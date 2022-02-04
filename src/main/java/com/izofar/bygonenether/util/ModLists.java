package com.izofar.bygonenether.util;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.init.ModBlocks;
import net.minecraft.world.level.block.Block;

import java.util.List;

public abstract class ModLists {

	public static final List<Block> WITHERED_BLOCKS = ImmutableList.of(ModBlocks.WITHERED_BLACKSTONE.get(), ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(), ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(), ModBlocks.WITHERED_DEBRIS.get());

	
}
