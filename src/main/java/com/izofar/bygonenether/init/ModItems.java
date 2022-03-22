package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.item.ModArmorItem;
import com.izofar.bygonenether.item.ModArmorMaterial;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class ModItems {

	public static final DeferredRegister<Item> MODDED_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BygoneNetherMod.MODID);
	public static final DeferredRegister<Item> VANILLA_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

	//public static final RegistryObject<Item> PIGLIN_BRUTE_SPAWN_EGG = VANILLA_ITEMS.register("piglin_brute_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.PIGLIN_BRUTE, 5843472, 16380836, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> PIGLIN_PRISONER_SPAWN_EGG = MODDED_ITEMS.register("piglin_prisoner_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.PIGLIN_PRISONER, 0xc79e88, 16380836, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> PIGLIN_HUNTER_SPAWN_EGG = MODDED_ITEMS.register("piglin_hunter_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.PIGLIN_HUNTER, 0xba6645, 16380836, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> WEX_SPAWN_EGG = MODDED_ITEMS.register("wex_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WEX, 0x7198c8, 0x2b4667, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> WARPED_ENDERMAN_SPAWN_EGG = MODDED_ITEMS.register("warped_enderman_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.WARPED_ENDERMAN, 0x0e8281, 0x0, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));

	public static final RegistryObject<Item> GILDED_NETHERITE_HELMET = MODDED_ITEMS.register("gilded_netherite_helmet", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.HEAD, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT).fireResistant()));
	public static final RegistryObject<Item> GILDED_NETHERITE_CHESTPLATE = MODDED_ITEMS.register("gilded_netherite_chestplate", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.CHEST, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT).fireResistant()));
	public static final RegistryObject<Item> GILDED_NETHERITE_LEGGINGS = MODDED_ITEMS.register("gilded_netherite_leggings", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.LEGS, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT).fireResistant()));
	public static final RegistryObject<Item> GILDED_NETHERITE_BOOTS = MODDED_ITEMS.register("gilded_netherite_boots", () -> new ModArmorItem(ModArmorMaterial.GILDED_NETHERITE, EquipmentSlot.FEET, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT).fireResistant()));
	
	public static final RegistryObject<Item> COBBLED_BLACKSTONE = MODDED_ITEMS.register("cobbled_blackstone", () -> new BlockItem(ModBlocks.COBBLED_BLACKSTONE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> WITHERED_BLACKSTONE = MODDED_ITEMS.register("withered_blackstone", () -> new BlockItem(ModBlocks.WITHERED_BLACKSTONE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> CRACKED_WITHERED_BLACKSTONE = MODDED_ITEMS.register("cracked_withered_blackstone", () -> new BlockItem(ModBlocks.CRACKED_WITHERED_BLACKSTONE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> CHISELED_WITHERED_BLACKSTONE = MODDED_ITEMS.register("chiseled_withered_blackstone", () -> new BlockItem(ModBlocks.CHISELED_WITHERED_BLACKSTONE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> WITHERED_DEBRIS = MODDED_ITEMS.register("withered_debris", () -> new BlockItem(ModBlocks.WITHERED_DEBRIS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> SOUL_STONE = MODDED_ITEMS.register("soul_stone", () -> new BlockItem(ModBlocks.SOUL_STONE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

	public static final RegistryObject<Item> WARPED_NETHER_BRICKS = MODDED_ITEMS.register("warped_nether_bricks", () -> new BlockItem(ModBlocks.WARPED_NETHER_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> CHISELED_WARPED_NETHER_BRICKS = MODDED_ITEMS.register("chiseled_warped_nether_bricks", () -> new BlockItem(ModBlocks.CHISELED_WARPED_NETHER_BRICKS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> WARPED_NETHER_BRICK_STAIRS = MODDED_ITEMS.register("warped_nether_brick_stairs", () -> new BlockItem(ModBlocks.WARPED_NETHER_BRICK_STAIRS.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> WARPED_NETHER_BRICK_SLAB = MODDED_ITEMS.register("warped_nether_brick_slab", () -> new BlockItem(ModBlocks.WARPED_NETHER_BRICK_SLAB.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	
	public static void register(IEventBus eventBus) {
		MODDED_ITEMS.register(eventBus);
		VANILLA_ITEMS.register(eventBus);
	}

}
