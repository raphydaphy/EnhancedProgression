package com.raphydaphy.vitality.registry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author Shadows
 * @Desc Pick this or IMeta, but not both.
 */
public interface IRegisterable {

	/**
	 * This is the thing that does model registration. Override for custom
	 * things.
	 * 
	 */
	@SideOnly(Side.CLIENT)
	public void registerModels();

	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModelLocation();

}
