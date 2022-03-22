package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.entity.ai.ModPiglinBruteAi;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBruteEntity.class)
public class ModifyPiglinBruteBrain {

    @Inject(
            method="brainProvider()Lnet/minecraft/entity/ai/brain/Brain$BrainCodec;",
            at = @At(value = "RETURN"),
            cancellable = true
    )
    private void brainProvider(CallbackInfoReturnable<Brain.BrainCodec<PiglinBruteEntity>> cir) {
        cir.setReturnValue(Brain.provider(ModPiglinBruteAi.MEMORY_TYPES, ModPiglinBruteAi.SENSOR_TYPES));
    }
}
