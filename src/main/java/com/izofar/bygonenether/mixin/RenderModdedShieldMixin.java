package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.event.ModShieldSetModelCallback;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class RenderModdedShieldMixin {

    @Final
    @Shadow
    public EntityModelSet entityModelSet;

    @Inject(method = "onResourceManagerReload", at = @At("HEAD"))
    private void setModelFabricShield(CallbackInfo ci){
        ModShieldSetModelCallback.EVENT.invoker().setModel(this.entityModelSet);
    }

}
