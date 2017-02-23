package com.raphydaphy.vitality.proxy;

import com.raphydaphy.vitality.init.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
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
	
	/*
	 * Displays a message in the record bar in gold since no color is specified
	 */
	@Override
	public void setActionText(String text)
	{
		setActionText(text, TextFormatting.GOLD);
	}
	
	/*
	 * Displays a message in the record bar in the color specified through the TextFormatting parameter
	 */
	@Override
	public void setActionText(String text, TextFormatting color)
	{
		Minecraft.getMinecraft().ingameGUI.setRecordPlaying(color + text, false);
	}
}