package com.raphydaphy.vitality.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class PacketManager
{
	private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public PacketManager() 
    {
    	
    }

    public static int nextID() 
    {
        return packetId++;
    }

    public static void registerMessages(String channelName) 
    {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages() 
    {
        // Register messages which are sent from the client to the server here:
        INSTANCE.registerMessage(PacketSendKey.Handler.class, PacketSendKey.class, nextID(), Side.SERVER);
    }
    
	public static float pointDistancePlane(double x1, double y1, double x2, double y2)
	{
		return (float) Math.hypot(x1 - x2, y1 - y2);
	}

	public static void dispatchTE(World world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null)
		{
			IBlockState state = tile.getWorld().getBlockState(tile.getPos());
			tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 8);
		}
	}

}