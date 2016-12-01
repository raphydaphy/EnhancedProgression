package com.raphydaphy.enhancedprogression.item;

import javax.annotation.Nullable;

import com.raphydaphy.enhancedprogression.EnhancedProgression;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSpellBag extends Item
{
	public ItemSpellBag()
	{
		setUnlocalizedName("spell_bag");
		setRegistryName("spell_bag");
		this.maxStackSize = 1;
	}
	
	public void registerItemModel()
	{
		EnhancedProgression.proxy.registerItemRenderer(this, 0, "spell_bag");
	}

	@Override
	public ItemSpellBag setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	
	@Nullable
	public int getSpellID(ItemStack spell)
	{
		switch(spell.getUnlocalizedName())
		{
		case "spell_card_vital_extraction": return 80;
		case "spell_card_lantern": return 81;
		case "spell_card_explosion": return 82;
		case "spell_card_fireball": return 83;
		case "spell_card_rapidfire": return 84;
		case "spell_card_transmutation": return 85;
		case "spell_card_hunger": return 86;
		case "spell_card_enhanced_extraction": return 87;
		case "spell_card_flight": return 88;
		case "spell_card_forcefield": return 89;
		default: return (Integer) null;
		}
	}
	
	public boolean addSpell(ItemStack bag, ItemStack spell)
	{
		if (bag.getTagCompound().getIntArray("spells").length == 0)
		{
			bag.getTagCompound().setIntArray("spells", new int[10]);
		}
		
		for (int curSpell : bag.getTagCompound().getIntArray("spells"))
		{
			// vital extraction ID
			if (curSpell == getSpellID(spell))
			{
				return false;
			}
		}
		int[] curSpells = bag.getTagCompound().getIntArray("spells");
		curSpells[curSpells.length + 1] = getSpellID(spell);
		bag.getTagCompound().setIntArray("spells", curSpells);
		return true;
	}
}
