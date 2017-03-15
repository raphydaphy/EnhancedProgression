package com.raphydaphy.vitality.gui;

import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.item.ItemWand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ModOverlays {
	public static void drawSelectedSpell(Minecraft mc, net.minecraft.client.gui.ScaledResolution res) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		ItemStack wandStack = player.getHeldItemMainhand();
		boolean tryOtherHand = false;

		if (wandStack == null) {
			tryOtherHand = true;
		} else if (!(wandStack.getItem() instanceof ItemWand)) {
			tryOtherHand = true;
		}

		if (tryOtherHand) {
			if (player.getHeldItemOffhand() != null) {
				wandStack = player.getHeldItemOffhand();
				if (!(wandStack.getItem() instanceof ItemWand)) {
					return;
				}
			}
		}
		if (wandStack == null) {
			return;
		}
		if (!(wandStack.hasTagCompound())) {
			return;
		} else if (!(wandStack.getTagCompound().hasKey(Spell.ACTIVE_KEY))) {
			return;
		}
		Spell active = Spell.spellMap.get(wandStack.getTagCompound().getInteger(Spell.ACTIVE_KEY));
		if (active == null) {
			return;
		}
		Item icon = active.getIcon();
		if (icon == null) {
			return;
		}
		int screenWidth = 0 + (res.getScaledHeight() / 15);
		int screenHeight = 0 + (res.getScaledHeight() / 12);
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate(screenWidth - 16, screenHeight - 16, 0);
		GlStateManager.scale(2, 2, 2);
		mc.getRenderItem().renderItemIntoGUI(new ItemStack(icon), 0, 0);
		GlStateManager.translate(-screenWidth, -screenHeight, 0);
		GlStateManager.popMatrix();

		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
	}
}