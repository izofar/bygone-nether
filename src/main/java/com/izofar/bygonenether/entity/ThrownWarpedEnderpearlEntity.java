package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.init.ModEntityTypes;
import com.izofar.bygonenether.init.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThrownWarpedEnderpearlEntity extends ProjectileItemEntity {

    public ThrownWarpedEnderpearlEntity(EntityType<? extends ThrownWarpedEnderpearlEntity> entityType, World level) {
        super(entityType, level);
    }

    public ThrownWarpedEnderpearlEntity(World level, LivingEntity entity) {
        super(ModEntityTypes.WARPED_ENDER_PEARL.get(), entity, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.WARPED_ENDER_PEARL.get();
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(RayTraceResult result) {
        super.onHit(result);

        for (int i = 0; i < 32; ++i) {
            this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        if (!this.level.isClientSide && !this.removed) {
            Entity entity = this.getOwner();
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverplayer= (ServerPlayerEntity)entity;
                if (serverplayer.connection.getConnection().isConnected() && serverplayer.level == this.level && !serverplayer.isSleeping()) {
                    if (this.random.nextFloat() < 0.05F && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                        EndermiteEntity endermite = EntityType.ENDERMITE.create(this.level);
                        endermite.setPlayerSpawned(true);
                        endermite.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                        this.level.addFreshEntity(endermite);
                    }

                    if (entity.isPassenger()) {
                        entity.stopRiding();
                    }

                    entity.teleportTo(this.getX(), this.getY(), this.getZ());
                    entity.fallDistance = 0.0F;

                    if (this.level.getBlockState(this.blockPosition()).is(Blocks.WATER)) {
                        serverplayer.addEffect(new EffectInstance(Effects.WATER_BREATHING, 300));
                    } else if (entity.isInLava() || this.level.getBlockState(this.blockPosition()).is(Blocks.LAVA)) {
                        serverplayer.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 900));
                    } else if (!this.isOnGround()) {
                        serverplayer.addEffect(new EffectInstance(Effects.SLOW_FALLING, 160));
                        serverplayer.addEffect(new EffectInstance(Effects.ABSORPTION, 300, 1));
                    }

                }
            } else if (entity != null) {
                entity.teleportTo(this.getX(), this.getY(), this.getZ());
                entity.fallDistance = 0.0F;
            }

            this.remove();
        }

    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof PlayerEntity && !entity.isAlive()) {
            this.remove();
        } else {
            super.tick();
        }

    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Nullable
    public Entity changeDimension(ServerWorld serverlevel, ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level.dimension() != serverlevel.dimension()) {
            this.setOwner(null);
        }

        return super.changeDimension(serverlevel, teleporter);
    }
}
