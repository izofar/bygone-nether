package com.izofar.bygonenether.world.feature;

import com.izofar.bygonenether.util.random.ModWeightedEntry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Supplier;

public class MobPassengerFeature<P extends Mob, V extends Mob> extends Feature<NoneFeatureConfiguration> {
    private final WeightedRandomList<ModWeightedEntry<Pair<Supplier<EntityType<? extends P>>, Supplier<EntityType<? extends V>>>>> entityTypes;

    public MobPassengerFeature(Supplier<EntityType<? extends P>> passenger, Supplier<EntityType<? extends V>> vehicle) {
        super(NoneFeatureConfiguration.CODEC);
        this.entityTypes = WeightedRandomList.create(new ModWeightedEntry<>(Pair.of(passenger, vehicle), 1));
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Pair<Supplier<EntityType<? extends P>>, Supplier<EntityType<? extends V>>> pair = this.entityTypes.getRandom(context.random()).get().getData();

        P passenger = this.createPassenger(context, pair);
        V vehicle = this.createVehicle(context, pair);

        passenger.startRiding(vehicle);
        context.level().addFreshEntityWithPassengers(vehicle);
        return true;
    }

    private V createVehicle(FeaturePlaceContext<NoneFeatureConfiguration> context, Pair<Supplier<EntityType<? extends P>>, Supplier<EntityType<? extends V>>> pair) {
        BlockPos position = context.origin().below();
        V vehicle = pair.getSecond().get().create(context.level().getLevel());
        vehicle.finalizeSpawn(context.level(), context.level().getCurrentDifficultyAt(position), MobSpawnType.SPAWNER, null, null);
        vehicle.setPos(position.getX(), position.getY(), position.getZ());
        vehicle.setPersistenceRequired();
        return vehicle;
    }

    private P createPassenger(FeaturePlaceContext<NoneFeatureConfiguration> context, Pair<Supplier<EntityType<? extends P>>, Supplier<EntityType<? extends V>>> pair) {
        BlockPos position = context.origin().below();
        P passenger = pair.getFirst().get().create(context.level().getLevel());
        passenger.finalizeSpawn(context.level(), context.level().getCurrentDifficultyAt(position), MobSpawnType.SPAWNER, null, null);
        passenger.setPos(position.getX(), position.getY(), position.getZ());
        passenger.setPersistenceRequired();
        return passenger;
    }

}
