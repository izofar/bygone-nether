package com.izofar.bygonenether.util.random;

import net.minecraft.util.WeightedRandom;

public class WeightedEntry<T> extends WeightedRandom.Item{

	public final T data;
	
    public WeightedEntry(T data, int weight) {
        super(weight);
        this.data = data;
     }

    public static <T> WeightedEntry of(T data, int weight) {
        return new WeightedEntry<>(data, weight);
    }


}
