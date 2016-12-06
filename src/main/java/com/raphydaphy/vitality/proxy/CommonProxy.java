package com.raphydaphy.vitality.proxy;

import com.raphydaphy.vitality.achievement.ModAchievements;
import com.raphydaphy.vitality.block.tile.TileAltar;
import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.init.ModRecipies;
import com.raphydaphy.vitality.init.WorldGenHandler;
import com.raphydaphy.vitality.recipe.AltarRecipes;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{

	public void registerItemRenderer(Item item, int meta, String id)
	{
	}

	public void setActionText(String text)
	{
	}

	public void init()
	{
	}

	public void preInit()
	{
		ModItems.init();
		ModBlocks.init();

		ModAchievements.init();
		
		ModRecipies.registerOreDict();
		AltarRecipes.init();

		GameRegistry.registerTileEntity(TileAltar.class, "enhancedprogression:altar");
		GameRegistry.registerWorldGenerator(new WorldGenHandler(), 2);
	}

}
