package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.entity.ai.PiglinPrisonerAi;
import com.izofar.bygonenether.init.ModCriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;

public class WitherSkeletonHorseEntity extends SkeletonHorseEntity {


    public WitherSkeletonHorseEntity(EntityType<? extends SkeletonHorseEntity> entityType, World level){ super(entityType, level); }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 35.0D).add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    public void tick() {
        super.tick();
        this.floatHorse();
    }

    @Override
    protected void randomizeAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(35.0D);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(1.0D);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand){
        if(!isTamed()) {
            this.tameWithName(player);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean hurt(DamageSource source, float amount){
        this.getPassengers().forEach((passenger) -> {
            if(passenger instanceof AbstractPiglinEntity && source.getEntity() instanceof LivingEntity) {
                PiglinPrisonerAi.setAngerTarget((AbstractPiglinEntity) passenger, (LivingEntity) source.getEntity());
            }
        });
        return super.hurt(source, amount);
    }

    @Override
    public boolean isTamed() {
        return true;
    }

    @Override
    public void addPassenger(Entity entity){
        super.addPassenger(entity);
        this.getIndirectPassengers().stream().filter((passenger) -> passenger instanceof ServerPlayerEntity).forEach((player) -> ModCriteriaTriggers.START_RIDING_TRIGGER.trigger((ServerPlayerEntity) player));
    }

    @Override
    public boolean canStandOnFluid(Fluid fluid) {
        return fluid.is(FluidTags.LAVA);
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        if (blockState.getMaterial().isLiquid()) {
            SoundType soundtype = blockState.getSoundType(level, blockPos, this);
            if (this.isVehicle() && this.canGallop) {
                ++this.gallopSoundCounter;
                if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                    this.playGallopSound(soundtype);
                } else if (this.gallopSoundCounter <= 5) {
                    this.playSound(SoundEvents.HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
                }
            }
        }
    }

    private void floatHorse() {
        if (this.isInLava()) {
            ISelectionContext collisioncontext = ISelectionContext.of(this);
            if (collisioncontext.isAbove(FlowingFluidBlock.STABLE_SHAPE, this.blockPosition(), true) && !this.level.getFluidState(this.blockPosition().above()).is(FluidTags.LAVA)) {
                this.onGround = true;
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D).add(0.0D, 0.05D, 0.0D));
            }
        }
    }

}
