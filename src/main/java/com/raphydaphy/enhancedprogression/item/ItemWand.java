package com.raphydaphy.enhancedprogression.item;

import com.raphydaphy.enhancedprogression.EnhancedProgression;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWand extends Item
{
	protected String name;

	public ItemWand(String name)
	{
		this.name = name;
		setUnlocalizedName(name);
		setRegistryName(name);
		this.maxStackSize = 1;
	}

	@Override
	public ItemWand setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}

	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int timeLeft)
	{
		if (!world.isRemote)
		{
			EntityLargeFireball ball = new EntityLargeFireball(world);
	        ball.explosionPower = timeLeft / 10;
	        ball.forceSpawn = true;
	        world.spawnEntityInWorld(ball);
	        ball.accelerationX = player.getLookVec().xCoord * 2;
	        ball.accelerationY = player.getLookVec().yCoord * 2;
	        ball.accelerationZ = player.getLookVec().zCoord * 2;
	        ball.posX = player.posX;
	        ball.posY = player.posY + 1;
	        ball.posZ = player.posZ;
		}
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}

	public void registerItemModel()
	{
		EnhancedProgression.proxy.registerItemRenderer(this, 0, name);
	}
	
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		playerIn.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
}
