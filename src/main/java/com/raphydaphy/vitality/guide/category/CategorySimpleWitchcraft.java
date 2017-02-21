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

public class CategorySimpleWitchcraft
{
    public static Map<ResourceLocation, EntryAbstract> buildCategory()
    {
        Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<ResourceLocation, EntryAbstract>();
        String keyBase = Reference.MOD_ID + "simple_witchcraft_";

        List<IPage> lanternPages = new ArrayList<IPage>();
        lanternPages.addAll(PageHelper.pagesForLongText("With some Essence in your wand, you can now cast a couple useful spells. The first spell that any magician should try is the Magic Lantern spell, used to summon torches at the minimal expense of five Essence. This primitive spell has the added ability of being able to be used without a wand, allowing you to cast the spell whenever you have an Essence Vial in your inventory with at least 5 Essence stored. To craft it, you will only need four coal, four sticks and a Bronze Ingot, as shown on the next page.", 390));
        lanternPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_lantern_1, "XYX", "YZY", "XYX", 'X', "stickWood", 'Y', "justALumpOfCoalPlzIgnore", 'Z', "ingotBronze")));
        lanternPages.addAll(PageHelper.pagesForLongText("Once you have crafted the Magic Lantern spell, you can proceed to use it by simply placing it in your offhand, holding a wand in your main hand and right-clicking any block. Provided that you have at least 5 Essence stored in your wand, this will place a torch in the world at that position, much like if you had have simply placed a torch normally. The benefits to using the spell are huge, however, as you don't have to carry around stacks of torches when going mining, and the Essence used to create the torches can be used for much more than just this, allowing you to save on Inventory space in more ways than one.", 390));
        EntryItemStack introEntry = new EntryItemStack(lanternPages, "The First Tiny Shimmer", new ItemStack(ModItems.spell_card_lantern_1));
        entries.put(new ResourceLocation(keyBase + "lantern"), introEntry);
        
        List<IPage> explosionPages = new ArrayList<IPage>();
        explosionPages.addAll(PageHelper.pagesForLongText("With only the Magic Lantern spell to show for all your effort, you may be wondering if collecting Essence was worth it. Luckily, the Contained Explosion spell is a far more powerful spell with untold power that you will surely want to use. Craft the spell using four gunpowder, four sticks and a bronze ingot to continue.", 390));
        explosionPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_explosion_1, "XYX", "YZY", "XYX", 'X', "stickWood", 'Y', Items.GUNPOWDER, 'Z', "ingotBronze")));
        explosionPages.addAll(PageHelper.pagesForLongText("With your shiny new Contained Explosion spell ready to use, you may be worried about destroying terrain, or even yourself, with the spell. Luckily, ever since the dawn of Magic, wizards have been perfecting this spell, causing it to now do no damage at all to terain or players. Although this limits you in combat options, it is overall for the best. To use the spell, equip it in your offhand much like you did with the Magic Lantern spell. Hold a wand in your main hand and simply right-click anywhere on the ground to summon a powerful explosion. For the cost of 50 Essence, the resulting explosion will deal 20 damage to any nearby mobs within a two block radius. Later in your wizarding career, you will be able to upgrade this spell to increase the damage and the range, but for now it should be more than good enough to crush your foes on sight.", 390));
        EntryItemStack explosionEntry = new EntryItemStack(explosionPages, "Better than TNT!", new ItemStack(ModItems.spell_card_explosion_1));
        entries.put(new ResourceLocation(keyBase + "explosion"), explosionEntry);
        
        List<IPage> fireballPages = new ArrayList<IPage>();
        fireballPages.addAll(PageHelper.pagesForLongText("Now that you have mastered the Contained Explosion spell, you may realise that it is lacking on a fundemental point - destruction. With no terrain damage, the Contained Explosion spell is uesless in many different applications. From griefing to PvP, you will at one point want to hurt things that aren't just mobs. To do that, you will need some fire charges, sticks and bronze ingots. Craft the Radiant Fireball spell using the recipe on the next page to continue.", 390));
        fireballPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_fireball_1, "XYX", "YZY", "XYX", 'X', "stickWood", 'Y', Items.FIRE_CHARGE, 'Z', "ingotBronze")));
        fireballPages.addAll(PageHelper.pagesForLongText("The Radiant Fireball spell is used much like every spell you have encountered so far. Simply put it in your offhand, hold a wand in your main hand and right-click. However, with this spell, you can hold down right-click to aim your fireball before releasing. Additionally, you can use the spell without looking directly at a block, by aiming it into the sky. Be careful when using this spell, because it will kill you if you aim it too close to you, and the fireball summoned does not always fly perfectly straight. However, due to these downsides, the spell has a very low casting cost of only 25 Essence, making it a very good option for early-game combat.", 390));
        EntryItemStack fireballEntry = new EntryItemStack(fireballPages, "Next Level Blasting", new ItemStack(ModItems.spell_card_fireball_1));
        entries.put(new ResourceLocation(keyBase + "fireball"), fireballEntry);
        
        List<IPage> fertilizationPages = new ArrayList<IPage>();
        fertilizationPages.addAll(PageHelper.pagesForLongText("While you are still very early on in your career, keeping yourself fed may be an issue. With the Renewed Fertilization spell, you can enjoy infinite bonemeal, which will keep all your crops and trees fully grown whenever you have enough essence. The spell is also quite cheap to craft, requiring just a few pieces of bonemeal as well as the normal sticks and bronze ingots.", 390));
        fertilizationPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_fertilization_1, "XYX", "YZY", "XYX", 'X', "stickWood", 'Y', "dyeWhite", 'Z', "ingotBronze")));
        fertilizationPages.addAll(PageHelper.pagesForLongText("Just like every other Basic Spell that you have used so far, place the spell in your offhand and your wand in your main hand, then simply walk up to a crop or sapling and right-click it. For 50 Essence, this spell grows the crop very quickly, and also works on crops that normally cannot be bonemealed such as sugarcane and crops from other mods. Later on, you will be able to upgrade this spell, like you can with most other spells, to decrease the cost of fertilization and also shorten the cooldown time from using the spell.", 390));
        EntryItemStack fertilizationEntry = new EntryItemStack(fertilizationPages, "Simple Necessities", new ItemStack(ModItems.spell_card_fertilization_1));
        entries.put(new ResourceLocation(keyBase + "fertilization"), fertilizationEntry);
        
        List<IPage> placementPages = new ArrayList<IPage>();
        placementPages.addAll(PageHelper.pagesForLongText("You probably remember Angel Blocks from the Extra Utilities mod. While useful, these blocks have the huge disadvantage of not only requiring obsidian to craft, but also being single use and therefore requiring you to dig them up before using them again. With your basic knowledge of Magic, you have the ability to create a spell that places either Cobblestone, Dirt of Oak Planks in the sky like an angel block, consuming nothing but Essence to use. With a base cost of 50 Essence per block for the Angelic Placement spell, this not only produces blocks out of pure Essence, but also aids you in building and climbing up to high places in your world. To craft it, you will need some sticks, bronze ingots and obsidian, but since the spell has unlimited uses once crafted it is not a very big disadvantage apart from preventing you from creating this spell until you have found diamonds. The crafting recipe is shown on the next page.", 390));
        placementPages.add(new PageIRecipe(new ShapedOreRecipe(ModItems.spell_card_placement_1, "XYX", "YZY", "XYX", 'X', "stickWood", 'Y', Blocks.OBSIDIAN, 'Z', "ingotBronze")));
        placementPages.addAll(PageHelper.pagesForLongText("With your new spell crafted, there are a few things to note that are different from most spells. Firstly, since you can choose to place either dirt, cobblestone or planks, you will need to select which one you want to use. Put the spell in your offhand and your wand in your main hand, like normal, then place one of those three blocks in the world. Hold shift and right-click the block in order to select it, allowing you to simply right-click in the air once a block is selected to place it. This does not consume any items from your inventory, which is a severe advantage over Angel Blocks.", 390));
        EntryItemStack placementEntry = new EntryItemStack(placementPages, "Like an Angel", new ItemStack(ModItems.spell_card_placement_1));
        entries.put(new ResourceLocation(keyBase + "placement"), placementEntry);
        
        List<IPage> multitoolPages = new ArrayList<IPage>();
        multitoolPages.addAll(PageHelper.pagesForLongText("With so many spells in your inventory, you might start to run out of space. An easy way to save on inventory space is using the Magic Multitool. This tool emulates a pickaxe, axe and a shovel all in a single tool, with a base durability of 5000. Additionally, this tool will repair using essence from any full Essence Vials in your inventory, at a rate of 10 Essence per durability point. This means that you can go mining, tree chopping or digging for very long periods of time with nothing but an essence vial and your Magic Multitool.", 390));
        multitoolPages.addAll(PageHelper.pagesForLongText("Crafting the Magic Multitool is very easy. Simply begin by ensuring that no spells are in your offhand, and throw a Wooden Pickaxe from Vanilla Minecraft onto the ground. Right-click the pickaxe with at least 1000 Essence stored in your wand. This will cause the pickaxe to transform into the Magic Multitool. From that point on, you will have no need for regular tools as the Magic Multitool has a mining level similar to Iron Tools, allowing you to mine nearly every type of block with ease. You can also upgrade this to an Imbued Multitool later on during your career.", 390));
        EntryItemStack multitoolEntry = new EntryItemStack(multitoolPages, "All in One!", new ItemStack(ModItems.magic_multitool));
        entries.put(new ResourceLocation(keyBase + "multitool"), multitoolEntry);
        
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