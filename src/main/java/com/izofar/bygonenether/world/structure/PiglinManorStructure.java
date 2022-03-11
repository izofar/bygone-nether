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
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PiglinManorStructure extends StructureFeature<JigsawConfiguration> {

    private final static int CHUNK_SEARCH_RADIUS = 3;
    private final static int MAX_TERRAIN_RANGE = 10;

    public static final List<SpawnerData> MANOR_ENEMIES = List.of(
            new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 2, 1, 1),
            new MobSpawnSettings.SpawnerData(ModEntityTypes.PIGLIN_HUNTER.get(), 1, 1, 1)
    );

    public PiglinManorStructure(Codec<JigsawConfiguration> codec) {
        super(codec, PiglinManorStructure::checkLocation);
    }

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    private static @NotNull Optional<PieceGenerator<JigsawConfiguration>> checkLocation(Context<JigsawConfiguration> context) {
        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);
        NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor());
        if (!checkChunk(context)
                || !ModStructureUtils.isRelativelyFlat(context, CHUNK_SEARCH_RADIUS, MAX_TERRAIN_RANGE)
                || ModStructureUtils.isLavaLake(blockReader)
                || !ModStructureUtils.verticalSpace(blockReader, 34, 72, 24))
            return Optional.empty();
        else
            return PiglinManorStructure.createPiecesGenerator(context);
    }

    private static boolean checkChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        return context.validBiome().test(context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(context.chunkPos().getMiddleBlockX()), QuartPos.fromBlock(64), QuartPos.fromBlock(context.chunkPos().getMiddleBlockZ())));
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        BlockPos blockpos = ModStructureUtils.getElevation(context, 34, ModStructureUtils.getScaledNetherHeight(72));
        JigsawConfiguration newConfig = new JigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(BygoneNetherMod.MODID, "piglin_manor/start_pool")), 1);
        return JigsawPlacement.addPieces(ModStructureUtils.duplicateContext(context, newConfig), PoolElementStructurePiece::new, blockpos, false, false);
    }
}
