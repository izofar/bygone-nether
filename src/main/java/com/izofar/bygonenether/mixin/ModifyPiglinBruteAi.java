package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.init.ModMemoryModuleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PiglinBruteAi.class)
public class ModifyPiglinBruteAi {

    @Inject(
            method="findNearestValidAttackTarget(Lnet/minecraft/world/entity/monster/piglin/AbstractPiglin;)Ljava/util/Optional;",
            at = @At(value = "RETURN"),
            cancellable = true
    )
    private static void bygonenether_findNearestValidAttackTarget(AbstractPiglin piglin, CallbackInfoReturnable<Optional<? extends LivingEntity>> cir) {
        Brain<PiglinBrute> brain = (Brain<PiglinBrute>) piglin.getBrain();
        Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(piglin, MemoryModuleType.ANGRY_AT);

        if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(piglin, optional.get())) {
            cir.setReturnValue(optional);
            return;
        }

        if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
            Optional<? extends LivingEntity> optional1 = PiglinBruteAi.getTargetIfWithinRange(piglin, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
            if (optional1.isPresent()) {
                cir.setReturnValue(optional1);
                return;
            }
        }

        Optional<Mob> optional3 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
        if (optional3.isPresent()) {
            cir.setReturnValue(optional3);
            return;
        }

        Optional<Player> optional2 = brain.getMemory(ModMemoryModuleTypes.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GILD);
        cir.setReturnValue(optional2.isPresent() && Sensor.isEntityAttackable(piglin, optional2.get()) ? optional2 : Optional.empty());
    }

    @Inject(
            method="setAngerTarget(Lnet/minecraft/world/entity/monster/piglin/PiglinBrute;Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(value = "RETURN")
    )
    private static void bygonenether_setAngerTarget(PiglinBrute piglin, LivingEntity entity, CallbackInfo ci) {
        if (entity.getType() == EntityType.PLAYER && piglin.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
            piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
        }
    }


}
