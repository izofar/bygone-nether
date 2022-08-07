package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class WraitherEntity extends WitherSkeletonEntity {

    private static final DataParameter<Boolean> DATA_IS_POSSESSED = EntityDataManager.defineId(WraitherEntity.class, DataSerializers.BOOLEAN);

    public WraitherEntity(EntityType<? extends WitherSkeletonEntity> entityType, World level) { super(entityType, level); }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.32D).add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag){
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Possessed", this.isPossessed());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag){
        super.readAdditionalSaveData(tag);
        this.setPossessed(tag.getBoolean("Possessed"));
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(DATA_IS_POSSESSED, true);
    }

    public boolean isPossessed(){ return this.entityData.get(DATA_IS_POSSESSED); }

    private void setPossessed(boolean disarmored){ this.entityData.set(DATA_IS_POSSESSED, disarmored); }

    @Override
    public boolean hurt(DamageSource source, float amount){
        boolean ret = super.hurt(source, amount);
        if(this.isPossessed() && this.getHealth() < 10.0F)
            this.dispossess();
        return ret;
    }

    private void dispossess(){
        this.setPossessed(false);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.setHealth(10.0F);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(4.0D);
        if(this.level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            WexEntity wex = ModEntityTypes.WEX.get().create(serverWorld);
            wex.moveTo(this.blockPosition().above(), this.yBodyRot, this.xRotO);
            wex.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
            wex.setPersistenceRequired();
            serverWorld.addFreshEntity(wex);
        }
    }
}
