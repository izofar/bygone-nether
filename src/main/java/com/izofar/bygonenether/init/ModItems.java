package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.item.ModArmorItem;
import com.izofar.bygonenether.item.ModArmorMaterial;
import com.izofar.bygonenether.item.ModShieldItem;
import com.izofar.bygonenether.item.WarpedEnderpearlItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ModItems {

	public static final DeferredRegister<Item> MODDED_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BygoneNetherMod.MODID);

	public static final RegistryObject<Item> PIGLIN_PRISONER_SPAWN_EGG = MODDED_ITEMS.register("piglin_prisoner_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.PIGLIN_PRISONER, 0xc79e88, 0xf9f3a4, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> PIGLIN_HUNTER_SPAWN_EGG = MODDED_ITEMS.register("piglin_hunter_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.PIGLIN_HUNTER, 0xba6645, 0xf9f3a4, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WEX_SPAWN_EGG = MODDED_ITEMS.register("wex_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WEX, 0x7198c8, 0x2b4667, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WARPED_ENDERMAN_SPAWN_EGG = MODDED_ITEMS.register("warped_enderman_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WARPED_ENDERMAN, 0x0e8281, 0x000000, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WRAITHER_SPAWN_EGG = MODDED_ITEMS.register("wraither_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WRAITHER, 0x273333, 0x474d4d, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> CORPOR_SPAWN_EGG = MODDED_ITEMS.register("corpor_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.CORPOR, 0x141414, 0x4a5757, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WITHER_SKELETON_KNIGHT_SPAWN_EGG = MODDED_ITEMS.register("wither_skeleton_knight_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WITHER_SKELETON_KNIGHT, 0x242424, 0x4e5252, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WITHER_SKELETON_HORSE_SPAWN_EGG = MODDED_ITEMS.register("wither_skeleton_horse_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WITHER_SKELETON_HORSE, 0x242424, 0x4d4747, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB)));

	public static final RegistryObject<Item> GILDED_NETHERITE_HELMET = MODDED_ITEMS.register("gilded_netherite_helmet", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.HEAD, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB).fireResistant()));
	public static final RegistryObject<Item> GILDED_NETHERITE_CHESTPLATE = MODDED_ITEMS.register("gilded_netherite_chestplate", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.CHEST, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB).fireResistant()));
	public static final RegistryObject<Item> GILDED_NETHERITE_LEGGINGS = MODDED_ITEMS.register("gilded_netherite_leggings", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.LEGS, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB).fireResistant()));
	public static final RegistryObject<Item> GILDED_NETHERITE_BOOTS = MODDED_ITEMS.register("gilded_netherite_boots", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.FEET, (new Item.Properties()).tab(ModCreativeModeTabs.MOD_TAB).fireResistant()));

	public static final RegistryObject<Item> COBBLED_BLACKSTONE = MODDED_ITEMS.register("cobbled_blackstone", () -> new BlockItem(ModBlocks.COBBLED_BLACKSTONE.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));

	public static final RegistryObject<Item> WITHERED_BLACKSTONE = MODDED_ITEMS.register("withered_blackstone", () -> new BlockItem(ModBlocks.WITHERED_BLACKSTONE.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_BLACKSTONE_STAIRS = MODDED_ITEMS.register("withered_blackstone_stairs", () -> new BlockItem(ModBlocks.WITHERED_BLACKSTONE_STAIRS.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_BLACKSTONE_SLAB = MODDED_ITEMS.register("withered_blackstone_slab", () -> new BlockItem(ModBlocks.WITHERED_BLACKSTONE_SLAB.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));

	public static final RegistryObject<Item> CRACKED_WITHERED_BLACKSTONE = MODDED_ITEMS.register("cracked_withered_blackstone", () -> new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> CRACKED_WITHERED_BLACKSTONE_STAIRS = MODDED_ITEMS.register("cracked_withered_blackstone_stairs", () -> new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE_STAIRS.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> CRACKED_WITHERED_BLACKSTONE_SLAB = MODDED_ITEMS.register("cracked_withered_blackstone_slab", () -> new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE_SLAB.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));

	public static final RegistryObject<Item> WITHERED_BASALT = MODDED_ITEMS.register("withered_basalt", () -> new BlockItem(ModBlocks.WITHERED_BASALT.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_COAL_BLACK = MODDED_ITEMS.register("withered_coal_block", () -> new BlockItem(ModBlocks.WITHERED_COAL_BLOCK.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_QUARTZ_BLOCK = MODDED_ITEMS.register("withered_quartz_block", () -> new BlockItem(ModBlocks.WITHERED_QUARTZ_BLOCK.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));


	public static final RegistryObject<Item> CHISELED_WITHERED_BLACKSTONE = MODDED_ITEMS.register("chiseled_withered_blackstone", () -> new BlockItem(ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WITHERED_DEBRIS = MODDED_ITEMS.register("withered_debris", () -> new BlockItem(ModBlocks.WITHERED_DEBRIS.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> SOUL_STONE = MODDED_ITEMS.register("soul_stone", () -> new BlockItem(ModBlocks.SOUL_STONE.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));

	public static final RegistryObject<Item> WARPED_NETHER_BRICKS = MODDED_ITEMS.register("warped_nether_bricks", () -> new BlockItem(ModBlocks.WARPED_NETHER_BRICKS.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> CHISELED_WARPED_NETHER_BRICKS = MODDED_ITEMS.register("chiseled_warped_nether_bricks", () -> new BlockItem(ModBlocks.CHISELED_WARPED_NETHER_BRICKS.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WARPED_NETHER_BRICK_STAIRS = MODDED_ITEMS.register("warped_nether_brick_stairs", () -> new BlockItem(ModBlocks.WARPED_NETHER_BRICK_STAIRS.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> WARPED_NETHER_BRICK_SLAB = MODDED_ITEMS.register("warped_nether_brick_slab", () -> new BlockItem(ModBlocks.WARPED_NETHER_BRICK_SLAB.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));

	public static final RegistryObject<Item> WITHER_WALTZ_MUSIC_DISC = MODDED_ITEMS.register("wither_waltz_music_disc", () -> new RecordItem(4, () -> ModSounds.WITHER_WALTZ.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB).stacksTo(1).rarity(Rarity.RARE), 5040));
	public static final RegistryObject<Item> WARPED_ENDER_PEARL = MODDED_ITEMS.register("warped_ender_pearl", () -> new WarpedEnderpearlItem((new Item.Properties()).stacksTo(16).tab(ModCreativeModeTabs.MOD_TAB).rarity(Rarity.RARE)));

	public static final RegistryObject<Item> NETHERITE_BELL = MODDED_ITEMS.register("netherite_bell", () -> new BlockItem(ModBlocks.NETHERITE_BELL.get(), new Item.Properties().tab(ModCreativeModeTabs.MOD_TAB)));
	public static final RegistryObject<Item> GILDED_NETHERITE_SHIELD = MODDED_ITEMS.register("gilded_netherite_shield", () -> new ModShieldItem((new Item.Properties()).durability(1512).tab(ModCreativeModeTabs.MOD_TAB)));

	public static void register(IEventBus eventBus) { MODDED_ITEMS.register(eventBus); }

}
