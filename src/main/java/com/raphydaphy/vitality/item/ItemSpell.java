package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.api.spell.Spell;

public class ItemSpell extends ItemBase {
	private final int spellId;

	public ItemSpell(String name, int spellId) {
		super(name, 1);
		this.spellId = spellId;
	}

	public int getSpellId() {
		return spellId;
	}

}
