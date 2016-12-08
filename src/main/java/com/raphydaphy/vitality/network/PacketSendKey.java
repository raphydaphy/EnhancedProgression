package com.raphydaphy.vitality.network;

import com.raphydaphy.vitality.item.ItemSpellBag;
import com.raphydaphy.vitality.nbt.NBTLib;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendKey implements IMessage 
{
    private int spellIDPacket;

    @Override
    public void fromBytes(ByteBuf buf) 
    {
        spellIDPacket = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) 
    {
        buf.writeInt(spellIDPacket);
    }

    public PacketSendKey(int spellID) 
    {
    	spellIDPacket = spellID;
    }
    
    public void init () { }

    public static class Handler implements IMessageHandler<PacketSendKey, IMessage> 
    {
        @Override
        public IMessage onMessage(PacketSendKey message, MessageContext ctx) 
        {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSendKey message, MessageContext ctx) 
        {
            EntityPlayerMP playerEntity = ctx.getServerHandler().playerEntity;
            if (playerEntity.getHeldItemOffhand().getItem() instanceof ItemSpellBag)
            {
            	System.out.println(playerEntity.worldObj.isRemote + " <-- remote : spellid --> " + message.spellIDPacket);
            	NBTLib.setInt(playerEntity.getHeldItemOffhand(), "selectedSpell", message.spellIDPacket);
            }
        }
    }
}