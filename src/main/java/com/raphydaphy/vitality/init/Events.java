package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.util.BoundHelper;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Events {

	@SubscribeEvent
	public static void onDeath(PlayerEvent.Clone event) {
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

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player.worldObj.rand.nextInt(100) == 1 && ConfigHandler.balance.enabledBoundEssence) {
			int storedAngelic = event.player.getEntityData().getInteger("essenceStoredAngelic");
			int storedAtmospheric = event.player.getEntityData().getInteger("essenceStoredAtmospheric");
			int storedDemonic = event.player.getEntityData().getInteger("essenceStoredDemonic");
			int storedEnergetic = event.player.getEntityData().getInteger("essenceStoredEnergetic");
			int storedExotic = event.player.getEntityData().getInteger("essenceStoredExotic");

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
					event.player.getEntityData().setInteger("essenceStoredAngelic", storedAngelic - 5);
				}
				if (storedAtmospheric >= 5) {
					event.player.getEntityData().setInteger("essenceStoredAtmospheric", storedAtmospheric - 5);
				}
				if (storedDemonic >= 5) {
					event.player.getEntityData().setInteger("essenceStoredDemonic", storedDemonic - 5);
				}
				if (storedEnergetic >= 5) {
					event.player.getEntityData().setInteger("essenceStoredEnergetic", storedEnergetic - 5);
				}
				event.player.getEntityData().setInteger("essenceStoredExotic", storedExotic - 10);
			}
		}
	}
}
