package com.raphydaphy.enhancedprogression.item;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.achievement.ICraftAchievement;
import com.raphydaphy.enhancedprogression.achievement.IPickupAchievement;
import com.raphydaphy.enhancedprogression.achievement.ModAchievements;

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
		EnhancedProgression.proxy.registerItemRenderer(this, 0, name);
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
			case "spell_card_transmutation":
			{
				return ModAchievements.craft_cryptic_transmutation;
			}
			case "spell_card_flight":
			{
				return ModAchievements.craft_elevated_momentum;
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