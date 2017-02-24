package com.raphydaphy.vitality.util;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;

public final class BoundHelper 
{
	public static void lightning(EntityPlayer player)
	{
		ParticleHelper.spawnParticles(EnumParticleTypes.CLOUD, player.worldObj, true, player.getPosition().down(), 30, 1);
		EntityLightningBolt lightning = new EntityLightningBolt(player.worldObj, player.posX, player.posY, player.posZ, false);
		if (!player.worldObj.isRemote)
		{
			player.worldObj.addWeatherEffect(lightning);
		}
		player.getEntityData().setInteger("essenceStoredAtmospheric", player.getEntityData().getInteger("essenceStoredAtmospheric") - 20);
	}
	
	public static void slowness(EntityPlayer player, byte amplifier, int duration)
	{
		ParticleHelper.spawnParticles(EnumParticleTypes.DRAGON_BREATH, player.worldObj, true, player.getPosition().down(), 30, 1);
		NBTTagCompound potionNBT = new NBTTagCompound();
		potionNBT.setByte("Amplifier", amplifier);
		potionNBT.setInteger("Duration", duration);
		potionNBT.setBoolean("ShowParticles", false);
		potionNBT.setByte("Id", (byte) 2);
		player.addPotionEffect(PotionEffect.readCustomPotionEffectFromNBT(potionNBT));
		player.getEntityData().setInteger("essenceStoredEnergetic", player.getEntityData().getInteger("essenceStoredEnergetic") - 20);
	}
	
	public static void levitation(EntityPlayer player, byte amplifier, int duration)
	{
		ParticleHelper.spawnParticles(EnumParticleTypes.END_ROD, player.worldObj, true, player.getPosition().down(), 30, 1);
		NBTTagCompound potionNBT = new NBTTagCompound();
		potionNBT.setByte("Amplifier", amplifier);
		potionNBT.setInteger("Duration", duration);
		potionNBT.setBoolean("ShowParticles", false);
		potionNBT.setByte("Id", (byte) 25);
		player.addPotionEffect(PotionEffect.readCustomPotionEffectFromNBT(potionNBT));
		player.getEntityData().setInteger("essenceStoredAngelic", player.getEntityData().getInteger("essenceStoredAngelic") - 20);
	}
}
