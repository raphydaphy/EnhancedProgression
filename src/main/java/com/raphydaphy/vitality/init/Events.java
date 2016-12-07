package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.block.tile.TileAltar;
import com.raphydaphy.vitality.gui.GuiSpellSelect;
import com.raphydaphy.vitality.item.ItemSpellBag;
import com.raphydaphy.vitality.item.ItemWand;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) 
	{
        if (KeyBindings.pickSpell.isPressed()) 
        {
        	Minecraft mc = Minecraft.getMinecraft();
        	if (mc.thePlayer.getHeldItemMainhand() != null &&
        		mc.thePlayer.getHeldItemOffhand() != null)
        	{
	        	if (mc.thePlayer.getHeldItemMainhand().getItem() instanceof ItemWand && 
	        		mc.thePlayer.getHeldItemOffhand().getItem() instanceof ItemSpellBag)
	        	{
	        		//mc.displayGuiScreen(new GuiSpellSelect());
	        	}
        	}
        }
    }
}
