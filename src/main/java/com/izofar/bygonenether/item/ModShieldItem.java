package com.izofar.bygonenether.item;

import com.izofar.bygonenether.client.renderer.ModShieldTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.concurrent.Callable;

public class ModShieldItem extends ShieldItem {

    public ModShieldItem(Properties properties) {
        super(properties.setISTER(ModShieldItem::getISTER));
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> getISTER() {
        return ModShieldTileEntityRenderer::new;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return false;
    }
}
