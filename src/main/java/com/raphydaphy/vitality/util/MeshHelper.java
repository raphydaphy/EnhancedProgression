package com.raphydaphy.vitality.util;

import com.raphydaphy.vitality.item.ItemBase;
import com.raphydaphy.vitality.registry.IMeta;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MeshHelper implements ItemMeshDefinition {
	private static MeshHelper instance;

	public MeshHelper() {
	}

	public static MeshHelper instance() {
		if (instance == null) {
			instance = new MeshHelper();
		}

		return instance;
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof IMeta)
			return ((IMeta) item).getMetaModelLocations().get(stack.getMetadata());
		else if (item instanceof ItemBase)
			return ((ItemBase) item).getModelLocation();

		return null;
	}
}