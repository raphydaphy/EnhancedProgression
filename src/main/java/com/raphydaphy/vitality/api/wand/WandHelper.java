package com.raphydaphy.vitality.api.wand;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;

import net.minecraft.item.ItemStack;

public class WandHelper {
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
	
	public static void setEssenceStored(ItemStack stack, int essence) {
		if (stack.hasTagCompound()) {
			if (essence >= 0 && essence <= getMaxEssence(stack))
			stack.getTagCompound().setInteger(Essence.KEY, essence);
		}
	}
	
	public static CoreType getCore(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			try
			{
				return CoreType.valueOf(stack.getTagCompound().getString(CoreType.KEY));
			}
			catch (Exception e)
			{
				return null;
			}
		}
		return null;
	}
	
	public static TipType getTip(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			try
			{
				return TipType.valueOf(stack.getTagCompound().getString(TipType.KEY));
			}
			catch (Exception e)
			{
				return null;
			}
		}
		return null;
	}
}
