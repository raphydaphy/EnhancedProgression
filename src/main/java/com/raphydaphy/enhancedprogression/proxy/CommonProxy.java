package com.raphydaphy.enhancedprogression.proxy;

import com.raphydaphy.enhancedprogression.achievement.ModAchievements;
import com.raphydaphy.enhancedprogression.block.tile.TileAltar;
import com.raphydaphy.enhancedprogression.init.ModBlocks;
import com.raphydaphy.enhancedprogression.init.ModItems;
import com.raphydaphy.enhancedprogression.init.ModMultiblocks;
import com.raphydaphy.enhancedprogression.recipe.AltarRecipes;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy 
{
	
	public void registerItemRenderer(Item item, int meta, String id) { }
	public void setActionText(String text) { }
	public void init() { }
	public void preInit()
	{
		ModItems.init();
		ModBlocks.init();
		
		ModMultiblocks.init();
		ModAchievements.init();
		
		AltarRecipes.init();
		
		GameRegistry.registerTileEntity(TileAltar.class, "enhancedprogression:altar");
	}
}
