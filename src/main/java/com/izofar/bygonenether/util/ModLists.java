package com.izofar.bygonenether.util;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.init.ModBlocks;
import com.izofar.bygonenether.init.ModEntityTypes;
import com.izofar.bygonenether.util.random.MobWeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.block.Block;

import java.util.List;

public abstract class ModLists {

	public static final List<Block> WITHERED_BLOCKS = ImmutableList.of(
			ModBlocks.WITHERED_BLACKSTONE.get(),
			ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(),
			ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(),
			ModBlocks.WITHERED_DEBRIS.get()
		);

    public static final WeightedRandomList<MobWeightedEntry<EntityType<? extends AbstractPiglin>>> PIGLIN_MANOR_MOBS = WeightedRandomList.create(
			new MobWeightedEntry<>(ModEntityTypes.PIGLIN_HUNTER.get(), 1),
			new MobWeightedEntry<>(EntityType.PIGLIN, 3)
		);
}
