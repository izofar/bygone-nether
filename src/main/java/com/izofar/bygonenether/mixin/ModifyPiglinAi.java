package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.entity.ai.ModPiglinBruteAi;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PiglinTasks.class)
public class ModifyPiglinAi {

    @Inject(
            method="angerNearbyPiglins(Lnet/minecraft/entity/player/PlayerEntity;Z)V",
            at = @At(value = "RETURN"),
            cancellable = true
    )
    private static void bygonenether_angerNearbyPiglins(PlayerEntity player, boolean requireVisibility, CallbackInfo ci) {
        List<PiglinBruteEntity> list = player.level.getEntitiesOfClass(PiglinBruteEntity.class, player.getBoundingBox().inflate(16.0D));
        list.stream().filter(PiglinTasks::isIdle).filter((piglinBrute) -> !requireVisibility || BrainUtil.canSee(piglinBrute, player)).forEach((piglinBrute) -> {
            if (piglinBrute.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER))
                ModPiglinBruteAi.setAngerTargetToNearestTargetablePlayerIfFound(piglinBrute, player);
            else
                ModPiglinBruteAi.setAngerTarget(piglinBrute, player);
        });
    }
}
