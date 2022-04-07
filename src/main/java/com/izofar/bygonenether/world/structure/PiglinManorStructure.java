package com.izofar.bygonenether.world.structure;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.init.ModEntityTypes;
import com.izofar.bygonenether.world.structure.util.ModStructureUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;

public class PiglinManorStructure extends Structure<NoFeatureConfig> {

    private static final String PIGLIN_MANOR_START_POOL = "piglin_manor/start_pool";

    public static final List<MobSpawnInfo.Spawners> MANOR_ENEMIES = ImmutableList.of(
            new MobSpawnInfo.Spawners(EntityType.PIGLIN, 2, 1, 1),
            new MobSpawnInfo.Spawners(ModEntityTypes.PIGLIN_HUNTER.get(), 1, 1, 1)
    );

    public PiglinManorStructure() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public  IStartFactory<NoFeatureConfig> getStartFactory() { return PiglinManorStructure.Start::new; }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() { return MANOR_ENEMIES; }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig){
        int x = chunkX * 16, z = chunkZ * 16;
        return /*ModStructureUtils.isRelativelyFlat(chunkGenerator, x, z, CHUNK_SEARCH_RADIUS, MAX_TERRAIN_RANGE) &&*/ !ModStructureUtils.isLavaLake(chunkGenerator, x, z) && ModStructureUtils.verticalSpace(chunkGenerator, x, z, 34, 72, 24);
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config){
            int x = chunkX * 16, z = chunkZ * 16;
            BlockPos centerPos = ModStructureUtils.getElevation(chunkGenerator, x, z, 34, 72);


            JigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            .get(new ResourceLocation(BygoneNetherMod.MODID, PIGLIN_MANOR_START_POOL)),
                            1),
                    AbstractVillagePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    centerPos,
                    this.pieces,
                    this.random,
                    false,
                    false);

            Vector3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
            int xOffset = centerPos.getX() - structureCenter.getX();
            int zOffset = centerPos.getZ() - structureCenter.getZ();
            for(StructurePiece structurePiece : this.pieces){
                structurePiece.move(xOffset, 0, zOffset);
            }

            this.calculateBoundingBox();
        }
    }

}
