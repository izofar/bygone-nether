package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class ModTags {
    public static TagKey<ConfiguredStructureFeature<?, ?>> NO_BASALT;

    public static void registerTags() {
        NO_BASALT = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(BygoneNetherMod.MODID, "no_basalt"));
    }
}
