package com.raphydaphy.vitality.block;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.init.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block
{

	protected String name;

	/*
	 * Constructor for BlockBase, accepts a Material and String
	 * material is the type of block, such as ROCK or WOOD
	 * name is the unlocalized name of the block, such as tin_ore
	 */
	public BlockBase(Material material, String name)
	{
		super(material);

		this.name = name;
		this.setCreativeTab(Reference.creativeTab);

		setUnlocalizedName(name);
		setRegistryName(name);
	}

	/*
	 * Registeres the block as an item in the inventory
	 * Uses ClientProxy to access the method client-side only
	 */
	public void registerItemModel(ItemBlock itemBlock)
	{
		Vitality.proxy.registerItemRenderer(itemBlock, 0, name);
	}

	/*
	 * Sets the creative tab of the block
	 * Used in the constructor to set it automatically
	 */
	@Override
	public BlockBase setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}

}