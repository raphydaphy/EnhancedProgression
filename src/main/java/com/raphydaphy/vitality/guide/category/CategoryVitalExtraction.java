package com.raphydaphy.vitality.guide.category;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.raphydaphy.vitality.guide.BookUtils;
import com.raphydaphy.vitality.guide.entry.EntryText;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.init.Reference;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.api.util.TextHelper;
import amerifrance.guideapi.page.PageText;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategoryVitalExtraction
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = Reference.MOD_ID + ":vital_extraction_";

        List<IPage> introPages = new ArrayList<IPage>();
        introPages.addAll(PageHelper.pagesForLongText("Ever since the dawn of this age, magicians have lived among us, using magic to aid them in their pathways. By concentrating the vital essence used in their spells, we have now enabled anyone to become a wizard, in only a few simple steps. This is the start of your journey. Good luck.", 370));
        entries.put(new ResourceLocation(keyBase + "intro"), new EntryText(introPages, keyBase + "intro", true));

        List<IPage> basicwandPages = new ArrayList<IPage>();
        basicwandPages.addAll(PageHelper.pagesForLongText("The essentials for any wizard in this world begin with a simple yet delicate item known as a Wand. For now, with your limited experience and resources, you will only be able to forge the most brittle wand known to Man, a Basic Wand. \nThe only benefit of this raw piece of equipment is that it can be made without any knowledge of magic, and using only materials found in the world. Collect two sticks and either a tin or copper ingot in order to craft the wand.\n\nWith your new wand in hand, you must now go on to learn how to use it, for without magical knowledge, even the most powerful of spells is useless. Your wand stores Essence, a mystical energy form used to cast spells and perform rituals. To check how much essence is currently in your wand, simply hold shift and right-click while the wand is in your main hand. You won't have any essence stored in your wand just yet, but soon enough you should easily be able to ammass a couple hundred essence in the core of your Basic Wand. Do be careful though, because your wand can break if overused, and therefore caution is advised when using it to extract essence, a process which is covered in the next section.", 370));
        entries.put(new ResourceLocation(keyBase + "basic_wand"), new EntryText(basicwandPages, keyBase + "basic_wand", true));

        List<IPage> vitalextractionPages = new ArrayList<IPage>();
        vitalextractionPages.addAll(PageHelper.pagesForLongText("Your new wand may be fragile, yet it can still perform basic spells. Any experienced wizard would know that the first spell required to begin their journey into Magic is the Vital Extraction spell, used to imbue your wand with Essence from natural sources such as trees and plants. The spell is fairly easy to craft, requiring just a few sticks, some bronze and some logs.\n\nOnce your Vital Extraction spell is crafted, simply place the spell in your offhand, which is done by pressing 'F' while the spell is in your dominant hand, and holding your Basic Wand in your main hand. Walk up to any nearby tree and hold right-click until the entire tree turns into Dead Logs. If you stop extracting before the process is complete, the essence gathered thus far will be left in an unfinished state and destroyed, so make sure you extract the entire tree before stopping. You will want to extract a decent amount of essence before continuing, so collect a couple hundred while you're at it.", 370));
        entries.put(new ResourceLocation(keyBase + "vital_extraction"), new EntryText(vitalextractionPages, keyBase + "vital_extraction", true));
        
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