package com.raphydaphy.vitality.recipe;

import com.raphydaphy.vitality.init.ConfigHandler;
import com.raphydaphy.vitality.registry.ModBlocks;
import com.raphydaphy.vitality.registry.ModItems;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipies {
	private static void addOreDictRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
	}

	public static void addShapelessOreRecipe(ItemStack output, Object... recipe) {
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, recipe));
	}

	public static void registerOreDict() {
		if (ConfigHandler.world.enableCopperAndTin) {
			OreDictionary.registerOre("oreCopper", ModBlocks.ORE_COPPER);
			OreDictionary.registerOre("oreTin", ModBlocks.ORE_TIN);
		}
		OreDictionary.registerOre("ingotTin", ModItems.INGOT_TIN);
		OreDictionary.registerOre("ingotCopper", ModItems.INGOT_COPPER);
		OreDictionary.registerOre("ingotBronze", ModItems.INGOT_BRONZE);

		OreDictionary.registerOre("itemBasicFuel", Items.COAL);
		OreDictionary.registerOre("itemBasicFuel", new ItemStack(Items.COAL, 1, 1));
	}

	public static void registerCrafting() {
		addShapelessOreRecipe(new ItemStack(ModItems.INGOT_BRONZE, 4), "ingotCopper", "ingotCopper", "ingotCopper",
				"ingotTin");

		addOreDictRecipe(new ItemStack(ModItems.EXTRACTION_ROD), "  Y", " X ", "X  ", 'X', "stickWood", 'Y', "dye");
		addOreDictRecipe(new ItemStack(ModItems.TECHNICIANS_ROD), "  Y", " X ", "X  ", 'X', "stickWood", 'Y',
				Items.REDSTONE);
		addOreDictRecipe(new ItemStack(ModItems.TRANSMUTATION_ROD), "  Y", " X ", "X  ", 'X', "stickWood", 'Y',
				Items.WATER_BUCKET);

		addOreDictRecipe(new ItemStack(ModItems.CORE_ANGELIC), "A  ", " S ", "  A", 'A',
				new ItemStack(ModItems.CRYSTAL_SHARD_ANGELIC), 'S', "stickWood");
		addOreDictRecipe(new ItemStack(ModItems.CORE_ATMOSPHERIC), "A  ", " S ", "  A", 'A',
				new ItemStack(Items.POTIONITEM, 1, OreDictionary.WILDCARD_VALUE), 'S', "stickWood");
		addOreDictRecipe(new ItemStack(ModItems.CORE_DEMONIC), "A  ", " S ", "  A", 'A',
				new ItemStack(Items.LAVA_BUCKET), 'S', "stickWood");
		addOreDictRecipe(new ItemStack(ModItems.CORE_ENERGETIC), "A  ", " S ", "  A", 'A',
				new ItemStack(Items.ROTTEN_FLESH), 'S', "stickWood");
		addOreDictRecipe(new ItemStack(ModItems.CORE_EXOTIC), "A  ", " S ", "  A", 'A',
				new ItemStack(ModItems.CRYSTAL_SHARD_EXOTIC), 'S', "stickWood");

		addOreDictRecipe(new ItemStack(ModItems.TIP_WOODEN), "WWW", "W W", 'W', "plankWood");

		addOreDictRecipe(new ItemStack(ModItems.SPELL_EXCAVATION), " B ", "BPB", " B ", 'B', "ingotBronze", 'P',
				Items.IRON_PICKAXE);
		addOreDictRecipe(new ItemStack(ModItems.SPELL_EXPLOSION), " B ", "BPB", " B ", 'B', "ingotBronze", 'P',
				"gunpowder");
		addOreDictRecipe(new ItemStack(ModItems.SPELL_FIREBALL), " B ", "BPB", " B ", 'B', "ingotBronze", 'P',
				Items.FIRE_CHARGE);
		addOreDictRecipe(new ItemStack(ModItems.SPELL_ILLUMINATION), " B ", "BPB", " B ", 'B', "ingotBronze", 'P',
				"itemBasicFuel");

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