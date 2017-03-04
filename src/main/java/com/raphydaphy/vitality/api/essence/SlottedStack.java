package com.raphydaphy.vitality.api.essence;

import net.minecraft.item.ItemStack;

public class SlottedStack {
	private final int slot;
	private final ItemStack stack;

	public SlottedStack(int slot, ItemStack stack) {
		this.slot = slot;
		this.stack = stack;
	}

	public ItemStack getStack() {
		return stack;
	}

	public int getSlotID() {
		return slot;
	}
}