package com.raphydaphy.enhancedprogression.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockTotem extends BlockBase {

	public BlockTotem(String name) {
		super(Material.ROCK, name);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	@Override
	public BlockTotem setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}


}
