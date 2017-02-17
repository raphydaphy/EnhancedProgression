package com.raphydaphy.vitality.item;

import javax.annotation.Nonnull;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.achievement.IPickupAchievement;
import com.raphydaphy.vitality.achievement.ModAchievements;
import com.raphydaphy.vitality.init.ModItems;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
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
		if (player.getHeldItemMainhand() != null)
		{
			if (!(player.getHeldItemMainhand().getItem() instanceof ItemWand))
			{
				for(int i = 0; i < player.inventory.getSizeInventory(); i++) 
				{
					ItemStack stackAt = player.inventory.getStackInSlot(i);
					if (stackAt != null)
					{
						if (stackAt.getItem() == ModItems.essence_vial_full)
						{
							ItemWand wandFunc = new ItemWand("func_wand", 3, 10000, false);
							if (wandFunc.getActiveSpell(player.getHeldItemOffhand()) > 809 && wandFunc.getActiveSpell(player.getHeldItemOffhand()) < 820)
							{
								if (!world.isRemote)
								{
									int essenceVal = 5;
									int cooldown = 20;
									if (wandFunc.getActiveSpell(player.getHeldItemOffhand()) == 811)
									{
										essenceVal = 2;
										cooldown = 10;
									}
									else if (wandFunc.getActiveSpell(player.getHeldItemOffhand()) == 812)
									{
										essenceVal = 0;
										cooldown = 5;
									}
									if (wandFunc.useEssence(essenceVal, stackAt))
									{
										wandFunc.spawnParticles(EnumParticleTypes.FLAME, world, true,pos, 15, 1);
										world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);
										ItemStack stackToPlace = new ItemStack(Blocks.TORCH);
										stackToPlace.onItemUse(player, world, pos, hand, side, par8, par9, par10);
										player.swingArm(hand);
										player.getCooldownTracker().setCooldown(this, cooldown);
										return EnumActionResult.SUCCESS;
									}
									else
									{
										Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
										return EnumActionResult.FAIL;
									}
								}
							}
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
