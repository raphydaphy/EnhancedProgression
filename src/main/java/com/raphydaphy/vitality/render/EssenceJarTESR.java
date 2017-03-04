package com.raphydaphy.vitality.render;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.block.tile.TileEssenceJar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EssenceJarTESR extends TileEntitySpecialRenderer<TileEssenceJar> {

	@Override
	public void renderTileEntityAt(TileEssenceJar essenceJar, double x, double y, double z, float partialTicks,
			int destroyStage) {
		if (essenceJar == null)
			return;

		if (essenceJar.getEssenceHeight() == 0)
			return;

		GlStateManager.pushMatrix();

		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		renderFluid(essenceJar.getEssenceHeight(), essenceJar.getEssenceType(), x, y, z);

		GlStateManager.popMatrix();
	}

	public void renderFluid(float maxHeight, Essence essenceType, double x, double y, double z) {
		maxHeight = maxHeight / 16;
		GlStateManager.translate(x, y + 0.0625, z);
		RenderHelper.disableStandardItemLighting();

		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();

		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		TextureAtlasSprite fluid = null;
		if (essenceType != null) {
			switch (essenceType) {
			case ANGELIC:
				fluid = Minecraft.getMinecraft().getTextureMapBlocks()
						.getAtlasSprite("vitality:blocks/essence_angelic");
				break;
			case ATMOSPHERIC:
				fluid = Minecraft.getMinecraft().getTextureMapBlocks()
						.getAtlasSprite(new ResourceLocation("vitality", "blocks/essence_atmospheric").toString());
				break;
			case DEMONIC:
				fluid = Minecraft.getMinecraft().getTextureMapBlocks()
						.getAtlasSprite(new ResourceLocation("vitality", "blocks/essence_demonic").toString());
				break;
			case ENERGETIC:
				fluid = Minecraft.getMinecraft().getTextureMapBlocks()
						.getAtlasSprite("vitality:blocks/essence_energetic");
				break;
			case EXOTIC:
				fluid = Minecraft.getMinecraft().getTextureMapBlocks()
						.getAtlasSprite(new ResourceLocation("vitality", "blocks/essence_exotic").toString());
				break;
			}
		} else {
			return;
		}

		if (fluid == Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
			fluid = Minecraft.getMinecraft().getTextureMapBlocks()
					.getAtlasSprite(FluidRegistry.LAVA.getStill().toString());
		}

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

		final int rgbaColor = FluidRegistry.WATER.getColor();
		final int rColor = rgbaColor >> 16 & 0xFF;
		final int gColor = rgbaColor >> 8 & 0xFF;
		final int bColor = rgbaColor & 0xFF;
		final int aColor = rgbaColor >> 24 & 0xFF;
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(rColor, gColor, bColor, aColor);

		float u1 = fluid.getMinU();
		float v1 = fluid.getMinV();
		float u2 = fluid.getMaxU();

		if (maxHeight > 0) {
			float texWidth = u2 - u1;
			// TOP
			buffer.pos(0.25, maxHeight + 0.05, 0.25).tex(u1 + 0.75 * texWidth, v1 + (maxHeight + 0.05) * texWidth)
					.color(rColor, gColor, bColor, aColor).endVertex();
			buffer.pos(0.25, maxHeight + 0.05, 0.75).tex(u1 + 0.75 * texWidth, v1).color(rColor, gColor, bColor, aColor)
					.endVertex();
			buffer.pos(0.75, maxHeight + 0.05, 0.75).tex(u1 + 0.25 * texWidth, v1).color(rColor, gColor, bColor, aColor)
					.endVertex();
			buffer.pos(0.75, maxHeight + 0.05, 0.25).tex(u1 + 0.25 * texWidth, v1 + (maxHeight + 0.05) * texWidth)
					.color(rColor, gColor, bColor, aColor).endVertex();

			// NORTH
			buffer.pos(0.75, maxHeight + 0.05, 0.25).tex(u1 + 0.75 * texWidth, v1 + (maxHeight + 0.05) * texWidth)
					.color(rColor, gColor, bColor, aColor).endVertex();
			buffer.pos(0.75, 0, 0.25).tex(u1 + 0.75 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
			buffer.pos(0.25, 0, 0.25).tex(u1 + 0.25 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
			buffer.pos(0.25, maxHeight + 0.05, 0.25).tex(u1 + 0.25 * texWidth, v1 + (maxHeight + 0.05) * texWidth)
					.color(rColor, gColor, bColor, aColor).endVertex();

			// EAST
			buffer.pos(0.25, 0, 0.75).tex(u1 + 0.75 * texWidth, v1 + (maxHeight + 0.05) * texWidth)
					.color(rColor, gColor, bColor, aColor).endVertex();
			buffer.pos(0.25, maxHeight + 0.05, 0.75).tex(u1 + 0.75 * texWidth, v1).color(rColor, gColor, bColor, aColor)
					.endVertex();
			buffer.pos(0.25, maxHeight + 0.05, 0.25).tex(u1 + 0.25 * texWidth, v1).color(rColor, gColor, bColor, aColor)
					.endVertex();
			buffer.pos(0.25, 0, 0.25).tex(u1 + 0.25 * texWidth, v1 + (maxHeight + 0.05) * texWidth)
					.color(rColor, gColor, bColor, aColor).endVertex();

			// SOUTH
			buffer.pos(0.75, 0, 0.75).tex(u1 + 0.75 * texWidth, v1 + (maxHeight + 0.05) * texWidth)
					.color(rColor, gColor, bColor, aColor).endVertex();
			buffer.pos(0.75, maxHeight + 0.05, 0.75).tex(u1 + 0.75 * texWidth, v1).color(rColor, gColor, bColor, aColor)
					.endVertex();
			buffer.pos(0.25, maxHeight + 0.05, 0.75).tex(u1 + 0.25 * texWidth, v1).color(rColor, gColor, bColor, aColor)
					.endVertex();
			buffer.pos(0.25, 0, 0.75).tex(u1 + 0.25 * texWidth, v1 + (maxHeight + 0.05) * texWidth)
					.color(rColor, gColor, bColor, aColor).endVertex();

			// WEST
			buffer.pos(0.75, maxHeight + 0.05, 0.75).tex(u1 + 0.75 * texWidth, v1 + (maxHeight + 0.05) * texWidth)
					.color(rColor, gColor, bColor, aColor).endVertex();
			buffer.pos(0.75, 0, 0.75).tex(u1 + 0.75 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
			buffer.pos(0.75, 0, 0.25).tex(u1 + 0.25 * texWidth, v1).color(rColor, gColor, bColor, aColor).endVertex();
			buffer.pos(0.75, maxHeight + 0.05, 0.25).tex(u1 + 0.25 * texWidth, v1 + (maxHeight + 0.05) * texWidth)
					.color(rColor, gColor, bColor, aColor).endVertex();
		}

		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
	}
}
