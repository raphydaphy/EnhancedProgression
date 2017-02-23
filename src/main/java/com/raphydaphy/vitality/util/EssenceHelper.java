package com.raphydaphy.vitality.util;

import com.raphydaphy.vitality.init.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;

public final class EssenceHelper 
{
	public static boolean fillVial(Item vialType, EntityPlayer player, int essenceAmount)
	{
    	for(int i = 0; i < player.inventory.getSizeInventory(); i++) 
		{
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if (stackAt != null)
			{
				if (stackAt.getItem() == vialType)
				{
					stackAt.setTagInfo("essenceStored", new NBTTagInt(NBTHelper.getInt(stackAt, "essenceStored", 0) + essenceAmount));
					return true;
				}
			}
		}
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) 
		{
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if (stackAt != null)
			{
				if (stackAt.getItem() == ModItems.essence_vial_empty)
				{
					ItemStack vialStack = new ItemStack(vialType);
					vialStack.setTagInfo("essenceStored", new NBTTagInt(essenceAmount));
					player.inventory.setInventorySlotContents(i, vialStack);
					return true;
				}
			}
		}
		return false;
	}
}
