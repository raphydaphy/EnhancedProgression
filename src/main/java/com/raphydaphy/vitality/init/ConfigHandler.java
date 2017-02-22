package com.raphydaphy.vitality.init;

import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID, name = Reference.MOD_ID + "/" + Reference.MOD_ID)
public class ConfigHandler 
{
	/*
	 * All the sections of Vitality's configuration
	 * Will show as seperate areas in the .cfg file
	 */
	public static CraftingSettings crafting = new CraftingSettings();
	public static WorldSettings world = new WorldSettings();
	
	/*
	 * For the config on any items that are crafted
	 * Disabling items here only removes the crafting recipe
	 */
	public static class CraftingSettings
    {
		
    }
	
	public static class WorldSettings
	{
		@Config.Comment({"Enable Copper and Tin Generation.", "Default: true"})
        public boolean enableCopperAndTin = true;
	}
}
