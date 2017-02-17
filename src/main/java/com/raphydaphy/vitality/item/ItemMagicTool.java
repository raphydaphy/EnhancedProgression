package com.raphydaphy.vitality.item;

import java.util.HashSet;

import javax.annotation.Nonnull;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.helper.ToolHelper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMagicTool extends ItemTool
{
	protected String name;
	protected String repairOredict;
	
	public ItemMagicTool(float attack, float speed, ToolMaterial toolMat, String unlocalizedName)
	{
        super(attack, speed, toolMat, new HashSet<Block>());

        this.name = unlocalizedName;
        this.setMaxDamage(this.getMaxDamage()*4);
        this.setHarvestLevel("pickaxe", toolMat.getHarvestLevel());
		setUnlocalizedName(unlocalizedName);
		setRegistryName(unlocalizedName);
		this.setCreativeTab(Vitality.creativeTab);
    }
	
	public void registerItemModel()
	{
		Vitality.proxy.registerItemRenderer(this, 0, name);
	}

	public ItemMagicTool setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, @Nonnull EntityLivingBase par3EntityLivingBase) {
		ToolHelper.damageItem(par1ItemStack, 1, par3EntityLivingBase, 25);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(@Nonnull ItemStack stack, @Nonnull World world, IBlockState state, @Nonnull BlockPos pos, @Nonnull EntityLivingBase entity) {
		if(state.getBlockHardness(world, pos) != 0F)
			ToolHelper.damageItem(stack, 1, entity, 25);

		return true;
	}

}
