package com.raphydaphy.vitality.item.rod;

import javax.annotation.Nonnull;

import com.raphydaphy.vitality.item.ItemBase;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * The Life Extraction Rod is used to extract essence out of ores and other blocks
 * This class defines all the things you can do with the item itself
 */
public class ItemTechniciansRod extends ItemBase
{
	
	public ItemTechniciansRod() 
	{
		super("technicians_rod", 1);
		this.setFull3D();
	}
	
	/*
	 * Called when the rod is used on a block, by right-clicking
	 * Used to switch the mode of pumps and infuse spell cards
	 */
	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10)
	{
		Block block = world.getBlockState(pos).getBlock();
		
		// check if the block is a pump
			// switch the pump output/input mode
		
		// if the block is not a pump, search for nearby spell cards
			// infuse spell cards into a spell bundle
		
		return EnumActionResult.PASS;
	}
}
