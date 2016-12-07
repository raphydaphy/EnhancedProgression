package com.raphydaphy.vitality.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.google.common.collect.ImmutableSet;
import com.raphydaphy.vitality.init.KeyBindings;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.item.ItemSpellBag;
import com.raphydaphy.vitality.item.ItemWand;
import com.raphydaphy.vitality.nbt.NBTLib;
import com.raphydaphy.vitality.network.PacketManager;
import com.raphydaphy.vitality.network.PacketSendKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiSpellSelect extends GuiScreen 
{

	private static final ResourceLocation[] spellTextures = new ResourceLocation[] {
			new ResourceLocation("vitality:textures/items/spell_card_vital_extraction"),
			new ResourceLocation("vitality:textures/items/spell_card_lantern"),
			new ResourceLocation("vitality:textures/items/spell_card_explosion"),
			new ResourceLocation("vitality:textures/items/spell_card_fireball"),
			new ResourceLocation("vitality:textures/items/spell_card_rapidfire"),
			new ResourceLocation("vitality:textures/items/spell_card_transmutation"),
			new ResourceLocation("vitality:textures/items/spell_card_hunger"),
			new ResourceLocation("vitality:textures/items/spell_card_enhanced_extraction"),
			new ResourceLocation("vitality:textures/items/spell_card_flight"),
			new ResourceLocation("vitality:textures/items/spell_card_forcefield")
	};
	
	public ItemStack getSpellStackFromID(int spellID)
	{
		switch (spellID)
		{
		case 80:
			return new ItemStack(ModItems.spell_card_vital_extraction);
		case 81:
			return new ItemStack(ModItems.spell_card_lantern);
		case 82:
			return new ItemStack(ModItems.spell_card_explosion);
		case 83:
			return new ItemStack(ModItems.spell_card_fireball);
		case 84:
			return new ItemStack(ModItems.spell_card_rapidfire);
		case 85:
			return new ItemStack(ModItems.spell_card_transmutation);
		case 86:
			return new ItemStack(ModItems.spell_card_hunger);
		case 87:
			return new ItemStack(ModItems.spell_card_enhanced_extraction);
		case 88:
			return new ItemStack(ModItems.spell_card_flight);
		case 89:
			return new ItemStack(ModItems.spell_card_forcefield);
		}
		return null;
	}

	int timeIn = 0;
	int slotSelected = -1;
	int[] spellArray;
	int spellArrayLength;
	int segments = 0;
	
	ItemStack[] realSpellArray;
	ItemStack wandStack;
	ItemStack bagStack;

	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		super.drawScreen(mx, my, partialTicks);

		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();

		int x = width / 2;
		int y = height / 2;
		int maxRadius = 80;

		boolean mouseIn = true;
		float angle = mouseAngle(x, y, mx, my);

		int highlight = 5;

		wandStack = Minecraft.getMinecraft().thePlayer.getHeldItemMainhand();
		if (!(wandStack.getItem() instanceof ItemWand))
		{
			wandStack = null;
		}
		
		bagStack = Minecraft.getMinecraft().thePlayer.getHeldItemOffhand();
		if (!(bagStack.getItem() instanceof ItemSpellBag))
		{
			bagStack = null;
		}
		
		GlStateManager.enableBlend();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		
		realSpellArray = new ItemStack[10];
		spellArray = bagStack.getTagCompound().getIntArray("spells");
		for (int i = 0; i < spellArray.length; i++)
		{
			if (spellArray[i] != 0)
			{
				segments++;
				spellArrayLength++;
				/*
				for (int counter = 0; i < realSpellArray.length; 
				{
					getSpellStackFromID(spellArray[counter);
				}
				*/
			}
		}
		
		int curRenderID = 0;
		float totalDeg = 0;
		float degPer = 360F / segments;

		List<int[]> stringPositions = new ArrayList();

		for(int seg = 0; seg < segments; seg++) {
			boolean mouseInSector = mouseIn && angle > totalDeg && angle < totalDeg + degPer;
			float radius = Math.max(0F, Math.min((timeIn + partialTicks - seg * 6F / segments) * 40F, maxRadius));

			GL11.glBegin(GL11.GL_TRIANGLE_FAN);

			float gs = 0.25F;
			if(seg % 2 == 0)
				gs += 0.1F;
			float r = gs;
			float g = gs;
			float b = gs;
			float a = 0.4F;
			
			if(mouseInSector) 
			{
				if(wandStack != null && bagStack != null) 
				{
					Color color = Color.CYAN;
					r = color.getRed() / 255F;
					g = color.getGreen() / 255F;
					b = color.getBlue() / 255F;
				}
			}

			GlStateManager.color(r, g, b, a);
			GL11.glVertex2i(x, y);
			stringPositions.clear();
			for(float i = degPer; i >= 0; i--) 
			{
				float rad = (float) ((i + totalDeg) / 180F * Math.PI);
				double xp = x + Math.cos(rad) * radius;
				double yp = y + Math.sin(rad) * radius;
				if(i == (int) (degPer / 2))
				{
					stringPositions.add(new int[] { seg, (int) xp, (int) yp, mouseInSector ? 'n' : 'r' });
					System.out.println("added a new one");
				}

				GL11.glVertex2d(xp, yp);
			}
			totalDeg += degPer;

			GL11.glVertex2i(x, y);
			GL11.glEnd();

			if(mouseInSector)
				radius -= highlight;
		}
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableTexture2D();
		//GlStateManager.scale(1, 1, 1);
		
		for(int[] pos : stringPositions) 
		{
			int xp = pos[1];
			int yp = pos[2];
			char c = (char) pos[3];
			
			ItemStack spellStack = null;
			try
			{
				spellStack = getSpellStackFromID(spellArray[pos[0]]);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				spellStack = null;
			}
			if(spellStack != null) 
			{
				int xsp = xp - 4;
				int ysp = yp;
				String name = "\u00a7" + c + spellStack.getDisplayName();
				int width = fontRendererObj.getStringWidth(name);

				double mod = 0.6;
				int xdp = (int) ((xp - x) * mod + x);
				int ydp = (int) ((yp - y) * mod + y);
				//GlStateManager.scale(1.5, 1.5, 1.5);
				mc.getRenderItem().renderItemIntoGUI(spellStack, xdp - 8, ydp - 8);
				//GlStateManager.scale(1, 1, 1);
				if(xsp < x)
				{
					xsp -= width - 8;
				}
				if(ysp < y)
				{
					ysp -= 9;
				}
				
				fontRendererObj.drawStringWithShadow(name, xsp, ysp, 0xFFFFFF);

				mod = 0.8;
				xdp = (int) ((xp - x) * mod + x);
				ydp = (int) ((yp - y) * mod + y);

				//mc.renderEngine.bindTexture(spellTextures[pos[0]]);
				drawModalRectWithCustomSizedTexture(xdp - 8, ydp - 8, 0, 0, 16, 16, 16, 16);
			}
		}

		float stime = 5F;
		float fract = Math.min(stime, timeIn + partialTicks) / stime;
		float s = 3F * fract;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		RenderHelper.enableGUIStandardItemLighting();
		/*
		if(controlledStacks != null && controlledStacks.length > 0) {
			int xs = width / 2 - 18 * controlledStacks.length / 2;
			int ys = height / 2;

			for(int i = 0; i < controlledStacks.length; i++) {
				float yoff = 25F + maxRadius;
				if(i == controlSlot)
					yoff += 5F;

				GlStateManager.translate(0, -yoff * fract, 0F);
				mc.getRenderItem().renderItemAndEffectIntoGUI(controlledStacks[i], xs + i * 18, ys);
				GlStateManager.translate(0, yoff * fract, 0F);
			}

		}
		
		if(socketableStack != null) {
			GlStateManager.scale(s, s, s);
			GlStateManager.translate(x / s - 8, y / s - 8, 0);
			mc.getRenderItem().renderItemAndEffectIntoGUI(socketableStack, 0, 0);
		}
		*/
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();

		GlStateManager.popMatrix();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		int x = width / 2;
		int y = height / 2;
		//int maxRadius = 80;

		boolean mouseIn = true;
		float angle = mouseAngle(x, y, mouseX, mouseY);
		
		//int curRenderID = 0;
		float totalDeg = 0;
		float degPer = 360F / segments;
		for(int seg = 0; seg < segments; seg++) 
		{
			boolean mouseInSector = mouseIn && angle > totalDeg && angle < totalDeg + degPer;
			if (mouseInSector)
			{
				System.out.println("In sector: " + mouseInSector + " Sector Number: " + seg);
			}
			totalDeg += degPer;
		}
		/*
		if(bagStack != null && spellArrayLength > 0 && wandStack != null) 
		{
			if(mouseButton == 0) 
			{
				NBTLib.setInt(bagStack, "selectedSpell", NBTLib.getInt(bagStack, "selectedSpell", -1) + 1);
				if (NBTLib.getInt(bagStack, "selectedSpell", 0) > spellArrayLength)
				{
					NBTLib.setInt(bagStack, "selectedSpell", 0);
				}
			} 
			else if(mouseButton == 1) 
			{
				NBTLib.setInt(bagStack, "selectedSpell", NBTLib.getInt(bagStack, "selectedSpell", 0) - 1);
				if (NBTLib.getInt(bagStack, "selectedSpell", 0) < 0)
				{
					NBTLib.setInt(bagStack, "selectedSpell", 0);
				}
			}
		}
		*/
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		if((KeyBindings.pickSpell.isPressed())) 
		{
			mc.displayGuiScreen(null);
			if(slotSelected != -1) 
			{
				/*
				int slot = slots.get(slotSelected);
				PlayerDataHandler.get(mc.thePlayer).stopLoopcast();

				NetworkMessage message = null;
				if(controllerStack != null)
					message = new MessageChangeControllerSlot(controlSlot, slot);
				else message = new MessageChangeSocketableSlot(slot);
				*/
				PacketManager.INSTANCE.sendToServer(new PacketSendKey());
			}
		}

		ImmutableSet<KeyBinding> set = ImmutableSet.of(mc.gameSettings.keyBindForward, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindSneak, mc.gameSettings.keyBindSprint, mc.gameSettings.keyBindJump);
		for(KeyBinding k : set)
			KeyBinding.setKeyBindState(k.getKeyCode(), isKeyDown(k));

		timeIn++;
	}

	public boolean isKeyDown(KeyBinding keybind) {
		int key = keybind.getKeyCode();
		if(key < 0) {
			int button = 100 + key;
			return Mouse.isButtonDown(button);
		}
		return Keyboard.isKeyDown(key);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private static float mouseAngle(int x, int y, int mx, int my) {
		Vector2f baseVec = new Vector2f(1F, 0F);
		Vector2f mouseVec = new Vector2f(mx - x, my - y);

		float ang = (float) (Math.acos(Vector2f.dot(baseVec, mouseVec) / (baseVec.length() * mouseVec.length())) * (180F / Math.PI));
		return my < y ? 360F - ang : ang;
	}

}