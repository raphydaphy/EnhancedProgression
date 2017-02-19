package com.raphydaphy.vitality.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.raphydaphy.vitality.init.Reference;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Page;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageFurnaceRecipe;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageImage;
import amerifrance.guideapi.page.PageText;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ItemVitalityGuide
{

	public static final Book vitalityGuide = new Book();
	
	public static void mainGuide(boolean client)
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
        
        
	        
		vitalityGuide.setTitle("Main Menu");
		vitalityGuide.setWelcomeMessage("Welcome to Vitality");
		vitalityGuide.setDisplayName("vitalityGuide");
		vitalityGuide.setColor(Color.GRAY);
		vitalityGuide.setCategoryList(categories);
		vitalityGuide.setRegistryName("vitalityGuide");
		vitalityGuide.setSpawnWithBook(true);

		//GuideAPI.BOOKS.register(vitalityGuide);
		
		GameRegistry.register(vitalityGuide);
	}
}