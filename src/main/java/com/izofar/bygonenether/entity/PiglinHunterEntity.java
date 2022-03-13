package com.izofar.bygonenether.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.world.World;

public class PiglinHunterEntity extends PiglinEntity {
    public PiglinHunterEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }
}
