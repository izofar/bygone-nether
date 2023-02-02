package com.izofar.bygonenether.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BasaltColumnFeature;
import net.minecraft.world.gen.feature.structure.BasaltDeltasStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public abstract class ModStructureUtils {

	private static final Predicate<Block> isAir = (block) -> block == Blocks.AIR || block == Blocks.CAVE_AIR;

	public static boolean isNearStructure(ChunkGenerator chunkGenerator, long seed, SharedSeedRandom random, int chunkX, int chunkZ, int radius, Structure<?> ...structures) {
		for(Structure<?> structure : structures) {
			StructureSeparationSettings structureseparationsettings = chunkGenerator.getSettings().getConfig(structure);
			if (structureseparationsettings != null) {
				for (int i = chunkX - radius; i <= chunkX + radius; ++i) {
					for (int j = chunkZ - radius; j <= chunkZ + radius; ++j) {
						ChunkPos chunkpos = structure.getPotentialFeatureChunk(structureseparationsettings, seed, random, i, j);
						if (i == chunkpos.x && j == chunkpos.z) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean isLavaLake(ChunkGenerator chunkGenerator, int x, int z) {
		IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(x, z);
		BlockPos.Mutable currentPos = new BlockPos.Mutable(x, 31, z);

		boolean isLake = true;

		if(columnOfBlocks.getBlockState(currentPos).getBlock() != Blocks.LAVA) isLake = false;
		else while(currentPos.getY() < 70) {
			currentPos.move(Direction.UP);
			isLake = isLake && (isAir.test(columnOfBlocks.getBlockState(currentPos).getBlock()));
		}
		return isLake;
	}
	
	public static boolean isBuried(ChunkGenerator chunkGenerator, int x, int z, int min, int max) {
		IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(x, z);
		BlockPos.Mutable currentPos = new BlockPos.Mutable(x, min, z);

		boolean found = false;
		while(currentPos.getY() < max){
			if (isAir.test(columnOfBlocks.getBlockState(currentPos.above()).getBlock()) && !isAir.test(columnOfBlocks.getBlockState(currentPos).getBlock()))
				found = true;
			currentPos.move(Direction.UP);
		}
		return !found;
	}
	
	public static boolean verticalSpace(ChunkGenerator chunkGenerator, int x, int z, int min, int max, int height) {
		IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(x, z);
		BlockPos.Mutable currentPos = new BlockPos.Mutable(x, max, z);

		int height_tracked = 0;
		while(currentPos.getY() >= min && height_tracked < height){
			if(isAir.test(columnOfBlocks.getBlockState(currentPos).getBlock())) height_tracked ++;
			else height_tracked = 0;
			currentPos.move(Direction.DOWN);
		}
		return height_tracked == height;
	}
	
	public static BlockPos getElevation(ChunkGenerator chunkGenerator, int x, int z, int min, int max) {
		IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(x, z);
		BlockPos.Mutable currentPos = new BlockPos.Mutable(x, min, z);
		BlockPos blockpos = new BlockPos(x, 0, z);

		boolean found = false;
		while(currentPos.getY() < max){
			if (isAir.test(columnOfBlocks.getBlockState(currentPos.above()).getBlock()) && !isAir.test(columnOfBlocks.getBlockState(currentPos).getBlock())) {
				blockpos = new BlockPos(blockpos.getX(), currentPos.getY(), blockpos.getZ());
				found = true;
			}
			currentPos.move(Direction.UP);
		}
		if (!found)
			blockpos = new BlockPos(blockpos.getX(), (max + min) / 2, blockpos.getZ());

		return blockpos;
	}

	public static int getFirstLandYFromPos(IWorldReader worldView, BlockPos pos) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		mutable.set(pos);
		IChunk currentChunk = worldView.getChunk(mutable);
		BlockState currentState = currentChunk.getBlockState(mutable);

		while(mutable.getY() >= 0 && isReplaceableByStructures(currentState)) {
			mutable.move(Direction.DOWN);
			currentState = currentChunk.getBlockState(mutable);
		}

		return mutable.getY();
	}

	private static boolean isReplaceableByStructures(BlockState blockState) {
		return isAir.test(blockState.getBlock()) || blockState.getMaterial().isLiquid() || blockState.getMaterial().isReplaceable();
	}

	public static <F extends Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings structureSeparationSettings, boolean transformLand) {
		Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

		if(transformLand) Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure).build();

		DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.DEFAULTS).put(structure, structureSeparationSettings).build();

		WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
			Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();
			if(structureMap instanceof ImmutableMap){
				Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
				tempMap.put(structure, structureSeparationSettings);
				settings.getValue().structureSettings().structureConfig = tempMap;
			} else structureMap.put(structure, structureSeparationSettings);
		});
	}

	public static void addBasaltRestrictions() {
		BasaltColumnFeature.CANNOT_PLACE_ON = ImmutableList.of(
				// Default
				Blocks.LAVA,
				Blocks.BEDROCK,
				Blocks.MAGMA_BLOCK,
				Blocks.SOUL_SAND,
				Blocks.NETHER_BRICKS,
				Blocks.NETHER_BRICK_FENCE,
				Blocks.NETHER_BRICK_STAIRS,
				Blocks.NETHER_WART,
				Blocks.CHEST,
				Blocks.SPAWNER,
				// New Fortresses:
				Blocks.NETHER_BRICK_SLAB,
				Blocks.CRACKED_NETHER_BRICKS,
				Blocks.CHISELED_NETHER_BRICKS,
				Blocks.RED_NETHER_BRICKS,
				Blocks.RED_NETHER_BRICK_STAIRS,
				Blocks.RED_NETHER_BRICK_SLAB,
				Blocks.CRIMSON_TRAPDOOR,
				// Wither Forts:
				ModBlocks.COBBLED_BLACKSTONE.get(),
				ModBlocks.WITHERED_BLACKSTONE.get(),
				ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(),
				ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(),
				ModBlocks.WITHERED_DEBRIS.get(),
				Blocks.IRON_BARS,
				Blocks.COAL_BLOCK
		);
		BasaltDeltasStructure.CANNOT_REPLACE = ImmutableList.of(
				// Default
				Blocks.BEDROCK,
				Blocks.NETHER_BRICKS,
				Blocks.NETHER_BRICK_FENCE,
				Blocks.NETHER_BRICK_STAIRS,
				Blocks.NETHER_WART,
				Blocks.CHEST,
				Blocks.SPAWNER,
				// New Fortresses:
				Blocks.NETHER_BRICK_SLAB,
				Blocks.CRACKED_NETHER_BRICKS,
				Blocks.CHISELED_NETHER_BRICKS,
				Blocks.RED_NETHER_BRICKS,
				Blocks.RED_NETHER_BRICK_STAIRS,
				Blocks.RED_NETHER_BRICK_SLAB,
				Blocks.CRIMSON_TRAPDOOR,
				// Wither Forts:
				ModBlocks.COBBLED_BLACKSTONE.get(),
				ModBlocks.WITHERED_BLACKSTONE.get(),
				ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(),
				ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(),
				ModBlocks.WITHERED_DEBRIS.get(),
				Blocks.IRON_BARS,
				Blocks.COAL_BLOCK
		);

	}

}
