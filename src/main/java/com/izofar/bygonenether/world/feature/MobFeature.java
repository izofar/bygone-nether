package com.izofar.bygonenether.world.feature;

import com.izofar.bygonenether.util.random.WeightedEntry;
import com.izofar.bygonenether.util.random.WeightedRandomList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Supplier;

public class MobFeature<T extends MobEntity> extends Feature<NoFeatureConfig> {

	private final Supplier<WeightedRandomList<EntityType<? extends T>>> entityTypes;

	public MobFeature(Supplier<WeightedRandomList<EntityType<? extends T>>> entityTypes) {
		super(NoFeatureConfig.CODEC);
		this.entityTypes = entityTypes;
	}
	
	public MobFeature(EntityType<? extends T> entityType) {
		super(NoFeatureConfig.CODEC);
		this.entityTypes = () -> WeightedRandomList.create(WeightedEntry.of(entityType, 1));
	}

	@Override
	public boolean place(ISeedReader world, ChunkGenerator chunkGenerator, Random random, BlockPos position, NoFeatureConfig config) {
		position = position.below();
		MobEntity entity = this.entityTypes.get().getRandom(world.getRandom()).create(world.getLevel());
		if (entity == null)
			return false;
		entity.absMoveTo((double) position.getX() + 0.5D, position.getY(), (double) position.getZ() + 0.5D, 0.0F, 0.0F);
		entity.finalizeSpawn(world, world.getCurrentDifficultyAt(position), SpawnReason.SPAWNER, null, null);
		entity.setPersistenceRequired();
		world.addFreshEntity(entity);
		return true;
	}

}
