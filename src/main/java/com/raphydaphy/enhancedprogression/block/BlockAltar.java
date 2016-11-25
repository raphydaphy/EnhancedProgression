package com.raphydaphy.enhancedprogression.block;

import javax.annotation.Nonnull;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.block.tile.TileAltar;
import com.raphydaphy.enhancedprogression.block.tile.TileSimpleInventory;
import com.raphydaphy.enhancedprogression.nbt.InventoryHelper;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAltar extends BlockBase
{
	protected String name;

	public BlockAltar(boolean isLit, String name)
	{
		super(Material.ROCK, name);
		setUnlocalizedName(name);
		this.setHardness(3.0F);
		this.name = name;
	}

	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side)
	{
		((TileAltar) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public void registerItemModel(ItemBlock itemBlock)
	{
		EnhancedProgression.proxy.registerItemRenderer(itemBlock, 0, name);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return new AxisAlignedBB(0.0, 0, 0.0, 1.0, 0.65, 1.0);
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean canRenderInLayer(BlockRenderLayer layer)
	{
		if (layer == BlockRenderLayer.CUTOUT)
		{
			return true;
		}
		return false;
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state)
	{
		TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);

		InventoryHelper.dropInventory(inv, world, state, pos);

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileAltar();
	}

	@Override
	public BlockAltar setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
}
