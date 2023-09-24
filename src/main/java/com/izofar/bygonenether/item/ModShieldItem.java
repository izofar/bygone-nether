package com.izofar.bygonenether.item;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.init.ModItems;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class ModShieldItem extends ShieldItem {

    private static final ClampedItemPropertyFunction isBlocking = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;

    public ModShieldItem(Properties properties) {
        super(properties);
    }

    public static void addShieldPropertyOverrides() {
        ItemProperties.register(ModItems.GILDED_NETHERITE_SHIELD, new ResourceLocation(BygoneNetherMod.MODID, "blocking"), isBlocking);
    }

    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return false;
    }
}
