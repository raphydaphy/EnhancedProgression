package com.raphydaphy.vitality.proxy;

import com.raphydaphy.vitality.init.Events;
import com.raphydaphy.vitality.init.WorldGenHandler;
import com.raphydaphy.vitality.recipe.ModRecipies;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	/*
	 * Initializes items, blocks and all other required things Dosen't manage
	 * crafting recipies or smelting
	 */
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new Events());
		GameRegistry.registerWorldGenerator(new WorldGenHandler(), 2);
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
