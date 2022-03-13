package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.item.ModArmorItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.nbt.IntNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingRecipe.class)
public class ModifyDamageInSmithingRecipes {

	@Inject(
			method="assemble(Lnet/minecraft/inventory/IInventory;)Lnet/minecraft/item/ItemStack;",
			at = @At(value = "RETURN"),
			cancellable = true
		)
	private void bygonenether_setDamage(IInventory inventory, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack itemstack = cir.getReturnValue();
		if(!(itemstack.getItem() instanceof ModArmorItem)) return;
		int damage = itemstack.getDamageValue();
		itemstack.getOrCreateTag().put("NetheriteDamage", IntNBT.valueOf(damage));
		itemstack.setDamageValue(0);
	}
	
}
