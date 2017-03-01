package com.raphydaphy.vitality.essence;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.vitality.registry.ModItems;
import com.raphydaphy.vitality.util.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;

public final class EssenceHelper {
	// TODO: replace all essenceStored instances with essenceStoredType (eg.
	// essenceStoredAngelic) to support multi-type essence containers
	public static boolean addEssenceFree(ItemStack stack, int essenceAmount, int maxEssence, String essenceType) 
	{
		if (NBTHelper.getInt(stack, "essenceStored", 0) + essenceAmount >= maxEssence) {
			stack.setTagInfo("essenceStored", new NBTTagInt(maxEssence));
			return true;
		} else {
			stack.setTagInfo("essenceStored",
					new NBTTagInt(NBTHelper.getInt(stack, "essenceStored", 0) + essenceAmount));
			return true;
		}
	}

	public static int getEssenceStored(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return NBTHelper.getInt(stack, "essenceStored", 0);
		}
		return 0;
	}

	public static int getMaxEssence(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return NBTHelper.getInt(stack, "maxEssence", 0);
		}
		return 0;
	}
	
	public static void setEssenceStored(ItemStack stack, int essence) {
		if (stack.hasTagCompound()) {
			NBTHelper.setInt(stack, "essenceStored", essence);
		}
	}

	public static String getWandCore(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return NBTHelper.getString(stack, "coreType", "Unknown");
		}
		return "Unknown";
	}

	public static boolean addEssence(ItemStack stack, int essenceAmount, int maxEssence, EntityPlayer player,
			String essenceType) {
		player.getEntityData().setInteger("essenceStored" + essenceType,
				player.getEntityData().getInteger("essenceStored" + essenceType) + essenceAmount);
		return addEssenceFree(stack, essenceAmount, maxEssence, essenceType);
	}

	public static boolean useEssence(ItemStack stack, int essenceAmount) {
		if (stack.hasTagCompound()) {
			if (stack.getTagCompound().getInteger("essenceStored") >= essenceAmount) {
				stack.setTagInfo("essenceStored",
						new NBTTagInt(stack.getTagCompound().getInteger("essenceStored") - essenceAmount));
				return true;
			}
		}
		return false;
	}

	public static List<String> coreToAcceptedEssenceTypesList(String coreType) {
		List<String> acceptedTypes = new ArrayList<String>();

		switch (coreType) {
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

	public static String vialItemToString(Item vial) {
		if (vial == ModItems.VIAL_ANGELIC) {
			return "Angelic";
		} else if (vial == ModItems.VIAL_ATMOSPHERIC) {
			return "Atmospheric";
		} else if (vial == ModItems.VIAL_DEMONIC) {
			return "Demonic";
		} else if (vial == ModItems.VIAL_ENERGETIC) {
			return "Energetic";
		} else if (vial == ModItems.VIAL_EXOTIC) {
			return "Exotic";
		}
		return "Unknown";
	}

	public static Item vialStringToItem(String vial) {
		switch (vial) {
		case "Angelic":
			return ModItems.VIAL_ANGELIC;
		case "Atmospheric":
			return ModItems.VIAL_ATMOSPHERIC;
		case "Demonic":
			return ModItems.VIAL_DEMONIC;
		case "Energetic":
			return ModItems.VIAL_ENERGETIC;
		case "Exotic":
			return ModItems.VIAL_EXOTIC;
		default:
			return ModItems.VIAL_EMPTY;
		}
	}

	public static boolean fillVial(Item vialType, EntityPlayer player, int essenceAmount) {
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if (stackAt != null) {
				if (stackAt.getItem() == vialType) {
					addEssence(stackAt, essenceAmount, 1000, player, vialItemToString(vialType));
					return true;
				}
			}
		}
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if (stackAt != null) {
				if (stackAt.getItem() == ModItems.VIAL_EMPTY) {
					ItemStack vialStack = new ItemStack(vialType);
					addEssence(vialStack, essenceAmount, 1000, player, vialItemToString(vialType));
					player.inventory.setInventorySlotContents(i, vialStack);
					return true;
				}
			}
		}
		return false;
	}

	public static void emptyVial(EntityPlayer player, ItemStack vial) {
		int slot = 0;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if (stackAt != null) {
				if (stackAt == vial) {
					slot = i;
				}
			}
		}
		player.inventory.setInventorySlotContents(slot, new ItemStack(ModItems.VIAL_EMPTY));
	}

	public static void fillEmptyVial(EntityPlayer player, ItemStack vial, int essenceAmount, Item vialType) {
		int slot = 0;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if (stackAt != null) {
				if (stackAt == vial) {
					slot = i;
				}
			}
		}
		ItemStack fullVial = new ItemStack(vialType);
		addEssenceFree(fullVial, essenceAmount, 1000, vialItemToString(vialType));
		player.inventory.setInventorySlotContents(slot, fullVial);
	}
}
