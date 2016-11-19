package com.raphydaphy.enhancedprogression.recipe;

import com.raphydaphy.enhancedprogression.init.ModBlocks;
import com.raphydaphy.enhancedprogression.init.ModItems;
import com.raphydaphy.enhancedprogression.nbt.AltarRecipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AltarRecipes {
	public static AltarRecipe recipeImbuedBronze;
	public static AltarRecipe recipeImbuedLog;
	
	public static void init()
	{
		recipeImbuedBronze = ModRecipes.registerAltarRecipe(1,
				new ItemStack(ModItems.ingot_bronze_imbued), new ItemStack(ModItems.ingot_bronze), new ItemStack(ModItems.ingot_bronze), new ItemStack(ModItems.ingot_bronze), new ItemStack(ModItems.ingot_bronze), 
				new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT), 
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT));
		
		recipeImbuedLog = ModRecipes.registerAltarRecipe(2,
				new ItemStack(ModBlocks.imbued_log), new ItemStack(Blocks.LOG), new ItemStack(ModItems.ingot_bronze_imbued), new ItemStack(ModItems.ingot_bronze_imbued),
				new ItemStack(Items.GOLD_NUGGET), new ItemStack(Items.GOLD_NUGGET), new ItemStack(Items.GOLD_NUGGET));
	}
}
