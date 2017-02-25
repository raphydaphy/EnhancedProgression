package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.block.BlockEssenceJar;
import com.raphydaphy.vitality.util.NBTHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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
			RayTraceResult raytraceresult = this.rayTrace(worldIn, player, true);
	        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, worldIn, stack, raytraceresult);
	        if (ret != null) return ret;

	        if (raytraceresult == null)
	        {
	            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	        }
	        else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
	        {
	            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	        }
	        else
	        {
	            BlockPos blockpos = raytraceresult.getBlockPos();

	            if (!worldIn.isBlockModifiable(player, blockpos))
	            {
	                return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	            }
	            else
	            {
	                if (!player.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, stack))
	                {
	                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	                }
	                else
	                {
	                    IBlockState iblockstate = worldIn.getBlockState(blockpos);

	                    if (!(iblockstate.getBlock() instanceof BlockEssenceJar))
	                    {
	                    	Vitality.proxy.setActionText("Storing " + NBTHelper.getInt(stack, "essenceStored", 0) + " / 1000 Essence", TextFormatting.DARK_PURPLE);
	            			player.swingArm(hand);
	            			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	                    }
	                    else
	                    {
	                        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	                    }
	                }
	            }
	        }
			
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
}