package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.item.ModArmorItem;
import com.izofar.bygonenether.item.ModArmorMaterial;
import com.izofar.bygonenether.item.ModShieldItem;
import com.izofar.bygonenether.item.WarpedEnderpearlItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ModItems {

	public static final DeferredRegister<Item> MODDED_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BygoneNetherMod.MODID);

	public static final RegistryObject<Item> PIGLIN_PRISONER_SPAWN_EGG = MODDED_ITEMS.register("piglin_prisoner_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.PIGLIN_PRISONER, 0xc79e88, 16380836, (new Item.Properties()).tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> PIGLIN_HUNTER_SPAWN_EGG = MODDED_ITEMS.register("piglin_hunter_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.PIGLIN_HUNTER, 0xba6645, 16380836, (new Item.Properties()).tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WEX_SPAWN_EGG = MODDED_ITEMS.register("wex_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WEX, 0x7198c8, 0x2b4667, (new Item.Properties()).tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WARPED_ENDERMAN_SPAWN_EGG = MODDED_ITEMS.register("warped_enderman_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WARPED_ENDERMAN, 0x0e8281, 0x0, (new Item.Properties()).tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WRAITHER_SPAWN_EGG = MODDED_ITEMS.register("wraither_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WRAITHER, 0x273333, 0x474d4d, (new Item.Properties()).tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> CORPOR_SPAWN_EGG = MODDED_ITEMS.register("corpor_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.CORPOR, 0x141414, 0x4a5757, (new Item.Properties()).tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WITHER_SKELETON_KNIGHT_SPAWN_EGG = MODDED_ITEMS.register("wither_skeleton_knight_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WITHER_SKELETON_KNIGHT, 0x242424, 0x4e5252, (new Item.Properties()).tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WITHER_SKELETON_HORSE_SPAWN_EGG = MODDED_ITEMS.register("wither_skeleton_horse_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WITHER_SKELETON_HORSE, 0x242424, 0x4d4747, (new Item.Properties()).tab(ModItemGroups.MOD_TAB)));

	public static final RegistryObject<Item> GILDED_NETHERITE_HELMET = MODDED_ITEMS.register("gilded_netherite_helmet", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlotType.HEAD, (new Item.Properties()).tab(ModItemGroups.MOD_TAB).fireResistant()));
	public static final RegistryObject<Item> GILDED_NETHERITE_CHESTPLATE = MODDED_ITEMS.register("gilded_netherite_chestplate", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlotType.CHEST, (new Item.Properties()).tab(ModItemGroups.MOD_TAB).fireResistant()));
	public static final RegistryObject<Item> GILDED_NETHERITE_LEGGINGS = MODDED_ITEMS.register("gilded_netherite_leggings", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlotType.LEGS, (new Item.Properties()).tab(ModItemGroups.MOD_TAB).fireResistant()));
	public static final RegistryObject<Item> GILDED_NETHERITE_BOOTS = MODDED_ITEMS.register("gilded_netherite_boots", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlotType.FEET, (new Item.Properties()).tab(ModItemGroups.MOD_TAB).fireResistant()));

	public static final RegistryObject<Item> COBBLED_BLACKSTONE = MODDED_ITEMS.register("cobbled_blackstone", () -> new BlockItem(ModBlocks.COBBLED_BLACKSTONE.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_BLACKSTONE = MODDED_ITEMS.register("withered_blackstone", () -> new BlockItem(ModBlocks.WITHERED_BLACKSTONE.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_BLACKSTONE_STAIRS = MODDED_ITEMS.register("withered_blackstone_stairs", () -> new BlockItem(ModBlocks.WITHERED_BLACKSTONE_STAIRS.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_BLACKSTONE_SLAB = MODDED_ITEMS.register("withered_blackstone_slab", () -> new BlockItem(ModBlocks.WITHERED_BLACKSTONE_SLAB.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> CRACKED_WITHERED_BLACKSTONE_STAIRS = MODDED_ITEMS.register("cracked_withered_blackstone_stairs", () -> new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE_STAIRS.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> CRACKED_WITHERED_BLACKSTONE_SLAB = MODDED_ITEMS.register("cracked_withered_blackstone_slab", () -> new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE_SLAB.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> CRACKED_WITHERED_BLACKSTONE = MODDED_ITEMS.register("cracked_withered_blackstone", () -> new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> CHISELED_WITHERED_BLACKSTONE = MODDED_ITEMS.register("chiseled_withered_blackstone", () -> new BlockItem(ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_BASALT = MODDED_ITEMS.register("withered_basalt", () -> new BlockItem(ModBlocks.WITHERED_BASALT.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_COAL_BLACK = MODDED_ITEMS.register("withered_coal_block", () -> new BlockItem(ModBlocks.WITHERED_COAL_BLOCK.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_QUARTZ_BLOCK = MODDED_ITEMS.register("withered_quartz_block", () -> new BlockItem(ModBlocks.WITHERED_QUARTZ_BLOCK.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_DEBRIS = MODDED_ITEMS.register("withered_debris", () -> new BlockItem(ModBlocks.WITHERED_DEBRIS.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> SOUL_STONE = MODDED_ITEMS.register("soul_stone", () -> new BlockItem(ModBlocks.SOUL_STONE.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WARPED_NETHER_BRICKS = MODDED_ITEMS.register("warped_nether_bricks", () -> new BlockItem(ModBlocks.WARPED_NETHER_BRICKS.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> CHISELED_WARPED_NETHER_BRICKS = MODDED_ITEMS.register("chiseled_warped_nether_bricks", () -> new BlockItem(ModBlocks.CHISELED_WARPED_NETHER_BRICKS.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WARPED_NETHER_BRICK_STAIRS = MODDED_ITEMS.register("warped_nether_brick_stairs", () -> new BlockItem(ModBlocks.WARPED_NETHER_BRICK_STAIRS.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WARPED_NETHER_BRICK_SLAB = MODDED_ITEMS.register("warped_nether_brick_slab", () -> new BlockItem(ModBlocks.WARPED_NETHER_BRICK_SLAB.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
	public static final RegistryObject<Item> WITHER_WALTZ_MUSIC_DISC = MODDED_ITEMS.register("wither_waltz_music_disc", () -> new MusicDiscItem(4, ModSounds.WITHER_WALTZ, new Item.Properties().tab(ModItemGroups.MOD_TAB).stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> WARPED_ENDER_PEARL = MODDED_ITEMS.register("warped_ender_pearl", () -> new WarpedEnderpearlItem((new Item.Properties()).stacksTo(16).tab(ModItemGroups.MOD_TAB).rarity(Rarity.RARE)));

	public static final RegistryObject<Item> NETHERITE_BELL = MODDED_ITEMS.register("netherite_bell", () -> new BlockItem(ModBlocks.NETHERITE_BELL.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB).rarity(Rarity.EPIC).fireResistant()));
	public static final RegistryObject<Item> GILDED_NETHERITE_SHIELD = MODDED_ITEMS.register("gilded_netherite_shield", () -> new ModShieldItem((new Item.Properties()).durability(1512).tab(ModItemGroups.MOD_TAB).rarity(Rarity.RARE).fireResistant()));

	public static RegistryObject<Item> CRUSHED_WITHERED_DEBRIS;
	public static RegistryObject<Item> NETHERITE_SCRAP_NUGGET;
	public static RegistryObject<Item> NETHERITE_SCRAP_INGOT;

	public static RegistryObject<Item> WITHERED_BLACKSTONE_VERTICAL_SLAB;
	public static RegistryObject<Item> CRACKED_WITHERED_BLACKSTONE_VERTICAL_SLAB;
	public static RegistryObject<Item> WARPED_NETHER_BRICK_VERTICAL_SLAB;

	public static void register(IEventBus eventBus) {
		registerModCompatibilityItems();
		MODDED_ITEMS.register(eventBus);
	}

	private static void registerModCompatibilityItems() {
		if (ModList.get().isLoaded("create")) {
			CRUSHED_WITHERED_DEBRIS = MODDED_ITEMS.register("crushed_withered_debris", () -> new Item((new Item.Properties()).tab(ModItemGroups.MOD_TAB)));
			NETHERITE_SCRAP_NUGGET = MODDED_ITEMS.register("netherite_scrap_nugget", () -> new Item((new Item.Properties()).tab(ModItemGroups.MOD_TAB)));
			NETHERITE_SCRAP_INGOT = MODDED_ITEMS.register("netherite_scrap_ingot", () -> new Item((new Item.Properties()).tab(ModItemGroups.MOD_TAB)));
		}
		if (ModList.get().isLoaded("quark")) {
			WITHERED_BLACKSTONE_VERTICAL_SLAB = MODDED_ITEMS.register("withered_blackstone_vertical_slab", () -> new BlockItem(ModBlocks.WITHERED_BLACKSTONE_VERTICAL_SLAB.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
			CRACKED_WITHERED_BLACKSTONE_VERTICAL_SLAB = MODDED_ITEMS.register("cracked_withered_blackstone_vertical_slab", () -> new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE_VERTICAL_SLAB.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
			WARPED_NETHER_BRICK_VERTICAL_SLAB = MODDED_ITEMS.register("warped_nether_brick_vertical_slab", () -> new BlockItem(ModBlocks.WARPED_NETHER_BRICK_VERTICAL_SLAB.get(), new Item.Properties().tab(ModItemGroups.MOD_TAB)));
		}
	}

}
