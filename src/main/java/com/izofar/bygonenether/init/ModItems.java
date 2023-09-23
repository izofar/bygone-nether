package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.item.ModShieldItem;
import com.izofar.bygonenether.item.WarpedEnderpearlItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

public class ModItems {

    public static final Item PIGLIN_PRISONER_SPAWN_EGG = register("piglin_prisoner_spawn_egg", new SpawnEggItem(ModEntityTypes.PIGLIN_PRISONER, 0xc79e88, 0xf9f3a4, new FabricItemSettings()));
    public static final Item PIGLIN_HUNTER_SPAWN_EGG = register("piglin_hunter_spawn_egg", new SpawnEggItem(ModEntityTypes.PIGLIN_HUNTER, 0xba6645, 0xf9f3a4, new FabricItemSettings()));
    public static final Item WEX_SPAWN_EGG = register("wex_spawn_egg", new SpawnEggItem(ModEntityTypes.WEX, 0x7198c8, 0x2b4667, new FabricItemSettings()));
    public static final Item WARPED_ENDERMAN_SPAWN_EGG = register("warped_enderman_spawn_egg", new SpawnEggItem(ModEntityTypes.WARPED_ENDERMAN, 0x0e8281, 0x000000, new FabricItemSettings()));
    public static final Item WRAITHER_SPAWN_EGG = register("wraither_spawn_egg", new SpawnEggItem(ModEntityTypes.WRAITHER, 0x273333, 0x474d4d, new FabricItemSettings()));
    public static final Item CORPOR_SPAWN_EGG = register("corpor_spawn_egg", new SpawnEggItem(ModEntityTypes.CORPOR, 0x141414, 0x4a5757, new FabricItemSettings()));
    public static final Item WITHER_SKELETON_KNIGHT_SPAWN_EGG = register("wither_skeleton_knight_spawn_egg", new SpawnEggItem(ModEntityTypes.WITHER_SKELETON_KNIGHT, 0x242424, 0x4e5252, new FabricItemSettings()));
    public static final Item WITHER_SKELETON_HORSE_EGG = register("wither_skeleton_horse_spawn_egg", new SpawnEggItem(ModEntityTypes.WITHER_SKELETON_HORSE, 0x242424, 0x4d4747, new FabricItemSettings()));

    public static final Item COBBLED_BLACKSTONE = register("cobbled_blackstone", new BlockItem(ModBlocks.COBBLED_BLACKSTONE, new FabricItemSettings()));

    public static final Item WITHERED_BLACKSTONE = register("withered_blackstone", new BlockItem(ModBlocks.WITHERED_BLACKSTONE, new FabricItemSettings()));
    public static final Item WITHERED_BLACKSTONE_STAIRS = register("withered_blackstone_stairs", new BlockItem(ModBlocks.WITHERED_BLACKSTONE_STAIRS, new FabricItemSettings()));
    public static final Item WITHERED_BLACKSTONE_SLAB = register("withered_blackstone_slab", new BlockItem(ModBlocks.WITHERED_BLACKSTONE_SLAB, new FabricItemSettings()));

    public static final Item CRACKED_WITHERED_BLACKSTONE = register("cracked_withered_blackstone", new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE, new FabricItemSettings()));
    public static final Item CRACKED_WITHERED_BLACKSTONE_STAIRS = register("cracked_withered_blackstone_stairs", new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE_STAIRS, new FabricItemSettings()));
    public static final Item CRACKED_WITHERED_BLACKSTONE_SLAB = register("cracked_withered_blackstone_slab", new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE_SLAB, new FabricItemSettings()));

    public static final Item WITHERED_BASALT = register("withered_basalt", new BlockItem(ModBlocks.WITHERED_BASALT, new FabricItemSettings()));
    public static final Item WITHERED_COAL_BLOCK = register("withered_coal_block", new BlockItem(ModBlocks.WITHERED_COAL_BLOCK, new FabricItemSettings()));
    public static final Item WITHERED_QUARTZ_BLOCK = register("withered_quartz_block", new BlockItem(ModBlocks.WITHERED_QUARTZ_BLOCK, new FabricItemSettings()));

    public static final Item CHISELED_WITHERED_BLACKSTONE = register("chiseled_withered_blackstone", new BlockItem(ModBlocks.CHISELED_WITHERED_BLACKSTONE, new FabricItemSettings()));
    public static final Item WITHERED_DEBRIS = register("withered_debris", new BlockItem(ModBlocks.WITHERED_DEBRIS, new FabricItemSettings()));
    public static final Item SOUL_STONE = register("soul_stone", new BlockItem(ModBlocks.SOUL_STONE, new FabricItemSettings()));

    public static final Item WARPED_NETHER_BRICKS = register("warped_nether_bricks", new BlockItem(ModBlocks.WARPED_NETHER_BRICKS, new FabricItemSettings()));
    public static final Item CHISELED_WARPED_NETHER_BRICKS = register("chiseled_warped_nether_bricks", new BlockItem(ModBlocks.CHISELED_WARPED_NETHER_BRICKS, new FabricItemSettings()));
    public static final Item WARPED_NETHER_BRICK_STAIRS = register("warped_nether_brick_stairs", new BlockItem(ModBlocks.WARPED_NETHER_BRICK_STAIRS, new FabricItemSettings()));
    public static final Item WARPED_NETHER_BRICK_SLAB = register("warped_nether_brick_slab", new BlockItem(ModBlocks.WARPED_NETHER_BRICK_SLAB, new FabricItemSettings()));

    public static final Item WITHER_WALTZ_MUSIC_DISC = register("wither_waltz_music_disc", new RecordItem(4, ModSounds.WITHER_WALTZ, new FabricItemSettings().maxCount(1), 5040));
    public static final Item WARPED_ENDER_PEARL = register("warped_ender_pearl", new WarpedEnderpearlItem(new FabricItemSettings().maxCount(16).rarity(Rarity.RARE)));

    public static final Item NETHERITE_BELL = register("netherite_bell", new BlockItem(ModBlocks.NETHERITE_BELL, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final Item GILDED_NETHERITE_SHIELD = register("gilded_netherite_shield", new ModShieldItem((new Item.Properties()).durability(1512).rarity(Rarity.RARE).fireResistant()));
    
    public static Item CRUSHED_WITHERED_DEBRIS;
    public static Item NETHERITE_SCRAP_NUGGET;
    public static Item NETHERITE_SCRAP_INGOT;

    private static Item register(String name, Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(BygoneNetherMod.MODID, name), item);
        ItemGroupEvents.modifyEntriesEvent(ModCreativeModeTabs.MOD_TAB).register(content -> content.accept(item));
        return item;
    }

    public static void registerItems() {
        registerModCompatibilityItems();
    }

    private static void registerModCompatibilityItems() {
        if (FabricLoader.getInstance().isModLoaded("create")) {
            CRUSHED_WITHERED_DEBRIS = register("crushed_withered_debris", new Item(new Item.Properties()));
            NETHERITE_SCRAP_NUGGET = register("netherite_scrap_nugget", new Item(new Item.Properties()));
            NETHERITE_SCRAP_INGOT = register("netherite_scrap_ingot", new Item(new Item.Properties()));
        }
    }
}
