package com.raphydaphy.vitality.proxy;

import com.raphydaphy.vitality.block.container.ContainerElementalCrafting;
import com.raphydaphy.vitality.gui.GUIElementalCrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		return new ContainerElementalCrafting(player.inventory, world, pos);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		return new GUIElementalCrafting(player.inventory, world, pos);
	}
}