package com.izofar.bygonenether.world.structure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.izofar.bygonenether.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.BasaltColumnsFeature;
import net.minecraft.world.level.levelgen.feature.DeltaFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public abstract class ModStructureUtils {

	private static Predicate<Block> isAir = (block) -> block == Blocks.AIR || block == Blocks.CAVE_AIR;
	
	public static boolean isLavaLake(NoiseColumn blockReader) {
		boolean isLake = true;
		if(blockReader.getBlock(31).getBlock() != Blocks.LAVA) isLake = false;
		else for(int i = 32; i < 70; i ++)
			isLake = isLake && (isAir.test(blockReader.getBlock(i).getBlock()));
		return isLake;
	}
	
	public static boolean isBuried(NoiseColumn blockReader, int min, int max) {

		boolean found = false;
		for (int i = min; i < max; i++) {
			if (isAir.test(blockReader.getBlock(i + 1).getBlock()) && !isAir.test(blockReader.getBlock(i).getBlock()))
				found = true;
		}
		return !found;
	}
	
	public static boolean verticalSpace(NoiseColumn blockReader, int min, int max, int height) {
		int height_tracked = 0;
		for(int i = max; i >= min && height_tracked < height; i --) {
			if(isAir.test(blockReader.getBlock(i).getBlock())) height_tracked ++;
			else height_tracked = 0;
		}
		return height_tracked == height;
	}
	
	public static BlockPos getElevation(PieceGeneratorSupplier.Context<JigsawConfiguration> context, int min, int max) {
		BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);
		NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor());

		boolean found = false;
		for (int i = min; i < max; i++) {
			if (isAir.test(blockReader.getBlock(i + 1).getBlock()) && !isAir.test(blockReader.getBlock(i).getBlock())) {
				blockpos = new BlockPos(blockpos.getX(), i, blockpos.getZ());
				found = true;
			}
		}
		if (!found)
			blockpos = new BlockPos(blockpos.getX(), new Random(context.seed()).nextInt(max - min) + min, blockpos.getZ());
		return blockpos;
	}

	public static PieceGeneratorSupplier.Context<JigsawConfiguration> duplicateContext(
			PieceGeneratorSupplier.Context<JigsawConfiguration> context, JigsawConfiguration config) {
		return new PieceGeneratorSupplier.Context<>(
				context.chunkGenerator(), 
				context.biomeSource(), 
				context.seed(),
				context.chunkPos(), 
				config, 
				context.heightAccessor(), 
				context.validBiome(), 
				context.structureManager(),
				context.registryAccess());
	}
	
	public static void addWitheredRestrictions() {
		   BasaltColumnsFeature.CANNOT_PLACE_ON = ImmutableList.of(
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
				   // Wither Forts:
				   ModBlocks.COBBLED_BLACKSTONE.get(),
				   ModBlocks.WITHERED_BLACKSTONE.get(),
				   ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(),
				   ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(),
				   ModBlocks.WITHERED_DEBRIS.get(),
				   Blocks.IRON_BARS,
				   Blocks.COAL_BLOCK
			   );
		   DeltaFeature.CANNOT_REPLACE = ImmutableList.of(
				   // Default
				   Blocks.BEDROCK, 
				   Blocks.NETHER_BRICKS, 
				   Blocks.NETHER_BRICK_FENCE, 
				   Blocks.NETHER_BRICK_STAIRS,
				   Blocks.NETHER_WART, 
				   Blocks.CHEST, 
				   Blocks.SPAWNER,
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

	public static boolean isNearStructure(ChunkGenerator chunk, long dist, ChunkPos inChunkPos, StructureFeature<? extends FeatureConfiguration> feature) {
		StructureFeatureConfiguration structurefeatureconfiguration = chunk.getSettings().getConfig(feature);
		if (structurefeatureconfiguration == null) return false;
		else {
			int i = inChunkPos.x;
			int j = inChunkPos.z;

			for (int k = i - 10; k <= i + 10; ++k) 
				for (int l = j - 10; l <= j + 10; ++l) {
					ChunkPos chunkpos = feature.getPotentialFeatureChunk(structurefeatureconfiguration, dist, k, l);
					if (k == chunkpos.x && l == chunkpos.z) return true;
				}
			return false;
		}
	}

	public static int getFirstLandYFromPos(LevelReader worldView, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		mutable.set(pos);
		ChunkAccess currentChunk = worldView.getChunk(mutable);
		BlockState currentState = currentChunk.getBlockState(mutable);

		while(mutable.getY() >= worldView.getMinBuildHeight() && isReplaceableByStructures(currentState)) {
			mutable.move(Direction.DOWN);
			currentState = currentChunk.getBlockState(mutable);
		}

		return mutable.getY();
	}

	private static boolean isReplaceableByStructures(BlockState blockState) {
		return blockState.isAir() || blockState.getMaterial().isLiquid() || blockState.getMaterial().isReplaceable();
	}

	public static <F extends StructureFeature<?>> void setupMapSpacingAndLand(F structure, StructureFeatureConfiguration config, boolean transformLand) {
		StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

		if(transformLand)
			StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder().addAll(StructureFeature.NOISE_AFFECTING_FEATURES).add(structure).build();

		StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder().putAll(StructureSettings.DEFAULTS).put(structure, config).build();

		BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
			Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();
			if(structureMap instanceof ImmutableMap){
				Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
				tempMap.put(structure, config);
				settings.getValue().structureSettings().structureConfig = tempMap;
			} else
				structureMap.put(structure, config);
		});
	}
	
}
