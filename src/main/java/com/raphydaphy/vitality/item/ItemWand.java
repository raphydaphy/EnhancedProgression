package com.raphydaphy.vitality.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.essence.EssenceHelper;
import com.raphydaphy.vitality.init.Reference;
import com.raphydaphy.vitality.util.NBTHelper;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWand extends ItemBase
{
	public ItemWand(String parName) 
	{
		super(parName, 1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
		if (player.isSneaking())
		{
			// i want to remove the [] brackets from the essence name but not sure how rn
			Vitality.proxy.setActionText("Storing " + 
										  EssenceHelper.getEssenceStored(stack) + " / " + 
										  EssenceHelper.getMaxEssence(stack) + " " + 
										  EssenceHelper.coreToAcceptedEssenceTypesList(EssenceHelper.getWandCore(stack)).toString() + 
										  " Essence", TextFormatting.BOLD);
			player.swingArm(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
	
	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10)
	{
		//if (world.getBlockState(pos).getBlock() instanceof IWandable)
		//{
			//TileEntity tile = world.getTileEntity(pos);
			
			//if (tile instanceof IEssenceContainer)
			//{
				if (NBTHelper.getString(stack, "curAction", "nothing") != "extractFromContainer")
				{
					System.out.println("started extraction");
					player.setActiveHand(hand);
					if (!world.isRemote)
					{
						NBTHelper.setString(stack, "curAction", "extractFromContainer");
					}
					return EnumActionResult.SUCCESS;
				}
			//}
		//}
		return EnumActionResult.PASS;
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if (NBTHelper.getString(stack, "curAction", "nothing") != "nothing")
		{
			
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
		System.out.println(NBTHelper.getString(stack, "curAction", null));
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase player, int timeLeft)
	{	
		System.out.println("stopped");
		if (!worldIn.isRemote)
		{
			NBTHelper.setString(stack, "curAction", "nothing");
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
		if (stack.hasTagCompound())
		{
	        String coreType = stack.getTagCompound().getString("coreType");
	        String tipType = stack.getTagCompound().getString("tipType");
	        
	        return tipType.toLowerCase() + "_" + coreType.toLowerCase() + "_wand";
		}
		else
		{
			return "invalid_wand";
		}
        
    }
	
	@Override
	@Nullable
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.NONE;
	}
	
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
		return true;
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        return new ModelResourceLocation(Reference.MOD_ID + ":wand", "inventory");
    }
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
}
