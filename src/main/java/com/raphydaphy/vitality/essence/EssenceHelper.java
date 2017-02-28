package com.raphydaphy.vitality.essence;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.util.NBTHelper;

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
	public static int getEssenceStored(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			return NBTHelper.getInt(stack, "essenceStored", 0);
		}
		return 0;
	}
	public static int getMaxEssence(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			return NBTHelper.getInt(stack, "maxEssence", 0);
		}
		return 0;
	}
	public static String getWandCore(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			return NBTHelper.getString(stack, "coreType", "Unknown");
		}
		return "Unknown";
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
	
	public static List coreToAcceptedEssenceTypesList(String coreType)
	{
		List<String> acceptedTypes = new ArrayList<String>();
		
		switch(coreType)
		{
		case "Angelic":
		case "Atmospheric":
		case "Demonic":
		case "Energetic":
		case "Exotic":
			acceptedTypes.add(coreType);
			break;
		}
		return acceptedTypes;
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
	
	public static Item vialStringToItem(String vial)
	{
		switch(vial)
		{
		case "Angelic":
			return ModItems.essence_vial_angelic;
		case "Atmospheric":
			return ModItems.essence_vial_atmospheric;
		case "Demonic":
			return ModItems.essence_vial_demonic;
		case "Energetic":
			return ModItems.essence_vial_energetic;
		case "Exotic":
			return ModItems.essence_vial_exotic;
		default:
			return ModItems.essence_vial_empty;
		}
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
	
	public static void emptyVial(EntityPlayer player, ItemStack vial)
	{
		int slot = 0;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) 
		{
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if (stackAt != null)
			{
				if (stackAt == vial)
				{
					slot = i;
				}
			}
		}
		player.inventory.setInventorySlotContents(slot, new ItemStack(ModItems.essence_vial_empty));
	}
	
	public static void fillEmptyVial(EntityPlayer player, ItemStack vial, int essenceAmount, Item vialType)
	{
		int slot = 0;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) 
		{
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if (stackAt != null)
			{
				if (stackAt == vial)
				{
					slot = i;
				}
			}
		}
		ItemStack fullVial = new ItemStack(vialType);
		addEssenceFree(fullVial, essenceAmount, 1000);
		player.inventory.setInventorySlotContents(slot, fullVial);
	}
}
