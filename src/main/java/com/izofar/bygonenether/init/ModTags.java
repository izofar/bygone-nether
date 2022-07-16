package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModTags {
    public static TagKey<Structure> NO_BASALT;

    public static void initTags(){
       NO_BASALT = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(BygoneNetherMod.MODID, "no_basalt"));
    }
}
