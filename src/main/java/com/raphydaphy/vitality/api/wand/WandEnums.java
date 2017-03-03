package com.raphydaphy.vitality.api.wand;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.vitality.api.essence.Essence;

import net.minecraft.util.IStringSerializable;

public class WandEnums {
	
	public enum WandResult{
		SUCCESS,
		FAIL,
		BREAK,
		DRAIN,
		//do stuff
	}
	
	public enum CoreType implements IStringSerializable{
		ANGELIC("Angelic",WandTier.BASIC),
		ATMOSPHERIC("Atmospheric",WandTier.BASIC),
		DEMONIC("Demonic",WandTier.BASIC),
		ENERGETIC("Energetic",WandTier.BASIC),
		EXOTIC("Exotic",WandTier.BASIC),
		;
		
		String name;
		WandTier accessTier;
		
		public static final String KEY = "core_type"; 
		
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}
		
		public List<Essence> acceptedTypes() {
			List<Essence> acceptedTypes = new ArrayList<Essence>();
			switch(this)
			{
			case ANGELIC:
				acceptedTypes.add(Essence.ANGELIC);
				break;
			case ATMOSPHERIC:
				acceptedTypes.add(Essence.ATMOSPHERIC);
				break;
			case DEMONIC:
				acceptedTypes.add(Essence.DEMONIC);
				break;
			case ENERGETIC:
				acceptedTypes.add(Essence.ENERGETIC);
				break;
			case EXOTIC:
				acceptedTypes.add(Essence.EXOTIC);
				break;
			}
			return acceptedTypes;
		}
		
		CoreType(String name, WandTier accessTier){
			this.name = name;
			this.accessTier = accessTier;
		}
	}
	
	public enum TipType implements IStringSerializable{
		WOODEN("Wooden", WandTier.BASIC),
		;
		String name;
		WandTier accessTier;
		
		public static final String KEY = "tip_type"; 
		
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}
		
		TipType(String name, WandTier accessTier){
			this.name = name;
			this.accessTier = accessTier;
		}
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
