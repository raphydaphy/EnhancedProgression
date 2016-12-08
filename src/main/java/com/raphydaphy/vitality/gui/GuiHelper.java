package com.raphydaphy.vitality.gui;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.vitality.init.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class GuiHelper 
{
	public static void renderCircle(int x, int y) {
		Minecraft mc = Minecraft.getMinecraft();

		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GlStateManager.colorMask(false, false, false, false);
		GlStateManager.depthMask(false);
		GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
		GL11.glStencilMask(0xFF);
		mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(ModItems.fluxed_stick), x, y);

		//mc.renderEngine.bindTexture(new ResourceLocation("we need a resource location"));
		int r = 90;
		int centerX = x + 0;
		int centerY = y + 0;
		int degs = (int) (360 * 100);
		float a = 0.02F;
		float red = 0.2F;
		float green = 0.3F;
		float blue = 0.3F;

		GlStateManager.disableLighting();
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.depthMask(true);
		GL11.glStencilMask(0xEE);
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
		VertexBuffer buf = Tessellator.getInstance().getBuffer();
		buf.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
		buf.pos(centerX, centerY, 0).color(red, green, blue, a).endVertex();

		for(int i = degs; i > 0; i--) 
		{
			double rad = (i - 90) / 180F * Math.PI;
			buf.pos(centerX + Math.cos(rad) * r, centerY + Math.sin(rad) * r, 0).color(red, green, blue, a).endVertex();
		}

		buf.pos(centerX, centerY, 0).color(red, green, blue, a).endVertex();
		Tessellator.getInstance().draw();

		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
}