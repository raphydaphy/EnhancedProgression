package com.raphydaphy.vitality.network;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public final class PacketManager {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("vitality");

	public static float pointDistancePlane(double x1, double y1, double x2, double y2) {
		return (float) Math.hypot(x1 - x2, y1 - y2);
	}

	public static void dispatchTE(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			IBlockState state = tile.getWorld().getBlockState(tile.getPos());
			tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 8);
		}
	}

}