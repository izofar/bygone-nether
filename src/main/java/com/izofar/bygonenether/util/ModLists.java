package com.izofar.bygonenether.util;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.init.ModBlocks;
import com.izofar.bygonenether.init.ModEntityTypes;
import com.izofar.bygonenether.init.ModStructures;
import com.izofar.bygonenether.util.random.WeightedEntry;
import com.izofar.bygonenether.util.random.WeightedRandomList;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.List;

public abstract class ModLists {

	public static final List<Block> WITHERED_BLOCKS = ImmutableList.of(ModBlocks.WITHERED_BLACKSTONE.get(), ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(), ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(), ModBlocks.WITHERED_DEBRIS.get());
	public static final List<Structure<?>> DELTALESS_STRUCTURES = ImmutableList.of(Structure.BASTION_REMNANT, ModStructures.CATACOMB.get(), ModStructures.NETHER_FORTRESS.get(), ModStructures.CITADEL.get());
    public static final WeightedRandomList<EntityType<? extends AbstractPiglinEntity>> PIGLIN_MANOR_MOBS = WeightedRandomList.create(
			WeightedEntry.of(ModEntityTypes.PIGLIN_HUNTER.get(), 1),
			WeightedEntry.of(EntityType.PIGLIN, 3)
		);
	public static final List<Structure<?>> SPAWNER_STRUCTURES = ImmutableList.of(ModStructures.NETHER_FORTRESS.get(), ModStructures.CATACOMB.get());
}
