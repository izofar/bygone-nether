package com.izofar.bygonenether.util.random;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.WeightedRandom;

import java.util.List;
import java.util.Random;

public class WeightedRandomList<T> extends WeightedRandom {

    private final List<WeightedEntry<T>> list;

    private WeightedRandomList(List<WeightedEntry<T>> list){
        this.list = list;
    }

    public static <T> WeightedRandomList<T> create(WeightedEntry<T> ...entries){
        return new WeightedRandomList<T>(ImmutableList.copyOf(entries));
    }

    public T getRandom(Random random){
        return WeightedRandom.getRandomItem(random, list).data;
    }
}
