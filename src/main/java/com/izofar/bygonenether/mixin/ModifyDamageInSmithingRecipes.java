package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.init.ModItems;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(UpgradeRecipe.class)
public class ModifyDamageInSmithingRecipes {

    @Inject(
            method="assemble(Lnet/minecraft/world/Container;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "RETURN")
    )
    private void bygonenether_setDamage(Container container, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemstack = cir.getReturnValue();
        if(!(itemstack.getItem() == ModItems.GILDED_NETHERITE_HELMET || itemstack.getItem() == ModItems.GILDED_NETHERITE_CHESTPLATE || itemstack.getItem() == ModItems.GILDED_NETHERITE_LEGGINGS || itemstack.getItem() == ModItems.GILDED_NETHERITE_BOOTS)) return;
        int damage = itemstack.getDamageValue();
        itemstack.getOrCreateTag().put("NetheriteDamage", IntTag.valueOf(damage));
        itemstack.setDamageValue(0);
    }

}
