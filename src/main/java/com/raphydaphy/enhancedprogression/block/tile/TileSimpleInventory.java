package com.raphydaphy.enhancedprogression.block.tile;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileSimpleInventory extends TileBase {
	
	protected static class SimpleItemStackHandler extends ItemStackHandler 
	{

		private final boolean allowWrite;
		private final TileSimpleInventory tile;

		public SimpleItemStackHandler(TileSimpleInventory inv, boolean allowWrite) 
		{
			super(inv.getSizeInventory());
			this.allowWrite = allowWrite;
			tile = inv;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) 
		{
			if(allowWrite) {
				return super.insertItem(slot, stack, simulate);
			} else return stack;
		}

		@Override
		public void onContentsChanged(int slot) 
		{
			tile.markDirty();
		}
	}
	
	protected SimpleItemStackHandler itemHandler = createItemHandler();
	
	public void readPacketNBT(NBTTagCompound par1NBTTagCompound) 
	{
		itemHandler = createItemHandler();
		itemHandler.deserializeNBT(par1NBTTagCompound);
	}

	@Override
	public void writePacketNBT(NBTTagCompound par1NBTTagCompound) 
	{
		par1NBTTagCompound.merge(itemHandler.serializeNBT());
	}

	public abstract int getSizeInventory();

	protected SimpleItemStackHandler createItemHandler() 
	{
		return new SimpleItemStackHandler(this, true);
	}

	public IItemHandlerModifiable getItemHandler() 
	{
		return itemHandler;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, @Nonnull EnumFacing side) 
	{
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Nonnull
	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, @Nonnull EnumFacing side) 
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
		return super.getCapability(cap, side);
	}

	
}