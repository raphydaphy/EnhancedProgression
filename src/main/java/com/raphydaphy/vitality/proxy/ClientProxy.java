package com.raphydaphy.vitality.proxy;

import com.raphydaphy.vitality.init.KeyBindings;
import com.raphydaphy.vitality.init.Reference;
import com.raphydaphy.vitality.item.ItemVitalityGuide;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;

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
		Minecraft.getMinecraft().ingameGUI.setRecordPlaying(TextFormatting.GOLD + text, false);
	}
	
	@Override
	public void showWandStatus(String text, int essenceStored, int essenceMax)
	{
		Minecraft.getMinecraft().ingameGUI.setRecordPlaying(TextFormatting.GOLD + text, false);
	}
	
	@Override
	public void init()
	{
        KeyBindings.init();
        
        
	}
	
	@Override 
	public void preInit()
	{
		if(Loader.isModLoaded("guideapi"))
        {
        	ItemVitalityGuide.mainGuide(true);
        }
	}

}
