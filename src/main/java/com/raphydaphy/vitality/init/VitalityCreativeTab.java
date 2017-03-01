package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.registry.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class VitalityCreativeTab extends CreativeTabs {

	public VitalityCreativeTab() {
		super(Reference.MOD_ID);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.EXTRACTION_ROD;
	}

}