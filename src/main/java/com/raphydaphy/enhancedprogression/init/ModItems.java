package com.raphydaphy.enhancedprogression.init;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.item.ItemAdvancedWand;
import com.raphydaphy.enhancedprogression.item.ItemBase;
import com.raphydaphy.enhancedprogression.item.ItemBasicWand;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems
{

	public static ItemBase ingot_copper;
	public static ItemBase ingot_tin;
	public static ItemBase ingot_bronze;
	public static ItemBase ingot_bronze_imbued;

	public static ItemBase imbued_stick;

	public static ItemBase spell_card_lantern;
	public static ItemBase spell_card_explosion;
	public static ItemBase spell_card_rapidfire;
	public static ItemBase spell_card_hunger;
	public static ItemBase spell_card_transmutation;

	public static ItemBase upgrade_template_basic;

	public static ItemBasicWand basic_wand_copper;
	public static ItemBasicWand basic_wand_tin;
	public static ItemAdvancedWand advanced_wand;

	public static void init()
	{
		ingot_copper = register(new ItemBase("ingot_copper", 64).setCreativeTab(EnhancedProgression.creativeTab));
		ingot_tin = register(new ItemBase("ingot_tin", 64).setCreativeTab(EnhancedProgression.creativeTab));
		ingot_bronze = register(new ItemBase("ingot_bronze", 64).setCreativeTab(EnhancedProgression.creativeTab));
		ingot_bronze_imbued = register(
				new ItemBase("ingot_bronze_imbued", 64).setCreativeTab(EnhancedProgression.creativeTab));

		imbued_stick = register(new ItemBase("imbued_stick", 64).setCreativeTab(EnhancedProgression.creativeTab));

		spell_card_lantern = register(
				new ItemBase("spell_card_lantern", 1).setCreativeTab(EnhancedProgression.creativeTab));
		spell_card_explosion = register(
				new ItemBase("spell_card_explosion", 1).setCreativeTab(EnhancedProgression.creativeTab));
		spell_card_rapidfire = register(
				new ItemBase("spell_card_rapidfire", 1).setCreativeTab(EnhancedProgression.creativeTab));
		spell_card_hunger = register(
				new ItemBase("spell_card_hunger", 1).setCreativeTab(EnhancedProgression.creativeTab));
		spell_card_transmutation = register(
				new ItemBase("spell_card_transmutation", 1).setCreativeTab(EnhancedProgression.creativeTab));

		upgrade_template_basic = register(
				new ItemBase("upgrade_template_basic", 16).setCreativeTab(EnhancedProgression.creativeTab));

		basic_wand_copper = register(
				new ItemBasicWand("basic_wand_copper").setCreativeTab(EnhancedProgression.creativeTab));
		basic_wand_tin = register(new ItemBasicWand("basic_wand_tin").setCreativeTab(EnhancedProgression.creativeTab));
		advanced_wand = (ItemAdvancedWand) register(
				new ItemAdvancedWand().setCreativeTab(EnhancedProgression.creativeTab));
	}

	private static <T extends Item> T register(T item)
	{
		GameRegistry.register(item);
		if (item instanceof ItemBase)
		{
			((ItemBase) item).registerItemModel();
		}
		else if (item instanceof ItemBasicWand)
		{
			((ItemBasicWand) item).registerItemModel();
		}
		else if (item instanceof ItemAdvancedWand)
		{
			((ItemAdvancedWand) item).registerItemModel();
		}
		return item;
	}

}