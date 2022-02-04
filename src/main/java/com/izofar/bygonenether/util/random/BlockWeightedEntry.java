package com.izofar.bygonenether.util.random;

import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.Block;

public class BlockWeightedEntry implements WeightedEntry {

	private final Block data;
	private final Weight weight;

	private BlockWeightedEntry(Block data, int weight) {
		this.data = data;
		this.weight = Weight.of(weight);
	}

	public static BlockWeightedEntry create(Block data, int weight) {
		return new BlockWeightedEntry(data, weight);
	}

	public Block getData() {
		return this.data;
	}

	@Override
	public Weight getWeight() {
		return this.weight;
	}
}
