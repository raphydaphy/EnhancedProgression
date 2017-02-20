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
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CatagoryCrypticTransmutation
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = Reference.MOD_ID + "cryptic_transmutation_";

        List<IPage> transmutationPages = new ArrayList<IPage>();
        transmutationPages.addAll(PageHelper.pagesForLongText("You already have five functional spells, but they are quite expensive to use and may not be as powerful as might need at this point in your career. Once you travel to the nether and kill some blazes, you will be able to create the Cryptic Transmutation spell. This is the key to unlocking the next tier of spells and items, and any good wizard knows that this spell is crucial to progression. The crafting recipe is shown on the next page.", 390));
        transmutationPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_transmutation, "XYX", "YZY", "XYX", 'X', "stickWood", 'Y', Items.BLAZE_POWDER, 'Z', "ingotBronze")));
        transmutationPages.addAll(PageHelper.pagesForLongText("Once you have crafted the Cryptic Transmutation spell, you can begin to use it by equipping the spell in your offhand, holding a wand in your main hand a right-clicking a diamond block. For 1000 Essence, this will transmutate the block into an Altar, a highhly advanced structure used to create more complex spells. You can easily fill your wand with a single extraction from an Essence Vial, allowing you to create the Altar with minimal effort. Since you don't know how to use the Altar yet, you will have to wait a few chapters before it becomes useful. Ensure to store it in a safe place until then.", 390));
        transmutationPages.addAll(PageHelper.pagesForLongText("Before you go on to use your altar, however, there is another usage for the Cryptic Transmutation spell aside from creating Altars. Right-clicking on a bookshelf with the spell instead of a diamond block will consume 500 Essence, and create a Spell Bag. This item is also rather useless without knowledge on how to use it, and will be explained soon. For now, having both a Spell Bag and an Altar will be very useful, so keep them around for when you need them.", 390));
        EntryItemStack transmutationEntry = new EntryItemStack(transmutationPages, "Harsh Enlightenment", new ItemStack(ModItems.spell_card_transmutation));
        entries.put(new ResourceLocation(keyBase + "transmutation"), transmutationEntry);
        
        List<IPage> spellBagPages = new ArrayList<IPage>();
        spellBagPages.addAll(PageHelper.pagesForLongText("You may notice that your inventory is quickly filling up with spells. The Spell Bag that you created using the Cryptic Transmutation spell allows you to store all your spells in a single inventory slot, enabling you to easily switch between spells and free up space in your inventory. To add spells to the bag, simply place the bag in a crafting grid, and add any spell next to it. This is a shapeless crafting recipe, but you can only add a single spell at a time or they will all be erased. An example on the next page shows how you would add the Angelic Placement spell to a Spell Bag. Do note that due to limitations in magic, the Radiant Fireball and Vital Extraction spells cannot be used in a spell bag or they will yeild nothing.", 390));
        spellBagPages.add(new PageIRecipe(new ShapelessOreRecipe(ModItems.spell_card_transmutation, ModItems.spell_bag, ModItems.spell_card_placement_1)));
        spellBagPages.addAll(PageHelper.pagesForLongText("Now that you have a few spells stored in your Spell Bag, place the bag in your offhand and a wand in your main hand, much like how you equip regular spells. Press 'G' while holding your wand, and an interface will come up allowing you to select a spell to cast. When the spell is selected, it will show in the middle of the GUI, and you will be able to cast it using your wand as if you had the spell in your offhand like normal. As of now, you cannot remove spells from your bag, so be careful not to add any spells that you don't want to keep, as you can only store 10 per bag. You can, of course, simply create a new bag, but that wastes Essence.", 390));
        EntryItemStack spellBagEntry = new EntryItemStack(transmutationPages, "Spell Storage", new ItemStack(ModItems.spell_bag));
        entries.put(new ResourceLocation(keyBase + "spell_bag"), spellBagEntry);
        
        List<IPage> infusionPages = new ArrayList<IPage>();
        infusionPages.addAll(PageHelper.pagesForLongText("With the Altar that you made earlier, you can begin to craft more advanced spells and wands. Unfortunately, the Altar cannot be used by itself, and you will need 25 Quartz Blocks and 24 Obsidian to create the Multiblock Structure required to use it. An image showing the completed structure is shown on the next page.", 390));
        infusionPages.add(new PageImage(new ResourceLocation("vitality", "textures/gui/tier_2_altar.png")));
        infusionPages.addAll(PageHelper.pagesForLongText("With your Altar Multiblock complete, you should be able to walk up to the Altar Block in the centre and hold shift while right-clicking it. This will tell you the current Tier of the Altar, which should say Tier 2 due to the quartz and obsidian that you built around it. Using the strength of the Obsidian and the brittle edges of the Quartz, you have created a perfect balance which allowws you to craft powerful items.", 390));
        infusionPages.addAll(PageHelper.pagesForLongText("In order to craft items using your Altar, you must throw the crafting ingredients onto tha Altar. The first item that you will need to craft is Imbued Bronze. In order to create the ingots, begin by throwing four pieces of Bronze onto the Altar, followed by four Iron, and then four Gold. When you look directly at the Altar, you will see an interface showing you the current items added to the Altar, and if they are the correct items for crafting a certain item, it will show the output item in the centre. On the next page is an image of what you should see once you throw all twelve items for the Imbued Bronze onto the Altar.", 390));
        infusionPages.add(new PageImage(new ResourceLocation("vitality", "textures/gui/imbued_bronze_crafting.png")));
        infusionPages.addAll(PageHelper.pagesForLongText("Now that all the items are prepared for crafting, simply right-click the Altar Block twice. You will get a confirmation message checking that you want to craft the item, and then it will be crafted on the second right-click. If you accidently throw the wrong items into the Altar, simply break the altar and all the items will be dropped, allowing you to re-place the Altar and try again. However, if you got it right on the first try, you will now have a shiny Imbued Bronze Ingot. You will need a few of these to continue, so craft a bunch up before moving forwards. Note that you can craft them in bulk if you want, by throwing four Iron Blocks, four Gold Blocks and four Bronze Blocks onto the altar instead of ingots, then crafting them using the same method of right-clicking twice. You will need an additional mod installed in order to do this, as Vitality does not include any Bronze Blocks at the moment, so install a mod such as Tinkers Construct or Mekanism to add the block.", 390));
        EntryItemStack infusionEntry = new EntryItemStack(infusionPages, "Magic Infusion", new ItemStack(ModBlocks.altar));
        entries.put(new ResourceLocation(keyBase + "infusion"), infusionEntry);
        
        List<IPage> logPages = new ArrayList<IPage>();
        logPages.addAll(PageHelper.pagesForLongText("The other main item needed to craft the higher tier items is known as Imbued Logs. These are slightly more expensive to craft, requiring two Imbued Bronze Ingots, three Gold Nuggets and a single Log. Throw all of these items onto your Altar, then right-click twice to craft the Imbued Log. An image showing the Altar with the correct items in it is shown on the next page.", 390));
        logPages.add(new PageImage(new ResourceLocation("vitality", "textures/gui/imbued_log_crafting.png")));
        logPages.addAll(PageHelper.pagesForLongText("Unfortunately you cannot craft Imbued Logs in bulk, but they are quicker to craft than Imbued Bronze since they only require six items instead of 12. Craft a few of these, and then convert a few into Imbued Sticks, using the recipies shown on the next two pages.", 390));
        logPages.add(new PageIRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.imbued_plank, 4), "X", "X", 'X', ModBlocks.imbued_log)));
        logPages.add(new PageIRecipe(new ShapedOreRecipe(new ItemStack(ModItems.imbued_stick, 4), "X", "X", 'X', ModBlocks.imbued_plank)));
        logPages.addAll(PageHelper.pagesForLongText("Craft a couple Imbued Sticks in order to continue, as you will need both Imbued Sticks as well as Imbued Bronze Ingots to create more advanced magic items.", 390));
        EntryItemStack logEntry = new EntryItemStack(logPages, "Logs of Power", new ItemStack(ModBlocks.imbued_log));
        entries.put(new ResourceLocation(keyBase + "log"), logEntry);
        
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