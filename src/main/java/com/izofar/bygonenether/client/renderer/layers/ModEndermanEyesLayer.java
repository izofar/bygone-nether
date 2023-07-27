package com.izofar.bygonenether.client.renderer.layers;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.WarpedEndermanModel;
import com.izofar.bygonenether.entity.WarpedEndermanEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModEndermanEyesLayer extends AbstractEyesLayer<WarpedEndermanEntity, WarpedEndermanModel> {

    private static final RenderType ENDERMAN_EYES = RenderType.eyes(new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/warped_enderman/warped_enderman_eyes.png"));

    public ModEndermanEyesLayer(IEntityRenderer<WarpedEndermanEntity, WarpedEndermanModel> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return ENDERMAN_EYES;
    }
}
