package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.Level;

public class Wraither extends WitherSkeleton {

    private static final EntityDataAccessor<Boolean> DATA_IS_POSSESSED = SynchedEntityData.defineId(Wraither.class, EntityDataSerializers.BOOLEAN);

    public Wraither(EntityType<? extends WitherSkeleton> entityType, Level level) { super(entityType, level); }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.MAX_HEALTH, 25.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Possessed", this.isPossessed());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setPossessed(tag.getBoolean("Possessed"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_POSSESSED, true);
    }

    public boolean isPossessed() {
        return this.entityData.get(DATA_IS_POSSESSED);
    }

    private void setPossessed(boolean possessed) {
        this.entityData.set(DATA_IS_POSSESSED, possessed);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
        if (this.isPossessed() && this.getHealth() < 10.0F) {
            this.dispossess();
        }
        return ret;
    }

    private void dispossess() {
        this.setPossessed(false);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            Wex wex = ModEntityTypes.WEX.create(serverLevel);
            wex.moveTo(this.blockPosition().above(), this.yBodyRot, this.xRotO);
            wex.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.CONVERSION, null, null);
            wex.setPersistenceRequired();
            serverLevel.addFreshEntity(wex);
        }
    }
}
