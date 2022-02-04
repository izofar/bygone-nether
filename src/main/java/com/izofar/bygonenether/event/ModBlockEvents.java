package com.izofar.bygonenether.event;

import com.izofar.bygonenether.util.ModLists;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ModBlockEvents {

	@SubscribeEvent
	public static void enforceNetheriteToBreakWitheredStone(PlayerInteractEvent.LeftClickBlock event) {
		if(!event.getPlayer().isCreative()
				&& ModLists.WITHERED_BLOCKS.contains(event.getWorld().getBlockState(event.getPos()).getBlock())
				&& !(event.getPlayer().getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof TieredItem tieredItem 
				&& tieredItem.getTier() == Tiers.NETHERITE))
			event.setCanceled(true);
	}
}
