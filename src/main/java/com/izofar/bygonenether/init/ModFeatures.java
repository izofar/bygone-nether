package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.util.ModLists;
import com.izofar.bygonenether.world.feature.MobFeature;
import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ModFeatures {

	public static final DeferredRegister<Feature<?>> MODDED_FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, BygoneNetherMod.MODID);

	public static final RegistryObject<Feature<NoFeatureConfig>> MOB_FEATURE_PIGLIN_PRISONER = MODDED_FEATURES.register("mob_feature_piglin_prisoner", () -> new MobFeature<>(ModEntityTypes.PIGLIN_PRISONER.get()));
	public static final RegistryObject<Feature<NoFeatureConfig>> MOB_FEATURE_PIGLIN_MANOR = MODDED_FEATURES.register("mob_feature_piglin_manor", () -> new MobFeature<>(() -> ModLists.PIGLIN_MANOR_MOBS));
	public static final RegistryObject<Feature<NoFeatureConfig>> MOB_FEATURE_WITHER_SKELETON = MODDED_FEATURES.register("mob_feature_wither_skeleton", () -> new MobFeature<>(EntityType.WITHER_SKELETON));
	public static final RegistryObject<Feature<NoFeatureConfig>> MOB_FEATURE_WARPED_ENDERMAN = MODDED_FEATURES.register("mob_feature_warped_enderman", () -> new MobFeature<>(ModEntityTypes.WARPED_ENDERMAN.get()));

	//public static final RegistryObject<? extends Feature<?>> SOUL_STONE_BLOBS = MODDED_FEATURES.register("soul_stone_blobs",  ModConfiguredFeatures.SOUL_STONE_BLOBS::feature);

	public static void register(IEventBus eventBus) { MODDED_FEATURES.register(eventBus); }

}
