package com.raphydaphy.vitality.util.shadows.registry;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class RegistryHelper {

	public static void setModelLoc(Block block) {
		if (block instanceof IMeta) {
			IMeta thing = ((IMeta) block);
			List<ModelResourceLocation> list = thing.getMetaModelLocations();
			for (int i = 0; i <= thing.getMaxMeta(); i++) {
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, list.get(i));
			}
		} else
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
					new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}

	public static void setModelLoc(Item item) {

		if (item instanceof IMeta) {
			IMeta thing = ((IMeta) item);
			List<ModelResourceLocation> list = thing.getMetaModelLocations();
			for (int i = 0; i <= thing.getMaxMeta(); i++) {
				ModelLoader.setCustomModelResourceLocation(item, i, list.get(i));
			}
		} else
			ModelLoader.setCustomModelResourceLocation(item, 0,
					new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

}
