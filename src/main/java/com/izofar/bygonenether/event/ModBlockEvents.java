package com.izofar.bygonenether.event;

import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import com.izofar.bygonenether.util.ModLists;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Blocks;

public class ModBlockEvents {
    public static void enforceNetheriteToBreakWitheredStone() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if(!player.isCreative() && ModLists.WITHERED_BLOCKS.contains(world.getBlockState(pos).getBlock()) && !(player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof TieredItem tieredItem && tieredItem.getTier().getLevel() == Tiers.NETHERITE.getLevel())) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }

    public static void onIronBarsBroken() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if(state.getBlock() == Blocks.IRON_BARS) {
                PiglinPrisonerAi.exciteNearbyPiglins(player, false);
            }
        });
    }
}
