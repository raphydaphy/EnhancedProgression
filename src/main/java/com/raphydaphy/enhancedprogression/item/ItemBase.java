package com.raphydaphy.enhancedprogression.item;

import com.raphydaphy.enhancedprogression.EnhancedProgression;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item {

	protected String name;

	public ItemBase(String name, int maxStack) {
		this.name = name;
		setUnlocalizedName(name);
		setRegistryName(name);
		this.maxStackSize = maxStack;
	}

	public void registerItemModel() {
		EnhancedProgression.proxy.registerItemRenderer(this, 0, name);
	}

	@Override
	public ItemBase setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

}