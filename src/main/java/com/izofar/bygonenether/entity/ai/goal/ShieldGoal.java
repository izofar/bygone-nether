package com.izofar.bygonenether.entity.ai.goal;

import com.izofar.bygonenether.entity.IShieldedMobEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ShieldGoal<T extends MobEntity & IShieldedMobEntity> extends Goal {

    protected final Class<? extends LivingEntity> targetType;
    protected final T mob;
    private int shieldCoolDown;
    private int shieldWarmup;
    private int shieldStagger;
    private int shieldDelay;
    @Nullable
    protected LivingEntity target;
    protected final EntityPredicate targetConditions;

    public ShieldGoal(T mob, Class<? extends LivingEntity> targetType){
        this.mob = mob;
        this.targetType = targetType;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetConditions = new EntityPredicate().range(this.getFollowDistance()).selector(null);
    }

    @Override
    public boolean canUse() {
        this.findTarget();
        return this.target != null;
    }

    @Override
    public boolean canContinueToUse() {
        if(this.mob.isShieldDisabled()){
            return false;
        } else if(this.target == null){
            return false;
        }else if (!this.target.isAlive()) {
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
        this.shieldDelay = 3 + this.mob.getRandom().nextInt(3);
        this.shieldStagger = 15 + this.mob.getRandom().nextInt(25);
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
            case INACTIVE:
                this.setDefaultCounters();
                break;
            case WARMUP:
                this.shieldWarmup--;
                break;
            case ACTIVE:
                if(this.mob.getTarget() == null) return;
                this.mob.getLookControl().setLookAt(this.mob.getTarget().getX(), this.mob.getTarget().getEyeY(), this.mob.getTarget().getZ(), 10.0F, (float) this.mob.getMaxHeadXRot());
                this.mob.startUsingShield();
                this.setDefaultCounters();
                break;
            case COOLDOWN :
                this.shieldCoolDown--;
                break;
        }
    }

    private static boolean targetDrawnBow(LivingEntity target) {
        if(target == null) return false;
        for(Hand interactionhand : Hand.values()) {
            ItemStack itemstack = target.getItemInHand(interactionhand);
            boolean drawnBow = itemstack.getItem() == Items.BOW && target.isUsingItem();
            boolean chargedCrossbow = itemstack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack);
            if (drawnBow || chargedCrossbow) {
                return true;
            }
        }
        return false;
    }

    protected AxisAlignedBB getTargetSearchArea(double radius) {
        return this.mob.getBoundingBox().inflate(radius, 4.0D, radius);
    }

    protected void findTarget() {
        LivingEntity potentialTarget;
        if (this.targetType != PlayerEntity.class && this.targetType != ServerPlayerEntity.class) {
            potentialTarget = this.mob.level.getNearestEntity(this.mob.level.getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), (p_148152_) -> true), this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        } else {
            potentialTarget = this.mob.level.getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
        this.target = targetDrawnBow(potentialTarget) ? potentialTarget : null;
    }

    protected double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    private void setDefaultCounters(){
        this.shieldWarmup = this.shieldDelay;
        this.shieldCoolDown = this.shieldStagger;
    }

    private ShieldStage getStage(){
        if(targetDrawnBow(this.target)){
            if(this.shieldWarmup <= 0){
                this.shieldWarmup = 0;
                return ShieldStage.ACTIVE;
            }else{
                return ShieldStage.WARMUP;
            }
        }else{
            if(this.shieldCoolDown <= 0){
                this.shieldCoolDown = 0;
                return ShieldStage.INACTIVE;
            }else{
                return ShieldStage.COOLDOWN;
            }
        }
    }

    private enum ShieldStage{
        INACTIVE, WARMUP, ACTIVE, COOLDOWN
    }
}
