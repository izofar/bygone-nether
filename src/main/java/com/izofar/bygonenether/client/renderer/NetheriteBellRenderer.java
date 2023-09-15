package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.entity.NetheriteBellBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class NetheriteBellRenderer implements BlockEntityRenderer<NetheriteBellBlockEntity> {

    public static final ResourceLocation BELL_RESOURCE_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/netherite_bell/netherite_bell_body.png");
    private final ModelPart bellBody;

    public NetheriteBellRenderer(BlockEntityRendererProvider.Context context) {
        this.bellBody = context.bakeLayer(ModelLayers.BELL).getChild("bell_body");
    }

    @Override
    public void render(NetheriteBellBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        float f = (float) blockEntity.ticks + partialTicks;
        float f1 = 0.0F;
        float f2 = 0.0F;
        if (blockEntity.shaking) {
            float f3 = Mth.sin(f / (float) Math.PI) / (4.0F + f / 3.0F);
            if (blockEntity.clickDirection == Direction.NORTH) {
                f1 = -f3;
            } else if (blockEntity.clickDirection == Direction.SOUTH) {
                f1 = f3;
            } else if (blockEntity.clickDirection == Direction.EAST) {
                f2 = -f3;
            } else if (blockEntity.clickDirection == Direction.WEST) {
                f2 = f3;
            }
        }
        this.bellBody.xRot = f1;
        this.bellBody.zRot = f2;
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entitySolid(BELL_RESOURCE_LOCATION));
        this.bellBody.render(poseStack, vertexconsumer, packedLight, packedOverlay);
    }
}
