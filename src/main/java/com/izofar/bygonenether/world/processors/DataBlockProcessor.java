package com.izofar.bygonenether.world.processors;

import com.izofar.bygonenether.init.ModProcessors;
import com.izofar.bygonenether.util.ModStructureUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Map;
import java.util.stream.Collectors;

public class DataBlockProcessor extends StructureProcessor {
    private static final ResourceLocation EMPTY_RL = new ResourceLocation("minecraft", "empty");

    public static final Codec<DataBlockProcessor> CODEC  = RecordCodecBuilder.create((instance) -> instance.group(
                    RegistryOps.retrieveRegistry(Registry.PROCESSOR_LIST_REGISTRY).forGetter((processor) -> processor.processorListRegistry),
                    Codec.mapPair(BlockState.CODEC.fieldOf("trigger"), BlockState.CODEC.fieldOf("replacement"))
                            .codec().listOf()
                            .xmap((list) -> list.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)),
                                    (map) -> map.entrySet().stream().map((entry) -> Pair.of(entry.getKey(), entry.getValue())).collect(Collectors.toList()))
                            .fieldOf("trigger_and_replacements")
                            .forGetter((processor) -> processor.triggerAndReplacementBlocks),
                    ResourceLocation.CODEC.optionalFieldOf("processor_list", EMPTY_RL).forGetter(processor -> processor.processorList),
                    Direction.CODEC.optionalFieldOf("direction", Direction.DOWN).forGetter(processor -> processor.direction),
                    Codec.INT.optionalFieldOf("length", 1000).forGetter(config -> config.length),
                    Codec.BOOL.optionalFieldOf("forced_placement", false).forGetter(config -> config.forcePlacement))
            .apply(instance, instance.stable(DataBlockProcessor::new)));

    public final Registry<StructureProcessorList> processorListRegistry;
    public final Map<BlockState, BlockState> triggerAndReplacementBlocks;
    public final ResourceLocation processorList;
    public final Direction direction;
    public final int length;
    public final boolean forcePlacement;

    private DataBlockProcessor(Registry<StructureProcessorList> processorListRegistry,
                               Map<BlockState, BlockState> triggerAndReplacementBlocks,
                               ResourceLocation processorList,
                               Direction direction,
                               int length,
                               boolean forcePlacement) {
        this.processorListRegistry = processorListRegistry;
        this.triggerAndReplacementBlocks = triggerAndReplacementBlocks;
        this.processorList = processorList;
        this.direction = direction;
        this.length = length;
        this.forcePlacement = forcePlacement;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos templateOffset, BlockPos worldOffset, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {

        BlockState blockState = structureBlockInfoWorld.state;
        if (triggerAndReplacementBlocks.containsKey(blockState)) {
            BlockPos worldPos = structureBlockInfoWorld.pos;

            BlockState replacementState = triggerAndReplacementBlocks.get(blockState);
            BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos().set(worldPos);
            StructureProcessorList structureProcessorList = null;
            if(processorList != null && !processorList.equals(EMPTY_RL)) {
                structureProcessorList = processorListRegistry.get(processorList);
            }

            if(levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(currentPos))) {
                return replacementState == null || replacementState.is(Blocks.STRUCTURE_VOID) ? null : new StructureTemplate.StructureBlockInfo(worldPos, replacementState, null);
            }

            int terrainY = Integer.MIN_VALUE;
            if(direction == Direction.DOWN && !forcePlacement) {
                terrainY = ModStructureUtils.getFirstLandYFromPos(levelReader, worldPos);
                if(terrainY <= levelReader.getMinBuildHeight() && length + 2 >= worldPos.getY() - levelReader.getMinBuildHeight()) {
                    // Replaces the data block itself
                    return (replacementState == null || replacementState.is(Blocks.STRUCTURE_VOID)) ?
                            null : new StructureTemplate.StructureBlockInfo(worldPos, replacementState, null);
                }
            }

            // Creates the pillars in the world that replaces air and liquids
            BlockState currentBlock = levelReader.getBlockState(worldPos.below());
            while((forcePlacement || !currentBlock.canOcclude()) &&
                    (forcePlacement || currentPos.getY() >= terrainY) &&
                    !levelReader.isOutsideBuildHeight(currentPos.getY()) &&
                    currentPos.closerThan(worldPos, length)
            ) {
                StructureTemplate.StructureBlockInfo newPillarState1 = new StructureTemplate.StructureBlockInfo(currentPos.subtract(worldPos).offset(templateOffset), replacementState, null);
                StructureTemplate.StructureBlockInfo newPillarState2 = new StructureTemplate.StructureBlockInfo(currentPos.immutable(), replacementState, null);

                if(structureProcessorList != null) {
                    for(StructureProcessor processor : structureProcessorList.list()) {
                        if(newPillarState2 == null) {
                            break;
                        }
                        newPillarState2 = processor.processBlock(levelReader, newPillarState1.pos, newPillarState2.pos, newPillarState1, newPillarState2, structurePlacementData);
                    }
                }

                if(newPillarState2 != null) {
                    levelReader.getChunk(currentPos).setBlockState(currentPos, newPillarState2.state, false);
                }

                currentPos.move(direction);
                currentBlock = levelReader.getBlockState(currentPos);
            }

            // Replaces the data block itself
            return (replacementState == null || replacementState.is(Blocks.STRUCTURE_VOID)) ?
                    null : new StructureTemplate.StructureBlockInfo(worldPos, replacementState, null);
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.DATA_BLOCK_PROCESSOR;
    }
}
