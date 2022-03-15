package com.izofar.bygonenether.world.structure;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.init.ModEntityTypes;
import com.izofar.bygonenether.world.structure.util.ModStructureUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier.Context;

import java.util.List;
import java.util.Optional;

public class CatacombStructure extends StructureFeature<JigsawConfiguration> {

	public static final List<SpawnerData> FORTRESS_ENEMIES = List.of(
			new MobSpawnSettings.SpawnerData(ModEntityTypes.WEX.get(), 1, 1, 1),
			new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 2, 1, 1)
		);
	
	public CatacombStructure(Codec<JigsawConfiguration> codec) { super(codec, CatacombStructure::checkLocation); }
	
	@Override
	public GenerationStep.Decoration step() { return GenerationStep.Decoration.SURFACE_STRUCTURES; }
	
	private static Optional<PieceGenerator<JigsawConfiguration>> checkLocation(Context<JigsawConfiguration> context) {
		BlockPos blockpos  = context.chunkPos().getMiddleBlockPosition(0);
		NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor());
		if (ModStructureUtils.isBuried(blockReader, 48, 72) || ModStructureUtils.isLavaLake(blockReader))
			return Optional.empty();
		else
			return createPiecesGenerator(context);
	}
	
	public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		BlockPos blockpos = ModStructureUtils.getElevation(context, 56, ModStructureUtils.getScaledNetherHeight(84));
		JigsawConfiguration newConfig = new JigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(BygoneNetherMod.MODID, "catacomb/start_pool")), 3);
		return JigsawPlacement.addPieces(ModStructureUtils.duplicateContext(context, newConfig), PoolElementStructurePiece::new, blockpos, true, false);
	}
	
}
