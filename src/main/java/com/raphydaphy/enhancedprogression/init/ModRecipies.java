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
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.imbued_plank, 4), ModBlocks.imbued_log);
	
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.basic_wand_copper), "  C", " S ", "S  ", 'S', Items.STICK, 'C', ModItems.ingot_copper);
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.basic_wand_tin), "  C", " S ", "S  ", 'S', Items.STICK, 'C', ModItems.ingot_tin);
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.advanced_wand), "  B", " S ", "S  ", 'S', ModItems.imbued_stick, 'B', ModItems.ingot_bronze_imbued);
		
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.altar), "D D", "DED", " D ", 'D', Items.DIAMOND, 'E', Blocks.ENCHANTING_TABLE);
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.imbued_stick, 4), "P", "P", 'P', ModBlocks.imbued_plank);
		
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_lantern), "SCS", "CBC", "SCS", 'S', Items.STICK, 'C', Items.COAL, 'B', ModItems.ingot_bronze);
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_explosion), "SGS", "GBG", "SGS", 'S', Items.STICK, 'G', Items.GUNPOWDER, 'B', ModItems.ingot_bronze);
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_rapidfire), "SAS", "ABA", "AGA", 'S', ModItems.imbued_stick, 'A', Items.ARROW, 'B', ModItems.ingot_bronze_imbued);
	}
	
	public static void registerSmelting()
	{
		GameRegistry.addSmelting(ModBlocks.ore_copper, new ItemStack(ModItems.ingot_copper), 0.7f);
		GameRegistry.addSmelting(ModBlocks.ore_tin, new ItemStack(ModItems.ingot_tin), 0.7f);
	} 
}
