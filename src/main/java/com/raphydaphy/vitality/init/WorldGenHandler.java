package com.raphydaphy.vitality.init;

import java.util.Random;

import com.raphydaphy.vitality.registry.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenHandler implements IWorldGenerator {

	// Which dimension to generate in by dimension ID (Nether -1, Overworld 0,
	// End 1, etc)
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case -1:
			break;
		case 0:
			if (ConfigHandler.world.enableCopperAndTin) {
				generateOre(world, random, chunkX * 16, chunkZ * 16, ModBlocks.ORE_COPPER.getDefaultState(), "ALL");
				generateOre(world, random, chunkX * 16, chunkZ * 16, ModBlocks.ORE_TIN.getDefaultState(), "ALL");

				generateOre(world, random, chunkX * 16, chunkZ * 16, ModBlocks.ORE_ANGELIC_CRYSTAL.getDefaultState(),
						"NOPLAIN");
				generateOre(world, random, chunkX * 16, chunkZ * 16, ModBlocks.ORE_EXOTIC_CRYSTAL.getDefaultState(),
						"EXOTIC");
			}
			break;
		case 1:
			break;
		}
	}

	/*
	 * This function will generate any ore given using an IBlockState
	 */
	private void generateOre(World world, Random rand, int chunkX, int chunkZ, IBlockState block, String genBiomes) {
		for (int k = 0; k < 16; k++) {
			int firstBlockXCoord = chunkX + rand.nextInt(16);
			int firstBlockZCoord = chunkZ + rand.nextInt(16);
			// Will be found between y = 0 and y = 60
			int quisqueY = rand.nextInt(60);
			BlockPos quisquePos = new BlockPos(firstBlockXCoord, quisqueY, firstBlockZCoord);
			Biome biome = world.getBiome(new BlockPos(chunkX * 16, 64, chunkZ * 16));
			switch (genBiomes) {
			case "EXOTIC":
				if (biome.getBiomeName() == "Jungle" || biome.getBiomeName() == "Mesa") {
					(new WorldGenMinable(block, 7)).generate(world, rand, quisquePos);
				}
				break;
			case "NOPLAIN":
				if (biome.getBiomeName() != "Plains" && biome.getBiomeName() != "Desert") {
					(new WorldGenMinable(block, 7)).generate(world, rand, quisquePos);
				}
				break;
			default:
				(new WorldGenMinable(block, 7)).generate(world, rand, quisquePos);
				break;
			}
		}
	}
}