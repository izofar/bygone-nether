package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.util.ModLists;
import com.izofar.bygonenether.world.feature.MobFeature;
import com.izofar.bygonenether.world.feature.MobPassengerFeature;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
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

	public static Holder<ConfiguredFeature<ReplaceSphereConfiguration, ?>> SOUL_STONE_BLOBS_CONFIGURED;
	public static Holder<PlacedFeature> SOUL_STONE_BLOBS_PLACED;

	public static void register(IEventBus eventBus) { MODDED_FEATURES.register(eventBus); }

	public static void registerPlacedFeatures() {
		SOUL_STONE_BLOBS_CONFIGURED = FeatureUtils.register("soul_stone_blobs", Feature.REPLACE_BLOBS, new ReplaceSphereConfiguration(Blocks.NETHERRACK.defaultBlockState(), ModBlocks.SOUL_STONE.get().defaultBlockState(), UniformInt.of(3, 7)));
		SOUL_STONE_BLOBS_PLACED = PlacementUtils.register("soul_stone_blobs", SOUL_STONE_BLOBS_CONFIGURED, CountPlacement.of(25), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());

	}
}
