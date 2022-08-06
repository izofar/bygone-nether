package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.entity.ThrownWarpedEnderpearl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WarpedEnderpearlRenderer extends SpriteRenderer<ThrownWarpedEnderpearl> {

    public WarpedEnderpearlRenderer(EntityRendererManager manager) {
        super(manager, Minecraft.getInstance().getItemRenderer());
    }

}
