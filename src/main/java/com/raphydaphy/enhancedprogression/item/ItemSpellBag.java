package com.raphydaphy.enhancedprogression.item;

import com.raphydaphy.enhancedprogression.EnhancedProgression;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSpellBag extends Item
{
	public ItemSpellBag()
	{
		setUnlocalizedName("spell_bag");
		setRegistryName("spell_bag");
		this.maxStackSize = 1;
	}
	
	public void registerItemModel()
	{
		EnhancedProgression.proxy.registerItemRenderer(this, 0, "spell_bag");
	}

	@Override
	public ItemSpellBag setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
}
