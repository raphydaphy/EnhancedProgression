package com.raphydaphy.enhancedprogression.block;

import com.raphydaphy.enhancedprogression.EnhancedProgression;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class BlockDeadLog extends BlockBase
{
	public static final PropertyInteger DECAY = PropertyInteger.create("decay", 1, 6);
	public BlockDeadLog(String name) 
	{
		super(Material.WOOD, name);
		this.setDefaultState(blockState.getBaseState().withProperty(DECAY, 6));
		setHardness(1.0f);
		setCreativeTab(EnhancedProgression.creativeTab);
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DECAY);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(DECAY, 6 - meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 6 - state.getValue(DECAY);
	}
}
