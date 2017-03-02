package com.raphydaphy.vitality.api.wand;

public class WandEnums {
	
	
	public enum WandResult{
		SUCCESS,
		FAIL,
		BREAK,
		DRAIN,
		//do stuff
	}
	
	public enum WandTier{
		BASIC,
		MEDIUM,
		HIGH,
		ETHEREAL,
		ETERNAL;
		
		//WandTier(String name, Item core, int maxCapacity?){}
	}

}
