package com.izofar.bygonenether.init;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.world.level.levelgen.feature.StructureFeature;

public abstract class ModTags {

	public enum STRUCTURE_TAGS {
		NO_DELTAS, DELTA_CHECK_CENTER_PIECE
	}

	public static final Map<StructureFeature<?>, Set<STRUCTURE_TAGS>> TAGGED_STRUCTURES = new HashMap<>();
	public static final Map<STRUCTURE_TAGS, Set<StructureFeature<?>>> REVERSED_TAGGED_STRUCTURES = new HashMap<>();

	public static void setupTags() {
		addTags(StructureFeature.BASTION_REMNANT, Stream.of(STRUCTURE_TAGS.NO_DELTAS).collect(Collectors.toSet()));
		addTags(ModStructures.WITHER_FORT.get(), Stream.of(STRUCTURE_TAGS.NO_DELTAS).collect(Collectors.toSet()));
	}

	private static void addTags(StructureFeature<?> structure, Set<STRUCTURE_TAGS> tags) {
		if (!TAGGED_STRUCTURES.containsKey(structure))
			TAGGED_STRUCTURES.put(structure, new HashSet<>());

		TAGGED_STRUCTURES.get(structure).addAll(tags);

		for (STRUCTURE_TAGS tag : tags) {
			if (!REVERSED_TAGGED_STRUCTURES.containsKey(tag))
				REVERSED_TAGGED_STRUCTURES.put(tag, new HashSet<>());

			REVERSED_TAGGED_STRUCTURES.get(tag).add(structure);
		}
	}
}