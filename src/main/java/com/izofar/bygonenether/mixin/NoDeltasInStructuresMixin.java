package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.init.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.feature.DeltaFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DeltaFeature.class)
public class NoDeltasInStructuresMixin {

    @Inject(
            method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
		)
    private void bygonenether_noDeltasInStructures(FeaturePlaceContext<DeltaFeatureConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        Registry<Structure> configuredStructureFeatureRegistry = context.level().registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
        StructureManager structureFeatureManager = ((WorldGenRegionAccessor)context.level()).getStructureFeatureManager();
        for (Holder<Structure> configuredStructureFeature : configuredStructureFeatureRegistry.getOrCreateTag(ModTags.NO_BASALT)) {
            if (structureFeatureManager.getStructureAt(context.origin(), configuredStructureFeature.value()).isValid()) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}
