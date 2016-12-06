/**
 * (raphy): Keeping this here because i literally changed
 * nothing in this file. going to rewrite my own soon
 * PLZ DONT KILL ME VAZKII ;(
 * 
 * This class was created by <Mikeemoo/boq/nevercast>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [? (GMT)]
 */
package com.raphydaphy.vitality.nbt;

import com.raphydaphy.vitality.block.tile.TileSimpleInventory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.VanillaDoubleChestItemHandler;

public class InventoryHelper
{

	public static IItemHandler getInventory(World world, BlockPos pos, EnumFacing side)
	{
		TileEntity te = world.getTileEntity(pos);

		if (te == null)
			return null;

		if (te instanceof TileEntityChest)
		{
			IItemHandler doubleChest = VanillaDoubleChestItemHandler.get((TileEntityChest) te);
			if (doubleChest != VanillaDoubleChestItemHandler.NO_ADJACENT_CHESTS_INSTANCE)
				return doubleChest;
		}

		IItemHandler ret = te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)
				? te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) : null;

		if (ret == null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
			ret = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		return ret;
	}

	public static void dropInventory(TileSimpleInventory inv, World world, IBlockState state, BlockPos pos)
	{
		if (inv != null)
		{
			for (int j1 = 0; j1 < inv.getSizeInventory(); ++j1)
			{
				ItemStack itemstack = inv.getItemHandler().getStackInSlot(j1);

				if (itemstack != null)
				{
					net.minecraft.inventory.InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(),
							itemstack);
				}
			}

			world.updateComparatorOutputLevel(pos, state.getBlock());
		}
	}

	public static void withdrawFromInventory(TileSimpleInventory inv, EntityPlayer player)
	{
		for (int i = inv.getSizeInventory() - 1; i >= 0; i--)
		{
			ItemStack stackAt = inv.getItemHandler().getStackInSlot(i);
			if (stackAt != null)
			{
				ItemStack copy = stackAt.copy();
				ItemHandlerHelper.giveItemToPlayer(player, copy);
				inv.getItemHandler().setStackInSlot(i, null);
				player.worldObj.updateComparatorOutputLevel(inv.getPos(), null);
				break;
			}
		}
	}

}