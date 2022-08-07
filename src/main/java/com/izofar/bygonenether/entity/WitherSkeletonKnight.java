package com.izofar.bygonenether.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class WitherSkeletonKnight extends WitherSkeleton {

    private static final EntityDataAccessor<Boolean> DATA_IS_DISARMORED = SynchedEntityData.defineId(WitherSkeletonKnight.class, EntityDataSerializers.BOOLEAN);
    private static final float BREAK_HEALTH = 20.0f;

    public WitherSkeletonKnight(EntityType<? extends WitherSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Disarmored", this.isDisarmored());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag){
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
    public void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty){
        super.populateDefaultEquipmentSlots(random, difficulty);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
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
