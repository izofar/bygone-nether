package com.izofar.bygonenether.entity.ai;

import com.izofar.bygonenether.item.ModArmorMaterial;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ModPiglinBruteAi {

    public static void setAngerTargetToNearestTargetablePlayerIfFound(PiglinBrute piglinBrute, LivingEntity defaultEntity) {
        Optional<Player> optional = piglinBrute.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) ? piglinBrute.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) : Optional.empty();
        PiglinBruteAi.setAngerTarget(piglinBrute, optional.isPresent()? optional.get() : defaultEntity);
    }

    public static boolean isWearingGild(LivingEntity entity) {
        for (ItemStack itemstack : entity.getArmorSlots()) {
            if (makesPiglinBrutesNeutral(itemstack)) {
                return true;
            }
        }
        return false;
    }

    private static boolean makesPiglinBrutesNeutral(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() instanceof ModArmorMaterial;
    }
}
