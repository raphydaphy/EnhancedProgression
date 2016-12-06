package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.block.tile.TileAltar;
import com.raphydaphy.vitality.network.PacketManager;
import com.raphydaphy.vitality.network.PacketSendKey;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class Events
{
	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (event.getType() == ElementType.ALL)
		{
			RayTraceResult pos = mc.objectMouseOver;

			if (pos != null)
			{
				TileEntity tile = pos.typeOfHit == RayTraceResult.Type.BLOCK
						? mc.theWorld.getTileEntity(pos.getBlockPos()) : null;

				if (tile != null && tile instanceof TileAltar)
				{
					((TileAltar) tile).renderHUD(mc, event.getResolution());
				}

			}
		}
	}
	
	@SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) 
	{
        if (KeyBindings.pickSpell.isPressed()) 
        {
            // Someone pressed the key. We send a message
        	System.out.println("it was pressed");
            PacketManager.INSTANCE.sendToServer(new PacketSendKey());
        }
    }
}
