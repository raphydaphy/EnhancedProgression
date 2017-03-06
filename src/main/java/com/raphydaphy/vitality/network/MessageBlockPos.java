package com.raphydaphy.vitality.network;

import com.raphydaphy.vitality.util.VitalData;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageBlockPos implements IMessage {
	// A default constructor is always required
	public MessageBlockPos() {
	}

	private int x;
	private int y;
	private int z;

	public MessageBlockPos(int x,int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		System.out.println("In da buff!");
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		System.out.println("Truly in da buf!");
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		System.out.println("outa the buff!");
		// Reads the int back from the buf. Note that if you have multiple
		// values, you must read in the same order you wrote.
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		System.out.println("out of buff completely!");
	}

	// The params of the IMessageHandler are <REQ, REPLY>
	// This means that the first param is the packet you are receiving, and the
	// second is the packet you are returning.
	// The returned packet can be used as a "response" from a sent packet.
	public static class BlockPosHandler implements IMessageHandler<MessageBlockPos, IMessage> {
		@Override
		public IMessage onMessage(MessageBlockPos message, MessageContext ctx) {
			System.out.println("into the playea");
			// This is the player the packet was sent to the server from
			EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
			// The values that were sent
			
			serverPlayer.getEntityData().setInteger(VitalData.POS_X, message.x);
			serverPlayer.getEntityData().setInteger(VitalData.POS_Y, message.y);
			serverPlayer.getEntityData().setInteger(VitalData.POS_Z, message.z);

			// No response packet
			System.out.println("Truly in the playa");
			return null;
		}
	}
}