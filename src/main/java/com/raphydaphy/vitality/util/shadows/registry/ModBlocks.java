package com.raphydaphy.vitality.util.shadows.registry;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.vitality.block.BlockBase;
import com.raphydaphy.vitality.block.BlockEssence;
import com.raphydaphy.vitality.block.BlockEssenceJar;
import com.raphydaphy.vitality.block.BlockExtractionCrucible;
import com.raphydaphy.vitality.block.BlockModOre;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {

	public static final Block ORE_COPPER = new BlockModOre("ore_copper");
	public static final Block ORE_TIN = new BlockModOre("ore_tin");

	public static final Block ORE_ANGELIC_CRYSTAL = new BlockModOre("angelic_crystal_ore");
	public static final Block ORE_EXOTIC_CRYSTAL = new BlockModOre("exotic_crystal_ore");

	public static final Block EXTRACTION_CRUCIBLE = new BlockExtractionCrucible();

	public static final Block JAR = new BlockEssenceJar();

	public static final Block ESSENCE_ANGELIC = new BlockEssence("essence_angelic");
	public static final Block ESSENCE_ATMOSPHERIC = new BlockEssence("essence_atmospheric");
	public static final Block ESSENCE_DEMONIC = new BlockEssence("essence_demonic");
	public static final Block ESSENCE_ENERGETIC = new BlockEssence("essence_energetic");
	public static final Block ESSENCE_EXOTIC = new BlockEssence("essence_exotic");

	public static final List<Block> BLOCK_LIST = getList();

	private static final List<Block> getList() {
		List<Block> list = new ArrayList<Block>();
		list.add(ORE_COPPER);
		list.add(ORE_TIN);
		list.add(ORE_ANGELIC_CRYSTAL);
		list.add(ORE_EXOTIC_CRYSTAL);
		list.add(EXTRACTION_CRUCIBLE);
		list.add(JAR);
		list.add(ESSENCE_ANGELIC);
		list.add(ESSENCE_ATMOSPHERIC);
		list.add(ESSENCE_DEMONIC);
		list.add(ESSENCE_ENERGETIC);
		list.add(ESSENCE_EXOTIC);
		return list;
	}

}
