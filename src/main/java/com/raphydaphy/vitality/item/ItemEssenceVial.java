package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.init.ModItems;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemEssenceVial extends ItemBase 
{
	protected String name;
	public ItemEssenceVial(String name) 
	{
		super(name, 1);
		this.name=name;
		super.setCreativeTab(Vitality.creativeTab);
	}
	
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player, EnumHand hand)
	{
		if (player.getHeldItemMainhand().getItem() != null)
		{
			if (player.getHeldItemMainhand().getItem() == ModItems.essence_vial_full && player.isSneaking())
			{
					Vitality.proxy.setActionText((I18n.format("gui.checkessence.name") + " "
							+ stack.getTagCompound().getInteger("essenceStored") +" " + (I18n.format("gui.essence.name"))));
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);	
			}
			if (player.getHeldItemMainhand().getItem() == ModItems.essence_vial_empty && player.isSneaking())
			{
					Vitality.proxy.setActionText((I18n.format("gui.checkessence.name") + " "
							+ "0" +" " + (I18n.format("gui.essence.name"))));
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);	
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);	
	}
}
