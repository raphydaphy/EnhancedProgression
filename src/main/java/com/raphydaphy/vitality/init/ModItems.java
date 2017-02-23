package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.item.ItemBase;
import com.raphydaphy.vitality.item.ItemEssenceVial;
import com.raphydaphy.vitality.item.rod.ItemExtractionRod;
import com.raphydaphy.vitality.item.rod.ItemTechniciansRod;
import com.raphydaphy.vitality.item.rod.ItemTransmutationRod;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems
{
	public static ItemBase ingot_copper;
	public static ItemBase ingot_tin;
	public static ItemBase ingot_bronze;
	
	public static ItemBase angelic_crystal_shard;
	public static ItemBase exotic_crystal_shard;
	
	public static ItemEssenceVial essence_vial_empty;
	public static ItemEssenceVial essence_vial_angelic;
	public static ItemEssenceVial essence_vial_atmospheric;
	public static ItemEssenceVial essence_vial_demonic;
	public static ItemEssenceVial essence_vial_energetic;
	public static ItemEssenceVial essence_vial_exotic;
	
	public static ItemExtractionRod life_extraction_rod;
	public static ItemTechniciansRod technicians_rod;
	public static ItemTransmutationRod transmutation_rod;

	public static void init()
	{
		ingot_copper = register(new ItemBase("ingot_copper", 64));
		ingot_tin = register(new ItemBase("ingot_tin", 64));
		ingot_bronze = register(new ItemBase("ingot_bronze", 64));
		
		angelic_crystal_shard = register(new ItemBase("angelic_crystal_shard", 64));
		exotic_crystal_shard = register(new ItemBase("exotic_crystal_shard", 64));
		
		essence_vial_empty = register(new ItemEssenceVial("essence_vial_empty"));
		essence_vial_angelic = register(new ItemEssenceVial("essence_vial_angelic"));
		essence_vial_atmospheric = register(new ItemEssenceVial("essence_vial_atmospheric"));
		essence_vial_demonic = register(new ItemEssenceVial("essence_vial_demonic"));
		essence_vial_energetic = register(new ItemEssenceVial("essence_vial_energetic"));
		essence_vial_exotic = register(new ItemEssenceVial("essence_vial_exotic"));
		
		life_extraction_rod = register(new ItemExtractionRod());
		technicians_rod = register(new ItemTechniciansRod());
		transmutation_rod = register(new ItemTransmutationRod());
	}

	private static <T extends Item> T register(T item)
	{
		GameRegistry.register(item);
		((ItemBase) item).registerItemModel();
		return item;
	}
}