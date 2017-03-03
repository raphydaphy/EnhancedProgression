package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.item.ItemWand;
import com.raphydaphy.vitality.registry.KeyBindings;
import com.raphydaphy.vitality.render.GUISpellSelect;
import com.raphydaphy.vitality.util.BoundHelper;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber()
public class Events {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) 
	{
		Minecraft mc = Minecraft.getMinecraft();
        if (KeyBindings.pickSpell.isPressed() && mc.inGameHasFocus) 
        {	
        	
        	boolean did = false;
        	if (mc.thePlayer.getHeldItemMainhand() != null)
        	{
	        	if (mc.thePlayer.getHeldItemMainhand().getItem() instanceof ItemWand)
	        	{
	        		did = true;
	        		mc.displayGuiScreen(new GUISpellSelect());
	        	}
        	}
        	if (!did && mc.thePlayer.getHeldItemOffhand() != null)
        	{
        		if (mc.thePlayer.getHeldItemOffhand().getItem() instanceof ItemWand)
	        	{
	        		did = true;
	        		mc.displayGuiScreen(new GUISpellSelect());
	        	}
        	}
        }
    }
	
	@SubscribeEvent
	public static void onDeath(PlayerEvent.Clone event) {
		int storedAngelic = event.getOriginal().getEntityData().getInteger(Essence.ANGELIC.getMultiKey());
		int storedAtmospheric = event.getOriginal().getEntityData().getInteger(Essence.ATMOSPHERIC.getMultiKey());
		int storedDemonic = event.getOriginal().getEntityData().getInteger(Essence.DEMONIC.getMultiKey());
		int storedEnergetic = event.getOriginal().getEntityData().getInteger(Essence.ENERGETIC.getMultiKey());
		int storedExotic = event.getOriginal().getEntityData().getInteger(Essence.EXOTIC.getMultiKey());

		event.getEntityPlayer().getEntityData().setInteger(Essence.ANGELIC.getMultiKey(), storedAngelic);
		event.getEntityPlayer().getEntityData().setInteger(Essence.ATMOSPHERIC.getMultiKey(), storedAtmospheric);
		event.getEntityPlayer().getEntityData().setInteger(Essence.DEMONIC.getMultiKey(), storedDemonic);
		event.getEntityPlayer().getEntityData().setInteger(Essence.ENERGETIC.getMultiKey(), storedEnergetic);
		event.getEntityPlayer().getEntityData().setInteger(Essence.EXOTIC.getMultiKey(), storedExotic);
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player.worldObj.rand.nextInt(100) == 1 && ConfigHandler.balance.enabledBoundEssence) {
			int storedAngelic = event.player.getEntityData().getInteger(Essence.ANGELIC.getMultiKey());
			int storedAtmospheric = event.player.getEntityData().getInteger(Essence.ATMOSPHERIC.getMultiKey());
			int storedDemonic = event.player.getEntityData().getInteger(Essence.DEMONIC.getMultiKey());
			int storedEnergetic = event.player.getEntityData().getInteger(Essence.ENERGETIC.getMultiKey());
			int storedExotic = event.player.getEntityData().getInteger(Essence.EXOTIC.getMultiKey());

			if (storedAngelic > 1000) {
				BoundHelper.levitation(event.player, (byte) 5, 50);
			} else if (storedAngelic > 500) {
				if (event.player.worldObj.rand.nextInt(50) == 1) {
					BoundHelper.levitation(event.player, (byte) 4, 100);
				}
			} else if (storedAngelic > 100) {
				if (event.player.worldObj.rand.nextInt(100) == 1) {
					BoundHelper.levitation(event.player, (byte) 2, 100);
				}
			}

			if (storedAtmospheric > 1000) {
				BoundHelper.lightning(event.player);
			} else if (storedAtmospheric > 500) {
				if (event.player.worldObj.rand.nextInt(50) == 1) {
					BoundHelper.lightning(event.player);
				}
			} else if (storedAtmospheric > 100) {
				if (event.player.worldObj.rand.nextInt(100) == 1) {
					BoundHelper.lightning(event.player);
				}
			}

			if (storedDemonic > 1000) {
				BoundHelper.fire(event.player);
			} else if (storedDemonic > 500) {
				if (event.player.worldObj.rand.nextInt(50) == 1) {
					BoundHelper.fire(event.player);
				}
			} else if (storedDemonic > 100) {
				if (event.player.worldObj.rand.nextInt(100) == 1) {
					BoundHelper.fire(event.player);
				}
			}

			if (storedEnergetic > 1000) {
				BoundHelper.slowness(event.player, (byte) 5, 100);
			} else if (storedEnergetic > 500) {
				if (event.player.worldObj.rand.nextInt(50) == 1) {
					BoundHelper.slowness(event.player, (byte) 4, 150);
				}
			} else if (storedEnergetic > 100) {
				if (event.player.worldObj.rand.nextInt(100) == 1) {
					BoundHelper.slowness(event.player, (byte) 2, 250);
				}
			}

			if (storedExotic >= 10) {
				if (storedAngelic >= 5) {
					event.player.getEntityData().setInteger(Essence.ANGELIC.getMultiKey(), storedAngelic - 5);
				}
				if (storedAtmospheric >= 5) {
					event.player.getEntityData().setInteger(Essence.ATMOSPHERIC.getMultiKey(), storedAtmospheric - 5);
				}
				if (storedDemonic >= 5) {
					event.player.getEntityData().setInteger(Essence.DEMONIC.getMultiKey(), storedDemonic - 5);
				}
				if (storedEnergetic >= 5) {
					event.player.getEntityData().setInteger(Essence.ENERGETIC.getMultiKey(), storedEnergetic - 5);
				}
				event.player.getEntityData().setInteger(Essence.EXOTIC.getMultiKey(), storedExotic - 10);
			}
		}
	}
}
