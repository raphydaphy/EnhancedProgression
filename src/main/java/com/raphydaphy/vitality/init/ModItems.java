package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.item.ItemBase;
import com.raphydaphy.vitality.item.ItemSpell;
import com.raphydaphy.vitality.item.ItemSpellBag;
import com.raphydaphy.vitality.item.ItemWand;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems
{

	public static ItemBase ingot_copper;
	public static ItemBase ingot_tin;
	public static ItemBase ingot_bronze;
	
	public static ItemBase ingot_bronze_imbued;
	public static ItemBase imbued_stick;
	
	public static ItemBase ingot_fluxed;
	public static ItemBase fluxed_stick;
	
	public static ItemSpellBag spell_bag;

	public static ItemBase spell_card_vital_extraction;
	
	public static ItemSpell spell_card_explosion_1;
	public static ItemSpell spell_card_explosion_2;
	public static ItemSpell spell_card_explosion_3;
	
	public static ItemSpell spell_card_fireball_1;
	public static ItemSpell spell_card_fireball_2;
	public static ItemSpell spell_card_fireball_3;
	
	public static ItemBase spell_card_lantern_1;
	public static ItemBase spell_card_lantern_2;
	public static ItemBase spell_card_lantern_3;
	
	public static ItemSpell spell_card_fertilization_1;
	public static ItemSpell spell_card_fertilization_2;
	public static ItemSpell spell_card_fertilization_3;
	
	public static ItemSpell spell_card_placement_1;
	public static ItemSpell spell_card_placement_2;
	public static ItemSpell spell_card_placement_3;
	
	public static ItemBase spell_card_rapidfire_1;
	public static ItemBase spell_card_rapidfire_2;
	
	public static ItemBase spell_card_transmutation;
	public static ItemBase spell_card_hunger;
	
	public static ItemSpell spell_card_enhanced_extraction_1;
	public static ItemSpell spell_card_enhanced_extraction_2;
	
	public static ItemBase spell_card_flight;
	public static ItemBase spell_card_forcefield;

	public static ItemBase upgrade_template_basic;

	public static ItemWand basic_wand_copper;
	public static ItemWand basic_wand_tin;
	public static ItemWand advanced_wand;
	public static ItemWand master_wand;

	public static ItemWand wand;

	public static void init()
	{
		ingot_copper = register(new ItemBase("ingot_copper", 64).setCreativeTab(Vitality.creativeTab));
		ingot_tin = register(new ItemBase("ingot_tin", 64).setCreativeTab(Vitality.creativeTab));
		ingot_bronze = register(new ItemBase("ingot_bronze", 64).setCreativeTab(Vitality.creativeTab));
		ingot_bronze_imbued = register(
				new ItemBase("ingot_bronze_imbued", 64).setCreativeTab(Vitality.creativeTab));
		ingot_fluxed = register(
				new ItemBase("ingot_fluxed", 64).setCreativeTab(Vitality.creativeTab));
		fluxed_stick = register(new ItemBase("fluxed_stick", 64).setCreativeTab(Vitality.creativeTab));
		imbued_stick = register(new ItemBase("imbued_stick", 64).setCreativeTab(Vitality.creativeTab));
		spell_bag = register(
				new ItemSpellBag().setCreativeTab(Vitality.creativeTab));
		
		// Three tiers of explosion spell cards
		spell_card_explosion_1 = register(new ItemSpell("spell_card_explosion_1", 820));
		spell_card_explosion_2 = register(new ItemSpell("spell_card_explosion_2", 821));
		spell_card_explosion_3 = register(new ItemSpell("spell_card_explosion_3", 822));
		
		// Three tiers of fireball spell cards
		spell_card_fireball_1 = register(
				new ItemSpell("spell_card_fireball_1", 830));
		spell_card_fireball_2 = register(
				new ItemSpell("spell_card_fireball_2", 831));
		spell_card_fireball_3 = register(
				new ItemSpell("spell_card_fireball_3", 832));
		
		// Three tiers of lantern spell cards
		spell_card_lantern_1 = register(
				new ItemBase("spell_card_lantern_1", 1).setCreativeTab(Vitality.creativeTab));
		spell_card_lantern_2 = register(
				new ItemBase("spell_card_lantern_2", 1).setCreativeTab(Vitality.creativeTab));
		spell_card_lantern_3 = register(
				new ItemBase("spell_card_lantern_3", 1).setCreativeTab(Vitality.creativeTab));
		
		// Two tiers of rapidfire spell cards
		spell_card_rapidfire_1 = register(
				new ItemBase("spell_card_rapidfire_1", 1).setCreativeTab(Vitality.creativeTab));
		spell_card_rapidfire_2 = register(
				new ItemBase("spell_card_rapidfire_2", 1).setCreativeTab(Vitality.creativeTab));
		
		// Three tiers of renewed fertilization cards
		spell_card_fertilization_1 = register(new ItemSpell("spell_card_fertilization_1", 900));
		spell_card_fertilization_2 = register(new ItemSpell("spell_card_fertilization_2", 901));
		spell_card_fertilization_3 = register(new ItemSpell("spell_card_fertilization_3", 902));
		
		// Three tiers of angelic placement cards
		spell_card_placement_1 = register(new ItemSpell("spell_card_placement_1", 910));
		spell_card_placement_2 = register(new ItemSpell("spell_card_placement_2", 911));
		spell_card_placement_3 = register(new ItemSpell("spell_card_placement_3", 912));
		
		spell_card_hunger = register(
				new ItemBase("spell_card_hunger", 1).setCreativeTab(Vitality.creativeTab));
		spell_card_transmutation = register(
				new ItemBase("spell_card_transmutation", 1).setCreativeTab(Vitality.creativeTab));
		spell_card_vital_extraction = register(
				new ItemBase("spell_card_vital_extraction", 1).setCreativeTab(Vitality.creativeTab));
		
		spell_card_enhanced_extraction_1 = register(new ItemSpell("spell_card_enhanced_extraction_1", 870));
		spell_card_enhanced_extraction_2 = register(new ItemSpell("spell_card_enhanced_extraction_2", 871));
		
		spell_card_flight = register(
				new ItemBase("spell_card_flight", 1).setCreativeTab(Vitality.creativeTab));
		spell_card_forcefield = register(
				new ItemBase("spell_card_forcefield", 1).setCreativeTab(Vitality.creativeTab));
		upgrade_template_basic = register(
				new ItemBase("upgrade_template_basic", 16).setCreativeTab(Vitality.creativeTab));
		basic_wand_copper = register(new ItemWand("basic_wand_copper", 1, ConfigHandler.wandSettings.basicWandStorage,
				ConfigHandler.wandSettings.canBasicWandBreak).setCreativeTab(Vitality.creativeTab));
		basic_wand_tin = register(new ItemWand("basic_wand_tin", 1, ConfigHandler.wandSettings.basicWandStorage,
				ConfigHandler.wandSettings.canBasicWandBreak).setCreativeTab(Vitality.creativeTab));
		advanced_wand = (ItemWand) register(
				new ItemWand("advanced_wand", 2, ConfigHandler.wandSettings.advancedWandStorage, false)
						.setCreativeTab(Vitality.creativeTab));
		master_wand = (ItemWand) register(
				new ItemWand("master_wand", 3, ConfigHandler.wandSettings.masterWandStorage, false)
						.setCreativeTab(Vitality.creativeTab));

	}

	private static <T extends Item> T register(T item)
	{
		GameRegistry.register(item);
		if (item instanceof ItemBase)
		{
			((ItemBase) item).registerItemModel();
		}
		else if (item instanceof ItemWand)
		{
			((ItemWand) item).registerItemModel();
		}
		else if (item instanceof ItemSpellBag)
		{
			((ItemSpellBag) item).registerItemModel();
		}
		return item;
	}

}