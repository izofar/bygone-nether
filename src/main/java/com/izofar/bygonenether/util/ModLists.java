package com.izofar.bygonenether.util;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.init.ModBlocks;
import com.izofar.bygonenether.init.ModEntityTypes;
import com.izofar.bygonenether.init.ModStructures;
import com.izofar.bygonenether.util.random.WeightedEntry;
import com.izofar.bygonenether.util.random.WeightedRandomList;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.List;
import java.util.function.Supplier;

public abstract class ModLists {

	public static final List<Block> WITHERED_BLOCKS = ImmutableList.of(ModBlocks.WITHERED_BLACKSTONE.get(), ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(), ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(), ModBlocks.WITHERED_DEBRIS.get());
	public static final List<Supplier<Structure<?>>> DELTALESS_STRUCTURES = ImmutableList.of(() -> Structure.BASTION_REMNANT, ModStructures.CATACOMB::get, ModStructures.NETHER_FORTRESS::get, ModStructures.CITADEL::get);

	public static final WeightedRandomList<Supplier<EntityType<? extends AbstractPiglinEntity>>> PIGLIN_MANOR_MOBS = WeightedRandomList.create(
			WeightedEntry.of((Supplier<EntityType<? extends AbstractPiglinEntity>>)ModEntityTypes.PIGLIN_HUNTER::get, 1),
			WeightedEntry.of((Supplier<EntityType<? extends AbstractPiglinEntity>>) () -> EntityType.PIGLIN, 3)
		);
	public static final WeightedRandomList<Supplier<EntityType<? extends WitherSkeletonEntity>>> CATACOMB_MOBS = WeightedRandomList.create(
			WeightedEntry.of((Supplier<EntityType<? extends WitherSkeletonEntity>>) ModEntityTypes.CORPOR::get, 1),
			WeightedEntry.of((Supplier<EntityType<? extends WitherSkeletonEntity>>) ModEntityTypes.WITHER_SKELETON_KNIGHT::get, 1),
			WeightedEntry.of((Supplier<EntityType<? extends WitherSkeletonEntity>> )ModEntityTypes.WRAITHER::get, 1),
			WeightedEntry.of((Supplier<EntityType<? extends WitherSkeletonEntity>>) () -> EntityType.WITHER_SKELETON, 3)
	);
}
