package com.raphydaphy.vitality.api.essence;

import java.util.HashMap;
import java.util.Map;

import com.raphydaphy.vitality.block.tile.TileEssenceJar;
import com.raphydaphy.vitality.item.ItemVial;
import com.raphydaphy.vitality.item.ItemVial.VialQuality;

import net.minecraft.item.ItemStack;

public class MiscEssence {
	public static Map<String, Essence> locator = new HashMap<String, Essence>();
	public static Map<VialQuality, Map<Essence, ItemVial>> vialMap = new HashMap<VialQuality, Map<Essence, ItemVial>>();
	
	/**
	 * Returns whether or not a jar was successfully filled by a vial.
	 * @param te
	 * @param stack
	 * @param toUse
	 * @return
	 */
	public static boolean fillJar(TileEssenceJar te, ItemStack stack, int toUse){
		if(!te.isFull() && stack.getItem() instanceof ItemVial && ((ItemVial) stack.getItem()).hasType()){
			int currentInVial = stack.getTagCompound().getInteger(Essence.KEY);
			
			if(currentInVial - toUse >= 0 && te.addEssence(((ItemVial) stack.getItem()).getVialType(), toUse)){
				stack.getTagCompound().setInteger(Essence.KEY, currentInVial - toUse);
				return true;
			}
		}
		return false;
	}
}
