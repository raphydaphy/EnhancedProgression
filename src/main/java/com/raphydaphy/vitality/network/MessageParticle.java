package com.raphydaphy.vitality.network;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageParticle implements IMessage {
	// A default constructor is always required
	public MessageParticle() {
	}

	int particle;
	long pos1;
	long pos2;

	public MessageParticle(EnumParticleTypes particle, BlockPos pos1, BlockPos pos2) {
		this.particle = particle.getParticleID();
		this.pos1 = pos1.toLong();
		this.pos2 = pos2.toLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(particle);
		buf.writeLong(pos1);
		buf.writeLong(pos2);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		particle = buf.readInt();
		pos1 = buf.readLong();
		pos2 = buf.readLong();
	}

	public static class MessageHandler implements IMessageHandler<MessageParticle, IMessage> {
		// Do note that the default constructor is required, but implicitly
		// defined in this case

		@Override
		public IMessage onMessage(MessageParticle message, MessageContext ctx) {
			BlockPos pos = BlockPos.fromLong(message.pos1);
			BlockPos dest = BlockPos.fromLong(message.pos2);
			WorldClient world = FMLClientHandler.instance().getWorldClient();
			EnumParticleTypes particles = EnumParticleTypes.getParticleFromId(message.particle);
			Random rand = world.rand;
			for (int jc = 0; jc < 30; jc++) {
				double rando = MathHelper.getRandomDoubleInRange(rand, -1.0D, 1.0D);
				double x = dest.getX() - pos.getX();
				double y = dest.getY() - (pos.getY() + rando);
				double z = dest.getZ() - pos.getZ();
				x /= 1.2D;
				y /= 1.4D;
				z /= 1.2D;
				world.spawnParticle(particles, pos.getX(), pos.getY() + rando, pos.getZ(), x, y, z);
			}
			return null;
		}
	}
}