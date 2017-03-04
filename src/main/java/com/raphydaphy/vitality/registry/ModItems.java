package com.raphydaphy.vitality.registry;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.item.ItemBase;
import com.raphydaphy.vitality.item.ItemExtractionSword;
import com.raphydaphy.vitality.item.ItemSpell;
import com.raphydaphy.vitality.item.ItemVial;
import com.raphydaphy.vitality.item.ItemVial.VialQuality;
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

	// Remember kids, always register your empty vial for that quality first, or
	// the world will burn down.

	// Ok i will remember

	public static final ItemVial VIAL_EMPTY = new ItemVial("empty_vial", null, VialQuality.BASIC);
	public static final ItemVial VIAL_ANGELIC = new ItemVial("angelic_vial", Essence.ANGELIC, VialQuality.BASIC,
			VIAL_EMPTY);
	public static final ItemVial VIAL_ATMOSPHERIC = new ItemVial("atmospheric_vial", Essence.ATMOSPHERIC,
			VialQuality.BASIC, VIAL_EMPTY);
	public static final ItemVial VIAL_DEMONIC = new ItemVial("demonic_vial", Essence.DEMONIC, VialQuality.BASIC,
			VIAL_EMPTY);
	public static final ItemVial VIAL_ENERGETIC = new ItemVial("energetic_vial", Essence.ENERGETIC,
			VialQuality.BASIC, VIAL_EMPTY);
	public static final ItemVial VIAL_EXOTIC = new ItemVial("exotic_vial", Essence.EXOTIC, VialQuality.BASIC,
			VIAL_EMPTY);

	public static final Item EXTRACTION_ROD = new ItemExtractionRod();
	public static final Item TECHNICIANS_ROD = new ItemTechniciansRod();
	public static final Item TRANSMUTATION_ROD = new ItemTransmutationRod();

	public static final Item EXTRACTION_SWORD = new ItemExtractionSword();

	public static final Item CORE_ANGELIC = new ItemBase("wand_core_angelic", 1);
	public static final Item CORE_ATMOSPHERIC = new ItemBase("wand_core_atmospheric", 1);
	public static final Item CORE_DEMONIC = new ItemBase("wand_core_demonic", 1);
	public static final Item CORE_ENERGETIC = new ItemBase("wand_core_energetic", 1);
	public static final Item CORE_EXOTIC = new ItemBase("wand_core_exotic", 1);

	public static final Item TIP_WOODEN = new ItemBase("wand_tip_wooden", 2);

	public static final Item WAND = new ItemWand("wand");

	public static final Item SPELL_ILLUMINATION = new ItemSpell("illumination", Spell.ILLUMINATION);
	public static final Item SPELL_FIREBALL = new ItemSpell("fireball", Spell.FIREBALL);

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
		list.add(SPELL_ILLUMINATION);
		list.add(SPELL_FIREBALL);
		return list;
	}

}