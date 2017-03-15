package com.raphydaphy.vitality.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.essence.MiscEssence;
import com.raphydaphy.vitality.item.ItemVial;
import com.raphydaphy.vitality.util.ParticleHelper;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockExtractionCrucible extends BlockBase {
	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
	protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
	protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

	public BlockExtractionCrucible() {
		super(Material.IRON, "life_extraction_crucible");
		this.setHardness(3F);
		this.setResistance(8F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
	}

	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LEGS);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks
	 * for render
	 */
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level) {
		worldIn.setBlockState(pos, state.withProperty(LEVEL, MathHelper.clamp_int(level, 0, 3)), 2);
		worldIn.updateComparatorOutputLevel(pos, this);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			@Nullable ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (stack == null)
			return false;
		else if (stack.getItem() instanceof ItemVial) {
			int level = state.getValue(LEVEL);
			ItemVial vial = (ItemVial) stack.getItem();
			if (vial.getVialType() == null
					|| vial.getVialType() == Essence.ATMOSPHERIC && level > 0 && MiscEssence.canBeAdded(stack, 25)) {
				MiscEssence.addEssence(stack, 25, false, player, Essence.ATMOSPHERIC, player.inventory.currentItem);
				this.setWaterLevel(world, pos, state, level - 1);
				ParticleHelper.spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true, pos, 5, 1);
				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1, 1);
				return true;
			}
		}
		return false;
	}

	public void fillWithRain(World worldIn, BlockPos pos) {
		if (worldIn.rand.nextInt(10) == 1) {
			float f = worldIn.getBiome(pos).getFloatTemperature(pos);

			if (worldIn.getBiomeProvider().getTemperatureAtHeight(f, pos.getY()) >= 0.15F) {
				IBlockState iblockstate = worldIn.getBlockState(pos);

				if (((Integer) iblockstate.getValue(LEVEL)).intValue() < 3) {
					worldIn.setBlockState(pos, iblockstate.cycleProperty(LEVEL), 2);
				}
			}
		}
	}

	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this);
	}

	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return ((Integer) blockState.getValue(LEVEL)).intValue();
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		return ((Integer) state.getValue(LEVEL)).intValue();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { LEVEL });
	}

	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}
}
