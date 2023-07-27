package com.izofar.bygonenether.client.renderer;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.entity.NetheriteBellTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetheriteBellTileEntityRenderer extends TileEntityRenderer<NetheriteBellTileEntity> {

    public static final ResourceLocation BELL_RESOURCE_LOCATION = new ResourceLocation(BygoneNetherMod.MODID, "textures/entity/netherite_bell/netherite_bell_body.png");
    private final ModelRenderer bellBody;

    public NetheriteBellTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        bellBody = new ModelRenderer(32, 32, 0, 0);
        this.bellBody.addBox(-3.0F, -6.0F, -3.0F, 6.0F, 7.0F, 6.0F);
        this.bellBody.setPos(8.0F, 12.0F, 8.0F);
        ModelRenderer modelrenderer = new ModelRenderer(32, 32, 0, 13);
        modelrenderer.addBox(4.0F, 4.0F, 4.0F, 8.0F, 2.0F, 8.0F);
        modelrenderer.setPos(-8.0F, -12.0F, -8.0F);
        this.bellBody.addChild(modelrenderer);
    }

    @Override
    public void render(NetheriteBellTileEntity bellTileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, int combinedOverlay) {
        float f = (float)bellTileEntity.ticks + partialTicks;
        float f1 = 0.0F;
        float f2 = 0.0F;
        if (bellTileEntity.shaking) {
            float f3 = MathHelper.sin(f / (float)Math.PI) / (4.0F + f / 3.0F);
            if (bellTileEntity.clickDirection == Direction.NORTH) {
                f1 = -f3;
            } else if (bellTileEntity.clickDirection == Direction.SOUTH) {
                f1 = f3;
            } else if (bellTileEntity.clickDirection == Direction.EAST) {
                f2 = -f3;
            } else if (bellTileEntity.clickDirection == Direction.WEST) {
                f2 = f3;
            }
        }

        this.bellBody.xRot = f1;
        this.bellBody.zRot = f2;
        IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.entitySolid(BELL_RESOURCE_LOCATION));
        this.bellBody.render(matrixStack, ivertexbuilder, packedLight, combinedOverlay);
    }
}
