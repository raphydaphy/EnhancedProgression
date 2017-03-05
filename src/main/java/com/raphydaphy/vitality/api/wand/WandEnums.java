package com.raphydaphy.vitality.api.wand;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.api.essence.Essence;

import net.minecraft.util.IStringSerializable;

public class WandEnums {

	private static Map<String, CoreType> coreMap = new HashMap<String, CoreType>();
	private static Map<String, TipType> tipMap = new HashMap<String, TipType>();
	
	public enum WandResult {
		SUCCESS, FAIL, BREAK, DRAIN,
		// do stuff
	}

	public enum CoreType implements IStringSerializable {
		ANGELIC("Angelic", WandTier.BASIC, Essence.ANGELIC, 0.5F, 0.5F), 
		ATMOSPHERIC("Atmospheric", WandTier.BASIC, Essence.ATMOSPHERIC, 0.75F, 0.8F), 
		DEMONIC("Demonic", WandTier.BASIC, Essence.DEMONIC, 3.0F, 2.0F), 
		ENERGETIC("Energetic", WandTier.BASIC, Essence.ENERGETIC, 0.25F, 1.0F), 
		EXOTIC("Exotic", WandTier.BASIC, Essence.EXOTIC, 1.0F, 1.5F);

		private String name;
		private WandTier accessTier;
		private Essence essence;
		private float cdmp;
		private float pmp;

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		public Essence getCoreType() {
			return this.essence;
		}
		
		public float getCooldownMultiplier(){
			return cdmp;
		}
		
		public float getPotencyMultiplier(){
			return pmp;
		}
		
		@Nullable
		public static CoreType getByName(String name){
			return coreMap.get(name);
		}
		
		CoreType(String name, WandTier accessTier, Essence type, float cdMP, float pMP) {
			this.name = name;
			this.accessTier = accessTier;
			this.essence = type;
			cdmp = cdMP;
			pmp = pMP;
			coreMap.put(name, this);
		}
	}

	public enum TipType implements IStringSerializable {
		WOODEN("Wooden", WandTier.BASIC, 1.5f),;
		String name;
		WandTier accessTier;

		private float cmp;
		
		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		TipType(String name, WandTier accessTier, float cMP) {
			this.name = name;
			this.accessTier = accessTier;
			cmp = cMP;
			tipMap.put(name, this);
		}
		
		public float getCostMultiplier(){
			return cmp;
		}
		
		@Nullable
		public static TipType getByName(String name){
			return tipMap.get(name);
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
