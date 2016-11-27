package com.raphydaphy.enhancedprogression.recipe;

import com.raphydaphy.enhancedprogression.init.ModBlocks;
import com.raphydaphy.enhancedprogression.init.ModItems;
import com.raphydaphy.enhancedprogression.nbt.AltarRecipe;

import net.minecraft.item.ItemStack;

public class AltarRecipes
{
	public static AltarRecipe recipeImbuedBronze;
	public static AltarRecipe recipeImbuedLog;
	public static AltarRecipe recipeFluxedIngot;
	public static AltarRecipe recipeFluxedLog;

	public static void init()
	{
		recipeImbuedBronze = ModRecipes.registerAltarRecipe(1, new ItemStack(ModItems.ingot_bronze_imbued),
				"ingotBronze", "ingotBronze", "ingotBronze", "ingotBronze", "ingotGold", "ingotGold", "ingotGold",
				"ingotGold", "ingotIron", "ingotIron", "ingotIron", "ingotIron");

		recipeImbuedLog = ModRecipes.registerAltarRecipe(2, new ItemStack(ModBlocks.imbued_log), "logWood",
				new ItemStack(ModItems.ingot_bronze_imbued), new ItemStack(ModItems.ingot_bronze_imbued), "nuggetGold",
				"nuggetGold", "nuggetGold");

		recipeFluxedIngot = ModRecipes.registerAltarRecipe(3, new ItemStack(ModItems.ingot_fluxed), "netherStar",
				"netherStar", "netherStar", "netherStar", "gemDiamond", "gemDiamond", "gemDiamond", "gemDiamond",
				new ItemStack(ModItems.ingot_bronze_imbued), new ItemStack(ModItems.ingot_bronze_imbued),
				new ItemStack(ModItems.ingot_bronze_imbued), new ItemStack(ModItems.ingot_bronze_imbued));

		recipeFluxedLog = ModRecipes.registerAltarRecipe(3, new ItemStack(ModBlocks.fluxed_log),
				new ItemStack(ModBlocks.imbued_log), new ItemStack(ModItems.ingot_fluxed),
				new ItemStack(ModItems.ingot_fluxed), "gemDiamond", "gemDiamond", "gemDiamond");
	}
}
