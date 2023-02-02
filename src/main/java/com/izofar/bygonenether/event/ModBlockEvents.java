package com.izofar.bygonenether.event;

import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import com.izofar.bygonenether.util.ModLists;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class ModBlockEvents {

	@SubscribeEvent
	public static void enforceNetheriteToBreakWitheredStone(PlayerInteractEvent.LeftClickBlock event) {
		if(!event.getEntity().isCreative()
				&& ModLists.WITHERED_BLOCKS.contains(event.getLevel().getBlockState(event.getPos()).getBlock())
				&& !(event.getEntity().getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof TieredItem tieredItem
				&& tieredItem.getTier().getLevel() >= Tiers.NETHERITE.getLevel()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onIronBarsBroken(BlockEvent.BreakEvent event){
		if(event.getState().getBlock() == Blocks.IRON_BARS){
			PiglinPrisonerAi.exciteNearbyPiglins(event.getPlayer(), false);
		}
	}
}