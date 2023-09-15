package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.init.ModItems;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class NoAxeDisableModShieldMixin {

    @Inject(
            method="disableShield(Z)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void bygonenether_disableShield(boolean becauseOfAxe, CallbackInfo ci) {
        if (((Player)(Object)this).getUseItem().getItem() == ModItems.GILDED_NETHERITE_SHIELD) {
            ci.cancel();
        }
    }
}
