package com.raphydaphy.vitality.block;

import java.util.List;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.block.tile.TileEssenceJar;
import com.raphydaphy.vitality.render.EssenceJarTESR;
import com.raphydaphy.vitality.util.EssenceHelper;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// The author of Chisel may have told me that my jar model is shit
// so ill fix that one day .. plz i hope he forgets it was scary

public class BlockEssenceJar extends BlockBase implements ITileEntityProvider {
	protected static final AxisAlignedBB AABB_MAIN = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.875D,
			0.8125D);
	protected static final AxisAlignedBB AABB_KNOB = new AxisAlignedBB(0.3125D, 0.875D, 0.3125D, 0.5625D, 1.0D,
			0.5625D);

	public BlockEssenceJar() {
		super(Material.GLASS, "essence_jar");
		this.setHardness(1F);
		this.setResistance(2F);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_MAIN);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_KNOB);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB_MAIN;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEssenceJar();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		super.registerModels();
		GameRegistry.registerTileEntity(TileEssenceJar.class, getRegistryName().getResourcePath());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEssenceJar.class, new EssenceJarTESR());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState blockState) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	private TileEssenceJar getTE(World world, BlockPos pos) {
		return (TileEssenceJar) world.getTileEntity(pos);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (heldItem == null) {
			return true;
		}

		else if (!world.isRemote) {
			TileEssenceJar te = getTE(world, pos);
			String essenceTypeVial = EssenceHelper.vialItemToString(heldItem.getItem());
			String essenceTypeJar = te.getEssenceType();
			int essenceStoredJar = te.getEssenceStored();
			if (!player.isSneaking()) {
				if (essenceTypeJar == essenceTypeVial || essenceTypeJar == "Unknown") {
					if (essenceStoredJar <= 1000) {
						if (EssenceHelper.useEssence(heldItem, 10)) {
							if (essenceTypeJar != essenceTypeVial) {
								te.setEssenceType(essenceTypeVial);
							}
							te.setEssenceStored(essenceStoredJar + 10);
							world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);
							player.swingArm(hand);
						}

					}
				}
			} else {

				if (essenceTypeJar == essenceTypeVial || essenceTypeVial == "Unknown") {
					if (essenceStoredJar >= 10) {
						if (essenceTypeJar == essenceTypeVial) {
							EssenceHelper.addEssenceFree(heldItem, 10, 1000);
						} else if (essenceTypeVial == "Unknown") {
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
