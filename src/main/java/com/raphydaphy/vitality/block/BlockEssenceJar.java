package com.raphydaphy.vitality.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.util.EssenceHelper;
import com.raphydaphy.vitality.util.ParticleHelper;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEssenceJar extends BlockBase
{
	public static final PropertyInteger STAT = PropertyInteger.create("stat", 0, 8);
	
	public BlockEssenceJar() 
	{
		super(Material.GLASS, "essence_jar");
		this.setHardness(1F);
		this.setResistance(2F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(STAT, Integer.valueOf(0)));
	}
	
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    public void setEssenceStat(World worldIn, BlockPos pos, IBlockState state, int stat)
    {
        worldIn.setBlockState(pos, state.withProperty(STAT, Integer.valueOf(MathHelper.clamp_int(stat, 0, 8))), 2);
        worldIn.updateComparatorOutputLevel(pos, this);
    }
    
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (heldItem == null)
        {
            return true;
        }
        else
        {
            int i = ((Integer)state.getValue(STAT)).intValue();
            String essenceType = EssenceHelper.vialItemToString(heldItem.getItem());
            
        	if (EssenceHelper.getJarStoring(i) == essenceType || i == 0)
        	{
        		if (i < EssenceHelper.getJarMax(essenceType) && !worldIn.isRemote)
                {
                	if (EssenceHelper.useEssence(heldItem, 10))
                	{
                		this.setEssenceStat(worldIn, pos, state, EssenceHelper.increaseJarForType(i, essenceType));
                        ParticleHelper.spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, worldIn, true, pos, 5, 1);
                        worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);
                        playerIn.swingArm(hand);
                	}
                    
                }
        	}
        }
        return false;
    }
    
    
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.essence_jar);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ModBlocks.essence_jar);
    }
    
    
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return ((Integer)blockState.getValue(STAT)).intValue();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(STAT, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(STAT)).intValue();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {STAT});
    }
}
