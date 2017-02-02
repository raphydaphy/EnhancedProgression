package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.recipe.RecipeSpellBag;

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
		OreDictionary.registerOre("justALumpOfCoalPlzIgnore", Items.COAL);
		OreDictionary.registerOre("justALumpOfCoalPlzIgnore", new ItemStack(Items.COAL, 1, 1));
		OreDictionary.registerOre("powderBlaze", Items.BLAZE_POWDER);
		OreDictionary.registerOre("fireCharge", Items.FIRE_CHARGE);
		
		OreDictionary.registerOre("oreCopper", ModBlocks.ore_copper);
		OreDictionary.registerOre("oreTin", ModBlocks.ore_tin);
		OreDictionary.registerOre("ingotCopper", ModItems.ingot_copper);
		OreDictionary.registerOre("ingotTin", ModItems.ingot_tin);
		OreDictionary.registerOre("ingotBronze", ModItems.ingot_bronze);
	}

	public static void registerCrafting()
	{
		addOreDictRecipe(new ItemStack(ModItems.basic_wand_copper), "  C", " S ", "S  ", 'S', "stickWood", 'C',
				"ingotCopper");
		addOreDictRecipe(new ItemStack(ModItems.basic_wand_tin), "  C", " S ", "S  ", 'S', "stickWood", 'C',
				"ingotTin");
		if (ConfigHandler.crafting.enableMagicLantern)
		{
			addOreDictRecipe(new ItemStack(ModItems.spell_card_lantern_1), "SCS", "CBC", "SCS", 'S', "stickWood", 'C',
					"justALumpOfCoalPlzIgnore", 'B', "ingotBronze");
			addOreDictRecipe(new ItemStack(ModItems.spell_card_lantern_2), "SCS", "CBC", "SCS", 'S', ModItems.imbued_stick, 'C',
					ModItems.spell_card_lantern_1, 'B', ModItems.ingot_bronze_imbued);
			addOreDictRecipe(new ItemStack(ModItems.spell_card_lantern_3), "SCS", "CBC", "SCS", 'S', ModItems.fluxed_stick, 'C',
					ModItems.spell_card_lantern_2, 'B', ModItems.ingot_fluxed);
		}
		if (ConfigHandler.crafting.enableVitalExtraction)
		{
			addOreDictRecipe(new ItemStack(ModItems.spell_card_vital_extraction), "SLS", "LBL", "SLS", 'S', "stickWood",
					'L', "logWood", 'B', "ingotBronze");
		}
		if (ConfigHandler.crafting.enableContainedExplosion)
		{
			addOreDictRecipe(new ItemStack(ModItems.spell_card_explosion_1), "SGS", "GBG", "SGS", 'S', "stickWood", 'G',
					"gunpowder", 'B', "ingotBronze");
			
			addOreDictRecipe(new ItemStack(ModItems.spell_card_explosion_2), "SGS", "GBG", "SGS", 'S', ModItems.imbued_stick, 'G',
					ModItems.spell_card_explosion_1, 'B', ModItems.ingot_bronze_imbued);
			
			addOreDictRecipe(new ItemStack(ModItems.spell_card_explosion_3), "SGS", "GBG", "SGS", 'S', ModItems.fluxed_stick, 'G',
					ModItems.spell_card_explosion_2, 'B', ModItems.ingot_fluxed);
		}
		if (ConfigHandler.crafting.enableRadiantFireball)
		{
			addOreDictRecipe(new ItemStack(ModItems.spell_card_fireball_1), "SFS", "FBF", "SFS", 'S', "stickWood", 'F',
					"fireCharge", 'B', "ingotBronze");
			addOreDictRecipe(new ItemStack(ModItems.spell_card_fireball_2), "SFS", "FBF", "SFS", 'S', ModItems.imbued_stick, 'F',
					ModItems.spell_card_fireball_1, 'B', ModItems.ingot_bronze_imbued);
			addOreDictRecipe(new ItemStack(ModItems.spell_card_fireball_3), "SFS", "FBF", "SFS", 'S', ModItems.fluxed_stick, 'F',
					ModItems.spell_card_fireball_2, 'B', ModItems.ingot_fluxed);
		}
		if (ConfigHandler.crafting.enableCrypticTransmutation)
		{
			addOreDictRecipe(new ItemStack(ModItems.spell_card_transmutation), "SPS", "PBP", "SPS", 'S', "stickWood",
					'P', "powderBlaze", 'B', "ingotBronze");
		}

		GameRegistry.addShapedRecipe(new ItemStack(ModItems.advanced_wand), "  B", " S ", "S  ", 'S',
				ModItems.imbued_stick, 'B', ModItems.ingot_bronze_imbued);
		
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.master_wand), "  I", " S ", "S  ", 'S',
				ModItems.fluxed_stick, 'I', ModItems.ingot_fluxed);

		if (ConfigHandler.crafting.enableBronzeCrafting)
		{
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ingot_bronze, 4), ModItems.ingot_tin,
					ModItems.ingot_copper, ModItems.ingot_copper, ModItems.ingot_copper);
		}
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.imbued_plank, 4), ModBlocks.imbued_log);
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.imbued_stick, 4), "P", "P", 'P', ModBlocks.imbued_plank);
		
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.fluxed_plank, 4), ModBlocks.fluxed_log);
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.fluxed_stick, 4), "P", "P", 'P', ModBlocks.fluxed_plank);
		
		CraftingManager.getInstance().addRecipe(new RecipeSpellBag());
		
		if (ConfigHandler.crafting.enableRapidfire)
		{
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_rapidfire), "SAS", "ABA", "SAS", 'S',
					ModItems.imbued_stick, 'A', Items.ARROW, 'B', ModItems.ingot_bronze_imbued);
		}
		if (ConfigHandler.crafting.enableHunger)
		{
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_hunger), "SRS", "RBR", "SRS", 'S',
					ModItems.imbued_stick, 'R', Items.ROTTEN_FLESH, 'B', ModItems.ingot_bronze_imbued);
		}
		if (ConfigHandler.crafting.enableEnhancedExtraction)
		{
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_enhanced_extraction), "SDS", "DBD", "SDS",
					'S', ModItems.imbued_stick, 'D', Items.DIAMOND, 'B', ModItems.ingot_bronze_imbued);
		}
		if (ConfigHandler.crafting.enableElevatedMomentum)
		{
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_flight), "SFS", "FIF", "SFS",
					'S', ModItems.fluxed_stick, 'F', Items.FEATHER, 'I', ModItems.ingot_fluxed);
		}
		if (ConfigHandler.crafting.enableUnabridgedImmortality)
		{
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_forcefield), "SHS", "HIH", "SHS",
					'S', ModItems.fluxed_stick, 'F', Items.SHIELD, 'I', ModItems.ingot_fluxed);
		}
		
	}

	public static void registerSmelting()
	{
		GameRegistry.addSmelting(ModBlocks.ore_copper, new ItemStack(ModItems.ingot_copper), 0.7f);
		GameRegistry.addSmelting(ModBlocks.ore_tin, new ItemStack(ModItems.ingot_tin), 0.7f); 
	}
}
