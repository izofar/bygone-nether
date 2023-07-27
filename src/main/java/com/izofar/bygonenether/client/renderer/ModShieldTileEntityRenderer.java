package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.init.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = BygoneNetherMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModShieldTileEntityRenderer extends ItemStackTileEntityRenderer {

    private static final IItemPropertyGetter isBlocking = (stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;

    private static final RenderMaterial GILDED_NETHERITE_SHIELD_BASE_LOCATION = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(BygoneNetherMod.MODID, "entity/shield/gilded_netherite_shield_base"));
    private static final RenderMaterial GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_LOCATION = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(BygoneNetherMod.MODID, "entity/shield/gilded_netherite_shield_base_nopattern"));

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().location().equals(AtlasTexture.LOCATION_BLOCKS)) {
            event.addSprite(GILDED_NETHERITE_SHIELD_BASE_LOCATION.texture());
            event.addSprite(GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_LOCATION.texture());
        }
    }

    public static void addShieldPropertyOverrides() {
        ItemModelsProperties.register(ModItems.GILDED_NETHERITE_SHIELD.get(), new ResourceLocation(BygoneNetherMod.MODID, "blocking"), isBlocking);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, int packedOverlay) {
        matrixStack.pushPose();
        matrixStack.scale(1, -1, -1);
        RenderMaterial rendermaterial = GILDED_NETHERITE_SHIELD_BASE_NOPATTERN_LOCATION;

        IVertexBuilder ivertexbuilder = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, shieldModel.renderType(rendermaterial.atlasLocation()), true, stack.hasFoil()));

        this.shieldModel.handle().render(matrixStack, ivertexbuilder, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        this.shieldModel.plate().render(matrixStack, ivertexbuilder, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

}
