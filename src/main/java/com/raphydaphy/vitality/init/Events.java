package com.raphydaphy.vitality.init;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events 
{
	@SubscribeEvent
	public static void onDrawScreenPost(PlayerEvent.Clone event)
	{
		int storedAngelic = event.getOriginal().getEntityData().getInteger("essenceStoredAngelic");
		int storedAtmospheric = event.getOriginal().getEntityData().getInteger("essenceStoredAtmospheric");
		int storedDemonic = event.getOriginal().getEntityData().getInteger("essenceStoredDemonic");
		int storedEnergetic = event.getOriginal().getEntityData().getInteger("essenceStoredEnergetic");
		int storedExotic = event.getOriginal().getEntityData().getInteger("essenceStoredExotic");
		
		event.getEntityPlayer().getEntityData().setInteger("essenceStoredAngelic", storedAngelic);
		event.getEntityPlayer().getEntityData().setInteger("essenceStoredAtmospheric", storedAtmospheric);
		event.getEntityPlayer().getEntityData().setInteger("essenceStoredDemonic", storedDemonic);
		event.getEntityPlayer().getEntityData().setInteger("essenceStoredEnergetic", storedEnergetic);
		event.getEntityPlayer().getEntityData().setInteger("essenceStoredExotic", storedExotic);
	}
}
