package com.raphydaphy.vitality.network;

import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.spell.SpellHelper;
import com.raphydaphy.vitality.item.ItemWand;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageChangeSpell implements IMessage {
	// A default constructor is always required
	public MessageChangeSpell() {
	}

	private int spell;

	public MessageChangeSpell(int newID) {
		this.spell = newID;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(spell);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple
		// values, you must read in the same order you wrote.
		spell = buf.readInt();

	}

	// The params of the IMessageHandler are <REQ, REPLY>
	// This means that the first param is the packet you are receiving, and the
	// second is the packet you are returning.
	// The returned packet can be used as a "response" from a sent packet.
	public static class ChangeSpellHandler implements IMessageHandler<MessageChangeSpell, IMessage> {

		@Override
		public IMessage onMessage(MessageChangeSpell message, MessageContext ctx) {
			// This is the player the packet was sent to the server from
			EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
			// The value that was sent
			int id = message.spell;
			if (serverPlayer.getHeldItemMainhand() != null)
			{
				if (serverPlayer.getHeldItemMainhand().getItem() instanceof ItemWand) {
					serverPlayer.getHeldItemMainhand().getTagCompound().setInteger(Spell.ACTIVE_KEY,
							id);
				} 
			}
			if (serverPlayer.getHeldItemOffhand() != null)
			{
				if (serverPlayer.getHeldItemOffhand().getItem() instanceof ItemWand) {
				serverPlayer.getHeldItemOffhand().getTagCompound().setInteger(Spell.ACTIVE_KEY,
						id);
				}
			}

			// No response packet
			return null;
		}
	}
}