package com.izofar.bygonenether.mixin;


import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteBrain;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PiglinBruteBrain.class)
public class ModifyPiglinBruteAi {

    @Inject(
            method="findNearestValidAttackTarget(Lnet/minecraft/entity/monster/piglin/AbstractPiglinEntity;)Ljava/util/Optional;",
            at = @At(value = "RETURN"),
            cancellable = true
    )
    private static void bygonenether_findNearestValidAttackTarget(AbstractPiglinEntity piglin, CallbackInfoReturnable<Optional<? extends LivingEntity>> cir) {
        Brain<PiglinBruteEntity> brain = (Brain<PiglinBruteEntity>) piglin.getBrain();

        Optional<LivingEntity> optional = BrainUtil.getLivingEntityFromUUIDMemory(piglin, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && EntityPredicates.ATTACK_ALLOWED.test(optional.get())) {
            cir.setReturnValue(optional);
            return;
        }

        if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
            Optional<? extends LivingEntity> optional1 = PiglinBruteBrain.getTargetIfWithinRange(piglin, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
            if (optional1.isPresent()) {
                cir.setReturnValue(optional1);
                return;
            }
        }

        Optional<MobEntity> optional3 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
        if (optional3.isPresent()) {
            cir.setReturnValue(optional3);
            return;
        }

        Optional<PlayerEntity> optional2 = brain.getMemory(ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD.get());
        cir.setReturnValue(optional2.isPresent() && EntityPredicates.ATTACK_ALLOWED.test(optional2.get()) ? optional2 : Optional.empty());
    }
}
