package com.raphydaphy.enhancedprogression.proxy;

import com.raphydaphy.enhancedprogression.init.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy
{

	@Override
	public void registerItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta,
				new ModelResourceLocation(Reference.MOD_ID + ":" + id, "inventory"));
	}

	@Override
	public void setActionText(String text)
	{
		Minecraft.getMinecraft().ingameGUI.setRecordPlaying(TextFormatting.BLUE + text, false);
	}

}
