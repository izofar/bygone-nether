package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.entity.ai.goal.ShieldGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import java.util.UUID;

public class WitherSkeletonKnightEntity extends WitherSkeletonEntity implements IShieldedMobEntity {

    private static final DataParameter<Boolean> DATA_IS_SHIELDED = EntityDataManager.defineId(WitherSkeletonKnightEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_SHIELD_HAND = EntityDataManager.defineId(WitherSkeletonKnightEntity.class, DataSerializers.BOOLEAN); // True for Main Hand, False for Offhand
    private static final DataParameter<Integer> DATA_SHIELD_COOLDOWN = EntityDataManager.defineId(WitherSkeletonKnightEntity.class, DataSerializers.INT);

    private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("3520BCE0-D755-458F-944B-A528DB8EF9DC");
    private static final AttributeModifier SPEED_MODIFIER_BLOCKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Shielded speed penalty", -0.10D, AttributeModifier.Operation.ADDITION);

    private static final DataParameter<Boolean> DATA_IS_DISARMORED = EntityDataManager.defineId(WitherSkeletonKnightEntity.class, DataSerializers.BOOLEAN);
    private static final float BREAK_HEALTH = 20.0f;

    public WitherSkeletonKnightEntity(EntityType<? extends WitherSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ShieldGoal<>(this, PlayerEntity.class));
        super.registerGoals();
    }

    @Override
    public void tick(){
        super.tick();
        if(!this.level.isClientSide){
            this.decrementShieldCooldown();
        }
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag){
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Disarmored", this.isDisarmored());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag){
        super.readAdditionalSaveData(tag);
        this.setDisarmored(tag.getBoolean("Disarmored"));
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(DATA_IS_DISARMORED, false);
        this.entityData.define(DATA_IS_SHIELDED, false);
        this.entityData.define(DATA_SHIELD_HAND, false);
        this.entityData.define(DATA_SHIELD_COOLDOWN, 0);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);
        this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SHIELD));
    }

    public boolean isDisarmored(){
        return this.entityData.get(DATA_IS_DISARMORED);
    }

    private void setDisarmored(boolean disarmored){
        this.entityData.set(DATA_IS_DISARMORED, disarmored);
    }

    @Override
    public boolean hurt(DamageSource source, float damage){
        boolean result = super.hurt(source, damage);
        if(!this.isDisarmored() && this.getHealth() < BREAK_HEALTH) {
            this.setDisarmored(true);
            this.playSound(SoundEvents.SHIELD_BREAK, 1.2F, 0.8F + this.level.random.nextFloat() * 0.4F);
            this.setSpeed(0.25f);
        }
        return result;
    }

    @Override
    public void knockback(float strength, double x, double z){
        if(!this.isUsingShield()){
            super.knockback(strength, x, z);
        }else{
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level.random.nextFloat() * 0.4F);
        }
    }

    @Override
    protected void blockUsingShield(LivingEntity attacker) {
        super.blockUsingShield(attacker);
        if (attacker.getMainHandItem().canDisableShield(this.useItem, this, attacker)) {
            this.disableShield();
        }
    }

    private void disableShield() {
        this.setShieldCooldown(60);
        this.stopUsingShield();
        this.level.broadcastEntityEvent(this, (byte)30);
        this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
    }

    @Override
    public boolean isShieldDisabled(){
        return this.getShieldCooldown() > 0;
    }

    @Override
    public void startUsingShield(){
        if(this.isUsingShield() || this.isShieldDisabled()) return;
        for(Hand interactionhand : Hand.values()) {
            if (this.getItemInHand(interactionhand).getItem() == Items.SHIELD) {
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
    public void stopUsingShield(){
        if(!this.isUsingShield()) return;
        for(Hand hand : Hand.values()) {
            if (this.getItemInHand(hand).getItem() == Items.SHIELD) {
                this.stopUsingItem();
                this.setUsingShield(false);
                ModifiableAttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if(attributeinstance != null)
                    attributeinstance.removeModifier(SPEED_MODIFIER_BLOCKING);
            }
        }
    }

    public boolean isUsingShield(){
        return this.entityData.get(DATA_IS_SHIELDED);
    }

    public void setUsingShield(boolean isShielded){
        this.entityData.set(DATA_IS_SHIELDED, isShielded);
    }

    private boolean isShieldMainhand(){
        return this.entityData.get(DATA_SHIELD_HAND);
    }

    private void setShieldMainhand(boolean isShieldedMainHand){
        this.entityData.set(DATA_SHIELD_HAND, isShieldedMainHand);
    }

    private int getShieldCooldown(){
        return this.entityData.get(DATA_SHIELD_COOLDOWN);
    }

    private void setShieldCooldown(int newShieldCooldown){
        this.entityData.set(DATA_SHIELD_COOLDOWN, newShieldCooldown);
    }

    private void decrementShieldCooldown(){
        this.setShieldCooldown(Math.max(this.getShieldCooldown() - 1, 0));
    }

    public Hand getShieldHand(){
        return this.isUsingShield() ? (this.isShieldMainhand() ? Hand.MAIN_HAND : Hand.OFF_HAND) : null;
    }
}
