package com.raphydaphy.vitality.recipe;

import com.raphydaphy.vitality.init.ConfigHandler;
import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.init.ModItems;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipies 
{
	private static void addOreDictRecipe(ItemStack output, Object... recipe)
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}
	
	public static void registerOreDict()
	{
		if (ConfigHandler.world.enableCopperAndTin)
		{
			OreDictionary.registerOre("oreCopper", ModBlocks.ore_copper);
			OreDictionary.registerOre("oreTin", ModBlocks.ore_tin);
		}
	}
	
	public static void registerCrafting()
	{
		addOreDictRecipe(new ItemStack(ModItems.life_extraction_rod), "X  ", " X ", "  Y", 'X', "stickWood", 'Y', "dye");
		addOreDictRecipe(new ItemStack(ModItems.technicians_rod), "X  ", " X ", "  Y", 'X', "stickWood", 'Y', Items.REDSTONE);
		addOreDictRecipe(new ItemStack(ModItems.transmutation_rod), "X  ", " X ", "  Y", 'X', "stickWood", 'Y', Items.WATER_BUCKET);
		
		addOreDictRecipe(new ItemStack(ModItems.wooden_demonic_wand), "Y  ", " X ", "  Y", 'X', ModItems.wand_core_demonic, 'Y', ModItems.wand_tip_wooden);
	}
	
	public static void registerSmelting()
	{
		if (ConfigHandler.world.enableCopperAndTin)
		{
			GameRegistry.addSmelting(ModBlocks.ore_copper, new ItemStack(ModItems.ingot_copper), 0.7f);
			GameRegistry.addSmelting(ModBlocks.ore_tin, new ItemStack(ModItems.ingot_tin), 0.7f);
		}
	}
}