package com.izofar.bygonenether.init;


import com.izofar.bygonenether.BygoneNetherMod;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;

public abstract class ModConfiguredFeatures {

	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_PIGLIN_PRISONER = ModFeatures.MOB_FEATURE_PIGLIN_PRISONER.get().configured(NoneFeatureConfiguration.INSTANCE);
	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_PIGLIN_MANOR = ModFeatures.MOB_FEATURE_PIGLIN_MANOR.get().configured(NoneFeatureConfiguration.INSTANCE);
	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_STRIDER = ModFeatures.MOB_FEATURE_STRIDER.get().configured(NoneFeatureConfiguration.INSTANCE);
	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_WITHER_SKELETON = ModFeatures.MOB_FEATURE_WITHER_SKELETON.get().configured(NoneFeatureConfiguration.INSTANCE);
	public static final ConfiguredFeature<?, ?> CONFIGURED_MOB_FEATURE_WARPED_ENDERMAN = ModFeatures.MOB_FEATURE_WARPED_ENDERMAN.get().configured(NoneFeatureConfiguration.INSTANCE);

	public static final ConfiguredFeature<ReplaceSphereConfiguration, ?> SOUL_STONE_BLOBS = Feature.REPLACE_BLOBS.configured(new ReplaceSphereConfiguration(Blocks.NETHERRACK.defaultBlockState(), ModBlocks.SOUL_STONE.get().defaultBlockState(), UniformInt.of(3, 7)));

	public static void registerConfiguredFeatures() {
		Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;
		
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_piglin_prisoner"), CONFIGURED_MOB_FEATURE_PIGLIN_PRISONER);
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_piglin_manor"), CONFIGURED_MOB_FEATURE_PIGLIN_MANOR);
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_strider"), CONFIGURED_MOB_FEATURE_STRIDER);
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_wither_skeleton"), CONFIGURED_MOB_FEATURE_WITHER_SKELETON);
		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "mob_feature_wwarped_enderman"), CONFIGURED_MOB_FEATURE_WARPED_ENDERMAN);

		Registry.register(registry, new ResourceLocation(BygoneNetherMod.MODID, "soul_stone_blobs"), SOUL_STONE_BLOBS);
	}
}
