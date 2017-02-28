package com.raphydaphy.vitality.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.init.ModItems;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockModOre extends BlockBase {
	/*
	 * Basic default constructor for any modded ores Sets the hardness to
	 * slightly higher than iron Resistance is the strength against TNT name is
	 * the unlocalized name of the ore block
	 */
	public BlockModOre(String name) {
		super(Material.ROCK, name);

		setHardness(3f);
		setResistance(5f);
	}

	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this == ModBlocks.angelic_crystal_ore ? ModItems.angelic_crystal_shard
				: (this == ModBlocks.exotic_crystal_ore ? ModItems.exotic_crystal_shard : Item.getItemFromBlock(this));
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random random) {
		if (this == ModBlocks.angelic_crystal_ore || this == ModBlocks.exotic_crystal_ore) {
			return 2 + random.nextInt(5);
		}
		return 1;
	}

	/**
	 * Get the quantity dropped based on the given fortune level
	 */
	public int quantityDroppedWithBonus(int fortune, Random random) {
		if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(
				(IBlockState) this.getBlockState().getValidStates().iterator().next(), random, fortune)) {
			int i = random.nextInt(fortune + 2) - 1;

			if (i < 0) {
				i = 0;
			}

			return this.quantityDropped(random) * (i + 1);
		} else {
			return this.quantityDropped(random);
		}
	}

	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		Random rand = world instanceof World ? ((World) world).rand : new Random();
		if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this)) {
			int i = 0;

			if (this == ModBlocks.exotic_crystal_ore) {
				i = MathHelper.getRandomIntegerInRange(rand, 3, 6);
			} else if (this == ModBlocks.angelic_crystal_ore) {
				i = MathHelper.getRandomIntegerInRange(rand, 1, 4);
			}

			return i;
		}
		return 0;
	}
}