package com.raphydaphy.vitality.registry;

import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.spell.SpellExcavation;
import com.raphydaphy.vitality.spell.SpellExplosion;
import com.raphydaphy.vitality.spell.SpellFireball;
import com.raphydaphy.vitality.spell.SpellIllumination;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ModSpells {

	public static Spell FIREBALL;
	public static Spell ILLUMINATION;
	public static Spell EXPLOSION;
	public static Spell EXCAVATION;

	public static void init(FMLInitializationEvent e) {
		FIREBALL = new SpellFireball();
		ILLUMINATION = new SpellIllumination();
		EXPLOSION = new SpellExplosion();
		EXCAVATION = new SpellExcavation();
	}

}
