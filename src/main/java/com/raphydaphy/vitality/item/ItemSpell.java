package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.achievement.IPickupAchievement;
import com.raphydaphy.vitality.achievement.ModAchievements;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class ItemSpell extends ItemBase implements IPickupAchievement
{
	public static int spellID;
	public static String name;
	public ItemSpell(String parName, int parSpellID) 
	{
		super(parName, 1);
		super.setCreativeTab(Vitality.creativeTab);
		name = parName;
		spellID = parSpellID;
	}
	
	public int getID()
	{
		return spellID;
	}
	
	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) 
	{
		switch(this.name)
		{
			case "spell_card_vital_extraction":
			{
				return ModAchievements.craft_vital_extraction;
			}
			case "spell_card_lantern":
			{
				return ModAchievements.craft_magic_lantern;
			}
			case "spell_card_explosion":
			{
				return ModAchievements.craft_contained_explosion;
			}
			case "spell_card_fireball":
			{
				return ModAchievements.craft_radiant_fireball;
			}
			case "spell_card_fertilization":
			{
				return ModAchievements.craft_renewed_fertilization;
			}
			case "spell_card_placement":
			{
				return ModAchievements.craft_angelic_placement;
			}
			case "spell_card_transmutation":
			{
				return ModAchievements.craft_cryptic_transmutation;
			}
			case "spell_card_flight":
			{
				return ModAchievements.craft_elevated_momentum;
			}
			case "spell_card_forcefield":
			{
				return ModAchievements.craft_unabridged_immortality;
			}
			default:
			{
				return null;
			}
		}
	}

}
