package com.izofar.bygonenether.init;


import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;

public abstract class ModConfiguredFeatures {

	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_PIGLIN_PRISONER = ModFeatures.MOB_FEATURE_PIGLIN_PRISONER.get().configured(IFeatureConfig.NONE);
	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_PIGLIN_MANOR_INSIDE = ModFeatures.MOB_FEATURE_PIGLIN_MANOR_INSIDE.get().configured(IFeatureConfig.NONE);
	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_PIGLIN_MANOR_OUTSIDE = ModFeatures.MOB_FEATURE_PIGLIN_MANOR_OUTSIDE.get().configured(IFeatureConfig.NONE);
	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_STRIDER = ModFeatures.MOB_FEATURE_STRIDER.get().configured(IFeatureConfig.NONE);
	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_WITHER_SKELETON = ModFeatures.MOB_FEATURE_WITHER_SKELETON.get().configured(IFeatureConfig.NONE);
	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_WARPED_ENDERMAN = ModFeatures.MOB_FEATURE_WARPED_ENDERMAN.get().configured(IFeatureConfig.NONE);

	public static final ConfiguredFeature<?, ?> SOUL_STONE_BLOBS = Feature.REPLACE_BLOBS.configured(new BlobReplacementConfig(Blocks.NETHERRACK.defaultBlockState(), ModBlocks.SOUL_STONE.get().defaultBlockState(), FeatureSpread.of(3, 4))).range(128).squared().count(25);

	public static void registerConfiguredFeatures() {
		Registry<ConfiguredFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_FEATURE;
		
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_piglin_prisoner"), CONFIGURED_MOB_FEATURE_PIGLIN_PRISONER);
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_piglin_inside"), CONFIGURED_MOB_FEATURE_PIGLIN_MANOR_INSIDE);
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_piglin_outside"), CONFIGURED_MOB_FEATURE_PIGLIN_MANOR_OUTSIDE);
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_strider"), CONFIGURED_MOB_FEATURE_STRIDER);
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_wither_skeleton"), CONFIGURED_MOB_FEATURE_WITHER_SKELETON);
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_wwarped_enderman"), CONFIGURED_MOB_FEATURE_WARPED_ENDERMAN);

		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "soul_stone_blobs"), SOUL_STONE_BLOBS);
	}
}
