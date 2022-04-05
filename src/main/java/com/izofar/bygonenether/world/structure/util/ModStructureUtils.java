package com.izofar.bygonenether.world.structure.util;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.BasaltColumnsFeature;
import net.minecraft.world.level.levelgen.feature.DeltaFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraftforge.fml.ModList;

import java.util.Random;
import java.util.function.Predicate;

public abstract class ModStructureUtils {

	private static final Predicate<Block> isAir = (block) -> block == Blocks.AIR || block == Blocks.CAVE_AIR;
	
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

	public static void addBasaltRestrictions() {
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
		   DeltaFeature.CANNOT_REPLACE = ImmutableList.of(
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

	public static int getScaledNetherHeight(int vanillaHeight){
		return (int) (vanillaHeight / 128.0F * (ModList.get().isLoaded("amplifiednether") ? 256.0F : 128.0F));
	}
	
}
