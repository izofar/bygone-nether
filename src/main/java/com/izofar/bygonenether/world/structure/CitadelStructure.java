package com.izofar.bygonenether.world.structure;

import com.izofar.bygonenether.init.ModEntityTypes;
import com.izofar.bygonenether.util.ModStructureUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
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

public class CitadelStructure extends StructureFeature<JigsawConfiguration> {

	private static final int STRUCTURE_SEARCH_RADIUS = 4;

	public CitadelStructure(Codec<JigsawConfiguration> codec) { super(codec, CitadelStructure::createPiecesGenerator, PostPlacementProcessor.NONE); }

	@Override
	public GenerationStep.Decoration step() { return GenerationStep.Decoration.SURFACE_STRUCTURES; }
	
	private static boolean checkLocation(Context<JigsawConfiguration> context) {
		BlockPos blockpos  = context.chunkPos().getMiddleBlockPosition(0);
		NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor());
		return checkChunk(context)
				&& !ModStructureUtils.isLavaLake(blockReader)
				&& ModStructureUtils.verticalSpace(blockReader, 34, ModStructureUtils.getScaledNetherHeight(72), 12)
				&& !ModStructureUtils.isNearStructure(context.chunkGenerator(), context.seed(), context.chunkPos(), STRUCTURE_SEARCH_RADIUS, BuiltinStructureSets.NETHER_COMPLEXES);
	}

	private static boolean checkChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
		worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
		return context.validBiome().test(context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(context.chunkPos().getMiddleBlockX()), QuartPos.fromBlock(64), QuartPos.fromBlock(context.chunkPos().getMiddleBlockZ())));
	}
	
	public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator( PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		if(!checkLocation(context)) return Optional.empty();

		BlockPos blockpos = ModStructureUtils.getElevation(context, 48, ModStructureUtils.getScaledNetherHeight(70));
		return JigsawPlacement.addPieces(context, PoolElementStructurePiece::new, blockpos, false, false);
	}
}
