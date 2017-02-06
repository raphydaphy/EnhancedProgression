package com.raphydaphy.vitality.block;

import javax.annotation.Nonnull;

import com.raphydaphy.vitality.block.tile.TileSpellForge;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSpellForge extends BlockBase
{
	
	public BlockSpellForge() {
		super(Material.ANVIL, "spell_forge");
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return true;
	}	 
	
	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileSpellForge();
	}
	
	public void onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side)
	{
		if (player == null)
		{
			System.out.println("error!!!!!!!!!11");
		}
		if (stack == null)
		{
			System.out.println("error!!!!!!!");
		}
		((TileSpellForge) world.getTileEntity(pos)).onWanded(player, stack);
	}
}
