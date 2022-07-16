package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.Level;

public class PossessedWitherSkeleton extends WitherSkeleton {

    public PossessedWitherSkeleton(EntityType<? extends WitherSkeleton> entityType, Level level) { super(entityType, level); }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.MAX_HEALTH, 10.0D);
    }

    @Override
    public void die(DamageSource source){
        if(this.getLevel() instanceof ServerLevel serverLevel) {
            WexEntity wex = ModEntityTypes.WEX.get().create(serverLevel);
            wex.moveTo(this.blockPosition().above(), this.yBodyRot, this.xRotO);
            wex.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.CONVERSION, null, null);
            wex.setPersistenceRequired();
            serverLevel.addFreshEntity(wex);
        }
        this.convertTo(EntityType.WITHER_SKELETON, true);
    }
}
