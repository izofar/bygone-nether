package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.util.ModLists;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.feature.DeltaFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DeltaFeature.class)
public class NoDeltasInStructuresMixin {

    @Inject(
            method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
		)
    private void bygonenether_noDeltasInStructures(FeaturePlaceContext<DeltaFeatureConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        SectionPos sectionPos = SectionPos.of(context.origin());
        for (StructureFeature<?> structure : ModLists.DELTALESS_STRUCTURES) {
            List<? extends StructureStart<?>> structureStarts = context.level().startsForFeature(sectionPos, structure);
            if (!structureStarts.isEmpty() && structureStarts.stream().anyMatch(structureStart -> structureStart.getPieces().stream().anyMatch(box -> box.getBoundingBox().isInside(context.origin()))))
            {
                cir.setReturnValue(false);
                break;
            }
        }
    }
}
