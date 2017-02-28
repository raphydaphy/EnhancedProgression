package com.raphydaphy.vitality.util;

import com.raphydaphy.vitality.item.ItemBase;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
		if (stack.getItem() instanceof ItemBase) {
			return ((ItemBase) stack.getItem()).getModelLocation(stack);
		}

		return null;
	}
}