package com.raphydaphy.vitality.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageEssenceUpdate implements IMessage {
	// A default constructor is always required
	public MessageEssenceUpdate() {
	}

	private int id;
	private int cost;

	public MessageEssenceUpdate(int id, int cost) {
		this.id = id;
		this.cost = cost;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
		buf.writeInt(cost);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
		cost = buf.readInt();
	}

	// The params of the IMessageHandler are <REQ, REPLY>
	// This means that the first param is the packet you are receiving, and the
	// second is the packet you are returning.
	// The returned packet can be used as a "response" from a sent packet.
	public static class Handler implements IMessageHandler<MessageEssenceUpdate, IMessage> {
		@Override
		public IMessage onMessage(MessageEssenceUpdate message, MessageContext ctx) {
			// This is the player the packet was sent to the server from
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			if(message.id == player.getEntityId()) player.getEntityData().setInteger("wandCurEssenceStored", message.cost);

			// No response packet
			return null;
		}
	}
}