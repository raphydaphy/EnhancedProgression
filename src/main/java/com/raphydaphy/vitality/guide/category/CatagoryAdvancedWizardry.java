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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
 
public class CatagoryAdvancedWizardry
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = Reference.MOD_ID + "advanced_wizardry_";

        List<IPage> advancedWandPages = new ArrayList<IPage>();
        advancedWandPages.addAll(PageHelper.pagesForLongText("Now that you have access to Imbued materials, you can throw away your Basic Wand in favor of an unbreakable one with 10x as much Essence Storage. This makes it much easier to use with Essence Vials as you don't need to worry about wasting essence when making deposits of 1000 Essence each to the Vial, and allows you to cast more spells before refilling. The wand is quite cheap to craft, requiring just two Imbued Sticks and a single Imbued Bronze Ingot. The recipe is shown on the next page.", 390));
        advancedWandPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.advanced_wand, "  X", " Y ", "Y  ", 'X', ModItems.imbued_stick, 'Y', ModItems.ingot_bronze_imbued)));
        advancedWandPages.addAll(PageHelper.pagesForLongText("Now that you have an Advanced Wand, you can begin to fill it up using the traditional method, by extracting essence out of Trees, Cactus and Crops, or you can move on to craft the Enhanced Extraction spell in the next section.", 390));
        EntryItemStack advancedWandEntry = new EntryItemStack(advancedWandPages, "Unbreakable", new ItemStack(ModItems.advanced_wand));
        entries.put(new ResourceLocation(keyBase + "advanced_wand"), advancedWandEntry);
        
        List<IPage> enhancedExtractionPages = new ArrayList<IPage>();
        enhancedExtractionPages.addAll(PageHelper.pagesForLongText("Vital Extraction is very slow. With a maximum of 25 Essence per log, 30 per Cactus or 70 per fully-grown crop, it dosen't yeild nearly enough for some of the more advanced spells that you are begining to make. With the Enhanced Extraction spell, you can utilise the power of Ore to extract the vital paprts of Nature out of them. This also works on Mob Spawnners, due to the termendeous power they hold that enables them to materialise creatures out of thin air. The spell is quite expensive, requiring four diamonds, four Imbued Sticks and four Imbued Bronze Ingots, as shown on the next page.", 390));
        enhancedExtractionPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_explosion_1, "XYX", "YZY", "XYX", 'X', "stickWood", 'Y', Items.DIAMOND, 'Z', "ingotBronze")));
        enhancedExtractionPages.addAll(PageHelper.pagesForLongText("Usage of the Enhanced Extraction spell is very simple. Either put it into a spell bag and select it, or just put the spell in your offhand, then a wand in your main hand, as per normal. Walk up to either a Mob Spawnner or any ores including Coal, Iron, Gold, Diamond and Emerald, and right-click the block. This will cause essence to drain into your wand very quickly, filling it with up to 15000 Essence depenting on what you extracted. Any ores that weren't listed above will only yeild 50 Essence, and the other ores will yeild between 100 - 1000 depending on the rarity.", 390));
        EntryItemStack enhancedExtractionEntry = new EntryItemStack(enhancedExtractionPages, "Better Extraction", new ItemStack(ModItems.spell_card_enhanced_extraction_1));
        entries.put(new ResourceLocation(keyBase + "enhanced_extraction"), enhancedExtractionEntry);
        
        List<IPage> upgradePages = new ArrayList<IPage>();
        upgradePages.addAll(PageHelper.pagesForLongText("With your new Imbued Logs and Ingots, you can begin to upgrade your existing spells. Almost all the spells you have made so far can be upgraded, except for Vital Extraction. To upgrade any spell, you will need four of the Tier 1 version of the spell, as well as four Imbued Sticks and a single Imbued Bronze Ingot. The same patten is used to make any Tier 2 spell using Tier 1 spells, following the recipe shown on the next page. Simply replace the Radiant Fireball spells with the type of spell that you want to upgrade when crafting.", 390));
        upgradePages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_fireball_1, "XYX", "YZY", "XYX", 'X', ModItems.imbued_stick, 'Y', ModItems.spell_card_fireball_1, 'Z', ModItems.ingot_bronze_imbued)));
        upgradePages.addAll(PageHelper.pagesForLongText("When you have crafted some Tier 2 Spells, you can use them much the same as regular Tier 1 Spells. Just put them in your offhand or into a spell bag, hold a wand in your main hand and right-click. With access to Tier 2 Spells now, you will likely want to replace all the spells in your Spell Bag with Tier 2 counterparts, so you will need to create a new spell bag.", 390));
        upgradePages.addAll(PageHelper.pagesForLongText("You can also now upgrade your Magic Multitool to a Fluxed Multitool. You will need a Advanced Wand full of essence (10000 Essence), and the Magic Multitool that you made earlier. Simply throw the Magic Multitool on the ground and ensure you have no spells equipped. Right-click the Magic Multitool with your full Advanced Wand. This will consume all 10000 Essence, and turn the Magic Multitool into a Imbued Multitool. This is far better than the Magic Multitool because it can mine a 3x3 area per block, and is much faster. If you don't want to mine 3x3, simply hold shift while mining a block and it will function like a faster version of the Magic Multitool.", 390));
        EntryItemStack upgradeEntry = new EntryItemStack(upgradePages, "Upgraded Reality", new ItemStack(ModItems.spell_card_fireball_2));
        entries.put(new ResourceLocation(keyBase + "spell_upgrades"), upgradeEntry);
        
        List<IPage> lightningPages = new ArrayList<IPage>();
        lightningPages.addAll(PageHelper.pagesForLongText("Because the light of a single torch just sometimes isn't enough, with your new imbued items you can now summon Lightning Bolts at your will! This advanced spell requires four Imbued Sticks, a single Imbued Bronze Ingot and four Diamond Axes to craft. It is quite costly, yet worth it in the long run as you can smite your enemies with ease. The crafting recipe is shown on the next page.", 390));
        lightningPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_fireball_2, "XYX", "YZY", "XYX", 'X', Items.DIAMOND_AXE, 'Y', ModItems.spell_card_fireball_1, 'Z', ModItems.ingot_bronze_imbued)));
        lightningPages.addAll(PageHelper.pagesForLongText("Now that you have the Imbued Lightning spell, you can use it similar to how you use most block-based spells. Simply put the spell in either a spell bag or just in your offhand, put a wand in your main hand and right-click a block. A powerful bolt of lightning will be summoned from the skies to aid you in combat, destroying anything in its path. The spell costs 250 Essence per use, so it is quite expensive, but at this point in your career you should be able to get this much essence quite easily using the Enhanced Extraction spell you made earlier.", 390));
        EntryItemStack lightningEntry = new EntryItemStack(lightningPages, "A Bigger Shimmer", new ItemStack(ModItems.spell_card_lightning_1));
        entries.put(new ResourceLocation(keyBase + "lightning"), lightningEntry);
        
        List<IPage> rapidfirePages = new ArrayList<IPage>();
        rapidfirePages.addAll(PageHelper.pagesForLongText("When the cooldown time for most other spells is too slow for your combat style, you may realise the need to develop a Spell that bypasses this. The Imbued Rapidfire spell shoots arrows very quickly at quite an expense to the Essence stored in your wand. This powerful spell is crafted with just four arrows, four Imbued Sticks and four Imbued Bronze Ingots, as shown on the next page.", 390));
        rapidfirePages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_rapidfire_1, "XYX", "YZY", "XYX", 'X', ModItems.imbued_stick, 'Y', Items.ARROW, 'Z', ModItems.ingot_bronze_imbued)));
        rapidfirePages.addAll(PageHelper.pagesForLongText("The usage of this spell is quite simple. Place the spell in either a spell bag or just in your offhand, and a wand in your main hand, then hold right-click for a few seconds, then let go. The arrows will continue to fire at a lesser rate than they started at. To stop using the spell, switch to a different item in your hotbar. If you have a lot of essence to spare, continue holding down right-click the entire time, resulting in a much faster stream of arrows at a much higher cost to your Essence, at 100 per arrow compared to 10.", 390));
        EntryItemStack rapidfireEntry = new EntryItemStack(rapidfirePages, "Better than Bows!", new ItemStack(ModItems.spell_card_rapidfire_1));
        entries.put(new ResourceLocation(keyBase + "rapidfire"), rapidfireEntry);
        
        List<IPage> hungerPages = new ArrayList<IPage>();
        hungerPages.addAll(PageHelper.pagesForLongText("When playing with mods such as Spice of Life installed, managing your hunger can become very difficult. The Imbued Hunger spell will fill it up in one, for the cost of 500 Essence. This spell is quite powerful, yet without any food overhaul mods installed it can be less practical due to the high casting cost of the spell. To craft it, you will need four rotten flesh as well as the normal items required for Imbued tier spells (Four Imbued Sticks and an Imbued Bronze Ingot). The recipe is shown on the next page.", 390));
        hungerPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_hunger, "XYX", "YZY", "XYX", 'X', ModItems.imbued_stick, 'Y', Items.ROTTEN_FLESH, 'Z', ModItems.ingot_bronze_imbued)));
        hungerPages.addAll(PageHelper.pagesForLongText("Now that you have the spell, using it is very simple. Put it in either a spell bag or your offhand, hold a wand, like normal, and then hold down right-click. This will play the normal eating animation as if you were holding food in your main hand, and once the animation is complete you will be completely fed with 20 Hunger and 20 Saturation, which also provides a nice health regen for a short period after using the spell. ", 390));
        EntryItemStack hungerEntry = new EntryItemStack(hungerPages, "Imbued Food?", new ItemStack(ModItems.spell_card_hunger));
        entries.put(new ResourceLocation(keyBase + "hunger"), hungerEntry);
        
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