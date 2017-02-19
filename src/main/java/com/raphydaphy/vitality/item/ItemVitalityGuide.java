package com.raphydaphy.vitality.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageText;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ItemVitalityGuide
{

	public static Book vitalityGuide = new Book();
	
	public static void initBook()
    {
        vitalityGuide.setTitle("guide.Vitality.title");
        vitalityGuide.setDisplayName("guide.Vitality.display");
        vitalityGuide.setWelcomeMessage("guide.Vitality.welcome");
        vitalityGuide.setAuthor("guide.Vitality.author");
        vitalityGuide.setRegistryName("VitalityGuide");
        vitalityGuide.setColor(Color.RED);

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
            GuideAPI.setModel(vitalityGuide);
    }

    public static void initCategories()
    {
    	PageText page1 = new PageText("Hi there. Welcome to Vitality!");
		
		ArrayList<IPage> pages = new ArrayList<IPage>();
	    pages.add(page1);
	    
	    EntryItemStack entry1 = new EntryItemStack(pages, "TestEntry1", new ItemStack(Items.POTATO));
	    
	    Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        entries.put(new ResourceLocation("guideapi", "testEntry1"), entry1);
        
        CategoryItemStack category1 = new CategoryItemStack(entries, "TestCategory2", new ItemStack(Blocks.BRICK_STAIRS));
        
	    ArrayList<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();
        categories.add(category1);
    }
}