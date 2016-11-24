package com.raphydaphy.enhancedprogression.item;

public class ItemAdvancedWand extends ItemBasicWand
{

	public ItemAdvancedWand() {
		super("advanced_wand");
	}
	
	@Override
	public boolean canBreak()
	{
		return false;
	}
	
	@Override
	public int getWandTier()
	{
		return 2;
	}
	
	@Override
	public int getMaxEssence()
	{
		return 10000;
	}

}
