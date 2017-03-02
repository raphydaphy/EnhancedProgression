package com.raphydaphy.vitality.api.wand;

public class WandEnums {
	
	public enum WandResult{
		SUCCESS,
		FAIL,
		BREAK,
		DRAIN,
		//do stuff
	}
	
	
	// basic medium and high are kinda dodgy names
	// kept basic in this version coz dunno what is better but
	// maybe empowered instead of medium, and dunno what to put
	// instead of high........ those dots were just for OCD btw
	// - BASIC
	// - EMPOWERED
	// - ETHEREAL (whatever tf that is it sounds cool)
	// - ETERNAL
	public enum WandTier{
		BASIC,
		EMPOWERED,
		ETHEREAL,
		ETERNAL;
		
		//WandTier(String name, Item core, int maxCapacity?){}
	}

}
