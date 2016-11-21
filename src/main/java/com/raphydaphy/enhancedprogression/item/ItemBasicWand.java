package com.raphydaphy.enhancedprogression.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.block.BlockAltar;
import com.raphydaphy.enhancedprogression.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBasicWand extends Item
{
	protected String name;

	private int essenceStored = 0;
	private int essenceCapacity = 0;
	
	private List<BlockPos> replaceBlock = new ArrayList<BlockPos>();
	private boolean unfinished = false;
	private int currentBlockId = 0;
	private BlockPos curBlock;
	private int delay = 0;
	
	public ItemBasicWand(String name) {
		this.name = name;
		this.setEssenceCapacity(1000);
		setUnlocalizedName(name);
		setRegistryName(name);
		this.maxStackSize = 1;
	}
	
	@Override
	public ItemBasicWand setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
	
	public void setEssenceCapacity(int capacity)
	{
		essenceCapacity = capacity;
	}
	
	public int getEssenceStored()
	{
		return essenceStored;
	}
	
	public int getMaxEssence()
	{
		return essenceCapacity;
	}
	
	public boolean useEssence(int amount)
	{
		if (getEssenceStored() - amount > 0)
		{
			essenceStored -= amount;
			return true;
		}
		return false;
	}
	
	public boolean addEssence(int amount)
	{
		if (getEssenceStored() + amount < getMaxEssence() + 1)
		{
			essenceStored += amount;
			return true;
		}
		return false;
	}
	
	private static Iterable<BlockPos.MutableBlockPos> WOOD_SEARCH_AREA = BlockPos.getAllInBoxMutable(new BlockPos(-5, -5, -5), new BlockPos(5, 25, 5));
	
	boolean checkPlatform(int xOff, int yOff, int zOff, Block block, BlockPos pos, World world) 
	{	
		return world.getBlockState(pos.add(xOff, yOff, zOff)).getBlock() == block;
	}
	
	public void registerItemModel() {
		EnhancedProgression.proxy.registerItemRenderer(this, 0, name);
	}
	
	 @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }
	 
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected)
    {
		
		if(unfinished)
		{
			if (isSelected)
			{
				if (delay == 0 && replaceBlock.size() > currentBlockId && !world.isRemote)
				{
					curBlock = replaceBlock.get(currentBlockId);
					world.playSound(null, curBlock,SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1, 1);
					world.setBlockState(curBlock, ModBlocks.dead_log.getDefaultState());
					
					this.addEssence(ThreadLocalRandom.current().nextInt(2, 15 + 1));
					
					currentBlockId++;
					delay = 10;
					
					if (ThreadLocalRandom.current().nextInt(1, 50 + 1) == 6)
					{
						if (canBreak())
						{
							world.playSound(null, curBlock,SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1, 1);
							stack.stackSize = 0;
							unfinished = false;
						}
					}
				}
				else if (replaceBlock.size() > currentBlockId && !world.isRemote)
				{
					delay--;
					
				}
				else if (!world.isRemote)
				{
					unfinished = false;
				}
			}
			else
			{
				unfinished = false;
			}
		}
    }
	
	public boolean canBreak()
	{
		return true;
	}
	
	public NBTTagCompound getNBTShareTag(ItemStack stack)
    {
        return stack.getTagCompound();
    }
	
	
	public boolean getShareTag()
    {
        return true;
    }
	 
	public boolean updateItemStackNBT(NBTTagCompound nbt)
    {
        return false;
    }
	 
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
		if (playerIn.isSneaking())
		{
			EnhancedProgression.proxy.setActionText((I18n.format("gui.checkessence.name") + " " + getEssenceStored() + "/" + getMaxEssence() + " " + (I18n.format("gui.essence.name"))));
		}
		playerIn.swingArm(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }
	
	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) 
	{
		Block block = world.getBlockState(pos).getBlock();

		if(block instanceof BlockAltar) 
		{
			boolean wanded;
			wanded = ((BlockAltar) block).onUsedByWand(player, par1ItemStack, world, pos, side);
			if(wanded && world.isRemote)
			{
				player.swingArm(hand);
			}
			
			return wanded ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		}
		else if(block instanceof BlockLog) 
		{
			System.out.println("Starting new loop");
			
			replaceBlock.clear();
			currentBlockId = 0;
			delay = 0;
			
			for (BlockPos position : WOOD_SEARCH_AREA) 
			{
				if(checkPlatform(position.getX(), position.getY(), position.getZ(), block, pos, world))
				{
					BlockPos toReplace = pos.add(position.getX(), position.getY(), position.getZ());
					replaceBlock.add(toReplace);
				}
			}
			unfinished = true;
			player.setActiveHand(hand);
			return EnumActionResult.SUCCESS;
		}
		else
		{
			if (player.isSneaking())
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.checkessence.name") + " " + getEssenceStored() + "/" + getMaxEssence() + " " + (I18n.format("gui.essence.name"))));
			}
		}
		return EnumActionResult.PASS;
	}
	
	 /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
}
