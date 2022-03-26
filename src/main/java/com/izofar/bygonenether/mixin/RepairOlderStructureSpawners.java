package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.init.ModStructures;
import com.izofar.bygonenether.util.ModLists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.spawner.AbstractSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(MobSpawnerTileEntity.class)
public class RepairOlderStructureSpawners {

    @Inject(
            method = "tick()V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void bygonenether_tick(CallbackInfo ci){
        MobSpawnerTileEntity This = (MobSpawnerTileEntity)(Object)this;
        if(This.getLevel() instanceof ClientWorld || !This.getSpawner().getEntityId().toString().equals("minecraft:")) return;
        SectionPos sectionPos = SectionPos.of(This.getBlockPos());
        for (Structure<?> structure : ModLists.SPAWNER_STRUCTURES) {
            Optional<? extends StructureStart<?>> structureStart = ((ISeedReader) (This.getLevel())).startsForFeature(sectionPos, structure).findAny();
            if (structureStart.isPresent() && structureStart.get().getPieces().stream().anyMatch(box -> box.getBoundingBox().isInside(This.getBlockPos()))) {
                if (structure == ModStructures.NETHER_FORTRESS.get())
                    setMobSpawner(This, This.getLevel(), This.getBlockState(), This.getBlockPos(), EntityType.BLAZE);
                else if (structure == ModStructures.CATACOMB.get())
                    setMobSpawner(This, This.getLevel(), This.getBlockState(), This.getBlockPos(), EntityType.WITHER_SKELETON);
            }
        }
    }

    private static void setMobSpawner(MobSpawnerTileEntity tileentity, World world, BlockState blockstate, BlockPos blockpos, EntityType<?> entitytype){
        if (blockstate.is(Blocks.SPAWNER)) {
            AbstractSpawner spawner = tileentity.getSpawner();
            spawner.setEntityId(entitytype);
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("id", Registry.ENTITY_TYPE.getKey(entitytype).toString());
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.put("Entity", nbt);
            compoundnbt.putInt("Weight", 1);
            spawner.spawnPotentials.removeIf((element) -> true);
            spawner.spawnPotentials.add(new WeightedSpawnerEntity(compoundnbt));
            tileentity.setChanged();
            world.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
        }
    }

}
