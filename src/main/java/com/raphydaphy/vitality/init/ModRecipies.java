package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.recipe.RecipeSpellBag;

import net.minecraft.init.Blocks;
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
		
		if (ConfigHandler.world.enableCopperGen)
		{
			OreDictionary.registerOre("oreCopper", ModBlocks.ore_copper);
		}
		if (ConfigHandler.world.enableTinGen)
		{
			OreDictionary.registerOre("oreTin", ModBlocks.ore_tin);
		}
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
			if (ConfigHandler.crafting.enableExpertMode)
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_lantern_1), "SCS", "CBC", "SCS", 'S', "stickWood", 'C',
						"torch", 'B', Items.CLAY_BALL);
				addOreDictRecipe(new ItemStack(ModItems.spell_card_lantern_2), "SCS", "CBC", "SCS", 'S', ModItems.imbued_stick, 'C',
						ModItems.spell_card_lantern_1, 'B', ModItems.ingot_bronze_imbued);
				addOreDictRecipe(new ItemStack(ModItems.spell_card_lantern_3), "SCS", "CBC", "SCS", 'S', ModItems.fluxed_stick, 'C',
						ModItems.spell_card_lantern_2, 'B', ModItems.ingot_fluxed);
			}
			else
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_lantern_1), "SCS", "CBC", "SCS", 'S', "stickWood", 'C',
						"justALumpOfCoalPlzIgnore", 'B', "ingotBronze");
				addOreDictRecipe(new ItemStack(ModItems.spell_card_lantern_2), "SCS", "CBC", "SCS", 'S', ModItems.imbued_stick, 'C',
						ModItems.spell_card_lantern_1, 'B', ModItems.ingot_bronze_imbued);
				addOreDictRecipe(new ItemStack(ModItems.spell_card_lantern_3), "SCS", "CBC", "SCS", 'S', ModItems.fluxed_stick, 'C',
						ModItems.spell_card_lantern_2, 'B', ModItems.ingot_fluxed);
			}
		}
		if (ConfigHandler.crafting.enableVitalExtraction)
		{
			if (ConfigHandler.crafting.enableExpertMode)
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_vital_extraction), "SLS", "LBL", "SLS", 'S', "stickWood",
						'L', "treeSapling", 'B', Items.CLAY_BALL);
			}
			else
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_vital_extraction), "SLS", "LBL", "SLS", 'S', "stickWood",
						'L', "logWood", 'B', "ingotBronze");
			}
		}
		if (ConfigHandler.crafting.enableContainedExplosion)
		{
			if (ConfigHandler.crafting.enableExpertMode)
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_explosion_1), "SGS", "GBG", "SGS", 'S', "rodStone", 'G',
						"gunpowder", 'B', Items.CLAY_BALL);
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_explosion_2), "SGS", "GBG", "SGS", 'S', ModItems.imbued_stick, 'G',
						ModItems.spell_card_explosion_1, 'B', ModItems.ingot_bronze_imbued);
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_explosion_3), "SGS", "GBG", "SGS", 'S', ModItems.fluxed_stick, 'G',
						ModItems.spell_card_explosion_2, 'B', ModItems.ingot_fluxed);
			}
			else
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_explosion_1), "SGS", "GBG", "SGS", 'S', "stickWood", 'G',
						"gunpowder", 'B', "ingotBronze");
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_explosion_2), "SGS", "GBG", "SGS", 'S', ModItems.imbued_stick, 'G',
						ModItems.spell_card_explosion_1, 'B', ModItems.ingot_bronze_imbued);
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_explosion_3), "SGS", "GBG", "SGS", 'S', ModItems.fluxed_stick, 'G',
						ModItems.spell_card_explosion_2, 'B', ModItems.ingot_fluxed);
			}
		}
		if (ConfigHandler.crafting.enableRadiantFireball)
		{
			if (ConfigHandler.crafting.enableExpertMode)
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fireball_1), "SFS", "FBF", "SFS", 'S', "rodStone", 'F',
						"slimecrystalMagma", 'B', "ingotDawnstone");
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fireball_2), "SFS", "FBF", "SFS", 'S', ModItems.imbued_stick, 'F',
						ModItems.spell_card_fireball_1, 'B', ModItems.ingot_bronze_imbued);
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fireball_3), "SFS", "FBF", "SFS", 'S', ModItems.fluxed_stick, 'F',
						ModItems.spell_card_fireball_2, 'B', ModItems.ingot_fluxed);
			}
			else
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fireball_1), "SFS", "FBF", "SFS", 'S', "stickWood", 'F',
						Items.FIRE_CHARGE, 'B', "ingotBronze");
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fireball_2), "SFS", "FBF", "SFS", 'S', ModItems.imbued_stick, 'F',
						ModItems.spell_card_fireball_1, 'B', ModItems.ingot_bronze_imbued);
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fireball_3), "SFS", "FBF", "SFS", 'S', ModItems.fluxed_stick, 'F',
						ModItems.spell_card_fireball_2, 'B', ModItems.ingot_fluxed);
			}
		}
		if (ConfigHandler.crafting.enableRenewedFertilization)
		{
			if (ConfigHandler.crafting.enableExpertMode)
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fertilization_1), "SGS", "GBG", "SGS", 'S', "rodStone", 'G',
						"dyeWhite", 'B', Items.CLAY_BALL);
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fertilization_2), "SGS", "GBG", "SGS", 'S', ModItems.imbued_stick, 'G',
						ModItems.spell_card_fertilization_1, 'B', ModItems.ingot_bronze_imbued);
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fertilization_3), "SGS", "GBG", "SGS", 'S', ModItems.fluxed_stick, 'G',
						ModItems.spell_card_fertilization_2, 'B', ModItems.ingot_fluxed);
			}
			else
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fertilization_1), "SGS", "GBG", "SGS", 'S', "stickWood", 'G',
						"dyeWhite", 'B', "ingotBronze");
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fertilization_2), "SGS", "GBG", "SGS", 'S', ModItems.imbued_stick, 'G',
						ModItems.spell_card_fertilization_1, 'B', ModItems.ingot_bronze_imbued);
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_fertilization_3), "SGS", "GBG", "SGS", 'S', ModItems.fluxed_stick, 'G',
						ModItems.spell_card_fertilization_2, 'B', ModItems.ingot_fluxed);
			}
		}
		if (ConfigHandler.crafting.enableAngelicPlacement)
		{
			if (ConfigHandler.crafting.enableExpertMode)
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_placement_1), "SGS", "GBG", "SGS", 'S', "rodStone", 'G',
						Items.FEATHER, 'B', Items.CLAY_BALL);
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_placement_2), "SGS", "GBG", "SGS", 'S', ModItems.imbued_stick, 'G',
						ModItems.spell_card_placement_1, 'B', ModItems.ingot_bronze_imbued);
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_placement_3), "SGS", "GBG", "SGS", 'S', ModItems.fluxed_stick, 'G',
						ModItems.spell_card_placement_2, 'B', ModItems.ingot_fluxed);
			}
			else
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_placement_1), "SGS", "GBG", "SGS", 'S', "stickWood", 'G',
						Blocks.OBSIDIAN, 'B', "ingotBronze");
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_placement_2), "SGS", "GBG", "SGS", 'S', ModItems.imbued_stick, 'G',
						ModItems.spell_card_placement_1, 'B', ModItems.ingot_bronze_imbued);
				
				addOreDictRecipe(new ItemStack(ModItems.spell_card_placement_3), "SGS", "GBG", "SGS", 'S', ModItems.fluxed_stick, 'G',
						ModItems.spell_card_placement_2, 'B', ModItems.ingot_fluxed);
			}
		}
		if (ConfigHandler.crafting.enableCrypticTransmutation)
		{
			if (ConfigHandler.crafting.enableExpertMode)
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_transmutation), "SPS", "PBP", "SPS", 'S', "stickIron",
						'P', "manaPearl", 'B', "ingotDawnstone");
			}
			else
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_transmutation), "SPS", "PBP", "SPS", 'S', "stickWood",
						'P', Items.BLAZE_POWDER, 'B', "ingotBronze");
			}
		}

		GameRegistry.addShapedRecipe(new ItemStack(ModItems.advanced_wand), "  B", " S ", "S  ", 'S',
				ModItems.imbued_stick, 'B', ModItems.ingot_bronze_imbued);
		
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.master_wand), "  I", " S ", "S  ", 'S',
				ModItems.fluxed_stick, 'I', ModItems.ingot_fluxed);
		
		addOreDictRecipe(new ItemStack(ModItems.essence_vial_empty), "GBG", " G ",  'G', "blockGlass",
			 'B', "ingotBronze");

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
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_rapidfire_1), "SAS", "ABA", "SAS", 'S',
					ModItems.imbued_stick, 'A', Items.ARROW, 'B', ModItems.ingot_bronze_imbued);
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_rapidfire_2), "SAS", "ABA", "SAS", 'S',
					ModItems.fluxed_stick, 'A', ModItems.spell_card_rapidfire_1, 'B', ModItems.ingot_fluxed);
		}
		if (ConfigHandler.crafting.enableHunger)
		{
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_hunger), "SRS", "RBR", "SRS", 'S',
					ModItems.imbued_stick, 'R', Items.ROTTEN_FLESH, 'B', ModItems.ingot_bronze_imbued);
		}
		if (ConfigHandler.crafting.enableImbuedLightning)
		{
			addOreDictRecipe(new ItemStack(ModItems.spell_card_lightning_1), "SDS", "DBD", "SDS", 'S', ModItems.imbued_stick,
					'D', Items.DIAMOND_AXE, 'B',ModItems.ingot_bronze_imbued);
			addOreDictRecipe(new ItemStack(ModItems.spell_card_lightning_2), "SDS", "DBD", "SDS", 'S', ModItems.fluxed_stick,
					'D', ModItems.spell_card_lightning_1, 'B',ModItems.ingot_fluxed);
		}
		if (ConfigHandler.crafting.enableEnhancedExtraction)
		{
			if (ConfigHandler.crafting.enableExpertMode)
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_enhanced_extraction_1), "SDS", "DBD", "SDS", 'S', ModItems.imbued_stick,
						'D', "ingotElvenElementium", 'B',ModItems.ingot_bronze_imbued);
				GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_enhanced_extraction_2), "SDS", "DBD", "SDS",
						'S', ModItems.fluxed_stick, 'D', ModItems.spell_card_enhanced_extraction_1, 'B', ModItems.ingot_fluxed);
			}
			else
			{
				GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_enhanced_extraction_1), "SDS", "DBD", "SDS",
						'S', ModItems.imbued_stick, 'D', Items.DIAMOND, 'B', ModItems.ingot_bronze_imbued);
				GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_enhanced_extraction_2), "SDS", "DBD", "SDS",
						'S', ModItems.fluxed_stick, 'D', ModItems.spell_card_enhanced_extraction_1, 'B', ModItems.ingot_fluxed);
			}
			
		}
		if (ConfigHandler.crafting.enableElevatedMomentum)
		{
			if (ConfigHandler.crafting.enableExpertMode)
			{
				addOreDictRecipe(new ItemStack(ModItems.spell_card_flight), "SFS", "FIF", "SFS",
						'S', ModItems.fluxed_stick, 'F', "ingotDraconiumAwakened", 'I', ModItems.ingot_fluxed);
			}
			else
			{
				GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_flight), "SFS", "FIF", "SFS",
						'S', ModItems.fluxed_stick, 'F', Items.FEATHER, 'I', ModItems.ingot_fluxed);
			}
		}
		if (ConfigHandler.crafting.enableUnabridgedImmortality)
		{
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.spell_card_forcefield), "SHS", "HIH", "SHS",
					'S', ModItems.fluxed_stick, 'F', Items.SHIELD, 'I', ModItems.ingot_fluxed);
		}
		
	}

	public static void registerSmelting()
	{
		if (ConfigHandler.world.enableCopperGen)
		{
			GameRegistry.addSmelting(ModBlocks.ore_copper, new ItemStack(ModItems.ingot_copper), 0.7f);
		}
		if (ConfigHandler.world.enableTinGen)
		{
			GameRegistry.addSmelting(ModBlocks.ore_tin, new ItemStack(ModItems.ingot_tin), 0.7f); 
		}
	}
}