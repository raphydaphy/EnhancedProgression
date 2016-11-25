package com.raphydaphy.enhancedprogression.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class EPCreativeTab extends CreativeTabs
{

	public EPCreativeTab()
	{
		super(Reference.MOD_ID);
	}

	@Override
	public Item getTabIconItem()
	{
		return ModItems.ingot_copper;
	}

}