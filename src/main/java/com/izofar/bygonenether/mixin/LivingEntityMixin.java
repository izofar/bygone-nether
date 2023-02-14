package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.init.ModItems;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "hurt", at = @At("HEAD"))
    private void ah(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if((Object) this instanceof Player entity) {
            if(!entity.level.isClientSide && !entity.getAbilities().instabuild) {
                Iterable<ItemStack> armorItems = entity.getArmorSlots();
                for (ItemStack stack : armorItems) {
                    LogManager.getLogger().warn("a");
                    if(stack.hurt((int)amount, entity.getRandom(), (ServerPlayer)entity)) {
                        LogManager.getLogger().warn("b");
                        replaceArmor(stack, entity);
                    }
                }
            }
        }
    }

    private void replaceArmor(ItemStack stack, Player player) {
        ListTag list = stack.getEnchantmentTags();
        Item item;
        int slot;
        LogManager.getLogger().warn("1");
        if (stack.getItem() == ModItems.GILDED_NETHERITE_HELMET) {
            item = Items.NETHERITE_HELMET;
            slot = 3;
            LogManager.getLogger().warn("2");
        } else if (stack.getItem() == ModItems.GILDED_NETHERITE_CHESTPLATE) {
            item = Items.NETHERITE_CHESTPLATE;
            slot = 2;
            LogManager.getLogger().warn("3");
        } else if (stack.getItem() == ModItems.GILDED_NETHERITE_LEGGINGS) {
            item = Items.NETHERITE_LEGGINGS;
            slot = 1;
            LogManager.getLogger().warn("4");
        } else if (stack.getItem() == ModItems.GILDED_NETHERITE_BOOTS) {
            item = Items.NETHERITE_BOOTS;
            slot = 0;
            LogManager.getLogger().warn("5");
        }else return;

        LogManager.getLogger().warn("6");
        stack.shrink(1);
        LogManager.getLogger().warn("7");
        ItemStack newStack = new ItemStack(item, 1);
        LogManager.getLogger().warn("8");
        newStack.addTagElement("Enchantments", list);
        LogManager.getLogger().warn("9");
        newStack.setDamageValue(stack.getTag().getInt("NetheriteDamage"));
        LogManager.getLogger().warn("10");
        player.getInventory().armor.set(slot, newStack);
        LogManager.getLogger().warn("11");
    }
}
