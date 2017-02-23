package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.util.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemEssenceVial extends ItemBase
{
	public ItemEssenceVial(String name)
	{
		super(name, 1);
	}
	

	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player, EnumHand hand)
    {
		if (player.isSneaking())
		{
			Vitality.proxy.setActionText("Storing " + NBTHelper.getInt(stack, "essenceStored", 0) + " / 1000 Essence", TextFormatting.DARK_PURPLE);
			player.swingArm(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
}