package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.item.ModArmorItem;
import com.izofar.bygonenether.item.ModArmorMaterial;
import com.izofar.bygonenether.item.ModShieldItem;
import com.izofar.bygonenether.item.WarpedEnderpearlItem;
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

import java.util.function.Consumer;

public class ModItems {

    public static final Item PIGLIN_PRISONER_SPAWN_EGG = new SpawnEggItem(ModEntityTypes.PIGLIN_PRISONER, 0xc79e88, 0xf9f3a4, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item PIGLIN_HUNTER_SPAWN_EGG = new SpawnEggItem(ModEntityTypes.PIGLIN_HUNTER, 0xba6645, 0xf9f3a4, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WEX_SPAWN_EGG = new SpawnEggItem(ModEntityTypes.WEX, 0x7198c8, 0x2b4667, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WARPED_ENDERMAN_SPAWN_EGG = new SpawnEggItem(ModEntityTypes.WARPED_ENDERMAN, 0x0e8281, 0x000000, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WRAITHER_SPAWN_EGG = new SpawnEggItem(ModEntityTypes.WRAITHER, 0x273333, 0x474d4d, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item CORPOR_SPAWN_EGG = new SpawnEggItem(ModEntityTypes.CORPOR, 0x141414, 0x4a5757, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WITHER_SKELETON_KNIGHT_SPAWN_EGG = new SpawnEggItem(ModEntityTypes.WITHER_SKELETON_KNIGHT, 0x242424, 0x4e5252, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WITHER_SKELETON_HORSE_EGG = new SpawnEggItem(ModEntityTypes.WITHER_SKELETON_HORSE, 0x242424, 0x4d4747, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));

    public static final Item GILDED_NETHERITE_HELMET = new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.HEAD, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB).fireproof());
    public static final Item GILDED_NETHERITE_CHESTPLATE = new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.CHEST, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB).fireproof());
    public static final Item GILDED_NETHERITE_LEGGINGS = new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.LEGS, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB).fireproof());
    public static final Item GILDED_NETHERITE_BOOTS = new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.FEET, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB).fireproof());

    public static final Item COBBLED_BLACKSTONE = new BlockItem(ModBlocks.COBBLED_BLACKSTONE, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));

    public static final Item WITHERED_BLACKSTONE = new BlockItem(ModBlocks.WITHERED_BLACKSTONE, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WITHERED_BLACKSTONE_STAIRS = new BlockItem(ModBlocks.WITHERED_BLACKSTONE_STAIRS, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WITHERED_BLACKSTONE_SLAB = new BlockItem(ModBlocks.WITHERED_BLACKSTONE_SLAB, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));

    public static final Item CRACKED_WITHERED_BLACKSTONE = new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item CRACKED_WITHERED_BLACKSTONE_STAIRS = new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE_STAIRS, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item CRACKED_WITHERED_BLACKSTONE_SLAB = new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE_SLAB, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));

    public static final Item WITHERED_BASALT = new BlockItem(ModBlocks.WITHERED_BASALT, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WITHERED_COAL_BLOCK = new BlockItem(ModBlocks.WITHERED_COAL_BLOCK, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WITHERED_QUARTZ_BLOCK = new BlockItem(ModBlocks.WITHERED_QUARTZ_BLOCK, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));

    public static final Item CHISELED_WITHERED_BLACKSTONE = new BlockItem(ModBlocks.CHISELED_WITHERED_BLACKSTONE, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WITHERED_DEBRIS = new BlockItem(ModBlocks.WITHERED_DEBRIS, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item SOUL_STONE = new BlockItem(ModBlocks.SOUL_STONE, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));

    public static final Item WARPED_NETHER_BRICKS = new BlockItem(ModBlocks.WARPED_NETHER_BRICKS, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item CHISELED_WARPED_NETHER_BRICKS = new BlockItem(ModBlocks.CHISELED_WARPED_NETHER_BRICKS, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WARPED_NETHER_BRICK_STAIRS = new BlockItem(ModBlocks.WARPED_NETHER_BRICK_STAIRS, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));
    public static final Item WARPED_NETHER_BRICK_SLAB = new BlockItem(ModBlocks.WARPED_NETHER_BRICK_SLAB, new FabricItemSettings().group(ModCreativeModeTabs.MOD_TAB));

    public static final Item WITHER_WALTZ_MUSIC_DISC = new RecordItem(4, ModSounds.WITHER_WALTZ, new FabricItemSettings().maxCount(1).group(ModCreativeModeTabs.MOD_TAB), 5040);
    public static final Item WARPED_ENDER_PEARL = new WarpedEnderpearlItem(new FabricItemSettings().maxCount(16).rarity(Rarity.RARE).group(ModCreativeModeTabs.MOD_TAB));

    public static final Item NETHERITE_BELL = new BlockItem(ModBlocks.NETHERITE_BELL, new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB).rarity(Rarity.EPIC).fireResistant());
    public static final Item GILDED_NETHERITE_SHIELD = new ModShieldItem((new Item.Properties()).durability(1512).tab(ModCreativeModeTabs.MOD_TAB).rarity(Rarity.RARE).fireResistant());
    
    public static Item CRUSHED_WITHERED_DEBRIS;
    public static Item NETHERITE_SCRAP_NUGGET;
    public static Item NETHERITE_SCRAP_INGOT;
    
    public static void registerItems() {
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "piglin_prisoner_spawn_egg"), PIGLIN_PRISONER_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "piglin_hunter_spawn_egg"), PIGLIN_HUNTER_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "wex_spawn_egg"), WEX_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "warped_enderman_spawn_egg"), WARPED_ENDERMAN_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "wraither_spawn_egg"), WRAITHER_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "corpor_spawn_egg"), CORPOR_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "wither_skeleton_knight_spawn_egg"), WITHER_SKELETON_KNIGHT_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "wither_skeleton_horse_spawn_egg"), WITHER_SKELETON_HORSE_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "gilded_netherite_helmet"), GILDED_NETHERITE_HELMET);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "gilded_netherite_chestplate"), GILDED_NETHERITE_CHESTPLATE);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "gilded_netherite_leggings"), GILDED_NETHERITE_LEGGINGS);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "gilded_netherite_boots"), GILDED_NETHERITE_BOOTS);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "cobbled_blackstone"), COBBLED_BLACKSTONE);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "withered_blackstone"), WITHERED_BLACKSTONE);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "withered_blackstone_stairs"), WITHERED_BLACKSTONE_STAIRS);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "withered_blackstone_slab"), WITHERED_BLACKSTONE_SLAB);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "cracked_withered_blackstone"), CRACKED_WITHERED_BLACKSTONE);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "cracked_withered_blackstone_stairs"), CRACKED_WITHERED_BLACKSTONE_STAIRS);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "cracked_withered_blackstone_slab"), CRACKED_WITHERED_BLACKSTONE_SLAB);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "chiseled_withered_blackstone"), CHISELED_WITHERED_BLACKSTONE);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "withered_basalt"), WITHERED_BASALT);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "withered_coal_block"), WITHERED_COAL_BLOCK);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "withered_quartz_block"), WITHERED_QUARTZ_BLOCK);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "withered_debris"), WITHERED_DEBRIS);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "soul_stone"), SOUL_STONE);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "warped_nether_bricks"), WARPED_NETHER_BRICKS);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "chiseled_warped_nether_bricks"), CHISELED_WARPED_NETHER_BRICKS);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "warped_nether_brick_stairs"), WARPED_NETHER_BRICK_STAIRS);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "warped_nether_brick_slab"), WARPED_NETHER_BRICK_SLAB);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "wither_waltz_music_disc"), WITHER_WALTZ_MUSIC_DISC);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "warped_ender_pearl"), WARPED_ENDER_PEARL);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "netherite_bell"), NETHERITE_BELL);
        Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "gilded_netherite_shield"), GILDED_NETHERITE_SHIELD);

        registerModCompatibilityItems();
    }

    private static void registerModCompatibilityItems() {
        if (FabricLoader.getInstance().isModLoaded("create")) {
            CRUSHED_WITHERED_DEBRIS = new Item(new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB));
            NETHERITE_SCRAP_NUGGET = new Item(new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB));
            NETHERITE_SCRAP_INGOT = new Item(new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB));

            Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "crushed_withered_debris"), CRUSHED_WITHERED_DEBRIS);
            Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "netherite_scrap_nugget"), NETHERITE_SCRAP_NUGGET);
            Registry.register(Registry.ITEM, new ResourceLocation(BygoneNetherMod.MODID, "netherite_scrap_ingot"), NETHERITE_SCRAP_INGOT);
        }
    }
}
