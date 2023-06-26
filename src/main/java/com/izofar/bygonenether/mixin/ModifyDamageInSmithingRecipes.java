package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.item.ModArmorItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTransformRecipe.class)
public class ModifyDamageInSmithingRecipes {

	@Inject(
			method="assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;",
			at = @At(value = "RETURN"),
			cancellable = true
		)
	private void bygonenether_assemble(Container container, RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack itemstack = cir.getReturnValue();
		if (itemstack.getItem() instanceof ModArmorItem) {
			int damage = itemstack.getDamageValue();
			itemstack.getOrCreateTag().put("NetheriteDamage", IntTag.valueOf(damage));
			itemstack.setDamageValue(0);
		}
	}
	
}
