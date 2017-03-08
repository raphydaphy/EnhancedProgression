package com.raphydaphy.vitality.gui;

import com.raphydaphy.vitality.registry.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public final class ModOverlays
{
	public static void drawSelectedSpell(Minecraft mc, net.minecraft.client.gui.ScaledResolution res){
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		int screenWidth = 0 + (res.getScaledHeight() / 15);
		int screenHeight = 0 + (res.getScaledHeight() / 10);
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate(screenWidth - 16, screenHeight - 16, 0);
		GlStateManager.scale(2, 2, 2);
		mc.getRenderItem().renderItemIntoGUI(new ItemStack(ModItems.SPELL_FIREBALL), 0, 0);
		GlStateManager.translate(-screenWidth, -screenHeight, 0);
		GlStateManager.popMatrix();
					
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
	}
}