package com.raphydaphy.enhancedprogression.init;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin 
{

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void register(IModRegistry registry) 
	{
		  registry.addDescription(new ItemStack(ModBlocks.altar), "Used to craft Imbued items, an altar is a multiblock structure centered around the altar block itself.");
		  registry.addDescription(new ItemStack(ModBlocks.imbued_log), "Crafted with three gold nuggets, one log and two imbued bronze ingots in a tier 2+ altar, Imbued Logs are used for higher tier crafting recipes.");
		  registry.addDescription(new ItemStack(ModItems.ingot_bronze_imbued), "Crafted with four gold, four bronze and four iron ingots in an altar of any tier, Imbued Bronze Ingots are the first item craftable in an altar and are used for higher tier altar and crafting recipes.");
		  registry.addDescription(new ItemStack(ModItems.basic_wand_tin), "The Basic Wand is a fragile wand constructed in a simple crafting table. Due to the poor quality of the materials used to craft it, it can snap at a moments notice. However, if you are willing to use such a wand, you can right click on a tree to fill it with essence. Shift + Rightclicking at any time will tell you how much essence you currently have stored in the wand, and rightclicking on an altar will start crafting an item if the correct items are in the inventory. Shift + Rightclick on an altar to see what tier it is.");
		  registry.addDescription(new ItemStack(ModItems.basic_wand_copper), "The Basic Wand is a fragile wand constructed in a simple crafting table. Due to the poor quality of the materials used to craft it, it can snap at a moments notice. However, if you are willing to use such a wand, you can right click on a tree to fill it with essence. Shift + Rightclicking at any time will tell you how much essence you currently have stored in the wand, and rightclicking on an altar will start crafting an item if the correct items are in the inventory. Shift + Rightclick on an altar to see what tier it is.");
		  registry.addDescription(new ItemStack(ModItems.advanced_wand), "By infusing your wand with Imbued Bronze, you unlock more power and capacity. The Advanced Wand cannot break when extracting essence, and can store 10000 essence instead of a mere 1000 in the Basic Wand.");
		  registry.addDescription(new ItemStack(ModItems.imbued_stick), "Imbued Sticks are a crafting regent item used for spell cards, higher tier wands and other Advanced tier Enhanced Progression tech.");
		  registry.addDescription(new ItemStack(ModItems.ingot_bronze), "Bronze ingots are obtained in a mere crafting table and used to craft some basic items used to improve your bas");
		  registry.addDescription(new ItemStack(ModItems.ingot_copper), "The most basic metal. Used to craft bronze and basic wands.");
		  registry.addDescription(new ItemStack(ModItems.ingot_tin), "A simple metal used to craft bronze and basic wands.");
		  registry.addDescription(new ItemStack(ModBlocks.totem_bottom), "Unimplemented at the moment. It looks kind of cool tho.");
		  registry.addDescription(new ItemStack(ModBlocks.totem_top), "Unimplemented at the moment. It looks kind of cool tho.");
		  registry.addDescription(new ItemStack(ModBlocks.totem_middle), "Unimplemented at the moment. It looks kind of cool tho.");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) 
	{
		// TODO Auto-generated method stub

	}

}
