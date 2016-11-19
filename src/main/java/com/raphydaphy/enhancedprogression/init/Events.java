package com.raphydaphy.enhancedprogression.init;

import com.raphydaphy.enhancedprogression.block.tile.TileAltar;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events {

	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) 
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(event.getType() == ElementType.ALL) 
		{
			RayTraceResult pos = mc.objectMouseOver;

			if(pos != null) 
			{
				TileEntity tile = pos.typeOfHit == RayTraceResult.Type.BLOCK ? mc.theWorld.getTileEntity(pos.getBlockPos()) : null;

				
				if(tile != null && tile instanceof TileAltar)
				{
					((TileAltar) tile).renderHUD(mc, event.getResolution());
				}

			}
		}
	}
}
