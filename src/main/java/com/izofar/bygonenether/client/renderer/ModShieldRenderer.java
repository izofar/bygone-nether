package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT, modid = BygoneNetherMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModShieldRenderer extends BlockEntityWithoutLevelRenderer {

    private static ModShieldRenderer instance;
    private static ClampedItemPropertyFunction isBlocking = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;

    private static final Material GILDED_NETHERITE_SHIELD_BASE_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(BygoneNetherMod.MODID, "entity/gilded_netherite_shield_base"));
    private static final Material GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(BygoneNetherMod.MODID, "entity/gilded_netherite_shield_base_nopattern"));

    public ModShieldRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @SubscribeEvent
    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        instance = new ModShieldRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
        event.registerReloadListener(instance);
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            event.addSprite(GILDED_NETHERITE_SHIELD_BASE_LOCATION.texture());
            event.addSprite(GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_LOCATION.texture());
        }
    }

    public static void addShieldPropertyOverrides() {
        ItemProperties.register(ModItems.GILDED_NETHERITE_SHIELD.get(), new ResourceLocation(BygoneNetherMod.MODID, "blocking"), isBlocking);
    }

    public static ModShieldRenderer getInstance() {
        return instance;
    }

    @Override
    public void renderByItem(ItemStack stack, TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.scale(1, -1, -1);
        boolean flag = stack.getTagElement("BlockEntityTag") != null;
        Material rendermaterial = flag ? ModelBakery.SHIELD_BASE
                : ModelBakery.NO_PATTERN_SHIELD;

        Item shield = stack.getItem();
        if (shield == ModItems.GILDED_NETHERITE_SHIELD.get()) {
            rendermaterial = flag ? GILDED_NETHERITE_SHIELD_BASE_LOCATION : GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_LOCATION;
        }

        VertexConsumer vertexConsumer = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, shieldModel.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
        this.shieldModel.handle().render(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        if (flag) {
            List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack));
            BannerRenderer.renderPatterns(poseStack, buffer, packedLight, packedOverlay, this.shieldModel.plate(), rendermaterial, false, list, stack.hasFoil());
        } else {
            this.shieldModel.plate().render(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        poseStack.popPose();
    }
}
