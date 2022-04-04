package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;

public class WitherSkeletonHorse extends SkeletonHorse {

    public WitherSkeletonHorse(EntityType<? extends SkeletonHorse> entityType, Level level){
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    @Override
    public boolean hurt(DamageSource source, float amount){
        this.getPassengers().forEach((passenger) -> {
            if(passenger instanceof AbstractPiglin piglin && source.getEntity() instanceof LivingEntity target)
                PiglinPrisonerAi.setAngerTarget(piglin, target);
        });
        return super.hurt(source, amount);
    }

    @Override
    public boolean isTamed() { return true; }

}
