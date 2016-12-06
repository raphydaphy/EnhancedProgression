package com.raphydaphy.vitality.nbt;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class AltarComponent
{
	private final BlockPos offset;

	public AltarComponent(BlockPos offset)
	{
		this.offset = offset;
	}

	public BlockPos getOffset()
	{
		return offset;
	}

	public int getX(EnumFacing direction)
	{
		switch (direction)
		{
		case EAST:
			return -this.getOffset().getZ();
		case SOUTH:
			return -this.getOffset().getX();
		case WEST:
			return this.getOffset().getZ();
		default:
			return this.getOffset().getX();
		}
	}

	public int getY()
	{
		return this.getOffset().getY();
	}

	public int getZ(EnumFacing direction)
	{
		switch (direction)
		{
		case EAST:
			return this.getOffset().getX();
		case SOUTH:
			return -this.getOffset().getZ();
		case WEST:
			return -this.getOffset().getX();
		default:
			return this.getOffset().getZ();
		}
	}

	public BlockPos getOffset(EnumFacing direction)
	{
		return new BlockPos(getX(direction), offset.getY(), getZ(direction));
	}
}