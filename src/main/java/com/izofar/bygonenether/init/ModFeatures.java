package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.util.ModLists;
import com.izofar.bygonenether.world.feature.MobFeature;
import com.izofar.bygonenether.world.feature.MobPassengerFeature;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.ReplaceBlobsFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;

public class ModFeatures {
    public static final Feature<NoneFeatureConfiguration> MOB_FEATURE_PIGLIN_PRISONER = new MobFeature<>(() -> ModEntityTypes.PIGLIN_PRISONER);
    public static final Feature<NoneFeatureConfiguration> MOB_FEATURE_PIGLIN_MANOR_INSIDE = new MobFeature<>(ModLists.PIGLIN_MANOR_MOBS);
    public static final Feature<NoneFeatureConfiguration> MOB_FEATURE_PIGLIN_MANOR_OUTSIDE = new MobPassengerFeature<>(() -> ModEntityTypes.PIGLIN_HUNTER, () -> ModEntityTypes.WITHER_SKELETON_HORSE);
    public static final Feature<NoneFeatureConfiguration> MOB_FEATURE_STRIDER = new MobFeature<>(() -> EntityType.STRIDER);
    public static final Feature<NoneFeatureConfiguration> MOB_FEATURE_WITHER_SKELETON = new MobFeature<>(() -> EntityType.WITHER_SKELETON);
    public static final Feature<NoneFeatureConfiguration> MOB_FEATURE_CATACOMB = new MobFeature<>(ModLists.CATACOMB_MOBS);
    public static final Feature<NoneFeatureConfiguration> MOB_FEATURE_WARPED_ENDERMAN = new MobFeature<>(() -> ModEntityTypes.WARPED_ENDERMAN);

    public static final Feature<ReplaceSphereConfiguration> SOUL_STONE_BLOBS = new ReplaceBlobsFeature(ReplaceSphereConfiguration.CODEC);

    public static void registerFeatures() {
        Registry.register(Registry.FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_piglin_prisoner"), MOB_FEATURE_PIGLIN_PRISONER);
        Registry.register(Registry.FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_piglin_manor_inside"), MOB_FEATURE_PIGLIN_MANOR_INSIDE);
        Registry.register(Registry.FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_piglin_manor_outside"), MOB_FEATURE_PIGLIN_MANOR_OUTSIDE);
        Registry.register(Registry.FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_strider"), MOB_FEATURE_STRIDER);
        Registry.register(Registry.FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_wither_skeleton"), MOB_FEATURE_WITHER_SKELETON);
        Registry.register(Registry.FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_catacomb"), MOB_FEATURE_CATACOMB);
        Registry.register(Registry.FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_warped_enderman"), MOB_FEATURE_WARPED_ENDERMAN);
        Registry.register(Registry.FEATURE, new ResourceLocation(BygoneNetherMod.MODID, "soul_stone_blobs"), SOUL_STONE_BLOBS);
    }
}
