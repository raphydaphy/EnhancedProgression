package com.raphydaphy.vitality.proxy;

import java.util.Iterator;
import java.util.List;

import com.raphydaphy.vitality.block.BlockBase;
import com.raphydaphy.vitality.item.ItemBase;
import com.raphydaphy.vitality.util.registry.IMeta;
import com.raphydaphy.vitality.util.registry.IRegisterable;
import com.raphydaphy.vitality.util.registry.ModBlocks;
import com.raphydaphy.vitality.util.registry.ModItems;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		doModels(ModBlocks.BLOCK_LIST);
		doModels(ModItems.ITEM_LIST);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	public void doModels(List<?> list) {
		Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {
			Object k = iterator.next();
			if (k instanceof IMeta) {
				IMeta thing = (IMeta) k;
				List<ModelResourceLocation> list2 = thing.getMetaModelLocations();
				for (int i = 0; i <= thing.getMaxMeta(); i++) {
					if (k instanceof Block)
						ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock((Block) k), i, list2.get(i));
					else if (k instanceof Item)
						ModelLoader.setCustomModelResourceLocation((Item) k, i, list2.get(i));
				}
			} else if (k instanceof IRegisterable)
				((IRegisterable) k).registerModels();
		}
	}

	/*
	 * Displays a message in the record bar in the color specified through the
	 * TextFormatting parameter
	 */
	@SideOnly(Side.CLIENT)
	public static void setActionText(String text, TextFormatting color) {
		Minecraft.getMinecraft().ingameGUI.setRecordPlaying(color + text, false);
	}

}