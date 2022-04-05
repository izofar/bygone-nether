package com.izofar.bygonenether.mixin;

import com.izofar.bygonenether.init.ModStructures;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LocateCommand.class)
public class LocateBygoneStructureInstead {

    @ModifyVariable(
            method="locate(Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/world/level/levelgen/feature/StructureFeature;)I",
            at = @At(value = "HEAD"),
            ordinal = 0
    )
    private static StructureFeature<?> bygonenether_locate(StructureFeature<?> feature) {
        return feature == StructureFeature.NETHER_BRIDGE ? ModStructures.NETHER_FORTRESS.get() : feature;
    }
}
