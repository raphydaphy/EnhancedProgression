package com.raphydaphy.vitality.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLightOrb extends BlockBase {

	public BlockLightOrb() {
		super(Material.GLASS, "light_orb");
		setLightOpacity(0);
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 15;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		float f = 0.25F;

		double minX = f;
		double minY = f;
		double minZ = f;
		double maxX = 1F - f;
		double maxY = 1F - f;
		double maxZ = 1F - f;

		return new AxisAlignedBB(pos.getX() + minX, pos.getY() + minY, pos.getZ() + minZ, pos.getX() + maxX,
				pos.getY() + maxY, pos.getZ() + maxZ);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		Random rand = worldIn.rand;
		for (int i = 0; i < 1000; i++) {
			double x = (double) (pos.getX()) + 0.5 + ((rand.nextDouble()) - 0.5);
			double y = (double) pos.getY() + (rand.nextDouble()) + 0.5;
			double z = (double) pos.getZ() + 0.5 + ((rand.nextDouble()) - 0.5);
			int[] pars = new int[1];
			if (rand.nextInt(2) == 1) {
				pars[0] = 165;
			} else {
				pars[0] = 133;
			}
			worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0.0D, 0.0D, 0.0D, pars);
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		Random rand = worldIn.rand;
		for (int i = 0; i < 1000; i++) {
			double x = (double) (pos.getX()) + 0.5 + ((rand.nextDouble() / 2) - 0.25);
			double y = (double) pos.getY() + 0.5 + (rand.nextDouble() / 2);
			double z = (double) pos.getZ() + 0.5 + ((rand.nextDouble() / 2) - 0.25);
			int[] pars = new int[1];
			if (rand.nextInt(2) == 1) {
				pars[0] = 152;
			} else {
				pars[0] = 179;
			}
			worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0.0D, 0.0D, 0.0D, pars);
		}
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		double x = (double) (pos.getX()) + 0.5 + ((rand.nextDouble() / 2) - 0.25);
		double y = (double) pos.getY() + 0.5 + (rand.nextDouble() / 2);
		double z = (double) pos.getZ() + 0.5 + ((rand.nextDouble() / 2) - 0.25);
		int[] pars = new int[1];
		if (rand.nextInt(2) == 1) {
			pars[0] = 168;
		} else {
			pars[0] = 57;
		}
		worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0.0D, 0.0D, 0.0D, pars);
	}
}
