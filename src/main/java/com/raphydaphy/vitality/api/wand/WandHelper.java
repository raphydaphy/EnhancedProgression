package com.raphydaphy.vitality.api.wand;

import java.util.AbstractMap.SimpleEntry;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class WandHelper {
	
	public static final String CORE_TYPE = "core";
	public static final String TIP_TYPE = "tip";
	
	public static int getEssenceStored(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return stack.getTagCompound().getInteger(Essence.KEY);
		}
		return 0;
	}

	public static int getMaxEssence(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return stack.getTagCompound().getInteger(Essence.MAX_KEY);
		}
		return 0;
	}

	public static boolean setEssenceStored(ItemStack stack, int essence) {
		if (stack.hasTagCompound()) {
			if (essence >= 0 && essence <= getMaxEssence(stack)) {
				stack.getTagCompound().setInteger(Essence.KEY, essence);
				return true;
			}
		}
		return false;
	}
	
	public SimpleEntry<CoreType, TipType> deserializeWandNBT(ItemStack wand){
		NBTTagCompound tag = wand.getTagCompound();
		String c = tag.getString()
	}
}
