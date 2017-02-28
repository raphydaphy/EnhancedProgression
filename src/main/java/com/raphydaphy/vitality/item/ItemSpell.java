package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.achievement.IPickupAchievement;
import com.raphydaphy.vitality.achievement.ModAchievements;
import com.raphydaphy.vitality.helper.SpellControl;
import com.raphydaphy.vitality.init.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10)
	{
		if (!ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), new ItemStack(ModItems.advanced_wand)) &
			!ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), new ItemStack(ModItems.master_wand)) &
			!ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), new ItemStack(ModItems.basic_wand_tin)) &
			!ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), new ItemStack(ModItems.basic_wand_copper)))
		{
			for(int i = 0; i < player.inventory.getSizeInventory(); i++) 
			{
				ItemStack stackAt = player.inventory.getStackInSlot(i);
				if (stackAt != null)
				{
					if (stackAt.getItem() == ModItems.essence_vial_full)
					{
						SpellControl wandFunc = new SpellControl();
						if (wandFunc.getActiveSpell(player.getHeldItem(hand)) > 809 && wandFunc.getActiveSpell(player.getHeldItem(hand)) < 820)
						{
							if (wandFunc.lanternSpell(stackAt, player, world, pos, hand, side, par8, par9, par10, this))
							{
								return EnumActionResult.SUCCESS;
							}
							return EnumActionResult.FAIL;
						}
						
					}
				}
			}
		}
		return EnumActionResult.PASS;
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
			case "spell_card_fertilization_1":
			{
				return ModAchievements.craft_renewed_fertilization;
			}
			case "spell_card_placement_1":
			{
				return ModAchievements.craft_angelic_placement;
			}
			case "spell_card_lightning_1":
			{
				return ModAchievements.craft_imbued_lightning;
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