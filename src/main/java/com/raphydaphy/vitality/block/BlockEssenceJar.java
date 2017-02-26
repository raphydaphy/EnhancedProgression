package com.raphydaphy.vitality.block;

import com.raphydaphy.vitality.block.tile.TileEssenceJar;
import com.raphydaphy.vitality.util.EssenceHelper;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEssenceJar extends BlockBase implements ITileEntityProvider
{
	
	public BlockEssenceJar() 
	{
		super(Material.GLASS, "essence_jar");
		this.setHardness(1F);
		this.setResistance(2F);
	}
	
	@Override
	  public boolean hasTileEntity(IBlockState state)
	  {
	    return true;
	  }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEssenceJar();
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    private TileEssenceJar getTE(World world, BlockPos pos) {
        return (TileEssenceJar) world.getTileEntity(pos);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                    EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) 
    {
    	System.out.println("hi");
    	if (heldItem == null)
    	{
    		return true;
    	}
    	
    	else if (!world.isRemote) 
        {
            TileEssenceJar te = getTE(world, pos);
            String essenceTypeVial = EssenceHelper.vialItemToString(heldItem.getItem());
            String essenceTypeJar = te.getEssenceType();
            int essenceStoredJar = te.getEssenceStored();
            if (!player.isSneaking())
            {
            	if (essenceTypeJar == essenceTypeVial || essenceTypeJar == "Unknown")
            	{
            		if (essenceStoredJar <= 1000)
                    {
                    	if (EssenceHelper.useEssence(heldItem, 10))
                    	{
                    		if (essenceTypeJar != essenceTypeVial)
                    		{
                    			te.setEssenceType(essenceTypeVial);
                    		}
                    		te.setEssenceStored(essenceStoredJar + 10);
                            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);
                            player.swingArm(hand);
                    	}
                        
                    }
            	}
            }
            else
            {
                
            	if (essenceTypeJar == essenceTypeVial || essenceTypeVial == "Unknown")
            	{
            		if (essenceStoredJar >= 10)
                    {
                		if (essenceTypeJar == essenceTypeVial)
                		{
                			EssenceHelper.addEssenceFree(heldItem, 10, 1000);
                		}
                		else if (essenceTypeVial == "Unknown")
                		{
                			EssenceHelper.fillEmptyVial(player, heldItem, 10, heldItem.getItem());
                		}
                		te.setEssenceStored(essenceStoredJar - 10);
                        world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1, 1);
                        player.swingArm(hand);
                        
                    }
            	}
            }
            
        }
        
        return true;
    }
	
}
