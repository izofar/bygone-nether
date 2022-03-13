package com.izofar.bygonenether.util.random;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.WeightedRandom;

import java.util.Random;

public class WeightedRandomList<T> extends WeightedRandom {

    private final ImmutableList<WeightedEntry<T>> list;

    private WeightedRandomList(ImmutableList<WeightedEntry<T>> list){
        this.list = list;
    }

    public static <T> WeightedRandomList<T> create(WeightedEntry<T> ...entries){
        return new WeightedRandomList<T>(ImmutableList.copyOf(entries));
    }

    public T getRandom(Random random){
        return WeightedRandom.getRandomItem(random, list).data;
    }
}
