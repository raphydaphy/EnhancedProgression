package com.raphydaphy.vitality.api.spell;

public class SpellHelper {
	// TODO: this class is shit but maps are too hard so rip 
	
	// maps are too hard 4 me
	public static Spell getSpellFromID(int id)
	{
		switch(id) 
		{
		case 0:
			return Spell.ILLUMINATION;
		case 1:
			return Spell.FIREBALL;
		}
		return null;
	}
	
	// maps are still too hard
	public static int getIDFromSpell(Spell spell)
	{
		switch(spell)
		{
		case ILLUMINATION:
			return 0;
		case FIREBALL:
			return 1;
		}
		return 0;
	}
}
