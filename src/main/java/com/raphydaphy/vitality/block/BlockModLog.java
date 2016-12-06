package com.raphydaphy.vitality.block;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.achievement.IPickupAchievement;
import com.raphydaphy.vitality.achievement.ModAchievements;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class BlockModLog extends BlockBase implements IPickupAchievement
{
	protected String name;
	//	public static final PropertyEnum<BlockModLog.EnumLogAxis> MOD_LOG_AXIS = PropertyEnum.<BlockModLog.EnumLogAxis>create("axis", BlockModLog.EnumLogAxis.class);

	public BlockModLog(String name)
	{
		super(Material.WOOD, name);
		this.name = name;
		this.setSoundType(SoundType.WOOD);
		this.setHardness(2.0F);
		setUnlocalizedName(name);
	}

	public void registerItemModel(ItemBlock itemBlock)
	{
		Vitality.proxy.registerItemRenderer(itemBlock, 0, name);
	}

	@Override
	public BlockModLog setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	@Override
	public Achievement getAchievementOnPickup(ItemStack stack, EntityPlayer player, EntityItem item) 
	{
		switch(this.name)
		{
			case "fluxed_log":
			{
				return ModAchievements.pickup_fluxed_log;
			}
			case "imbued_log":
			{
				return ModAchievements.pickup_imbued_log;
			}
			default:
			{
				return null;
			}
		}
	}
	/* 
	 * fuck it idk how to fix this
	 * 
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getStateFromMeta(meta).withProperty(MOD_LOG_AXIS, BlockModLog.EnumLogAxis.fromFacingAxis(facing.getAxis()));
    }
	
	public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:

                switch ((BlockModLog.EnumLogAxis)state.getValue(MOD_LOG_AXIS))
                {
                    case X:
                        return state.withProperty(MOD_LOG_AXIS, BlockModLog.EnumLogAxis.Z);
                    case Z:
                        return state.withProperty(MOD_LOG_AXIS, BlockModLog.EnumLogAxis.X);
                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    @Override public boolean canSustainLeaves(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos){ return true; }
    @Override public boolean isWood(net.minecraft.world.IBlockAccess world, BlockPos pos){ return true; }

    public static enum EnumLogAxis implements IStringSerializable
    {
	    X("x"),
	    Y("y"),
	    Z("z"),
	    NONE("none");
	
	    private final String name;
	
	    private EnumLogAxis(String name)
	    {
	        this.name = name;
	    }
	
	    public String toString()
	    {
	        return this.name;
	    }
	
	    public static BlockModLog.EnumLogAxis fromFacingAxis(EnumFacing.Axis axis)
	    {
	        switch (axis)
	        {
	            case X:
	                return X;
	            case Y:
	                return Y;
	            case Z:
	                return Z;
	            default:
	                return NONE;
	        }
	    }
	
		@Override
		public String getName() {
			return this.name();
		}
    }
    */
}
