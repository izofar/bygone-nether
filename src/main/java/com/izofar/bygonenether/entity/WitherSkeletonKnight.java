package com.izofar.bygonenether.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class WitherSkeletonKnight extends WitherSkeletonEntity {

    private static final DataParameter<Boolean> DATA_IS_DISARMORED = EntityDataManager.defineId(WitherSkeletonKnight.class, DataSerializers.BOOLEAN);
    private static final float BREAK_HEALTH = 20.0f;

    public WitherSkeletonKnight(EntityType<? extends WitherSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag){
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Disarmored", this.isDisarmored());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag){
        super.readAdditionalSaveData(tag);
        this.setDisarmored(tag.getBoolean("Disarmored"));
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(DATA_IS_DISARMORED, false);
    }

    public boolean isDisarmored(){
        return this.entityData.get(DATA_IS_DISARMORED);
    }

    private void setDisarmored(boolean disarmored){
        this.entityData.set(DATA_IS_DISARMORED, disarmored);
    }

    @Override
    public boolean hurt(DamageSource source, float damage){
        boolean result = super.hurt(source, damage);
        if(!this.isDisarmored() && this.getHealth() < BREAK_HEALTH) {
            this.setDisarmored(true);
            this.playSound(SoundEvents.SHIELD_BREAK, 1.2F, 0.8F + this.level.random.nextFloat() * 0.4F);
            this.setSpeed(0.25f);
        }
        return result;
    }
}
