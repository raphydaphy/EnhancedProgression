package com.raphydaphy.enhancedprogression.block;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.raphydaphy.enhancedprogression.EnhancedProgression;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDeadLog extends BlockBase
{
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
		worldIn.setBlockState(pos, state.withProperty(DECAY, 1));
		worldIn.scheduleBlockUpdate(pos, this, 10, 1);
	}
	
	protected int getDecay(IBlockState state)
    {
        return ((Integer)state.getValue(DECAY)).intValue();
    }
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		if (getDecay(state) < 6 && (ThreadLocalRandom.current().nextInt(1, 10 + 1) == 6))
		{
			worldIn.setBlockState(pos, state.withProperty(DECAY, getDecay(state) + 1));
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
	
	public int quantityDropped(Random random)
    {
        return 0;
    }
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		int decay = getDecay(state);
		ItemStack charcoal = new ItemStack(Items.COAL, 0, 1);
		switch (decay)
		{
		case 1:
			if ((ThreadLocalRandom.current().nextInt(1, 6 + 1) == 2))
			{
				net.minecraft.inventory.InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), charcoal);
			}
			break;
		case 2:
			if ((ThreadLocalRandom.current().nextInt(1, 5 + 1) == 2))
			{
				net.minecraft.inventory.InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), charcoal);
			}
			break;
		case 3:
			if ((ThreadLocalRandom.current().nextInt(1, 4 + 1) == 2))
			{
				net.minecraft.inventory.InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), charcoal);
			}
			break;
		case 4:
			if ((ThreadLocalRandom.current().nextInt(1, 3 + 1) == 2))
			{
				net.minecraft.inventory.InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), charcoal);
			}
			break;
		case 5:
			if ((ThreadLocalRandom.current().nextInt(1, 2 + 1) == 2))
			{
				net.minecraft.inventory.InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), charcoal);
			}
			break;
		case 6:
			net.minecraft.inventory.InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), charcoal);
			break;
		}
		
    }
}
