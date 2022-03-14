package com.izofar.bygonenether.world.structure;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.BygoneNetherMod;
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

public class NetherFortressStructure extends Structure<NoFeatureConfig> {

	public static final List<MobSpawnInfo.Spawners> FORTRESS_ENEMIES = ImmutableList.of(
			new MobSpawnInfo.Spawners(EntityType.BLAZE, 10, 2, 3),
			new MobSpawnInfo.Spawners(EntityType.ZOMBIFIED_PIGLIN, 5, 4, 4),
			new MobSpawnInfo.Spawners(EntityType.WITHER_SKELETON, 8, 5, 5),
			new MobSpawnInfo.Spawners(EntityType.SKELETON, 2, 5, 5),
			new MobSpawnInfo.Spawners(EntityType.MAGMA_CUBE, 3, 4, 4),
			new MobSpawnInfo.Spawners(EntityType.MAGMA_CUBE, 4, 3, 3)
		);
	private static final String FORTRESS_START_POOL = "fortress/start_pool";

	public NetherFortressStructure() { super(NoFeatureConfig.CODEC); }

	@Override
	public  IStartFactory<NoFeatureConfig> getStartFactory() { return NetherFortressStructure.Start::new; }

	@Override
	public GenerationStage.Decoration step() { return GenerationStage.Decoration.SURFACE_STRUCTURES; }

	@Override
	public List<MobSpawnInfo.Spawners> getDefaultSpawnList() { return FORTRESS_ENEMIES; }

	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
		return ModStructureUtils.isLavaLake(chunkGenerator, chunkX * 16, chunkZ * 16);
	}

	public static class Start extends StructureStart<NoFeatureConfig>{

		public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
			super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
		}

		@Override
		public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config){
			BlockPos centerPos = ModStructureUtils.getElevation(chunkGenerator, chunkX * 16, chunkZ * 16, 45, ModStructureUtils.getScaledNetherHeight(54));
			JigsawManager.addPieces(
					dynamicRegistryManager,
					new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
							.get(new ResourceLocation(BygoneNetherMod.MODID, FORTRESS_START_POOL)),
							10),
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
