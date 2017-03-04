package com.raphydaphy.vitality.proxy;

import com.raphydaphy.vitality.init.WorldGenHandler;
import com.raphydaphy.vitality.network.MessageActionText;
import com.raphydaphy.vitality.network.MessageChangeSpell;
import com.raphydaphy.vitality.network.PacketManager;
import com.raphydaphy.vitality.recipe.ModRecipies;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	/*
	 * Initializes items, blocks and all other required things Dosen't manage
	 * crafting recipies or smelting
	 */
	public void preInit(FMLPreInitializationEvent e) {
		GameRegistry.registerWorldGenerator(new WorldGenHandler(), 2);
		int disc = 0;
		PacketManager.INSTANCE.registerMessage(new MessageChangeSpell.ChangeSpellHandler(), MessageChangeSpell.class,
				++disc, Side.SERVER);
		PacketManager.INSTANCE.registerMessage(new MessageActionText.MessageHandler(), MessageActionText.class, ++disc,
				Side.CLIENT);
	}

	/*
	 * Used to initialize crafting and smelting recipies
	 */
	public void init(FMLInitializationEvent e) {
		ModRecipies.registerOreDict();
		ModRecipies.registerCrafting();
		ModRecipies.registerSmelting();
	}

	/*
	 * Currently unused as nothing needs to be initialized in post
	 */
	public void postInit(FMLPostInitializationEvent e) {
	}
}
