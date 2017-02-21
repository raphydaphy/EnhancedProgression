package com.raphydaphy.vitality.guide.category;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.init.Reference;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageImage;
import amerifrance.guideapi.page.PageText;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CatagoryFluxedStateOfMind
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = Reference.MOD_ID + "fluxed_state_";

        List<IPage> tier3Pages = new ArrayList<IPage>();
        tier3Pages.addAll(PageHelper.pagesForLongText("Now that you have reached the final part of your journey, your old Altar will not suffice. Surround the Obsidian layer of your altar with Imbued Planks to create a Tier 3 Altar. Like with Tier 2, you can shift+rightclick with a wand while looking at the altar to check that you built it correctly. On the next page is an image of a correctly built Tier 3 Altar. Note that you can use any variants or the Obsidian and Quartz, including modded variants like shown in the picture.", 390));
        tier3Pages.add(new PageImage(new ResourceLocation("vitality", "textures/gui/tier_3_altar.png")));
        tier3Pages.addAll(PageHelper.pagesForLongText("Your new Tier 3 Altar is able to create some incredibly powerful items, but at a hefty cost. Every Fluxed Item requires Nether Stars, which are obviously not an abundant resource in your world. Collect a few nether stars to continue.", 390));
        EntryItemStack tier3Entry = new EntryItemStack(tier3Pages, "An Even Better Altar", new ItemStack(ModBlocks.altar));
        entries.put(new ResourceLocation(keyBase + "tier_3_altar"), tier3Entry);
        
        List<IPage> godTierPages = new ArrayList<IPage>();
        godTierPages.addAll(PageHelper.pagesForLongText("With your shiny new Tier 3 Altar ready for use, you will now want to create some Fluxed Ingots. These god-like items require four Nether Stars, four Imbued Bronze Ingots and four Diamonds to craft, and they are crafted just like the Imbued Bronze Ingots that you made earlier - by throwing items onto the Altar and right-clicking twice. The image below shows how your Altar should look once you are ready to craft the Ingots.", 390));
        godTierPages.add(new PageImage(new ResourceLocation("vitality", "textures/gui/fluxed_ingot_crafting.png")));
        godTierPages.addAll(PageHelper.pagesForLongText("You are going to need a couple Fluxed Ingots in order to start making Fluxed Spells and a Master Wand, so craft up a few while your at it.", 390));
        EntryItemStack godTierEntry = new EntryItemStack(godTierPages, "God Tier Items", new ItemStack(ModItems.ingot_fluxed));
        entries.put(new ResourceLocation(keyBase + "godTier"), godTierEntry);
        
        List<IPage> fluxedLogPages = new ArrayList<IPage>();
        fluxedLogPages.addAll(PageHelper.pagesForLongText("Now that you have a few Fluxed Ingots, you will need to use a couple to make Fluxed Logs. These logs are quite expensive, requiring three Diamonds, two Fluxed Ingots and a single Imbued Log. Throw these items onto your Altar and it should look like the image below.", 390));
        fluxedLogPages.add(new PageImage(new ResourceLocation("vitality", "textures/gui/fluxed_log_crafting.png")));
        fluxedLogPages.addAll(PageHelper.pagesForLongText("Now that you are ready to craft the Fluxed Log, simply right-click the altar twice while holding a wand, just like normal Altar Crafting, and the Fluxed Logs will be crafted. Make a few of these so you are ready to create the FLuxed Spells very soon", 390));
        EntryItemStack fluxedLogEntry = new EntryItemStack(fluxedLogPages, "Maximal Power", new ItemStack(ModBlocks.fluxed_log));
        entries.put(new ResourceLocation(keyBase + "fluxedLog"), fluxedLogEntry);
        
        List<IPage> masterWandPages = new ArrayList<IPage>();
        masterWandPages.addAll(PageHelper.pagesForLongText("With all your Fluxed Ingots and Logs, you can now create a Master Wand with relative ease. Simply combine two Fluxed Sticks and a Fluxed Ingot in a Vanilla Crafting Table using the recipe below to craft the wand.", 390));
        masterWandPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.master_wand, "  X", " Y ", "Y  ", 'X', ModItems.fluxed_stick, 'Y', ModItems.fluxed_stick)));
        masterWandPages.addAll(PageHelper.pagesForLongText("With your Master Wand ready for use, you can now store 100000 Essence within the the Core of your wand, allowing you to have enough essence for long trips even without Essence Vials. It is still wise to bring an Essence Vial with you, however, as it will repair your Multitools and allow you to place Torches without a wand in your main hand using the Magic/Imbued/Fluxed Lantern spell.", 390));
        EntryItemStack masterWandEntry = new EntryItemStack(masterWandPages, "Essence Mastery", new ItemStack(ModItems.master_wand));
        entries.put(new ResourceLocation(keyBase + "master_wand"), masterWandEntry);
        
        List<IPage> fluxedUpgradePages = new ArrayList<IPage>();
        fluxedUpgradePages.addAll(PageHelper.pagesForLongText("With the power of Fluxed Ingots, you can now upgrade your Tier 2 Imbued spells once again to become Fluxed Spells. Simply craft four of an Imbued spell with four Fluxed Sticks and a Fluxed Ingot using the pattern below. This works on all spells except Vital Extraction and Imbued Hunger.", 390));
        fluxedUpgradePages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_lantern_3, "XYX", "YZY", "XYX", 'X', ModItems.fluxed_stick, 'Y', ModItems.spell_card_lantern_2, 'Z', ModItems.ingot_fluxed)));
        fluxedUpgradePages.addAll(PageHelper.pagesForLongText("You can also upgrade your Imbued Multitool to a Fluxed Multitool now. Simply throw your Imbued Multitool on the ground and right-click it with a Master Wand at full capacity. This means you will need 100000 Essence to craft the Fluxed Multitool. The Fluxed Multitool is far better than the Imbued Multitool since it can mine a 7x7 radius and is much faster. Like the Imbued Multitool, you can hold shift to disable the AoE mining and only mine a single block at a time. This also uses a lot less essence compared to mining AoE.", 390));
        EntryItemStack fluxedUpgradeEntry = new EntryItemStack(fluxedUpgradePages, "Moar Upgrades", new ItemStack(ModItems.spell_card_lantern_3));
        entries.put(new ResourceLocation(keyBase + "placement"), fluxedUpgradeEntry);
        
        List<IPage> flightPages = new ArrayList<IPage>();
        flightPages.addAll(PageHelper.pagesForLongText("Through your journey as a wizard, you learned many things. Now, forcing all your magic in to a single spell is required to create something even more powerful than before. By elevating your momentum, you reach into the skies, unbound from gravity forever. Collect four fluxed sticks, four feathers and a fluxed ingot to create this marvel of magic and science", 390));
        flightPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_flight, "XYX", "YZY", "XYX", 'X', ModItems.fluxed_stick, 'Y', Items.FEATHER, 'Z', ModItems.ingot_fluxed)));
        flightPages.addAll(PageHelper.pagesForLongText("With your new spell ready, simply equip it like you would with any normal spell, or put it in a spell bag. For 2000 Essence, when you right-click, you will be granted the power of flight until your next death. The fluxed state of the Essence infused into this spell makes it more powerful than ever before, giving you god-like powers.", 390));
        EntryItemStack flightEntry = new EntryItemStack(flightPages, "F*** Gravity", new ItemStack(ModItems.spell_card_flight));
        entries.put(new ResourceLocation(keyBase + "flight"), flightEntry);
        
        List<IPage> immortalPages = new ArrayList<IPage>();
        immortalPages.addAll(PageHelper.pagesForLongText("Ever since the dawn of this era, your magic has been empouring the world. After much hard work and dedication, you have now reached the end of your career. There is only one last thing left to do, but any wise wizard would turn it down.", 390));
        immortalPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_forcefield, "XYX", "YZY", "XYX", 'X', ModItems.fluxed_stick, 'Y', Items.SHIELD, 'Z', ModItems.ingot_fluxed)));
        immortalPages.addAll(PageHelper.pagesForLongText("The power of immortality is strong. Even once you are begging to die, you won't be able to. Think wisely before crafting this spell, but if you really want it, craft it using the recipe on the previous page. Using the spell costs 5000 Essence, and the spell will then protect you until you stop holding right-click. So maybe its not as bad as it sounded earlier, but still, this spell SHOULD be avoided. Please, don't use it.", 390));
        EntryItemStack immortalEntry = new EntryItemStack(immortalPages, "Like a God!", new ItemStack(ModItems.spell_card_forcefield));
        entries.put(new ResourceLocation(keyBase + "immortal"), immortalEntry);
        
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