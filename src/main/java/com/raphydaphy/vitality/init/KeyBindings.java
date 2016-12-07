package com.raphydaphy.vitality.init;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindings {

    public static KeyBinding pickSpell;

    public static void init() 
    {
    	System.out.println("Registered key bindings..");
        pickSpell = new KeyBinding("key.pickSpell", Keyboard.KEY_O, "key.categories.vitality");
        ClientRegistry.registerKeyBinding(pickSpell);
    }
}