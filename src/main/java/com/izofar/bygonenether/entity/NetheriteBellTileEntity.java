package com.izofar.bygonenether.entity;

import com.izofar.bygonenether.init.ModEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;

public class NetheriteBellTileEntity extends TileEntity implements ITickableTileEntity {

    private static final int DURATION = 50;
    private static final int GLOW_DURATION = 300; // PiglinPrisonerAi.CELEBRATION_TIME
    private static final int MIN_TICKS_BETWEEN_SEARCHES = 60;
    private static final int MAX_RESONATION_TICKS = 40;
    private static final int TICKS_BEFORE_RESONATION = 5;
    private static final int SEARCH_RADIUS = 48;
    private static final int HEAR_BELL_RADIUS = 32;

    private long lastRingTimestamp;
    public int ticks;
    public boolean shaking;
    public Direction clickDirection;
    private List<LivingEntity> nearbyEntities;
    private boolean resonating;
    private int resonationTicks;

    public NetheriteBellTileEntity() {
        super(ModEntityTypes.NETHERITE_BELL.get());
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.updateEntities();
            this.resonationTicks = 0;
            this.clickDirection = Direction.from3DDataValue(type);
            this.ticks = 0;
            this.shaking = true;
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    public void tick() {
        if (this.shaking) {
            ++this.ticks;
        }

        if (this.ticks >= DURATION) {
            this.shaking = false;
            this.ticks = 0;
        }

        if (this.ticks >= TICKS_BEFORE_RESONATION && this.resonationTicks == 0 && this.arePiglinPrisonersNearby()) {
            this.resonating = true;
            this.playResonateSound();
        }

        if (this.resonating) {
            if (this.resonationTicks < MAX_RESONATION_TICKS) {
                ++this.resonationTicks;
            } else {
                this.completePiglinPrisonerRescue(this.level);
                this.showBellParticles(this.level);
                this.resonating = false;
            }
        }

    }

    private void playResonateSound() {
        this.level.playSound(null, this.getBlockPos(), SoundEvents.BELL_RESONATE, SoundCategory.BLOCKS, 1.5F, 0.8F);
    }

    public void onHit(Direction direction) {
        BlockPos blockpos = this.getBlockPos();
        this.clickDirection = direction;
        if (this.shaking) {
            this.ticks = 0;
        } else {
            this.shaking = true;
        }

        this.level.blockEvent(blockpos, this.getBlockState().getBlock(), 1, direction.get3DDataValue());
    }

    private void updateEntities() {
        BlockPos blockpos = this.getBlockPos();
        if (this.level.getGameTime() > this.lastRingTimestamp + MIN_TICKS_BETWEEN_SEARCHES || this.nearbyEntities == null) {
            this.lastRingTimestamp = this.level.getGameTime();
            AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos)).inflate(SEARCH_RADIUS);
            this.nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
        }

        if (!this.level.isClientSide) {
            for(LivingEntity livingentity : this.nearbyEntities) {
                if (livingentity.isAlive() && !livingentity.removed && blockpos.closerThan(livingentity.position(), HEAR_BELL_RADIUS)) {
                    livingentity.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, this.level.getGameTime());
                }
            }
        }
    }

    private boolean arePiglinPrisonersNearby() {
        for (LivingEntity livingentity : this.nearbyEntities) {
            if (isRescuedPiglinPrisonerWithinRange(livingentity)) {
                return true;
            }
        }
        return false;
    }

    private void completePiglinPrisonerRescue(World world) {
        if (!world.isClientSide) {
            this.nearbyEntities.stream().filter(this::isRescuedPiglinPrisonerWithinRange).forEach(this::rescue);
        }
    }

    private void showBellParticles(World world) {
        if (world.isClientSide) {
            BlockPos blockpos = this.getBlockPos();
            MutableInt mutableint = new MutableInt(16700985);
            int i = (int)this.nearbyEntities.stream().filter((p_222829_1_) -> blockpos.closerThan(p_222829_1_.position(), 48.0D)).count();
            this.nearbyEntities.stream().filter(this::isRescuedPiglinPrisonerWithinRange).forEach((p_235655_4_) -> {
                float f1 = MathHelper.sqrt((p_235655_4_.getX() - (double)blockpos.getX()) * (p_235655_4_.getX() - (double)blockpos.getX()) + (p_235655_4_.getZ() - (double)blockpos.getZ()) * (p_235655_4_.getZ() - (double)blockpos.getZ()));
                double d0 = (double)((float)blockpos.getX() + 0.5F) + (double)(1.0F / f1) * (p_235655_4_.getX() - (double)blockpos.getX());
                double d1 = (double)((float)blockpos.getZ() + 0.5F) + (double)(1.0F / f1) * (p_235655_4_.getZ() - (double)blockpos.getZ());
                int j = MathHelper.clamp((i - 21) / -2, 3, 15);

                for (int k = 0; k < j; ++k) {
                    int l = mutableint.addAndGet(5);
                    double d2 = (double) ColorHelper.PackedColor.red(l) / 255.0D;
                    double d3 = (double)ColorHelper.PackedColor.green(l) / 255.0D;
                    double d4 = (double)ColorHelper.PackedColor.blue(l) / 255.0D;
                    world.addParticle(ParticleTypes.ENTITY_EFFECT, d0, (float)blockpos.getY() + 0.5F, d1, d2, d3, d4);
                }

            });
        }
    }

    private boolean isLivingEntityWithinRange(LivingEntity livingEntity) {
        return livingEntity.isAlive() && !livingEntity.removed && this.getBlockPos().closerThan(livingEntity.position(), HEAR_BELL_RADIUS);
    }

    private boolean isRescuedPiglinPrisonerWithinRange(LivingEntity livingEntity) {
        return isLivingEntityWithinRange(livingEntity) && livingEntity.getType() == ModEntityTypes.PIGLIN_PRISONER.get() && ((PiglinPrisonerEntity) livingEntity).getTempter() != null;
    }

    private void rescue(LivingEntity entity) {
        glow(entity);
        broadcastRescue(entity);
    }

    private void glow(LivingEntity entity) {
        entity.addEffect(new EffectInstance(Effects.GLOWING, GLOW_DURATION));
    }

    private void broadcastRescue(LivingEntity entity) {
        ((PiglinPrisonerEntity) entity).rescue();
    }
}
