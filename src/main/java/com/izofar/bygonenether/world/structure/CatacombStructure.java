package com.izofar.bygonenether.world.structure;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.util.ModStructureUtils;
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

public class CatacombStructure extends Structure<NoFeatureConfig> {

	public static final List<MobSpawnInfo.Spawners> CATACOMB_ENEMIES = ImmutableList.of(
			new MobSpawnInfo.Spawners(EntityType.MAGMA_CUBE, 2, 1, 1)
	);

	private static final String CATACOMB_START_POOL = "catacomb/start_pool";

	public CatacombStructure() { super(NoFeatureConfig.CODEC); }

	@Override
	public  IStartFactory<NoFeatureConfig> getStartFactory() { return CatacombStructure.Start::new; }

	@Override
	public GenerationStage.Decoration step() { return GenerationStage.Decoration.SURFACE_STRUCTURES; }

	@Override
	public List<MobSpawnInfo.Spawners> getDefaultSpawnList() { return CATACOMB_ENEMIES; }

	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig){
		int x = chunkX * 16, z = chunkZ * 16;
		return !ModStructureUtils.isBuried(chunkGenerator, x, z, 48, 72) && !ModStructureUtils.isLavaLake(chunkGenerator, x, z);
	}

	public static class Start extends StructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
			super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
		}

		@Override
		public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config){
			int x = chunkX * 16, z = chunkZ * 16;
			BlockPos centerPos = ModStructureUtils.getElevation(chunkGenerator, x, z, 56, 84);


			JigsawManager.addPieces(
					dynamicRegistryManager,
					new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
							.get(new ResourceLocation(BygoneNetherMod.MODID, CATACOMB_START_POOL)),
							3),
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
