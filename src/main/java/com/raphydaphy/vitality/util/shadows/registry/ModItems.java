package com.raphydaphy.vitality.util.shadows.registry;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.vitality.item.ItemBase;
import com.raphydaphy.vitality.item.ItemEssenceVial;
import com.raphydaphy.vitality.item.ItemExtractionSword;
import com.raphydaphy.vitality.item.ItemWand;
import com.raphydaphy.vitality.item.rod.ItemExtractionRod;
import com.raphydaphy.vitality.item.rod.ItemTechniciansRod;
import com.raphydaphy.vitality.item.rod.ItemTransmutationRod;

import net.minecraft.item.Item;

public class ModItems {
	public static final Item INGOT_COPPER = new ItemBase("ingot_copper");
	public static final Item INGOT_TIN = new ItemBase("ingot_tin");
	public static final Item INGOT_BRONZE = new ItemBase("ingot_bronze");

	public static final Item CRYSTAL_SHARD_ANGELIC = new ItemBase("angelic_crystal_shard");
	public static final Item CRYSTAL_SHARD_EXOTIC = new ItemBase("exotic_crystal_shard");

	public static final Item VIAL_EMPTY = new ItemEssenceVial("essence_vial_empty");
	public static final Item VIAL_ANGELIC = new ItemEssenceVial("essence_vial_angelic");
	public static final Item VIAL_ATMOSPHERIC = new ItemEssenceVial("essence_vial_atmospheric");
	public static final Item VIAL_DEMONIC = new ItemEssenceVial("essence_vial_demonic");
	public static final Item VIAL_ENERGETIC = new ItemEssenceVial("essence_vial_energetic");
	public static final Item VIAL_EXOTIC = new ItemEssenceVial("essence_vial_exotic");

	public static final Item EXTRACTION_ROD = new ItemExtractionRod();
	public static final Item TECHNICIANS_ROD = new ItemTechniciansRod();
	public static final Item TRANSMUTATION_ROD = new ItemTransmutationRod();

	public static final Item EXTRACTION_SWORD = new ItemExtractionSword();

	public static final Item CORE_ANGELIC = new ItemBase("wand_core_angelic", 1);
	public static final Item CORE_ATMOSPHERIC = new ItemBase("wand_core_atmospheric", 1);
	public static final Item CORE_DEMONIC = new ItemBase("wand_core_demonic", 1);
	public static final Item CORE_ENERGETIC = new ItemBase("wand_core_energetic", 1);
	public static final Item CORE_EXOTIC = new ItemBase("wand_core_exotic", 1);

	public static final Item TIP_WOODEN = new ItemBase("wand_tip_wooden", 1);

	public static final Item WAND = new ItemWand("wand");

	public static final List<Item> ITEM_LIST = getList();

	private static final List<Item> getList() {
		List<Item> list = new ArrayList<Item>();
		list.add(INGOT_COPPER);
		list.add(INGOT_TIN);
		list.add(INGOT_BRONZE);
		list.add(CRYSTAL_SHARD_ANGELIC);
		list.add(CRYSTAL_SHARD_EXOTIC);
		list.add(VIAL_EMPTY);
		list.add(VIAL_ANGELIC);
		list.add(VIAL_ATMOSPHERIC);
		list.add(VIAL_DEMONIC);
		list.add(VIAL_ENERGETIC);
		list.add(VIAL_EXOTIC);
		list.add(EXTRACTION_ROD);
		list.add(TECHNICIANS_ROD);
		list.add(TRANSMUTATION_ROD);
		list.add(EXTRACTION_SWORD);
		list.add(CORE_ANGELIC);
		list.add(CORE_ATMOSPHERIC);
		list.add(CORE_DEMONIC);
		list.add(CORE_ENERGETIC);
		list.add(CORE_EXOTIC);
		list.add(TIP_WOODEN);
		list.add(WAND);
		return list;
	}

}