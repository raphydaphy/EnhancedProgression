package com.raphydaphy.vitality.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockModOre extends BlockBase
{
	/*
	 * Basic default constructor for any modded ores
	 * Sets the hardness to slightly higher than iron
	 * Resistance is the strength against TNT
	 * name is the unlocalized name of the ore block
	 */
	public BlockModOre(String name)
	{
		super(Material.ROCK, name);

		setHardness(3f);
		setResistance(5f);
	}
}