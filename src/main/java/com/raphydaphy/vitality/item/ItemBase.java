package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.achievement.ICraftAchievement;
import com.raphydaphy.vitality.achievement.IPickupAchievement;
import com.raphydaphy.vitality.achievement.ModAchievements;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class ItemBase extends Item implements ICraftAchievement, IPickupAchievement
{

	protected String name;

	public ItemBase(String name, int maxStack)
	{
		this.name = name;
		setUnlocalizedName(name);
		setRegistryName(name);
		this.maxStackSize = maxStack;
	}

	public void registerItemModel()
	{
		Vitality.proxy.registerItemRenderer(this, 0, name);
	}

	@Override
	public ItemBase setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
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
			case "spell_card_lantern_1":
			{
				return ModAchievements.craft_magic_lantern;
			}
			case "spell_card_explosion_1":
			{
				return ModAchievements.craft_contained_explosion;
			}
			case "spell_card_fireball_1":
			{
				return ModAchievements.craft_radiant_fireball;
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

	@Override
	public Achievement getAchievementOnPickup(ItemStack stack, EntityPlayer player, EntityItem item) 
	{
		switch(this.name)
		{
			case "ingot_bronze_imbued":
			{
				return ModAchievements.pickup_imbued_bronze;
			}
			case "ingot_fluxed":
			{
				return ModAchievements.pickup_fluxed_ingot;
			}
			default:
			{
				return null;
			}
		}
	}

}