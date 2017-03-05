package com.raphydaphy.vitality.api.essence;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.api.wand.WandEnums.WandTier;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

// constructor end: cooldown, potency, cost
// all the base numbers are the cost needed for the illumination spell
// every other spell's stats are a multiple of the base costs
public enum Essence implements IStringSerializable {
	ANGELIC("Angelic", WandTier.BASIC, 1, TextFormatting.AQUA, 2, 1, 1), ATMOSPHERIC("Atmospheric", WandTier.BASIC, 0,
			TextFormatting.DARK_PURPLE, 15, 2, 2), DEMONIC("Demonic", WandTier.BASIC, -1, TextFormatting.RED, 20, 5,
					5), ENERGETIC("Energetic", WandTier.BASIC, -2000, TextFormatting.DARK_AQUA, 18, 3,
							3), EXOTIC("Exotic", WandTier.BASIC, -2000, TextFormatting.DARK_GREEN, 30, 1, 1),;

	private String name;
	private WandTier accessTier;
	private int empoweredDimensionID;
	private TextFormatting format;
	private int baseCooldown;
	private int basePotency;
	private int baseCost;

	/**
	 * Use this for single containers. Use getMultiKey() for multiples. Used to
	 * get/set the amount of essence in an IEssenceContainer.
	 */
	public static final String KEY = "stored_essence";

	/**
	 * The maximum amount of essence a single container can store Currently only
	 * used for wands
	 */
	public static final String MAX_KEY = "max_essence";

	/**
	 * Use this for single containers, Multi-Containers should just rely on
	 * checking ints by getMultiKey(). Used to get/set the type of essence in an
	 * IEssenceContainer.
	 */
	public static final String TYPE_KEY = "essence_type";

	Essence(String name, WandTier accessTier,
			@Nullable /* well not really nullable but put -2000 for none */ int empoweredDimensionID,
			TextFormatting format, int baseCooldown, int basePotency, int baseCost) {
		this.name = name;
		this.accessTier = accessTier;
		this.format = format;
		if (empoweredDimensionID != -2000)
			this.empoweredDimensionID = empoweredDimensionID; // this doesnt do
																// anything rn
																// maybe neato
																// idea for
																// later
		this.baseCooldown = baseCooldown;
		this.basePotency = basePotency;
		this.baseCost = baseCost;
		MiscEssence.locator.put(name, this);
	}

	@Override
	public String getName() {
		return name;
	}

	public TextFormatting getColor() {
		return format;
	}

	public static Essence getByName(String nameIn) {
		return MiscEssence.locator.get(nameIn);
	}

	public String getMultiKey() {
		return KEY + "_" + name;
	}

	public int getCooldown() {
		return baseCooldown;
	}

	public int getPotency() {
		return basePotency;
	}

	public int getCost() {
		return baseCost;
	}

}
