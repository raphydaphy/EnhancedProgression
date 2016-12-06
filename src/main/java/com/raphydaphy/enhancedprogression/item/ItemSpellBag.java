package com.raphydaphy.enhancedprogression.item;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.init.ModItems;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

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
	
	/*
	 * Returns the localized name of a spell
	 * Uses the spell ID to find the name
	 * 
	 * Note: needs to actually use localisation
	 * at the moment, this just gives a string
	 * so for other languages it will be wrong
	 */
	public String getSpellNameFromID(int spellID)
	{
		switch (spellID)
		{
		case 80:
			return "Vital Extraction";
		case 81:
			return "Magic Lantern";
		case 82:
			return "Contained Explosion";
		case 83:
			return "Radiant Fireball";
		case 84:
			return "Rapid Fire";
		case 85:
			return "Cryptic Transmutation";
		case 86:
			return "Hunger";
		case 87:
			return "Enhanced Extraction";
		case 88:
			return "Ëlevated Momentum";
		case 89:
			return "Unabridged Immortality";
		}
		return "Invalid Spell!";
	}

	@Override
	public ItemSpellBag setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	
	/*
	 * Called when the player right-clicks while holding the item
	 */
	public ActionResult<ItemStack> onItemRightClick(ItemStack bag, World worldIn, EntityPlayer player, EnumHand hand)
	{
		if (player.getHeldItemMainhand() != null)
		{
			if (player.isSneaking() && player.getHeldItemMainhand().getItem() == ModItems.spell_bag)
			{
				if (bag.hasTagCompound())
				{
					if (bag.getTagCompound().getInteger("selectedSpell") != 0)
					{
						int[] spellArray = bag.getTagCompound().getIntArray("spells");
						for (int i = 0; i < spellArray.length; i++)
						{
							if (spellArray[i] != 0)
							{
								if (spellArray[i] == bag.getTagCompound().getInteger("selectedSpell"))
								{
									if (spellArray[i + 1] != 0)
									{
										bag.getTagCompound().setInteger("selectedSpell", spellArray[i + 1]);
									}
									else
									{
										bag.getTagCompound().setInteger("selectedSpell", spellArray[0]);
									}
									EnhancedProgression.proxy.setActionText((I18n.format("gui.setspell.name")) + " " + getSpellNameFromID(bag.getTagCompound().getInteger("selectedSpell")));
									player.swingArm(hand);
									return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, bag);	
								}
							}
							else
							{
								break;
							}
						}
					}
					else
					{
						int[] spellArray = bag.getTagCompound().getIntArray("spells");
						bag.getTagCompound().setInteger("selectedSpell", spellArray[0]);
						EnhancedProgression.proxy.setActionText((I18n.format("gui.setspell.name")) + " " + getSpellNameFromID(bag.getTagCompound().getInteger("selectedSpell")));
						player.swingArm(hand);
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, bag);
					}
				}
				EnhancedProgression.proxy.setActionText((I18n.format("gui.nospells.name")));
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, bag);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, bag);
	}
}
