package com.izofar.bygonenether.entity.ai.goal;

import com.izofar.bygonenether.entity.IShieldedMob;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ShieldGoal<T extends Mob & IShieldedMob> extends Goal {

    protected final Class<? extends LivingEntity> targetType;
    protected final T mob;
    private int shieldCoolDown;
    private int shieldWarmup;
    private int shieldStagger;
    private int shieldDelay;
    @Nullable
    protected LivingEntity target;
    protected final TargetingConditions targetConditions;

    public ShieldGoal(T mob, Class<? extends LivingEntity> targetType) {
        this.mob = mob;
        this.targetType = targetType;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(null);
    }

    @Override
    public boolean canUse() {
        this.findTarget();
        return this.target != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.isShieldDisabled()) {
            return false;
        } else if (this.target == null) {
            return false;
        } else if (!this.target.isAlive()) {
            return false;
        } else if (this.mob.distanceToSqr(this.target) > this.getFollowDistance() * this.getFollowDistance()) {
            return false;
        } else {
            return this.getStage() != ShieldStage.INACTIVE;
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.target);
        this.shieldDelay = this.adjustedTickDelay(3 + this.mob.getRandom().nextInt(3));
        this.shieldStagger = this.adjustedTickDelay(15 + this.mob.getRandom().nextInt(25));
        this.setDefaultCounters();
        super.start();
    }

    @Override
    public void stop() {
        this.target = null;
        this.setDefaultCounters();
        this.mob.stopUsingShield();
    }

    @Override
    public void tick() {
        switch (this.getStage()) {
            case INACTIVE -> this.setDefaultCounters();
            case WARMUP -> this.shieldWarmup--;
            case ACTIVE -> {
                if (this.mob.getTarget() == null) return;
                this.mob.getLookControl().setLookAt(this.mob.getTarget().getX(), this.mob.getTarget().getEyeY(), this.mob.getTarget().getZ(), 10.0F, (float) this.mob.getMaxHeadXRot());
                this.mob.startUsingShield();
                this.setDefaultCounters();
            }
            case COOLDOWN -> this.shieldCoolDown--;
        }
    }

    private static boolean targetDrawnBow(LivingEntity target) {
        if (target == null) {
            return false;
        }

        for (InteractionHand interactionhand : InteractionHand.values()) {
            ItemStack itemstack = target.getItemInHand(interactionhand);
            boolean drawnBow = itemstack.is(Items.BOW) && target.isUsingItem();
            boolean chargedCrossbow = itemstack.is(Items.CROSSBOW) && CrossbowItem.isCharged(itemstack);
            if (drawnBow || chargedCrossbow) {
                return true;
            }
        }
        return false;
    }

    protected AABB getTargetSearchArea(double radius) {
        return this.mob.getBoundingBox().inflate(radius, 4.0D, radius);
    }

    protected void findTarget() {
        LivingEntity potentialTarget;
        if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
            potentialTarget = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), (p_148152_) -> true), this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        } else {
            potentialTarget = this.mob.level().getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
        this.target = targetDrawnBow(potentialTarget) ? potentialTarget : null;
    }

    protected double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    private void setDefaultCounters() {
        this.shieldWarmup = this.shieldDelay;
        this.shieldCoolDown = this.shieldStagger;
    }

    private ShieldStage getStage() {
        if (targetDrawnBow(this.target)) {
            if (this.shieldWarmup <= 0) {
                this.shieldWarmup = 0;
                return ShieldStage.ACTIVE;
            } else {
                return ShieldStage.WARMUP;
            }
        } else {
            if (this.shieldCoolDown <= 0) {
                this.shieldCoolDown = 0;
                return ShieldStage.INACTIVE;
            } else {
                return ShieldStage.COOLDOWN;
            }
        }
    }

    private enum ShieldStage {
        INACTIVE, WARMUP, ACTIVE, COOLDOWN
    }
}
