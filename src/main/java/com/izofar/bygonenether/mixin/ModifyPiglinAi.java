package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.entity.ai.ModPiglinBruteAi;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PiglinAi.class)
public class ModifyPiglinAi {

    @Inject(
            method="angerNearbyPiglins(Lnet/minecraft/world/entity/player/Player;Z)V",
            at = @At(value = "RETURN"),
            cancellable = true
    )
    private static void bygonenether_angerNearbyPiglins(Player player, boolean requireVisibility, CallbackInfo ci) {
        List<PiglinBrute> list = player.level.getEntitiesOfClass(PiglinBrute.class, player.getBoundingBox().inflate(16.0D));
        list.stream().filter(PiglinAi::isIdle).filter((piglinBrute) -> !requireVisibility || BehaviorUtils.canSee(piglinBrute, player)).forEach((piglinBrute) -> {
            if (piglinBrute.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER))
                ModPiglinBruteAi.setAngerTargetToNearestTargetablePlayerIfFound(piglinBrute, player);
            else
                PiglinBruteAi.setAngerTarget(piglinBrute, player);
        });
    }
}
