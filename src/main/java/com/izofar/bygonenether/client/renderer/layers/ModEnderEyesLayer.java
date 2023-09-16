package com.izofar.bygonenether.client.renderer.layers;

import com.izofar.bygonenether.BygoneNetherMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.EnderMan;

@Environment(EnvType.CLIENT)
public class ModEnderEyesLayer<T extends EnderMan, M extends EndermanModel<T>> extends EyesLayer<T, M> {

    private static final RenderType ENDERMAN_EYES = RenderType.eyes(new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_eyes.png"));

    public ModEnderEyesLayer(RenderLayerParent<T, M> layer) {
        super(layer);
    }

    public RenderType renderType() {
        return ENDERMAN_EYES;
    }
}
