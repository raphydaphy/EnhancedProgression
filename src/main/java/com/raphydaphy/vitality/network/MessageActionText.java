package com.raphydaphy.vitality.network;

import java.nio.charset.Charset;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.proxy.ClientProxy;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageActionText implements IMessage {
	// A default constructor is always required
	public MessageActionText() {
	}

	String toSend;
	String essence;

	public MessageActionText(String toSend, Essence color) {
		this.toSend = toSend;
		essence = color.getName();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		byte[] msg = toSend.getBytes();
		buf.writeInt(msg.length);
		buf.writeBytes(msg);
		byte[] ess = essence.getBytes();
		buf.writeInt(ess.length);
		buf.writeBytes(ess);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int k = buf.readInt();
		toSend = buf.readBytes(k).toString(Charset.defaultCharset());
		int j = buf.readInt();
		essence = buf.readBytes(j).toString(Charset.defaultCharset());
	}

	public static class MessageHandler implements IMessageHandler<MessageActionText, IMessage> {
		// Do note that the default constructor is required, but implicitly
		// defined in this case

		@Override
		public IMessage onMessage(MessageActionText message, MessageContext ctx) {
			ClientProxy.setActionText(message.toSend, Essence.getByName(message.essence).getColor());
			return null;
		}
	}
}