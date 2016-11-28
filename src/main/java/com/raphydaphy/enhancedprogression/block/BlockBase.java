package com.raphydaphy.enhancedprogression.block;

import java.util.Random;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.achievement.IPickupAchievement;
import com.raphydaphy.enhancedprogression.achievement.ModAchievements;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBase extends Block implements IPickupAchievement
{

	protected String name;

	public BlockBase(Material material, String name)
	{
		super(material);

		this.name = name;

		setUnlocalizedName(name);
		setRegistryName(name);
	}

	public void registerItemModel(ItemBlock itemBlock)
	{
		EnhancedProgression.proxy.registerItemRenderer(itemBlock, 0, name);
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
	}

	@Override
	public BlockBase setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	public Achievement getAchievementOnPickup(ItemStack stack, EntityPlayer player, EntityItem item) {
		switch(this.name)
		{
			case "imbued_log":
			{
				return ModAchievements.pickup_imbued_log;
			}
			case "fluxed_log":
			{
				return ModAchievements.pickup_fluxed_log;
			}	
			default:
			{
				return null;
			}
		}
	}

}