package com.raphydaphy.vitality.guide.category;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.init.Reference;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageText;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CategoryVitalExtraction
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = Reference.MOD_ID + ":vital_extraction_";

        List<IPage> introPages = new ArrayList<IPage>();
        introPages.addAll(PageHelper.pagesForLongText("Ever since the dawn of this age, magicians have lived among us, using magic to aid them in their pathways. By concentrating the vital essence used in their spells, we have now enabled anyone to become a wizard, in only a few simple steps. This is the start of your journey. Good luck.", 390));
        EntryItemStack introEntry = new EntryItemStack(introPages, "The Beginning", new ItemStack(Items.BOOK));
        entries.put(new ResourceLocation(keyBase + "intro"), introEntry);
        
        List<IPage> basicWandPages = new ArrayList<IPage>();
        basicWandPages.addAll(PageHelper.pagesForLongText("The essentials for any wizard in this world begin with a simple yet delicate item known as a Wand. For now, with your limited experience and resources, you will only be able to forge the most brittle wand known to Man, a Basic Wand. \nThe only benefit of this raw piece of equipment is that it can be made without any knowledge of magic, and using only materials found in the world. Collect two sticks and either a tin or copper ingot in order to craft the wand, using the recipe shown on the next page.", 390));
        basicWandPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.basic_wand_copper, "  X", " Y ", "Y  ", 'X', "ingotCopper", 'Y', "stickWood")));
        basicWandPages.addAll(PageHelper.pagesForLongText("With your new wand in hand, you must now go on to learn how to use it, for without magical knowledge, even the most powerful of spells are useless. Your wand stores Essence, a mystical energy form used to cast spells and perform rituals. To check how much essence is currently in your wand, simply hold shift and right-click while the wand is in your main hand. You won't have any essence stored in your wand just yet, but soon enough you should easily be able to ammass a couple hundred essence in the core of your Basic Wand. Do be careful though, because your wand can break if overused, and therefore caution is advised when using it to extract essence, a process which is covered in the next section.", 390));
        EntryItemStack basicWandEntry = new EntryItemStack(basicWandPages, "A Spark of Life!", new ItemStack(ModItems.basic_wand_tin));
        entries.put(new ResourceLocation(keyBase + "basic_wand"), basicWandEntry);
        
        List<IPage> vitalExtractionPages = new ArrayList<IPage>();
        vitalExtractionPages.addAll(PageHelper.pagesForLongText("Your new wand may be fragile, yet it can still perform basic spells. Any experienced wizard would know that the first spell required to begin their journey into Magic is the Vital Extraction spell, used to imbue your wand with Essence from natural sources such as trees and plants. The spell is fairly easy to craft, requiring just a few sticks, some bronze and some logs, as shown on the next page.",390));
        vitalExtractionPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_vital_extraction, "SLS", "LBL", "SLS", 'S', "stickWood", 'L', "logWood", 'B', "ingotBronze")));
        vitalExtractionPages.addAll(PageHelper.pagesForLongText("Once your Vital Extraction spell is crafted, simply place the spell in your offhand, which is done by pressing 'F' while the spell is in your dominant hand, and holding your Basic Wand in your main hand. Walk up to any nearby tree and hold right-click until the entire tree turns into Dead Logs. If you stop extracting before the process is complete, the essence gathered thus far will be left in an unfinished state and destroyed, so make sure you extract the entire tree before stopping. You will want to extract a decent amount of essence before continuing, so collect a couple hundred while you're at it.", 390));
        EntryItemStack vitalextractionEntry = new EntryItemStack(vitalExtractionPages, "Essence Extraction", new ItemStack(ModItems.spell_card_vital_extraction));
        entries.put(new ResourceLocation(keyBase + "vital_extraction"), vitalextractionEntry);
        
        List<IPage> essenceStoragePages = new ArrayList<IPage>();
        essenceStoragePages.addAll(PageHelper.pagesForLongText("Since your new wand is so weak, it can only store 1000 Essence within its core. For long trips away from home, when mining or even exploring, you will likely need much more than this. A simple method of storing large amounts of Essence is avalable very early on in your wizarding career, known as Essence Vials. These small flasks of power can hold a whopping 10000000 Essence, but in most cases the Essence must be transfered back into the Wand before usage. In order to create one, you will first need an Empty Essence Vial, crafted with a few pieces of glass and a single Bronze, as shown on the next page.",390));
        essenceStoragePages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.essence_vial_empty, " B ", "G G", " G ", 'G', "blockGlass", 'B', "ingotBronze")));
        essenceStoragePages.addAll(PageHelper.pagesForLongText("Unfortunately, an Empty Essence Vial is quite useless by itself. To fill it up, you will need a deposit of 1000 Essence, which is the entire capacity of your Basic Wand. Harvest a large number of logs or crops using the Vital Extraction spell that you made eariler in order to fill it up. Once it is full, you can continue. With a full wand ready, unequip any active spells from your offhand and throw your Empty Essence Vial on the ground. By simply right-clicking the ground near the item, the Essence stored in your wand will travel into the Vial, filling it up and imbuing it with power. You can check how much Essence is stored in your newly-filled vial by holding shift and right-clicking while holding it, much like how you check the Essence levels in your wand. ", 390));
        essenceStoragePages.addAll(PageHelper.pagesForLongText("Since storing 1000 Essence in your Vial isn't much better than simply having a second wand, you can add more essence by throwing the Vial on the ground a second time and right-clicking again. Make sure you don't have any spells equipped when doing so, and it will proceed to add 1000 more Essence to the Vial, as long as your Wand has enough stored inside it to transfer. This can be repeated as many times as you want to fill the Vial up to the previously mentioned capacity of 10000000 Essence, which you will likely never reach. When you eventually run out of Essence in your wand, simply equip a Vital Extraction spell, throw the Vial on the ground and right-click. This will take essence back out of the Vial and store it in your wand, depositing 1000 Essence per click. Be careful not to over-fill your wand, as this wastes your precious Essence and cannot be recovered.", 390));
        EntryItemStack essenceStorageEntry = new EntryItemStack(essenceStoragePages, "Essence Storage", new ItemStack(ModItems.essence_vial_full));
        entries.put(new ResourceLocation(keyBase + "essence_storage"), essenceStorageEntry);
        
        for (Entry<ResourceLocation, EntryAbstract> entry : entries.entrySet())
        {
            for (IPage page : entry.getValue().pageList)
            {
                if (page instanceof PageText)
                {
                    ((PageText) page).setUnicodeFlag(true);
                }
            }
        }

        return entries;
    }
}