package com.raphydaphy.vitality.block.tile;

import com.raphydaphy.vitality.essence.IEssenceContainer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEssenceJar extends TileEntity implements IEssenceContainer{
	private int essenceStored = 0;
	private String essenceType = "Unknown";

	@Override
	public int getEssenceStored() {
		return essenceStored;
	}
	
	@Override
	public String getEssenceType() {
		return essenceType;
	}

	public int getEssenceHeight() {
		int height = (int) Math.floor(essenceStored / 90.909);
		if (essenceStored > 0 && height == 0) {
			height = 1;
		} else if (essenceStored < 1000 && height == 11) {
			height = 10;
		}
		return height;
	}

	@Override
	public void setEssenceStored(int newAmount) {
		this.essenceStored = newAmount;
		updateTE();
	}
	
	@Override
	public void setEssenceType(String newType) {
		this.essenceType = newType;
		updateTE();
	}

	private void updateTE() {
		markDirty();
		if (worldObj != null) {
			IBlockState state = worldObj.getBlockState(getPos());
			worldObj.notifyBlockUpdate(getPos(), state, state, 3);
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		// getUpdateTag() is called whenever the chunkdata is sent to the
		// client. In contrast getUpdatePacket() is called when the tile entity
		// itself wants to sync to the client. In many cases you want to send
		// over the same information in getUpdateTag() as in getUpdatePacket().
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		// Prepare a packet for syncing our TE to the client. Since we only have
		// to sync the stack
		// and that's all we have we just write our entire NBT here. If you have
		// a complex
		// tile entity that doesn't need to have all information on the client
		// you can write
		// a more optimal NBT here.
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		// Here we get the packet from the server and read it into our client
		// side tile entity
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("essenceStored")) {
			essenceStored = compound.getInteger("essenceStored");
		} else {
			essenceStored = 0;
		}

		if (compound.hasKey("essenceType")) {
			essenceType = compound.getString("essenceType");
		} else {
			essenceType = "Unknown";
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("essenceStored", essenceStored);
		compound.setString("essenceType", essenceType);
		return compound;
	}
}
