package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.entity.ai.goal.ShieldGoal;
import com.izofar.bygonenether.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import java.util.UUID;

public class PiglinHunterEntity extends PiglinEntity implements IShieldedMobEntity {

    private static final DataParameter<Boolean> DATA_IS_SHIELDED = EntityDataManager.defineId(PiglinHunterEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_SHIELD_HAND = EntityDataManager.defineId(PiglinHunterEntity.class, DataSerializers.BOOLEAN); // True for Main Hand, False for Offhand
    private static final DataParameter<Integer> DATA_SHIELD_COOLDOWN = EntityDataManager.defineId(PiglinHunterEntity.class, DataSerializers.INT);

    private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("3520BCE0-D755-458F-944B-A528DB8EF9DC");
    private static final AttributeModifier SPEED_MODIFIER_BLOCKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Shielded speed penalty", -0.10D, AttributeModifier.Operation.ADDITION);

    private static final float SHIELDED_BASE_PROBABILITY = 0.35F;
    private static final float GILDED_SHIELDED_BASE_PROBABILITY = 0.05F;
    private static final float CROSSBOW_PROBABILITY = 0.5F;

    public PiglinHunterEntity(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ShieldGoal<>(this, PlayerEntity.class));
        super.registerGoals();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
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
    public void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);
        float f = SHIELDED_BASE_PROBABILITY + 0.4F * difficulty.getEffectiveDifficulty() / 2.25F / CROSSBOW_PROBABILITY;
        if (this.getItemBySlot(EquipmentSlotType.MAINHAND).getItem() != Items.CROSSBOW && this.random.nextFloat() < f) {
            Item shield = this.random.nextFloat() < GILDED_SHIELDED_BASE_PROBABILITY * f ? ModItems.GILDED_NETHERITE_SHIELD.get() : Items.SHIELD;
            this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(shield));
        }
    }

    @Override
    protected float getEquipmentDropChance(EquipmentSlotType slot) {
        if (slot == EquipmentSlotType.OFFHAND) {
            return 0.0F;
        }
        else {
            return super.getEquipmentDropChance(slot);
        }
    }

    @Override
    public void knockback(float strength, double x, double z) {
        if (!this.isUsingShield()) {
            super.knockback(strength, x, z);
        } else {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level.random.nextFloat() * 0.4F);
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
        for (Hand interactionhand : Hand.values()) {
            if (this.getItemInHand(interactionhand).getItem() == ModItems.GILDED_NETHERITE_SHIELD.get()) {
                this.startUsingItem(interactionhand);
                this.setUsingShield(true);
                this.setShieldMainhand(interactionhand == Hand.MAIN_HAND);
                ModifiableAttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
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
        for (Hand interactionhand : Hand.values()) {
            if (this.getItemInHand(interactionhand).getItem() == ModItems.GILDED_NETHERITE_SHIELD.get()) {
                this.stopUsingItem();
                this.setUsingShield(false);
                ModifiableAttributeInstance  attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
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

    public Hand getShieldHand() {
        return this.isUsingShield() ? (this.isShieldMainhand() ? Hand.MAIN_HAND : Hand.OFF_HAND) : null;
    }

}
