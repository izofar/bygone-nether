package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class NoAxeDisableModShieldMixin {

    @Inject(
            method="disableShield(Z)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void bygonenether_disableShield(boolean becauseOfAxe, CallbackInfo ci) {
        if (((PlayerEntity)(Object)this).getUseItem().getItem() == ModItems.GILDED_NETHERITE_SHIELD.get()) {
            ci.cancel();
        }
    }
}
