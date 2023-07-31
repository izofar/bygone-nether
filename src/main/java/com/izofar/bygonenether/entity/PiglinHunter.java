package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.entity.ai.goal.ShieldGoal;
import com.izofar.bygonenether.init.ModItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class PiglinHunter extends Piglin implements IShieldedMob{

    private static final EntityDataAccessor<Boolean> DATA_IS_SHIELDED = SynchedEntityData.defineId(PiglinHunter.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SHIELD_HAND = SynchedEntityData.defineId(PiglinHunter.class, EntityDataSerializers.BOOLEAN); // True for Main Hand, False for Offhand
    private static final EntityDataAccessor<Integer> DATA_SHIELD_COOLDOWN = SynchedEntityData.defineId(PiglinHunter.class, EntityDataSerializers.INT);

    private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("0B51435E-10B6-11EE-BE56-0242AC120002");
    private static final AttributeModifier SPEED_MODIFIER_BLOCKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Shielded speed penalty", -0.10D, AttributeModifier.Operation.ADDITION);
    private static final float SHIELDED_BASE_PROBABILITY = 0.35F;
    private static final float GILDED_SHIELDED_BASE_PROBABILITY = 0.05F;
    private static final float CROSSBOW_PROBABILITY = 0.5F;

    public PiglinHunter(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ShieldGoal<>(this, Player.class));
        super.registerGoals();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.decrementShieldCooldown();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_SHIELDED, false);
        this.entityData.define(DATA_SHIELD_HAND, false);
        this.entityData.define(DATA_SHIELD_COOLDOWN, 0);
    }

    @Override
    public void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        float f = SHIELDED_BASE_PROBABILITY + 0.4F * difficulty.getEffectiveDifficulty() / 2.25F / CROSSBOW_PROBABILITY;
        if (!this.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.CROSSBOW) && this.random.nextFloat() < f) {
            Item shield = this.random.nextFloat() < GILDED_SHIELDED_BASE_PROBABILITY * f ? ModItems.GILDED_NETHERITE_SHIELD.get() : Items.SHIELD;
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(shield));
        }
    }

    @Override
    protected float getEquipmentDropChance(EquipmentSlot slot) {
        if (slot == EquipmentSlot.OFFHAND) {
            return 0.0F;
        }
        else {
            return super.getEquipmentDropChance(slot);
        }
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (!this.isUsingShield()) {
            super.knockback(strength, x, z);
        } else {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level().random.nextFloat() * 0.4F);
        }
    }

    @Override
    public boolean isShieldDisabled() {
        return false;
    }

    @Override
    public void startUsingShield() {
        if (this.isUsingShield() || this.isShieldDisabled()) {
            return;
        }
        for (InteractionHand interactionhand : InteractionHand.values()) {
            if (this.getItemInHand(interactionhand).is(ModItems.GILDED_NETHERITE_SHIELD.get())) {
                this.startUsingItem(interactionhand);
                this.setUsingShield(true);
                this.setShieldMainhand(interactionhand == InteractionHand.MAIN_HAND);
                AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attributeinstance != null && !attributeinstance.hasModifier(SPEED_MODIFIER_BLOCKING)) {
                    attributeinstance.addTransientModifier(SPEED_MODIFIER_BLOCKING);
                }
            }
        }
    }

    @Override
    public void stopUsingShield() {
        if (!this.isUsingShield()) {
            return;
        }
        for (InteractionHand interactionhand : InteractionHand.values()) {
            if (this.getItemInHand(interactionhand).is(ModItems.GILDED_NETHERITE_SHIELD.get())) {
                this.stopUsingItem();
                this.setUsingShield(false);
                AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attributeinstance != null) {
                    attributeinstance.removeModifier(SPEED_MODIFIER_BLOCKING);
                }
            }
        }
    }

    public boolean isUsingShield() {
        return this.entityData.get(DATA_IS_SHIELDED);
    }

    public void setUsingShield(boolean isShielded) {
        this.entityData.set(DATA_IS_SHIELDED, isShielded);
    }

    private boolean isShieldMainhand() {
        return this.entityData.get(DATA_SHIELD_HAND);
    }

    private void setShieldMainhand(boolean isShieldedMainHand) {
        this.entityData.set(DATA_SHIELD_HAND, isShieldedMainHand);
    }

    private int getShieldCooldown() {
        return this.entityData.get(DATA_SHIELD_COOLDOWN);
    }

    private void setShieldCooldown(int newShieldCooldown) {
        this.entityData.set(DATA_SHIELD_COOLDOWN, newShieldCooldown);
    }

    private void decrementShieldCooldown() {
        this.setShieldCooldown(Math.max(this.getShieldCooldown() - 1, 0));
    }

    public InteractionHand getShieldHand() {
        return this.isUsingShield() ? (this.isShieldMainhand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND) : null;
    }
}
