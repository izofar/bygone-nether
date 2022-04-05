package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class WitherSkeletonHorseEntity extends SkeletonHorseEntity {
    public WitherSkeletonHorseEntity(EntityType<? extends SkeletonHorseEntity> entityType, World level){
        super(entityType, level);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 35.0D).add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    protected void randomizeAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(35.0D);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(1.0D);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    }

    @Override
    public boolean hurt(DamageSource source, float amount){
        this.getPassengers().forEach((passenger) -> {
            if(passenger instanceof AbstractPiglinEntity && source.getEntity() instanceof LivingEntity)
                PiglinPrisonerAi.setAngerTarget((AbstractPiglinEntity) passenger, (LivingEntity) source.getEntity());
        });
        return super.hurt(source, amount);
    }

    @Override
    public boolean isTamed() { return true; }
}
