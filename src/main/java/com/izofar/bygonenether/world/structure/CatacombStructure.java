package com.izofar.bygonenether.world.structure;

import com.izofar.bygonenether.util.ModStructureUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier.Context;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.List;
import java.util.Optional;

public class CatacombStructure extends StructureFeature<JigsawConfiguration> {

	private static final int STRUCTURE_SEARCH_RADIUS = 8;
	
	public CatacombStructure(Codec<JigsawConfiguration> codec) {
		super(codec, CatacombStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
	}
	
	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}
	
	private static boolean checkLocation(Context<JigsawConfiguration> context) {
		BlockPos blockpos  = context.chunkPos().getMiddleBlockPosition(0);
		NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor());
		return !ModStructureUtils.isBuried(blockReader, 48, ModStructureUtils.getScaledNetherHeight(72))
				&& !ModStructureUtils.isLavaLake(blockReader)
				&& !ModStructureUtils.isNearStructure(context.chunkGenerator(), context.seed(), context.chunkPos(), STRUCTURE_SEARCH_RADIUS, BuiltinStructureSets.NETHER_COMPLEXES);
	}
	
	public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		if (!checkLocation(context)) {
			return Optional.empty();
		}
		BlockPos blockpos = ModStructureUtils.getElevation(context, 56, ModStructureUtils.getScaledNetherHeight(84));
		return JigsawPlacement.addPieces(context, PoolElementStructurePiece::new, blockpos, true, false);
	}
	
}
