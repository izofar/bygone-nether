package com.izofar.bygonenether.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class ModShieldItem extends ShieldItem {

    public ModShieldItem(Properties properties) {
        super(properties);
    }

    /*
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ModShieldRenderer.getInstance();
            }
        });
    }*/

    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return false;
    }
}