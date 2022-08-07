package com.izofar.bygonenether.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class CorporEntity extends WitherSkeletonEntity {

    public CorporEntity(EntityType<? extends WitherSkeletonEntity> entityType, World world) { super(entityType, world); }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_AXE));
    }

}
