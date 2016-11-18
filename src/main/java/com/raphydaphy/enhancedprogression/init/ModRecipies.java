package com.raphydaphy.enhancedprogression.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipies 
{
	public static void registerCrafting()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ingot_bronze, 4), ModItems.ingot_tin, ModItems.ingot_copper, ModItems.ingot_copper, ModItems.ingot_copper);
	
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.basic_wand_copper), "  C", " S ", "S  ", 'S', Items.STICK, 'C', ModItems.ingot_copper);
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.basic_wand_tin), "  C", " S ", "S  ", 'S', Items.STICK, 'C', ModItems.ingot_tin);
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.altar), "D D", "DED", " D ", 'D', Items.DIAMOND, 'E', Blocks.ENCHANTING_TABLE);
	}
	
	public static void registerSmelting()
	{
		GameRegistry.addSmelting(ModBlocks.ore_copper, new ItemStack(ModItems.ingot_copper), 0.7f);
		GameRegistry.addSmelting(ModBlocks.ore_tin, new ItemStack(ModItems.ingot_tin), 0.7f);
	}
}
