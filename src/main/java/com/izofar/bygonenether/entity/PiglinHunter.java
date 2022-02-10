package com.izofar.bygonenether.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.Level;

public class PiglinHunter extends Piglin {
    public PiglinHunter(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }
}
