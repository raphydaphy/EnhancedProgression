package com.raphydaphy.vitality.recipe;

import com.raphydaphy.vitality.init.ConfigHandler;
import com.raphydaphy.vitality.util.shadows.registry.ModBlocks;
import com.raphydaphy.vitality.util.shadows.registry.ModItems;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipies {
	private static void addOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}

	public static void registerOreDict() {
		if (ConfigHandler.world.enableCopperAndTin) {
			OreDictionary.registerOre("oreCopper", ModBlocks.ORE_COPPER);
			OreDictionary.registerOre("oreTin", ModBlocks.ORE_TIN);
		}
	}

	public static void registerCrafting() {
		addOreDictRecipe(new ItemStack(ModItems.EXTRACTION_ROD), "  Y", " X ", "X  ", 'X', "stickWood", 'Y', "dye");
		addOreDictRecipe(new ItemStack(ModItems.TECHNICIANS_ROD), "  Y", " X ", "X  ", 'X', "stickWood", 'Y',
				Items.REDSTONE);
		addOreDictRecipe(new ItemStack(ModItems.TRANSMUTATION_ROD), "  Y", " X ", "X  ", 'X', "stickWood", 'Y',
				Items.WATER_BUCKET);

		CraftingManager.getInstance().addRecipe(new RecipeWand());
	}

	public static void registerSmelting() {
		if (ConfigHandler.world.enableCopperAndTin) {
			GameRegistry.addSmelting(ModBlocks.ORE_COPPER, new ItemStack(ModItems.INGOT_COPPER), 0.7f);
			GameRegistry.addSmelting(ModBlocks.ORE_TIN, new ItemStack(ModItems.INGOT_TIN), 0.7f);
		}

		GameRegistry.addSmelting(ModBlocks.ORE_ANGELIC_CRYSTAL, new ItemStack(ModItems.CRYSTAL_SHARD_ANGELIC, 4), 3.0f);
		GameRegistry.addSmelting(ModBlocks.ORE_EXOTIC_CRYSTAL, new ItemStack(ModItems.CRYSTAL_SHARD_EXOTIC, 4), 6.0f);
	}
}