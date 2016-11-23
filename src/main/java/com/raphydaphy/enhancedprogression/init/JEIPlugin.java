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
		  registry.addDescription(new ItemStack(ModBlocks.totem_bottom), "Unimplemented.");
		  registry.addDescription(new ItemStack(ModBlocks.totem_top), "Unimplemented.");
		  registry.addDescription(new ItemStack(ModBlocks.totem_middle), "Unimplemented.");
		  registry.addDescription(new ItemStack(ModItems.upgrade_template_basic), "Unimplemented.");
		  registry.addDescription(new ItemStack(ModItems.spell_card_lantern), "This simple spell can place a torch at the cost of 5 essence. Used by either placing the spell card in your offhand with a wand in your main hand, or by infusing it with a wand in an altar to add it to the wands inventory. Note that only Advanced+ wands can be infused with spells and Basic wands require the spell to be in the offhand always.");
		  registry.addDescription(new ItemStack(ModItems.spell_card_explosion), "For the cost of 50 essence, this spell will create an explosion on the block you aim it at. Due to the essence imbued into the explosion, this blast will not cause any terrain damage and will not hurt the person who casts the spell.");
		  registry.addDescription(new ItemStack(ModItems.spell_card_explosion), "For 100 essence, this spell will shoot arrows in a straight line from your cursor for one essence per arrow. With each arrow shot in a row, the cost incrases by one, so by the 50th arrow it would cost 50 essence. The arrows will continue to shoot until you stop holding rightclick or until you run out of essence, whichever happens first.");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) 
	{
		// TODO Auto-generated method stub

	}

}
