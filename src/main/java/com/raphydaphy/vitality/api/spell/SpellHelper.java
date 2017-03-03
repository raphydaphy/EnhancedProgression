package com.raphydaphy.vitality.api.spell;

public class SpellHelper {
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
}
