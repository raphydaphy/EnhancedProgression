package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.Vitality;

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
		Vitality.proxy.registerItemRenderer(this, 0, "spell_bag");
	}

	@Override
	public ItemSpellBag setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	
}
