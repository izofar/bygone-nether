package com.izofar.bygonenether.event;

import com.izofar.bygonenether.util.ModLists;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemTier;
import net.minecraft.item.TieredItem;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ModBlockEvents {

	@SubscribeEvent
	public static void enforceNetheriteToBreakWitheredStone(PlayerInteractEvent.LeftClickBlock event) {
		if(!event.getPlayer().isCreative()
				&& ModLists.WITHERED_BLOCKS.contains(event.getWorld().getBlockState(event.getPos()).getBlock())
				&& !(event.getPlayer().getItemBySlot(EquipmentSlotType.MAINHAND).getItem() instanceof TieredItem tieredItem
				&& tieredItem.getTier() == ItemTier.NETHERITE))
			event.setCanceled(true);
	}
}
