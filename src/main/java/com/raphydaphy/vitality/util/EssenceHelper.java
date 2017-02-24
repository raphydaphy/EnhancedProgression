package com.raphydaphy.vitality.util;

import com.raphydaphy.vitality.init.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;

public final class EssenceHelper 
{
	public static boolean addEssence(ItemStack stack, int essenceAmount, int maxEssence, EntityPlayer player, String essenceType)
	{
		player.getEntityData().setInteger("essenceStored" + essenceType, player.getEntityData().getInteger("essenceStored" + essenceType) + essenceAmount);
		
		if (NBTHelper.getInt(stack, "essenceStored", 0) + essenceAmount >= maxEssence)
		{
			stack.setTagInfo("essenceStored", new NBTTagInt(maxEssence));
			return true;
		}
		else
		{
			stack.setTagInfo("essenceStored", new NBTTagInt(NBTHelper.getInt(stack, "essenceStored", 0) + essenceAmount));
			return true;
		}
	}
	public static String vialItemToString(Item vial)
	{
		if (vial == ModItems.essence_vial_angelic)
		{
			return "Angelic";
		}
		else if (vial == ModItems.essence_vial_atmospheric)
		{
			return "Atmospheric";
		}
		else if (vial == ModItems.essence_vial_demonic)
		{
			return "Demonic";
		}
		else if (vial == ModItems.essence_vial_energetic)
		{
			return "Energetic";
		}
		else if (vial == ModItems.essence_vial_exotic)
		{
			return "Exotic";
		}
		return "Unknown";
	}
	public static boolean fillVial(Item vialType, EntityPlayer player, int essenceAmount)
	{
    	for(int i = 0; i < player.inventory.getSizeInventory(); i++) 
		{
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if (stackAt != null)
			{
				if (stackAt.getItem() == vialType)
				{
					addEssence(stackAt, essenceAmount, 1000, player, vialItemToString(vialType));
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
					addEssence(vialStack, essenceAmount, 1000, player, vialItemToString(vialType));
					player.inventory.setInventorySlotContents(i, vialStack);
					return true;
				}
			}
		}
		return false;
	}
}
