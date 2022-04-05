package com.izofar.bygonenether.world.structure;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.world.structure.util.ModStructureUtils;
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

public class NetherFortressStructure extends StructureFeature<JigsawConfiguration> {

	public static final List<SpawnerData> FORTRESS_ENEMIES = List.of(
			new MobSpawnSettings.SpawnerData(EntityType.BLAZE, 10, 2, 3),
			new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 5, 4, 4),
			new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKELETON, 8, 5, 5),
			new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 2, 5, 5),
			new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 3, 4, 4),
			new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 4, 3, 3)
		);

	public NetherFortressStructure(Codec<JigsawConfiguration> codec) { super(codec, NetherFortressStructure::checkLocation); }

	@Override
	public GenerationStep.Decoration step() { return GenerationStep.Decoration.SURFACE_STRUCTURES; }
	
	private static Optional<PieceGenerator<JigsawConfiguration>> checkLocation(Context<JigsawConfiguration> context) {
		BlockPos blockpos  = context.chunkPos().getMiddleBlockPosition(0);
		NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor());
		if (!checkChunk(context)
				|| !ModStructureUtils.isLavaLake(blockReader)
				|| ModStructureUtils.isNearStructure(context.chunkGenerator(), context.seed(), context.chunkPos(), StructureFeature.BASTION_REMNANT, 4))
			return Optional.empty();
		else
			return NetherFortressStructure.createPiecesGenerator(context);
	}

	private static boolean checkChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
		worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
		return context.validBiome().test(context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(context.chunkPos().getMiddleBlockX()), QuartPos.fromBlock(64), QuartPos.fromBlock(context.chunkPos().getMiddleBlockZ())));
	}
	
	public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator( PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		BlockPos blockpos = ModStructureUtils.getElevation(context, 45, ModStructureUtils.getScaledNetherHeight(54));
		JigsawConfiguration newConfig = new JigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(BygoneNetherMod.MODID, "fortress/start_pool")), 7);
		return JigsawPlacement.addPieces(ModStructureUtils.duplicateContext(context, newConfig), PoolElementStructurePiece::new, blockpos, false, false);
	}

}
