package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class Wraither extends WitherSkeletonEntity {

    public Wraither(EntityType<? extends WitherSkeletonEntity> entityType, World level) { super(entityType, level); }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.MAX_HEALTH, 10.0D);
    }

    @Override
    public void die(DamageSource source){
        if(this.level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            WexEntity wex = ModEntityTypes.WEX.get().create(serverWorld);
            wex.moveTo(this.blockPosition().above(), this.yBodyRot, this.xRotO);
            wex.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
            wex.setPersistenceRequired();
            serverWorld.addFreshEntity(wex);
        }
        this.convertTo(EntityType.WITHER_SKELETON, true);
    }
}
