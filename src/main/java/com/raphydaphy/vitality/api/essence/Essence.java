package com.raphydaphy.vitality.api.essence;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.api.wand.WandEnums.WandTier;
import com.raphydaphy.vitality.item.ItemVial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;

public enum Essence implements IStringSerializable{
	ANGELIC("angelic", WandTier.BASIC, 1),
	ATMOSPHERIC("atmospheric", WandTier.BASIC, 0),
	DEMONIC("demonic", WandTier.BASIC, -1),
	ENERGETIC("energetic", WandTier.BASIC, -2000),
	EXOTIC("exotic", WandTier.BASIC, -2000),
	;

	
	String name;
	WandTier accessTier;
	int empoweredDimensionID;
	
	/**
	 * Use this for single containers. Use getMultiKey() for multiples.
	 * Used to get/set the amount of essence in an IEssenceContainer.
	 */
	public static final String KEY = "stored_essence"; 
	
	/**
	 * Use this for single containers, Multi-Containers should just rely on checking ints by getMultiKey().  
	 * Used to get/set the type of essence in an IEssenceContainer.
	 */
	public static final String TYPE_KEY = "essence_type";
	
	Essence(String name, WandTier accessTier, @Nullable /*well not really nullable but put -2000 for none */ int empoweredDimensionID){
		this.name = name;
		this.accessTier = accessTier;
		if(empoweredDimensionID != -2000) this.empoweredDimensionID = empoweredDimensionID; //this doesnt do anything rn maybe neato idea for later
		MiscEssence.locator.put(name, this);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	
	public static Essence getByName(String nameIn){
		return MiscEssence.locator.get(nameIn);
	}
	
	public String getMultiKey(){
		return KEY + "_" + name;
	}

}
