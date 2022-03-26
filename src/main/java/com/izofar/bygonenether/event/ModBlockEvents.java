package com.izofar.bygonenether.event;

import com.izofar.bygonenether.util.ModLists;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.TieredItem;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ModBlockEvents {

	@SubscribeEvent
	public static void enforceNetheriteToBreakWitheredStone(PlayerInteractEvent.LeftClickBlock event) {
		Item item = event.getPlayer().getItemBySlot(EquipmentSlotType.MAINHAND).getItem();
		if(!event.getPlayer().isCreative()
				&& ModLists.WITHERED_BLOCKS.contains(event.getWorld().getBlockState(event.getPos()).getBlock())
				&& !(item instanceof TieredItem
				&& ((TieredItem) item).getTier() == ItemTier.NETHERITE))
			event.setCanceled(true);
	}
/*
	@SubscribeEvent
	public static void repairBrokenSpawnerBlocks(BlockEvent e){
		if (blockstate.is(Blocks.SPAWNER)) {
			TileEntity tileentity = world.getBlockEntity(blockpos);
			if (tileentity instanceof MobSpawnerTileEntity) {
				AbstractSpawner abstractspawner = ((MobSpawnerTileEntity)tileentity).getSpawner();
				EntityType<?> entitytype1 = this.getType(itemstack.getTag());
				abstractspawner.setEntityId(entitytype1);
				tileentity.setChanged();
				world.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
				itemstack.shrink(1);
				return ActionResultType.CONSUME;
			}
		}
	}*/
}
