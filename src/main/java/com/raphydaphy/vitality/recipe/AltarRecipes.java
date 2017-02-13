package com.raphydaphy.vitality.recipe;

import com.raphydaphy.vitality.init.ConfigHandler;
import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.nbt.AltarRecipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AltarRecipes
{
	public static AltarRecipe recipeImbuedBronze;
	public static AltarRecipe recipeBulkImbuedBronze;
	public static AltarRecipe recipeImbuedLog;
	public static AltarRecipe recipeFluxedIngot;
	public static AltarRecipe recipeFluxedLog;

	public static void init()
	{
		if (ConfigHandler.crafting.enableExpertMode)
		{
			recipeImbuedBronze = ModRecipes.registerAltarRecipe(1, new ItemStack(ModItems.ingot_bronze_imbued),
					"ingotBronze", "ingotBronze", "ingotBronze", "ingotBronze", "ingotSilver", "ingotSilver", "ingotSilver",
					"ingotSilver", "ingotDawnstone", "ingotDawnstone", "ingotDawnstone", "ingotDawnstone");
			
			recipeBulkImbuedBronze = ModRecipes.registerAltarRecipe(1, new ItemStack(ModItems.ingot_bronze_imbued, 9), 
					"blockBronze", "blockBronze", "blockBronze", "blockBronze", "blockSilver", "blockSilver", 
					"blockSilver", "blockSilver", "blockDawnstone", "blockDawnstone", "blockDawnstone", "blockDawnstone");
	
			recipeImbuedLog = ModRecipes.registerAltarRecipe(2, new ItemStack(ModBlocks.imbued_log), "logWood",
					new ItemStack(ModItems.ingot_bronze_imbued), new ItemStack(ModItems.ingot_bronze_imbued), "manaDiamond",
					"manaDiamond", "manaDiamond");
	
			recipeFluxedIngot = ModRecipes.registerAltarRecipe(3, new ItemStack(ModItems.ingot_fluxed), Items.END_CRYSTAL,
					Items.END_CRYSTAL, Items.END_CRYSTAL, Items.END_CRYSTAL, "manaDiamond", "manaDiamond", "manaDiamond", "manaDiamond",
					new ItemStack(ModItems.ingot_bronze_imbued), new ItemStack(ModItems.ingot_bronze_imbued),
					new ItemStack(ModItems.ingot_bronze_imbued), new ItemStack(ModItems.ingot_bronze_imbued));
	
			recipeFluxedLog = ModRecipes.registerAltarRecipe(3, new ItemStack(ModBlocks.fluxed_log),
					new ItemStack(ModBlocks.imbued_log), new ItemStack(ModItems.ingot_fluxed),
					new ItemStack(ModItems.ingot_fluxed), "ingotDraconiumAwakened", "ingotDraconiumAwakened", "ingotDraconiumAwakened");
		}
		else
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
}
