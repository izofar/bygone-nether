package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.util.ModLists;
import com.izofar.bygonenether.world.feature.MobFeature;
import com.izofar.bygonenether.world.feature.MobPassengerFeature;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.ReplaceBlobsFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ModFeatures {

	public static final DeferredRegister<Feature<?>> MODDED_FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, BygoneNetherMod.MODID);

	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_PIGLIN_PRISONER = MODDED_FEATURES.register("mob_feature_piglin_prisoner", () -> new MobFeature<>(ModEntityTypes.PIGLIN_PRISONER::get));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_PIGLIN_MANOR_INSIDE = MODDED_FEATURES.register("mob_feature_piglin_manor_inside", () -> new MobFeature<>(ModLists.PIGLIN_MANOR_MOBS));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_PIGLIN_MANOR_OUTSIDE = MODDED_FEATURES.register("mob_feature_piglin_manor_outside", () -> new MobPassengerFeature<>(ModEntityTypes.PIGLIN_HUNTER::get, ModEntityTypes.WITHER_SKELETON_HORSE::get));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_STRIDER = MODDED_FEATURES.register("mob_feature_strider", () -> new MobFeature<>(() -> EntityType.STRIDER));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_WITHER_SKELETON = MODDED_FEATURES.register("mob_feature_wither_skeleton", () -> new MobFeature<>(() -> EntityType.WITHER_SKELETON));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_CATACOMB = MODDED_FEATURES.register("mob_feature_catacomb", () -> new MobFeature<>(ModLists.CATACOMB_MOBS));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_WARPED_ENDERMAN = MODDED_FEATURES.register("mob_feature_warped_enderman", () -> new MobFeature<>(ModEntityTypes.WARPED_ENDERMAN::get));

	public static void register(IEventBus eventBus) { MODDED_FEATURES.register(eventBus); }

}
