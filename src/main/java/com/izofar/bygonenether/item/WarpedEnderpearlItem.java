package com.izofar.bygonenether.item;

import com.izofar.bygonenether.entity.ThrownWarpedEnderpearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class WarpedEnderpearlItem extends EnderPearlItem {

    private static final int COOL_DOWN = 10;

    public WarpedEnderpearlItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        player.getCooldowns().addCooldown(this, COOL_DOWN);
        if (!world.isClientSide) {
            ThrownWarpedEnderpearlEntity thrownenderpearl = new ThrownWarpedEnderpearlEntity(world, player);
            thrownenderpearl.setItem(itemstack);
            thrownenderpearl.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(thrownenderpearl);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.abilities.instabuild) {
            itemstack.shrink(1);
        }

        return ActionResult.sidedSuccess(itemstack, world.isClientSide());
    }
}
