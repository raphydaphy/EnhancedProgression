package com.raphydaphy.vitality.proxy;

import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.init.WorldGenHandler;

import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy 
{
	/*
	 * Used in ClientProxy to register items and blocks textures
	 */
	public void registerItemRenderer(Item item, int meta, String id) {}
	
	/*
	 * Initializes items, blocks and all other required things
	 * Dosen't manage crafting recipies or smelting
	 */
	public void preInit()
	{
		ModBlocks.init();
		ModItems.init();
		
		GameRegistry.registerWorldGenerator(new WorldGenHandler(), 2);
	}
	
	/*
	 * Used to initialize crafting and smelting recipies
	 */
	public void init() {}
	
	/*
	 * Currently unused as nothing needs to be initialized in post
	 */
	public void postInit() {}
	
	/*
	 * Used to display a message in the record area of the chat
	 * Contents set in ClientProxy since it is client-side only
	 */
	public void setActionText(String text) {}
	
	/*
	 * Alternate version of the function that also accepts a color
	 */
	public void setActionText(String text, TextFormatting color) {}
}
