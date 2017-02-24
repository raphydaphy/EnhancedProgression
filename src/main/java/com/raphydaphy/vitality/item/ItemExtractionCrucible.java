package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.init.ModBlocks;

import net.minecraft.item.ItemBlock;

public class ItemExtractionCrucible extends ItemBlock
{
	public ItemExtractionCrucible() {
		super(ModBlocks.life_extraction_crucible);
		this.maxStackSize = 1;
		this.setUnlocalizedName("life_extraction_crucible_item");
		this.setRegistryName("life_extraction_crucible_item");
	}

}
