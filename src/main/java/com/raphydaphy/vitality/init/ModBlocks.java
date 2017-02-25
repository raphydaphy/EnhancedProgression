package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.block.BlockBase;
import com.raphydaphy.vitality.block.BlockEssenceJar;
import com.raphydaphy.vitality.block.BlockExtractionCrucible;
import com.raphydaphy.vitality.block.BlockModOre;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks 
{
	public static BlockModOre ore_copper;
	public static BlockModOre ore_tin;
	
	public static BlockModOre angelic_crystal_ore;
	public static BlockModOre exotic_crystal_ore;
	
	public static BlockExtractionCrucible life_extraction_crucible;

	public static BlockEssenceJar essence_jar;

	
	public static void init()
	{
		if (ConfigHandler.world.enableCopperAndTin)
		{
			ore_copper = register(new BlockModOre("ore_copper"));
			ore_tin = register(new BlockModOre("ore_tin"));
		}
		
		angelic_crystal_ore = register(new BlockModOre("angelic_crystal_ore"));
		exotic_crystal_ore = register(new BlockModOre("exotic_crystal_ore"));
		
		life_extraction_crucible = register(new BlockExtractionCrucible());
		
		essence_jar = register(new BlockEssenceJar());
	}
	
	private static <T extends Block> T register(T block, ItemBlock itemBlock)
	{
		GameRegistry.register(block);
		GameRegistry.register(itemBlock);
		
		((BlockBase) block).registerItemModel(itemBlock);
		return block;
	}
	
	private static <T extends Block> T register(T block)
	{
		ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(block.getRegistryName());
		return register(block, itemBlock);
	}
}
