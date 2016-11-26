package com.raphydaphy.enhancedprogression;

import com.raphydaphy.enhancedprogression.init.EPCreativeTab;
import com.raphydaphy.enhancedprogression.init.Events;
import com.raphydaphy.enhancedprogression.init.ModRecipies;
import com.raphydaphy.enhancedprogression.init.Reference;
import com.raphydaphy.enhancedprogression.proxy.CommonProxy;
import com.raphydaphy.enhancedprogression.recipe.AltarRecipes;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.MINECRAFT_VERSION)
public class EnhancedProgression
{
	@Instance
	public static EnhancedProgression Instance;

	public static final EPCreativeTab creativeTab = new EPCreativeTab();

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		System.out.println("Setting up prerequisites");
		MinecraftForge.EVENT_BUS.register(Events.class);
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		System.out.println("Initializing Enhanced Progression...");
		ModRecipies.registerCrafting();
		ModRecipies.registerSmelting();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		System.out.println("Enhanced Progression Loaded Successfully :D");
	}

}