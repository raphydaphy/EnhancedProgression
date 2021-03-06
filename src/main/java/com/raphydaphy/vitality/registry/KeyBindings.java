package com.raphydaphy.vitality.registry;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyBindings {

	public static KeyBinding pickSpell;

	public static void init() {
		pickSpell = new KeyBinding("key.pickSpell", Keyboard.KEY_G, "key.categories.vitality");
		ClientRegistry.registerKeyBinding(pickSpell);
	}
}