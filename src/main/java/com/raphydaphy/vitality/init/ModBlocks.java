package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.block.BlockAltar;
import com.raphydaphy.vitality.block.BlockBase;
import com.raphydaphy.vitality.block.BlockDeadLog;
import com.raphydaphy.vitality.block.BlockModLog;
import com.raphydaphy.vitality.block.BlockOre;
import com.raphydaphy.vitality.block.BlockTotem;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks
{

	public static BlockOre ore_copper;
	public static BlockOre ore_tin;

	public static BlockBase imbued_plank;
	public static BlockBase fluxed_plank;
	
	public static BlockModLog fluxed_log;
	public static BlockModLog imbued_log;

	public static BlockDeadLog dead_log;

	public static BlockTotem totem_top;
	public static BlockTotem totem_middle;
	public static BlockTotem totem_bottom;

	public static Block altar;

	public static void init()
	{
		ore_copper = register(new BlockOre("ore_copper").setCreativeTab(Vitality.creativeTab));
		ore_tin = register(new BlockOre("ore_tin").setCreativeTab(Vitality.creativeTab));

		fluxed_plank = register(
				new BlockBase(Material.WOOD, "fluxed_plank").setCreativeTab(Vitality.creativeTab));
		imbued_plank = register(
				new BlockBase(Material.WOOD, "imbued_plank").setCreativeTab(Vitality.creativeTab));
		
		fluxed_log = register(
				new BlockModLog("fluxed_log").setCreativeTab(Vitality.creativeTab));
		imbued_log = register(
				new BlockModLog("imbued_log").setCreativeTab(Vitality.creativeTab));

		dead_log = register(new BlockDeadLog("dead_log"));

		totem_top = register(new BlockTotem("totem_top").setCreativeTab(Vitality.creativeTab));
		totem_middle = register(new BlockTotem("totem_middle").setCreativeTab(Vitality.creativeTab));
		totem_bottom = register(new BlockTotem("totem_bottom").setCreativeTab(Vitality.creativeTab));

		altar = register(new BlockAltar(false, "altar").setCreativeTab(Vitality.creativeTab));
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
		else if (block instanceof BlockModLog)
		{
			((BlockModLog) block).registerItemModel(itemBlock);
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