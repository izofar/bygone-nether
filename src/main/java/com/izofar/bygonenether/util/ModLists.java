package com.izofar.bygonenether.util;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.init.ModBlocks;
import com.izofar.bygonenether.init.ModEntityTypes;
import com.izofar.bygonenether.util.random.ModWeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

public abstract class ModLists {

	public static final List<Block> WITHERED_BLOCKS = ImmutableList.of(
			ModBlocks.WITHERED_BLACKSTONE.get(),
			ModBlocks.WITHERED_BLACKSTONE_STAIRS.get(),
			ModBlocks.WITHERED_BLACKSTONE_SLAB.get(),
			ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(),
			ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(),
			ModBlocks.CRACKED_WITHERED_BLACKSTONE_STAIRS.get(),
			ModBlocks.CRACKED_WITHERED_BLACKSTONE_SLAB.get(),
			ModBlocks.WITHERED_BASALT.get(),
			ModBlocks.WITHERED_COAL_BLOCK.get(),
			ModBlocks.WITHERED_QUARTZ_BLOCK.get(),
			ModBlocks.WITHERED_DEBRIS.get()
		);

    public static final WeightedRandomList<ModWeightedEntry<Supplier<EntityType<? extends AbstractPiglin>>>> PIGLIN_MANOR_MOBS = WeightedRandomList.create(
			new ModWeightedEntry<>(ModEntityTypes.PIGLIN_HUNTER::get, 1),
			new ModWeightedEntry<>(() -> EntityType.PIGLIN, 3)
		);

	public static final WeightedRandomList<ModWeightedEntry<Supplier<EntityType<? extends WitherSkeleton>>>> CATACOMB_MOBS = WeightedRandomList.create(
			new ModWeightedEntry<>(ModEntityTypes.CORPOR::get, 1),
			new ModWeightedEntry<>(ModEntityTypes.WITHER_SKELETON_KNIGHT::get, 2),
			new ModWeightedEntry<>(ModEntityTypes.WRAITHER::get, 3),
			new ModWeightedEntry<>(() -> EntityType.WITHER_SKELETON, 1)
	);
}
