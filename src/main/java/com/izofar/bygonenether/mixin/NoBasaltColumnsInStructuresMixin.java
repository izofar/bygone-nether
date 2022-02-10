package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.util.ModLists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.BasaltColumnsFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BasaltColumnsFeature.class)
public class NoBasaltColumnsInStructuresMixin {

    @Inject(
            method = "canPlaceAt(Lnet/minecraft/world/level/LevelAccessor;ILnet/minecraft/core/BlockPos$MutableBlockPos;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
		)
    private static void izomod_noBasaltColumnsInStructures(LevelAccessor levelAccessor, int seaLevel, BlockPos.MutableBlockPos mutableBlockPos, CallbackInfoReturnable<Boolean> cir) {
        SectionPos sectionPos = SectionPos.of(mutableBlockPos);
        if (!levelAccessor.getChunk(sectionPos.x(), sectionPos.z()).getStatus().isOrAfter(ChunkStatus.STRUCTURE_REFERENCES)) {
            BygoneNetherMod.LOGGER.warn("Bygone Nether: Detected a mod with a broken basalt columns configuredfeature that is trying to place blocks outside the 3x3 safe chunk area for features. Find the broken mod and report to them to fix the placement of their basalt columns feature.");
            return;
        }
        for (StructureFeature<?> structure : ModLists.DELTALESS_STRUCTURES) {
            List<? extends StructureStart<?>> structureStarts = ((WorldGenLevel)levelAccessor).startsForFeature(sectionPos, structure);
            if (!structureStarts.isEmpty() && structureStarts.stream().anyMatch(structureStart -> structureStart.getPieces().stream().anyMatch(box -> box.getBoundingBox().isInside(mutableBlockPos))))
            {
                cir.setReturnValue(false);
                break;
            }
        }
    }
}
