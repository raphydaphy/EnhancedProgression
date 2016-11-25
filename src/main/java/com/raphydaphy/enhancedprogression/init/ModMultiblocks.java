package com.raphydaphy.enhancedprogression.init;

import com.raphydaphy.enhancedprogression.block.multiblock.MultiblockSet;
import com.raphydaphy.enhancedprogression.block.tile.TileAltar;

public final class ModMultiblocks
{

	public static MultiblockSet altar;

	public static void init()
	{
		altar = TileAltar.makeMultiblockSet();
	}

}