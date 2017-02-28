package com.raphydaphy.vitality.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.essence.EssenceHelper;
import com.raphydaphy.vitality.essence.IEssenceContainer;
import com.raphydaphy.vitality.essence.IWandable;
import com.raphydaphy.vitality.init.Reference;
import com.raphydaphy.vitality.util.NBTHelper;
import com.raphydaphy.vitality.util.ParticleHelper;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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
										  EssenceHelper.coreToAcceptedEssenceTypesList(EssenceHelper.getWandCore(stack)) + 
										  " Essence", TextFormatting.BOLD);
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
	
	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		
		if (pos instanceof IWandable)
		{
			TileEntity tile = world.getTileEntity(pos);
			
			if (tile instanceof IEssenceContainer)
			{
				IEssenceContainer container = (IEssenceContainer)tile;
				int essenceStored = container.getEssenceStored();
				String essenceType = container.getEssenceType();
				
				if (essenceStored > 0)
				{
					List acceptedTypes = EssenceHelper.coreToAcceptedEssenceTypesList(EssenceHelper.getWandCore(stack));
					for (int i = 0;i < acceptedTypes.size(); i++)
					{
						if (acceptedTypes.get(i) == essenceType)
						{
							NBTHelper.setString(stack, "useAction", "BOW");
							NBTHelper.setString(stack, "curAction", "extractFromContainer");
							NBTHelper.setInt(stack, "posX", pos.getX());
							NBTHelper.setInt(stack, "posY", pos.getY());
							NBTHelper.setInt(stack, "posZ", pos.getZ());
							NBTHelper.setString(stack, "essenceTypeOperation", essenceType);
							return EnumActionResult.SUCCESS;
						}
					}
				}
			}
		}
		return EnumActionResult.PASS;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
		// runs every 10 ticks
		if (world.getTotalWorldTime() % 10 == 0)
		{
			if (stack.hasTagCompound())
			{
				if (isSelected)
				{
					BlockPos pos = new BlockPos(NBTHelper.getInt(stack, "posX", 0),
												NBTHelper.getInt(stack, "posY", 0),
												NBTHelper.getInt(stack, "posZ", 0));
					switch (NBTHelper.getString(stack, "curAction", "nothing"))
					{
					case "extractFromContainer":
						TileEntity tile = world.getTileEntity(pos);
						IEssenceContainer container = (IEssenceContainer)tile;
						int essenceStored = container.getEssenceStored();
						String essenceType = container.getEssenceType();
						
						if (essenceStored > 0)
						{
							container.setEssenceStored(essenceStored - 1);
						}
						return;
					default:
						NBTHelper.setString(stack, "useAction", "NONE");
						return;
					}
				}
				else
				{
					if (NBTHelper.getString(stack, "curAction", "nothing") != "nothing")
					{
						this.onItemUseFinish(stack, world, (EntityLivingBase)entity);
						return;
					}
				}
			}
		}
    }
	
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase player, int timeLeft)
	{	
		NBTHelper.setString(stack, "curAction", "nothing");
		NBTHelper.setString(stack, "useAction", "NONE");
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
	
	@Nullable
	public EnumAction getItemUseAction(ItemStack stack)
	{
		switch(NBTHelper.getString(stack, "useAction", "NONE"))
		{
		case "BOW":
			return EnumAction.NONE;
		case "EAT":
			return EnumAction.EAT;
		case "BLOCK":
			return EnumAction.BLOCK;
		case "DRINK":
			return EnumAction.DRINK;
		case "NONE":
			return EnumAction.NONE;
		}
		return EnumAction.NONE;
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
}
