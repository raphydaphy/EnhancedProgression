package com.raphydaphy.vitality.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class VitalityCreativeTab extends CreativeTabs
{

	public VitalityCreativeTab()
	{
		super(Reference.MOD_ID);
	}

	@Override
	public Item getTabIconItem()
	{
		return ModItems.advanced_wand;
	}

}