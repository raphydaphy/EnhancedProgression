package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.init.Reference;
import com.raphydaphy.vitality.util.registry.IRegisterable;
import com.raphydaphy.vitality.util.registry.RegistryHelper;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBase extends Item implements IRegisterable {

	public ItemBase(String name, int maxStack) {
		this(name, maxStack, true);
	}

	public ItemBase(String name, int maxStack, boolean registerTab) {
		setUnlocalizedName(Reference.MOD_ID + "." + name);
		setRegistryName(name);
		setMaxStackSize(maxStack);
		if (registerTab)
			setCreativeTab(Reference.creativeTab);
		GameRegistry.register(this);
	}

	public ItemBase(String name) {
		this(name, 64);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || ItemStack.areItemStacksEqual(oldStack, newStack) == false;
	}

	@Override
	public void registerModels() {
		RegistryHelper.setModelLoc(this);
	}

	public ModelResourceLocation getModelLocation() {
		return new ModelResourceLocation(getRegistryName(), "inventory");
	}

}