package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.entity.*;
import com.izofar.bygonenether.util.ModLists;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

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

    public static final BlockEntityType<NetheriteBellBlockEntity> NETHERITE_BELL = FabricBlockEntityTypeBuilder.create(NetheriteBellBlockEntity::new, ModBlocks.NETHERITE_BELL).build(Util.fetchChoiceType(References.BLOCK_ENTITY, new ResourceLocation(BygoneNetherMod.MODID, "netherite_bell").toString()));

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
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(BygoneNetherMod.MODID, "netherite_bell"), NETHERITE_BELL);
    }

    public static void modifyPiglinMemoryAndSensors() {
        PiglinBrute.SENSOR_TYPES = ModLists.PIGLIN_BRUTE_SENSOR_TYPES;
        PiglinBrute.MEMORY_TYPES = ModLists.PIGLIN_BRUTE_MEMORY_TYPES;
    }
}
