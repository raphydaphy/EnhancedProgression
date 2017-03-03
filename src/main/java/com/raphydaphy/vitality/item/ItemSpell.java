package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.api.spell.Spell;

public class ItemSpell extends ItemBase 
{
	private final Spell spell;
	public ItemSpell(String name, Spell spell) {
		super("spell_" + name, 1);
		this.spell = spell;
	}
	
	public Spell toSpell() {
		return spell;
	}

}
