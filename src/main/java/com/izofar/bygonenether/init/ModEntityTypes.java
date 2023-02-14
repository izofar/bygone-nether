package com.izofar.bygonenether.init;

import com.google.common.collect.ImmutableList;
import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.block.Blocks;

public class ModEntityTypes {
    public static final EntityType<Wex> WEX = FabricEntityTypeBuilder.create(MobCategory.MONSTER, Wex::new).dimensions(EntityDimensions.scalable(0.4F, 0.8F)).fireImmune().build();
    public static final EntityType<WarpedEnderMan> WARPED_ENDERMAN = FabricEntityTypeBuilder.create(MobCategory.MONSTER, WarpedEnderMan::new).dimensions(EntityDimensions.scalable(0.6F, 2.9F)).build();

    public static final EntityType<PiglinPrisoner> PIGLIN_PRISONER = FabricEntityTypeBuilder.create(MobCategory.MONSTER, PiglinPrisoner::new).dimensions(EntityDimensions.scalable(0.6F, 1.95F)).build();
    public static final EntityType<PiglinHunter> PIGLIN_HUNTER = FabricEntityTypeBuilder.create(MobCategory.MONSTER, PiglinHunter::new).dimensions(EntityDimensions.scalable(0.6F, 1.95F)).build();

    public static final EntityType<Wraither> WRAITHER = FabricEntityTypeBuilder.create(MobCategory.MONSTER, Wraither::new).dimensions(EntityDimensions.scalable(0.7F, 2.4F)).fireImmune().specificSpawnBlocks(Blocks.WITHER_ROSE).build();
    public static final EntityType<WitherSkeletonKnight> WITHER_SKELETON_KNIGHT = FabricEntityTypeBuilder.create(MobCategory.MONSTER, WitherSkeletonKnight::new).dimensions(EntityDimensions.scalable(0.7F, 2.4F)).fireImmune().specificSpawnBlocks(Blocks.WITHER_ROSE).build();
    public static final EntityType<Corpor> CORPOR = FabricEntityTypeBuilder.create(MobCategory.MONSTER, Corpor::new).dimensions(EntityDimensions.scalable(0.7F, 2.4F)).specificSpawnBlocks(Blocks.WITHER_ROSE).fireImmune().build();

    public static final EntityType<WitherSkeletonHorse> WITHER_SKELETON_HORSE = FabricEntityTypeBuilder.create(MobCategory.CREATURE, WitherSkeletonHorse::new).dimensions(EntityDimensions.scalable(1.3964844F, 1.6F)).fireImmune().build();

    public static final EntityType<ThrownWarpedEnderpearl> WARPED_ENDER_PEARL = FabricEntityTypeBuilder.<ThrownWarpedEnderpearl>create(MobCategory.MISC, ThrownWarpedEnderpearl::new).dimensions(EntityDimensions.scalable(0.25F, 0.25F)).build();

    public static void registerEntityTypes() {
        FabricDefaultAttributeRegistry.register(WEX, Wex.createAttributes());
        FabricDefaultAttributeRegistry.register(WARPED_ENDERMAN, WarpedEnderMan.createAttributes());
        FabricDefaultAttributeRegistry.register(PIGLIN_PRISONER, PiglinPrisoner.createAttributes());
        FabricDefaultAttributeRegistry.register(PIGLIN_HUNTER, PiglinHunter.createAttributes());
        FabricDefaultAttributeRegistry.register(WRAITHER, Wraither.createAttributes());
        FabricDefaultAttributeRegistry.register(WITHER_SKELETON_KNIGHT, WitherSkeletonKnight.createAttributes());
        FabricDefaultAttributeRegistry.register(CORPOR, Corpor.createAttributes());
        FabricDefaultAttributeRegistry.register(WITHER_SKELETON_HORSE, WitherSkeletonHorse.createAttributes());
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "wex"), WEX);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "warped_enderman"), WARPED_ENDERMAN);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "piglin_prisoner"), PIGLIN_PRISONER);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "piglin_hunter"), PIGLIN_HUNTER);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "wraither"), WRAITHER);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "wither_skeleton_knight"), WITHER_SKELETON_KNIGHT);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "corpor"), CORPOR);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "wither_skeleton_horse"), WITHER_SKELETON_HORSE);
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "warped_ender_pearl"), WARPED_ENDER_PEARL);
    }

    public static void modifyPiglinMemoryAndSensors() {
        PiglinBrute.SENSOR_TYPES = ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_ITEMS,
                SensorType.HURT_BY,
                ModSensorTypes.PIGLIN_BRUTE_SPECIFIC_SENSOR
        );

        PiglinBrute.MEMORY_TYPES = ImmutableList.of(
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.DOORS_TO_CLOSE,
                MemoryModuleType.NEAREST_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
                MemoryModuleType.NEARBY_ADULT_PIGLINS,
                MemoryModuleType.HURT_BY,
                MemoryModuleType.HURT_BY_ENTITY,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                MemoryModuleType.INTERACTION_TARGET,
                MemoryModuleType.PATH,
                MemoryModuleType.ANGRY_AT,
                MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
                MemoryModuleType.HOME,
                MemoryModuleType.UNIVERSAL_ANGER,
                ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD
        );
    }
}
