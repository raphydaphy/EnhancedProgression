package com.raphydaphy.enhancedprogression.block;

import java.util.Random;

import com.raphydaphy.enhancedprogression.EnhancedProgression;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDeadLog extends BlockBase
{
	private int frame = 0;
	public static final PropertyInteger DECAY = PropertyInteger.create("decay", 1, 6);
	public BlockDeadLog(String name) 
	{
		super(Material.WOOD, name);
		this.setDefaultState(blockState.getBaseState().withProperty(DECAY, 1));
		setHardness(1.0f);
		setCreativeTab(EnhancedProgression.creativeTab);
		this.setTickRandomly(true);
	}
	
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		worldIn.scheduleBlockUpdate(pos, this, 10, 1);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		if (frame < 6)
		{
			frame++;
			worldIn.setBlockState(pos, state.withProperty(DECAY, frame));
		}
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
