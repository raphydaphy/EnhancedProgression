package com.raphydaphy.enhancedprogression.item;

import javax.annotation.Nonnull;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.block.BlockAltar;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBasicWand extends Item
{
	protected String name;

	public ItemBasicWand(String name) {
		this.name = name;
		setUnlocalizedName(name);
		setRegistryName(name);
		this.maxStackSize = 1;
	}
	
	@Override
	public ItemBasicWand setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
	
	public void registerItemModel() {
		EnhancedProgression.proxy.registerItemRenderer(this, 0, name);
	}
	
	 @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }
	 
	 @Nonnull
		@Override
		public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
			Block block = world.getBlockState(pos).getBlock();

			if(block instanceof BlockAltar) {

				boolean wanded;
				wanded = ((BlockAltar) block).onUsedByWand(player, par1ItemStack, world, pos, side);
				if(wanded && world.isRemote)
				{
					player.swingArm(hand);
				}
				
				return wanded ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
			}

			return EnumActionResult.PASS;
		}

}
