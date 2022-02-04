package com.izofar.bygonenether.world.structure;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.init.ModEntityTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier.Context;

import java.util.List;
import java.util.Optional;

public class CitadelStructure extends StructureFeature<JigsawConfiguration> {
	
	public static final List<SpawnerData> CITADEL_ENEMIES = List.of(
			new MobSpawnSettings.SpawnerData(ModEntityTypes.WARPED_ENDERMAN.get(), 1, 1, 1),
			new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 1)
		);

	public CitadelStructure(Codec<JigsawConfiguration> codec) { super(codec, CitadelStructure::checkLocation); }

	@Override
	public GenerationStep.Decoration step() { return GenerationStep.Decoration.SURFACE_STRUCTURES; }
	
	private static Optional<PieceGenerator<JigsawConfiguration>> checkLocation(Context<JigsawConfiguration> context) {
		BlockPos blockpos  = context.chunkPos().getMiddleBlockPosition(0);
		NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor());
		if (!checkChunk(context) || ModStructureUtils.isLavaLake(blockReader) || !ModStructureUtils.verticalSpace(blockReader, 34, 72, 12))
			return Optional.empty();
		else 
			return CitadelStructure.createPiecesGenerator(context);
	}

	private static boolean checkChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
		worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
		return worldgenrandom.nextInt(5) >= 2 ? false : context.validBiome().test(context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(context.chunkPos().getMiddleBlockX()), QuartPos.fromBlock(64), QuartPos.fromBlock(context.chunkPos().getMiddleBlockZ())));
	}
	
	public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator( PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		BlockPos blockpos = ModStructureUtils.getElevation(context, 48, 70);
		JigsawConfiguration newConfig = new JigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(BygoneNetherMod.MODID, "citadel/start_pool")), 1);
		Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator = JigsawPlacement.addPieces(ModStructureUtils.duplicateContext(context, newConfig), PoolElementStructurePiece::new, blockpos, false, false);
		return structurePiecesGenerator;
	}
}
