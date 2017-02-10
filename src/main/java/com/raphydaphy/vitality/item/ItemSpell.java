package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.Vitality;

public class ItemSpell extends ItemBase
{
	public static int spellID;
	public static String name;
	public ItemSpell(String parName, int parSpellID) 
	{
		super(parName, 1);
		super.setCreativeTab(Vitality.creativeTab);
		name = parName;
		spellID = parSpellID;
	}
	
	public int getID()
	{
		return spellID;
	}

}
