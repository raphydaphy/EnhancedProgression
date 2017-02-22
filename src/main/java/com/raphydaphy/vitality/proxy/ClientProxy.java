package com.raphydaphy.vitality.proxy;

import com.raphydaphy.vitality.init.Reference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy
{
	/*
	 * Sets the inventory texture of Items/Blocks in the mod
	 */
	@Override
	public void registerItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta,
				new ModelResourceLocation(Reference.MOD_ID + ":" + id, "inventory"));
	}
}