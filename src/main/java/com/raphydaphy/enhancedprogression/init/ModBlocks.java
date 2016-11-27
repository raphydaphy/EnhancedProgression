package com.raphydaphy.enhancedprogression.init;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.block.BlockAltar;
import com.raphydaphy.enhancedprogression.block.BlockBase;
import com.raphydaphy.enhancedprogression.block.BlockDeadLog;
import com.raphydaphy.enhancedprogression.block.BlockOre;
import com.raphydaphy.enhancedprogression.block.BlockTotem;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks
{

	public static BlockOre ore_copper;
	public static BlockOre ore_tin;

	public static BlockBase imbued_log;
	public static BlockBase imbued_plank;
	
	public static BlockBase fluxed_log;
	public static BlockBase fluxed_plank;

	public static BlockDeadLog dead_log;

	public static BlockTotem totem_top;
	public static BlockTotem totem_middle;
	public static BlockTotem totem_bottom;

	public static Block altar;

	public static void init()
	{
		ore_copper = register(new BlockOre("ore_copper").setCreativeTab(EnhancedProgression.creativeTab));
		ore_tin = register(new BlockOre("ore_tin").setCreativeTab(EnhancedProgression.creativeTab));

		imbued_log = register(
				new BlockBase(Material.WOOD, "imbued_log").setCreativeTab(EnhancedProgression.creativeTab));
		imbued_plank = register(
				new BlockBase(Material.WOOD, "imbued_plank").setCreativeTab(EnhancedProgression.creativeTab));
		
		fluxed_log = register(
				new BlockBase(Material.WOOD, "fluxed_log").setCreativeTab(EnhancedProgression.creativeTab));
		fluxed_plank = register(
				new BlockBase(Material.WOOD, "fluxed_plank").setCreativeTab(EnhancedProgression.creativeTab));

		dead_log = register(new BlockDeadLog("dead_log"));

		totem_top = register(new BlockTotem("totem_top").setCreativeTab(EnhancedProgression.creativeTab));
		totem_middle = register(new BlockTotem("totem_middle").setCreativeTab(EnhancedProgression.creativeTab));
		totem_bottom = register(new BlockTotem("totem_bottom").setCreativeTab(EnhancedProgression.creativeTab));

		altar = register(new BlockAltar(false, "altar").setCreativeTab(EnhancedProgression.creativeTab));
	}

	private static <T extends Block> T register(T block, ItemBlock itemBlock)
	{
		GameRegistry.register(block);
		GameRegistry.register(itemBlock);

		if (block instanceof BlockBase)
		{
			((BlockBase) block).registerItemModel(itemBlock);
		}

		else if (block instanceof BlockAltar)
		{
			((BlockAltar) block).registerItemModel(itemBlock);
		}

		else if (block instanceof BlockTotem)
		{
			((BlockTotem) block).registerItemModel(itemBlock);
		}

		else if (block instanceof BlockDeadLog)
		{
			((BlockDeadLog) block).registerItemModel(itemBlock);
		}

		return block;
	}

	private static <T extends Block> T register(T block)
	{
		ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(block.getRegistryName());
		return register(block, itemBlock);
	}

}