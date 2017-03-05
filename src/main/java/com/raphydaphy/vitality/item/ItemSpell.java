package com.raphydaphy.vitality.item;

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
