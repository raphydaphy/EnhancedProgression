package com.raphydaphy.vitality.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.ImmutableSet;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.item.ItemSpell;
import com.raphydaphy.vitality.item.ItemWand;
import com.raphydaphy.vitality.network.MessageChangeSpell;
import com.raphydaphy.vitality.network.PacketManager;
import com.raphydaphy.vitality.registry.KeyBindings;
import com.raphydaphy.vitality.util.RenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GUISpellSelect extends GuiScreen {
	private int activeSector = -1;
	private List<Integer> spells = new ArrayList<Integer>();

	private ItemStack wandStack;

	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		super.drawScreen(mx, my, partialTicks);

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		wandStack = player.getHeldItemMainhand();
		if (wandStack == null || !wandStack.hasTagCompound() || !(wandStack.getItem() instanceof ItemWand))
			wandStack = player.getHeldItemOffhand();
		if (wandStack == null || !wandStack.hasTagCompound() || !(wandStack.getItem() instanceof ItemWand))
			return;

		if (Minecraft.getMinecraft().theWorld.getTotalWorldTime() % 50 == 0 || spells.size() == 0) {
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				// get the currently selected item in the players inventory
				ItemStack stackAt = player.inventory.getStackInSlot(i);
				// check that the current stack isnt null to prevent
				// NullPointerExceptions
				if (stackAt != null) {
					if (stackAt.getItem() instanceof ItemSpell) {
						if (!(spells.contains(((ItemSpell) stackAt.getItem()).getSpellId()))) {
							spells.add(((ItemSpell) stackAt.getItem()).getSpellId());
						}

					}
				}
			}
		}
		if (spells.size() > 0) {

			float angle = -90;
			int radius = 75;
			int amount = spells.size();

			int screenWidth = width / 2;
			int screenHeight = height / 2;

			// if any spells are held in the bag
			if (amount > 0) {
				float anglePer;
				if (!wandStack.hasTagCompound()) {
					return;
				}
				if (wandStack.getTagCompound().getInteger(Spell.ACTIVE_KEY) != -1) {
					anglePer = 360F / (amount - 1);
				} else {
					anglePer = 360F / amount;
				}

				net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
				GlStateManager.pushMatrix();
				RenderHelper.renderCircle(screenWidth, screenHeight);
				GlStateManager.popMatrix();
				activeSector = -1;
				for (int curItem = 0; curItem < spells.size(); curItem++) {
					Item icon = Spell.spellMap.get(spells.get(curItem)).getIcon();
					if (spells.get(curItem) == wandStack.getTagCompound().getInteger(Spell.ACTIVE_KEY)) {
						GlStateManager.pushMatrix();
						GlStateManager.translate(screenWidth - 16, screenHeight - 16, 0);
						GlStateManager.scale(2, 2, 2);
						if (wandStack.getTagCompound().getInteger(Spell.ACTIVE_KEY) != -1) {
							if (icon != null) {
								mc.getRenderItem().renderItemIntoGUI(new ItemStack(icon), 0, 0);
							}
						}
						GlStateManager.translate(-screenWidth, -screenHeight, 0);
						GlStateManager.popMatrix();
					} else {
						double xPos = screenWidth + Math.cos(angle * Math.PI / 180D) * radius - 13.6;
						double yPos = screenHeight + Math.sin(angle * Math.PI / 180D) * radius - 13.6;
						// i got no clue what this shit does its been half a
						// year since i wrote it xD
						if (mx > xPos && mx < xPos + 27.2 && my > yPos && my < yPos + 27.2) {
							activeSector = curItem;
							GlStateManager.pushMatrix();
							GlStateManager.translate(xPos, yPos, 0);
							GlStateManager.scale(1.7, 1.7, 1.7);

							if (icon != null) {

								mc.getRenderItem().renderItemIntoGUI(new ItemStack(icon), 0, 0);
							}
							GlStateManager.translate(-xPos, -yPos, 0);
							GlStateManager.popMatrix();
						} else {
							xPos = screenWidth + Math.cos(angle * Math.PI / 180D) * radius - 12;
							yPos = screenHeight + Math.sin(angle * Math.PI / 180D) * radius - 12;
							GlStateManager.pushMatrix();
							GlStateManager.translate(xPos, yPos, 0);
							GlStateManager.scale(1.5, 1.5, 1.5);
							if (icon != null) {
								mc.getRenderItem().renderItemIntoGUI(new ItemStack(icon), 0, 0);
							}
							GlStateManager.translate(-xPos, -yPos, 0);
							GlStateManager.popMatrix();
						}
						angle += anglePer;
					}
				}
				net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (activeSector != -1) {
			if (Minecraft.getMinecraft().thePlayer.getHeldItemMainhand() != null) {
				if (Minecraft.getMinecraft().thePlayer.getHeldItemMainhand().getItem() instanceof ItemWand) {
					Minecraft.getMinecraft().thePlayer.getHeldItemMainhand().getTagCompound()
							.setInteger(Spell.ACTIVE_KEY, spells.get(activeSector));
				}
			}
			PacketManager.INSTANCE.sendToServer(new MessageChangeSpell(spells.get(activeSector)));
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (!isKeyDown(KeyBindings.pickSpell)) {
			mc.displayGuiScreen(null);
		}

		ImmutableSet<KeyBinding> set = ImmutableSet.of(mc.gameSettings.keyBindForward, mc.gameSettings.keyBindLeft,
				mc.gameSettings.keyBindBack, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindSneak,
				mc.gameSettings.keyBindSprint, mc.gameSettings.keyBindJump);
		for (KeyBinding k : set) {
			KeyBinding.setKeyBindState(k.getKeyCode(), isKeyDown(k));
		}
	}

	public boolean isKeyDown(KeyBinding keybind) {
		int key = keybind.getKeyCode();
		if (key < 0) {
			int button = 100 + key;
			return Mouse.isButtonDown(button);
		}
		return Keyboard.isKeyDown(key);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}