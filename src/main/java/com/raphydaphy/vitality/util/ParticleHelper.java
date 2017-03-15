package com.raphydaphy.vitality.util;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ParticleHelper {
	/*
	 * Spawns particles based on paramaters used
	 */
	public static void spawnParticles(EnumParticleTypes particleType, World world, boolean forceSpawn, BlockPos pos,
			int count, double radius) {
		// simply runs the main particle spawning method without so many
		// arguments
		spawnParticlesServer(particleType, world, forceSpawn, pos.getX(), pos.getY(), pos.getZ(), count, radius);
	}

	public static void spawnParticlesServer(EnumParticleTypes particleType, World world, boolean forceSpawn, double x,
			double y, double z, int count, double radius) {
		// spawns some particles through the server instance
		if (!world.isRemote) {
			FMLCommonHandler.instance().getMinecraftServerInstance()
					.worldServerForDimension(world.provider.getDimension())
					.spawnParticle(particleType, forceSpawn, x, y, z, count, radius, radius, radius, 0.000D);
		}
	}

	/*
	 * Spawns particles on every edge of the block position given
	 */
	public static void outlineAll(BlockPos pos, EnumParticleTypes particle, World world) {
		outlineBottom(pos, particle, world);
		outlineBottom(pos.add(0, 1, 0), particle, world);
		outlineSides(pos, particle, world);
	}

	/*
	 * Spawns particles on the side edges of a block
	 */
	public static void outlineSides(BlockPos pos, EnumParticleTypes particle, World world) {
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();

		for (int i = 0; i < 10; i++) {
			spawnParticlesServer(particle, world, true, x, y, z, 1, 0.001);
			y += 0.1;
		}

		y = pos.getY();
		x = pos.getX() + 1;

		for (int i = 0; i < 10; i++) {
			spawnParticlesServer(particle, world, true, x, y, z, 1, 0.001);
			y += 0.1;
		}

		x = pos.getX();
		y = pos.getY();
		z = pos.getZ() + 1;

		for (int i = 0; i < 10; i++) {
			spawnParticlesServer(particle, world, true, x, y, z, 1, 0.001);
			y += 0.1;
		}

		x = pos.getX() + 1;
		y = pos.getY();
		z = pos.getZ() + 1;

		for (int i = 0; i < 10; i++) {
			spawnParticlesServer(particle, world, true, x, y, z, 1, 0.001);
			y += 0.1;
		}
	}

	/*
	 * Spawns particles around the bottom edges of a block
	 */
	public static void outlineBottom(BlockPos pos, EnumParticleTypes particle, World world) {
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();

		x = pos.getX() + 1;
		z = pos.getZ() + 1;

		// top square
		for (int i = 0; i < 10; i++) {
			spawnParticlesServer(particle, world, true, x, y, z, 1, 0.001);
			x -= 0.1;
		}
		x = pos.getX() + 1;
		for (int i = 0; i < 10; i++) {
			spawnParticlesServer(particle, world, true, x, y, z, 1, 0.001);
			z -= 0.1;
		}
		x = pos.getX();
		z = pos.getZ();
		for (int i = 0; i < 10; i++) {
			spawnParticlesServer(particle, world, true, x, y, z, 1, 0.001);
			x += 0.1;
		}
		x = pos.getX();
		for (int i = 0; i < 10; i++) {
			spawnParticlesServer(particle, world, true, x, y, z, 1, 0.001);
			z += 0.1;
		}
	}

	public static BlockPos multipleNoZero(BlockPos pos1, BlockPos pos2) {
		double[] posNums = { pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ() };
		for (int i = 0; i < 6; i++) {
			if (posNums[i] == 0D)
				posNums[i] = 1;
		}
		return new BlockPos(posNums[0] * posNums[3], posNums[1] * posNums[4], posNums[2] * posNums[5]);
	}
}
