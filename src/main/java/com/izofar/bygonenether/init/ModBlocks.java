package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class ModBlocks {
    public static final Block COBBLED_BLACKSTONE = new Block(FabricBlockSettings.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0f, 6.0f));

    public static final Block WITHERED_BLACKSTONE = new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 1200.0F).sound(SoundType.DEEPSLATE));
    public static final Block WITHERED_BLACKSTONE_STAIRS = new StairBlock(WITHERED_BLACKSTONE.defaultBlockState(), FabricBlockSettings.copy(WITHERED_BLACKSTONE));
    public static final Block WITHERED_BLACKSTONE_SLAB = new SlabBlock(FabricBlockSettings.copy(WITHERED_BLACKSTONE));

    public static final Block CRACKED_WITHERED_BLACKSTONE = new Block(FabricBlockSettings.copy(WITHERED_BLACKSTONE));
    public static final Block CRACKED_WITHERED_BLACKSTONE_STAIRS = new StairBlock(WITHERED_BLACKSTONE.defaultBlockState(), FabricBlockSettings.copy(WITHERED_BLACKSTONE));
    public static final Block CRACKED_WITHERED_BLACKSTONE_SLAB = new SlabBlock(FabricBlockSettings.copy(WITHERED_BLACKSTONE));

    public static final Block CHISELED_WITHERED_BLACKSTONE = new Block(FabricBlockSettings.copy(WITHERED_BLACKSTONE));

    public static final Block WITHERED_BASALT = new Block(FabricBlockSettings.copy(Blocks.BASALT));
    public static final Block WITHERED_COAL_BLOCK = new Block(FabricBlockSettings.copy(Blocks.COAL_BLOCK));
    public static final Block WITHERED_QUARTZ_BLOCK = new Block(FabricBlockSettings.copy(Blocks.QUARTZ_BLOCK));

    public static final Block WITHERED_DEBRIS = new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(30.0F, 1200.0F).sound(SoundType.ANCIENT_DEBRIS));
    public static final Block SOUL_STONE = new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.5F, 6.0F));

    public static final Block WARPED_NETHER_BRICKS = new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.NETHER).requiresCorrectToolForDrops().strength(2.0f, 6.0f).sound(SoundType.NETHER_BRICKS));
    public static final Block CHISELED_WARPED_NETHER_BRICKS = new Block(FabricBlockSettings.copy(WARPED_NETHER_BRICKS));
    public static final Block WARPED_NETHER_BRICK_STAIRS = new StairBlock(WARPED_NETHER_BRICKS.defaultBlockState(), FabricBlockSettings.copy(WARPED_NETHER_BRICKS));
    public static final Block WARPED_NETHER_BRICK_SLAB = new SlabBlock(FabricBlockSettings.copy(WARPED_NETHER_BRICKS));

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "cobbled_blackstone"), COBBLED_BLACKSTONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "withered_blackstone"), WITHERED_BLACKSTONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "withered_blackstone_stairs"), WITHERED_BLACKSTONE_STAIRS);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "withered_blackstone_slab"), WITHERED_BLACKSTONE_SLAB);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "cracked_withered_blackstone"), CRACKED_WITHERED_BLACKSTONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "cracked_withered_blackstone_stairs"), CRACKED_WITHERED_BLACKSTONE_STAIRS);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "cracked_withered_blackstone_slab"), CRACKED_WITHERED_BLACKSTONE_SLAB);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "chiseled_withered_blackstone"), CHISELED_WITHERED_BLACKSTONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "withered_basalt"), WITHERED_BASALT);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "withered_coal_block"), WITHERED_COAL_BLOCK);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "withered_quartz_block"), WITHERED_QUARTZ_BLOCK);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "withered_debris"), WITHERED_DEBRIS);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "soul_stone"), SOUL_STONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "warped_nether_bricks"), WARPED_NETHER_BRICKS);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "chiseled_warped_nether_bricks"), CHISELED_WARPED_NETHER_BRICKS);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "warped_nether_brick_stairs"), WARPED_NETHER_BRICK_STAIRS);
        Registry.register(Registry.BLOCK, new ResourceLocation(BygoneNetherMod.MODID, "warped_nether_brick_slab"), WARPED_NETHER_BRICK_SLAB);
    }
}
