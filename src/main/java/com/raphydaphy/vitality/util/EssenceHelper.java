package com.raphydaphy.vitality.util;

import com.raphydaphy.vitality.init.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;

public final class EssenceHelper 
{
	public static boolean addEssenceFree(ItemStack stack, int essenceAmount, int maxEssence)
	{
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
	public static boolean addEssence(ItemStack stack, int essenceAmount, int maxEssence, EntityPlayer player, String essenceType)
	{
		player.getEntityData().setInteger("essenceStored" + essenceType, player.getEntityData().getInteger("essenceStored" + essenceType) + essenceAmount);
		return addEssenceFree(stack, essenceAmount, maxEssence);
	}
	public static boolean useEssence(ItemStack stack, int essenceAmount)
	{
		if (stack.hasTagCompound())
		{
			if (stack.getTagCompound().getInteger("essenceStored") >= essenceAmount)
			{
				stack.setTagInfo("essenceStored",new NBTTagInt(stack.getTagCompound().getInteger("essenceStored") - essenceAmount));
				return true;
			}
		}
		return false;
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
	
	public static String getJarStoring(int stat)
	{
		// 0 = empty
		// 1-4 = atmospheric
		// 5-9 = demonic
		
		if (stat == 1 || stat == 2 || stat == 3 || stat == 4)
		{
			return "Atmospheric";
		}
		else if (stat == 5 || stat == 6 || stat == 7 || stat == 8)
		{
			return "Demonic";
		}
		return "Unknown";
	}
	
	public static int getJarMax(String type)
	{
		switch(type)
		{
		case "Atmospheric":
			return 4;
		case "Demonic":
			return 8;
		default:
			return 0;
		}
	}
	
	public static int getJarMin(String type)
	{
		switch(type)
		{
		case "Atmospheric":
			return 1;
		case "Demonic":
			return 5;
		default:
			return 0;
		}
	}
	
	public static int increaseJarForType(int stat, String type)
	{
		String jarType = getJarStoring(stat);
		if (jarType == type)
		{
			if (stat < getJarMax(type))
			{
				return stat + 1;
			}
			return getJarMax(type);
		}
		return getJarMin(type);
	}
	
	public static int decreaseJarForType(int stat, String type)
	{
		String jarType = getJarStoring(stat);
		if (jarType == type)
		{
			if (stat >= getJarMin(type))
			{
				return stat - 1;
			}
			return 0;
		}
		return 0;
	}
}
