package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.entity.PiglinPrisoner;
import com.izofar.bygonenether.entity.WarpedEnderMan;
import com.izofar.bygonenether.util.ModLists;
import com.izofar.bygonenether.world.feature.MobFeature;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ModFeatures {

	public static final DeferredRegister<Feature<?>> MODDED_FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, BygoneNetherMod.MODID);

	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_PIGLIN_PRISONER = MODDED_FEATURES.register("mob_feature_piglin_prisoner", () -> new MobFeature<PiglinPrisoner>(ModEntityTypes.PIGLIN_PRISONER.get()));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_PIGLIN_MANOR = MODDED_FEATURES.register("mob_feature_piglin_manor", () -> new MobFeature<AbstractPiglin>(() -> ModLists.PIGLIN_MANOR_MOBS));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_WITHER_SKELETON = MODDED_FEATURES.register("mob_feature_wither_skeleton", () -> new MobFeature<WitherSkeleton>(EntityType.WITHER_SKELETON));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MOB_FEATURE_WARPED_ENDERMAN = MODDED_FEATURES.register("mob_feature_warped_enderman", () -> new MobFeature<WarpedEnderMan>(ModEntityTypes.WARPED_ENDERMAN.get()));
	
	public static void register(IEventBus eventBus) { MODDED_FEATURES.register(eventBus); }
	
}
