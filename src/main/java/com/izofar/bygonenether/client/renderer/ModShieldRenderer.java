package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.client.BygoneNetherClient;
import com.izofar.bygonenether.event.ModShieldSetModelCallback;
import com.izofar.bygonenether.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ModShieldRenderer {

    private static ShieldModel shieldModel;

    private static final ClampedItemPropertyFunction isBlocking = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;

    private static final ResourceLocation GILDED_NETHERITE_SHIELD_BASE_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "entity/shield/gilded_netherite_shield_base");
    private static final ResourceLocation GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "entity/shield/gilded_netherite_shield_base_nopattern");

    private static final Material GILDED_NETHERITE_SHIELD_BASE_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, GILDED_NETHERITE_SHIELD_BASE_LOCATION);
    private static final Material GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_LOCATION);

    public static void stitchTextureModelLayer() {
        EntityModelLayerRegistry.registerModelLayer(BygoneNetherClient.GILDED_NETHERITE_SHIELD_MODEL_LAYER, ShieldModel::createLayer);

        ModShieldSetModelCallback.EVENT.register((loader) -> {
            shieldModel = new ShieldModel(loader.bakeLayer(BygoneNetherClient.GILDED_NETHERITE_SHIELD_MODEL_LAYER));
            return InteractionResult.PASS;
        });

        ClientSpriteRegistryCallback.event(TextureAtlas.LOCATION_BLOCKS).register((atlasTexture, registry) -> {
            registry.register(GILDED_NETHERITE_SHIELD_BASE_LOCATION);
            registry.register(GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_LOCATION);
        });

        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.GILDED_NETHERITE_SHIELD, (stack, transformType, poseStack, buffer, packedLight, packedOverlay) -> renderByItem(stack, poseStack, buffer, packedLight, packedOverlay));
    }

    public static void renderByItem(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.scale(1, -1, -1);
        boolean flag = stack.getTagElement("BlockEntityTag") != null;
        Material rendermaterial = flag ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;

        Item shield = stack.getItem();
        if (shield == ModItems.GILDED_NETHERITE_SHIELD) {
            rendermaterial = flag ? GILDED_NETHERITE_SHIELD_BASE_MATERIAL : GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_MATERIAL;
        }

        VertexConsumer vertexConsumer = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, shieldModel.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));
        shieldModel.handle().render(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        if (flag) {
            List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack));
            BannerRenderer.renderPatterns(poseStack, buffer, packedLight, packedOverlay, shieldModel.plate(), rendermaterial, false, list, stack.hasFoil());
        } else {
            shieldModel.plate().render(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        poseStack.popPose();
    }

    public static void addShieldPropertyOverrides() {
        ItemProperties.register(ModItems.GILDED_NETHERITE_SHIELD, new ResourceLocation(BygoneNetherMod.MODID, "blocking"), isBlocking);
    }

}
