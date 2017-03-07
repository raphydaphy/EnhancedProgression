package com.raphydaphy.vitality.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpellHelper {
	public static RayTraceResult rayTraceFancy(EntityPlayer playerIn, int ticks, double reach, World worldIn)
	{
		Vec3d vec3d = eyePosGlobal(ticks, playerIn);
        Vec3d vec3d1 = playerIn.getLook(ticks);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * reach, vec3d1.yCoord * reach, vec3d1.zCoord * reach);
        return worldIn.rayTraceBlocks(vec3d, vec3d2, false, false, true);
	}
	
	private static Vec3d eyePosGlobal(float partialTicks, EntityPlayer player)
    {
        if (partialTicks == 1.0F)
        {
            return new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        }
        else
        {
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks + (double)player.getEyeHeight();
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
            return new Vec3d(d0, d1, d2);
        }
    }
}
