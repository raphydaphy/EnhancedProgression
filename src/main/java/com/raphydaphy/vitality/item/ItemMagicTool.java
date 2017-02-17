package com.raphydaphy.vitality.item;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.helper.ToolHelper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
	
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        if(!playerIn.isSneaking()){
            return Items.IRON_HOE.onItemUse(new ItemStack(Items.IRON_HOE), playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);
        }
        else{
            return Items.IRON_SHOVEL.onItemUse(new ItemStack(Items.IRON_SHOVEL), playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);
        }
    }
	
	public Set<String> getToolClasses(ItemStack stack){
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.add("pickaxe");
        hashSet.add("axe");
        hashSet.add("shovel");
        return hashSet;
    }

}
