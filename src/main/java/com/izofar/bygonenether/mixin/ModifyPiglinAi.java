package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.entity.ai.ModPiglinBruteAi;
import com.izofar.bygonenether.item.ModArmorMaterial;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(PiglinAi.class)
public class ModifyPiglinAi {

    @Inject(method = "isWearingGold", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void wearsGoldArmor(@NotNull LivingEntity entity, @NotNull CallbackInfoReturnable<Boolean> cir){
        Iterable<ItemStack> iterable = entity.getArmorSlots();
        Iterator<ItemStack> var2 = iterable.iterator();

        if (!cir.getReturnValue()) {
            Item item;
            do {
                if (!var2.hasNext()) {
                    cir.setReturnValue(false);
                    return;
                }

                ItemStack itemStack = var2.next();
                item = itemStack.getItem();
            } while (!(item instanceof ArmorItem) || ((ArmorItem) item).getMaterial() != ModArmorMaterial.GILDED_NETHERITE);

            cir.setReturnValue(true);
        }
    }

    @Inject(
            method="angerNearbyPiglins(Lnet/minecraft/world/entity/player/Player;Z)V",
            at = @At(value = "RETURN")
    )
    private static void bygonenether_angerNearbyPiglins(Player player, boolean requireVisibility, CallbackInfo ci) {
        List<PiglinBrute> list = player.level.getEntitiesOfClass(PiglinBrute.class, player.getBoundingBox().inflate(16.0D));
        list.stream().filter(PiglinAi::isIdle).filter((piglinBrute) -> !requireVisibility || BehaviorUtils.canSee(piglinBrute, player)).forEach((piglinBrute) -> {
            if (piglinBrute.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                ModPiglinBruteAi.setAngerTargetToNearestTargetablePlayerIfFound(piglinBrute, player);
            }
            else {
                PiglinBruteAi.setAngerTarget(piglinBrute, player);
            }
        });
    }
}
