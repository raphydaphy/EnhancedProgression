package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.registry.RegistryHelper;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ItemWandPiece extends ItemBase {

	public ItemWandPiece(String name, int maxStack) {
		super(name, maxStack);
	}

	@Override
	public void registerModels() {
		RegistryHelper.setModelLoc(this);
	}

	@Override
	public ModelResourceLocation getModelLocation() {
		return new ModelResourceLocation(getRegistryName(), "inventory");
	}
}
