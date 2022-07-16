package com.izofar.bygonenether.world.structure;

import com.izofar.bygonenether.init.ModStructures;
import com.izofar.bygonenether.world.structure.util.ModStructureUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PiglinManorStructure extends Structure {

    public static final Codec<PiglinManorStructure> CODEC = RecordCodecBuilder.<PiglinManorStructure>mapCodec(instance ->
            instance.group(PiglinManorStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
            ).apply(instance, PiglinManorStructure::new)).codec();

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;

    public PiglinManorStructure(Structure.StructureSettings config,
                            Holder<StructureTemplatePool> startPool,
                            Optional<ResourceLocation> startJigsawName,
                            int size,
                            HeightProvider startHeight,
                            Optional<Heightmap.Types> projectStartToHeightmap,
                            int maxDistanceFromCenter) {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    private static @NotNull boolean checkLocation(Structure.GenerationContext context) {
        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);
        NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor(), context.randomState());
        return checkChunk(context)
                && !ModStructureUtils.isLavaLake(blockReader)
                && ModStructureUtils.verticalSpace(blockReader, 34, ModStructureUtils.getScaledNetherHeight(72), 24);
    }

    private static boolean checkChunk(Structure.GenerationContext context) {
        return context.validBiome().test(context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(context.chunkPos().getMiddleBlockX()), QuartPos.fromBlock(64), QuartPos.fromBlock(context.chunkPos().getMiddleBlockZ()), context.randomState().sampler()));
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        if(!checkLocation(context)) return Optional.empty();

        BlockPos blockpos = ModStructureUtils.getElevation(context, 34, ModStructureUtils.getScaledNetherHeight(72));
        return JigsawPlacement.addPieces(context, this.startPool, this.startJigsawName, this.size, blockpos, false, this.projectStartToHeightmap, this.maxDistanceFromCenter);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.PIGLIN_MANOR.get();
    }
}
