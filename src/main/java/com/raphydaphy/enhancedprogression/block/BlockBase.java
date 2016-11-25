package com.raphydaphy.enhancedprogression.block;

import java.util.Random;

import com.raphydaphy.enhancedprogression.EnhancedProgression;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBase extends Block
{

	protected String name;

	public BlockBase(Material material, String name)
	{
		super(material);

		this.name = name;

		setUnlocalizedName(name);
		setRegistryName(name);
	}

	public void registerItemModel(ItemBlock itemBlock)
	{
		EnhancedProgression.proxy.registerItemRenderer(itemBlock, 0, name);
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
	}

	@Override
	public BlockBase setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}

}