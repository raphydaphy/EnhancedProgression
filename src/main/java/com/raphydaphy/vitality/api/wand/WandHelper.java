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

	public static SimpleEntry<CoreType, TipType> getUsefulInfo(ItemStack wand) {
		NBTTagCompound tag = wand.getTagCompound();
		String c = tag.getString(WandHelper.CORE_TYPE);
		String t = tag.getString(WandHelper.TIP_TYPE);
		return new SimpleEntry<CoreType, TipType>(CoreType.getByName(c), TipType.getByName(t));
	}

	public static boolean canUseEssence(ItemStack wand, int toUse, Essence type) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);
		if (pair.getKey().getCoreType().equals(type)) {
			if (getEssenceStored(wand) >= toUse * pair.getValue().getCostMultiplier()) {
				return true;
			}
		}

		return false;
	}

	public static boolean useEssence(ItemStack wand, int toUse, Essence type) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);
		if (pair.getKey().getCoreType().equals(type)) {
			if (getEssenceStored(wand) >= toUse) {
				return setEssenceStored(wand, getEssenceStored(wand) - toUse);
			}
		}

		return false;
	}
}
