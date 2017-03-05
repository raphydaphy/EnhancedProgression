package com.raphydaphy.vitality.api.wand;

import com.raphydaphy.vitality.api.essence.Essence;

import net.minecraft.util.IStringSerializable;

public class WandEnums {

	public enum WandResult {
		SUCCESS, FAIL, BREAK, DRAIN,
		// do stuff
	}

	public enum CoreType implements IStringSerializable {
		ANGELIC("Angelic", WandTier.BASIC, Essence.ANGELIC), 
		ATMOSPHERIC("Atmospheric", WandTier.BASIC, Essence.ATMOSPHERIC), 
		DEMONIC("Demonic", WandTier.BASIC, Essence.DEMONIC), 
		ENERGETIC("Energetic", WandTier.BASIC, Essence.ENERGETIC), 
		EXOTIC("Exotic", WandTier.BASIC, Essence.EXOTIC),;

		private String name;
		private WandTier accessTier;
		private Essence essence;
		private int cdmp;
		private int cmp;
		private int pmp;

		public static final String KEY = "core_type";

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		public Essence getCoreType() {
			return this.essence;
		}
		
		public int getCooldownMultiplier(){
			return cdmp;
		}
		
		public int getPotencyMultiplier(){
			return pmp;
		}
		public int getCostMultiplier(){
			return cmp;
		}

		CoreType(String name, WandTier accessTier, Essence type, int cdMP, int cMP, int pMP) {
			this.name = name;
			this.accessTier = accessTier;
			this.essence = type;
			cdmp = cdMP;
			cmp = cMP;
			pmp = pMP;
		}
	}

	public enum TipType implements IStringSerializable {
		WOODEN("Wooden", WandTier.BASIC),;
		String name;
		WandTier accessTier;

		public static final String KEY = "tip_type";

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		TipType(String name, WandTier accessTier) {
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
	public enum WandTier {
		BASIC, EMPOWERED, ETHEREAL, ETERNAL;

		// WandTier(String name, Item core, int maxCapacity?){}
	}

}
