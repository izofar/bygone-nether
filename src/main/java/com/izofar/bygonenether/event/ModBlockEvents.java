package com.izofar.bygonenether.event;

import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import com.izofar.bygonenether.util.ModLists;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.TieredItem;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
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

	@SubscribeEvent
	public static void onIronBarsBroken(BlockEvent.BreakEvent event){
		if(event.getState().getBlock() == Blocks.IRON_BARS){
			PiglinPrisonerAi.exciteNearbyPiglins(event.getPlayer(), false);
		}
	}
}
