package com.izofar.bygonenether.client;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.model.CorporModel;
import com.izofar.bygonenether.client.model.PiglinHunterModel;
import com.izofar.bygonenether.client.model.WarpedEndermanModel;
import com.izofar.bygonenether.client.model.WitherSkeletonKnightModel;
import com.izofar.bygonenether.client.renderer.*;
import com.izofar.bygonenether.init.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class BNClient implements ClientModInitializer {
    public static final ModelLayerLocation WARPED_ENDERMAN = new ModelLayerLocation(new ResourceLocation(BygoneNetherMod.MODID, "warped_enderman"), "main");
    public static final ModelLayerLocation PIGLIN_HUNTER = new ModelLayerLocation(new ResourceLocation(BygoneNetherMod.MODID, "piglin_hunter"), "main");
    public static final ModelLayerLocation CORPOR = new ModelLayerLocation(new ResourceLocation(BygoneNetherMod.MODID, "corpor"), "main");
    public static final ModelLayerLocation WITHER_SKELETON_KNIGHT = new ModelLayerLocation(new ResourceLocation(BygoneNetherMod.MODID, "wither_skeleton_knight"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntityTypes.WEX, WexRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.WARPED_ENDERMAN, WarpedEnderManRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.PIGLIN_PRISONER, PiglinPrisonerRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.PIGLIN_HUNTER, PiglinHunterRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.WRAITHER, WraitherRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.WITHER_SKELETON_KNIGHT, WitherSkeletonKnightRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.CORPOR, CorporRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.WARPED_ENDER_PEARL, ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.WITHER_SKELETON_HORSE, WitherSkeletonHorseRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(WARPED_ENDERMAN, WarpedEndermanModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(PIGLIN_HUNTER, PiglinHunterModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(CORPOR, CorporModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(WITHER_SKELETON_KNIGHT, WitherSkeletonKnightModel::createBodyLayer);
    }
}
