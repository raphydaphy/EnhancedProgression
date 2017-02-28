package com.raphydaphy.vitality.init;

/*
 * this code is by TehNut
 * thx :)
 * Hopefully i can put it here
 * its under WTFPL anyway
 */

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenHandler implements IWorldGenerator {

    // Which dimension to generate in by dimension ID (Nether -1, Overworld 0, End 1, etc)
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
        switch (world.provider.getDimension()) {
            case -1:
                break;
            case 0:
                generateSurfaceTin(world, random, chunkX * 16, chunkZ * 16);
                generateSurfaceCopper(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 1:
                break;
        }
    }

    //The actual generation method.
    private void generateSurfaceCopper(World world, Random rand, int chunkX, int chunkZ) {
        for (int k = 0; k < 16; k++)
        {
            int firstBlockXCoord = chunkX + rand.nextInt(16);
            int firstBlockZCoord = chunkZ + rand.nextInt(16);
            //Will be found between y = 0 and y = 60
            int quisqueY = rand.nextInt(60);
            BlockPos quisquePos = new BlockPos(firstBlockXCoord, quisqueY, firstBlockZCoord);
            //The 8 as the second parameter sets the maximum vein size
            if (ConfigHandler.world.enableCopperGen)
            {
            	Biome biome = world.getBiomeGenForCoords(new BlockPos(chunkX *16, 64, chunkZ*16));
            	(new WorldGenMinable(ModBlocks.ore_copper.getDefaultState(), 7)).generate(world, rand, quisquePos);
            }
        }
    }
  //The actual generation method.
    private void generateSurfaceTin(World world, Random rand, int chunkX, int chunkZ) {
        for (int k = 0; k < 16; k++)
        {
            int firstBlockXCoord = chunkX + rand.nextInt(16);
            int firstBlockZCoord = chunkZ + rand.nextInt(16);
            //Will be found between y = 0 and y = 50
            int quisqueY = rand.nextInt(50);
            BlockPos quisquePos = new BlockPos(firstBlockXCoord, quisqueY, firstBlockZCoord);
            //The 5 as the second parameter sets the maximum vein size
            if (ConfigHandler.world.enableTinGen)
            {
            	(new WorldGenMinable(ModBlocks.ore_tin.getDefaultState(), 5)).generate(world, rand, quisquePos);
            }
        }
    }
}