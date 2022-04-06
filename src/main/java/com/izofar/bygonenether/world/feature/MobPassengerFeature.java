package com.izofar.bygonenether.world.feature;

import com.izofar.bygonenether.util.random.WeightedEntry;
import com.izofar.bygonenether.util.random.WeightedRandomList;
import com.mojang.datafixers.util.Pair;
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

public class MobPassengerFeature<P extends MobEntity, V extends MobEntity> extends Feature<NoFeatureConfig> {

    private final Supplier<WeightedRandomList<Pair<EntityType<? extends P>, EntityType<? extends V>>>> entityTypes;

    public MobPassengerFeature(EntityType<? extends P> passenger, EntityType<? extends V> vehicle) {
        super(NoFeatureConfig.CODEC);
        this.entityTypes = () -> WeightedRandomList.create(WeightedEntry.of(Pair.of(passenger, vehicle), 1));
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator chunkGenerator, Random random, BlockPos position, NoFeatureConfig config) {
        Pair<EntityType<? extends P>, EntityType<? extends V>> pair = this.entityTypes.get().getRandom(world.getRandom());

        P passenger = this.createPassenger(world, position, pair);
        V vehicle = this.createVehicle(world, position, pair);

        passenger.startRiding(vehicle);
        world.addFreshEntityWithPassengers(vehicle);

        return true;
    }

    private V createVehicle(ISeedReader world, BlockPos position, Pair<EntityType<? extends P>, EntityType<? extends V>> pair) {
        V vehicle = pair.getSecond().create(world.getLevel());
        vehicle.finalizeSpawn(world, world.getCurrentDifficultyAt(position), SpawnReason.SPAWNER, null, null);
        vehicle.setPos(position.getX(), position.getY(), position.getZ());
        vehicle.setPersistenceRequired();
        return vehicle;
    }

    private P createPassenger(ISeedReader world, BlockPos position, Pair<EntityType<? extends P>, EntityType<? extends V>> pair) {
        P passenger = pair.getFirst().create(world.getLevel());
        passenger.finalizeSpawn(world, world.getCurrentDifficultyAt(position), SpawnReason.SPAWNER, null, null);
        passenger.setPos(position.getX(), position.getY(), position.getZ());
        passenger.setPersistenceRequired();
        return passenger;
    }

}
