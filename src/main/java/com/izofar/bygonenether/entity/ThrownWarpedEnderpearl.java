package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.init.ModEntityTypes;
import com.izofar.bygonenether.init.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;

public class ThrownWarpedEnderpearl extends ThrowableItemProjectile {

    public ThrownWarpedEnderpearl(EntityType<? extends ThrownWarpedEnderpearl> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownWarpedEnderpearl(Level level, LivingEntity entity) {
        super(ModEntityTypes.WARPED_ENDER_PEARL.get(), entity, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.WARPED_ENDER_PEARL.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        for (int i = 0; i < 32; ++i) {
            this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        if (!this.level.isClientSide && !this.isRemoved()) {
            Entity entity = this.getOwner();
            if (entity instanceof ServerPlayer serverplayer) {
                if (serverplayer.connection.getConnection().isConnected() && serverplayer.level == this.level && !serverplayer.isSleeping()) {
                    if (this.random.nextFloat() < 0.05F && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                        Endermite endermite = EntityType.ENDERMITE.create(this.level);
                        endermite.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
                        this.level.addFreshEntity(endermite);
                    }

                    if (entity.isPassenger()) {
                        serverplayer.dismountTo(this.getX(), this.getY(), this.getZ());
                    }

                    entity.teleportTo(this.getX(), this.getY(), this.getZ());
                    entity.resetFallDistance();

                    if (this.getLevel().getBlockState(this.blockPosition()).is(Blocks.WATER)) {
                        serverplayer.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 300));
                    } else if(entity.isInLava() || this.getLevel().getBlockState(this.blockPosition()).is(Blocks.LAVA)) {
                        serverplayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 900));
                    } else if(!this.isOnGround()) {
                        serverplayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 160));
                        serverplayer.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 300, 1));
                    }

                }
            } else if (entity != null) {
                entity.teleportTo(this.getX(), this.getY(), this.getZ());
                entity.resetFallDistance();
            }

            this.discard();
        }

    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof Player && !entity.isAlive()) {
            this.discard();
        } else {
            super.tick();
        }
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel serverlevel, ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level.dimension() != serverlevel.dimension()) {
            this.setOwner(null);
        }

        return super.changeDimension(serverlevel, teleporter);
    }
}
