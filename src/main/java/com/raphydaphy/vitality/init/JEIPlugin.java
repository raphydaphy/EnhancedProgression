package com.raphydaphy.vitality.init;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin
{

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void register(IModRegistry registry)
	{
		registry.addDescription(new ItemStack(ModBlocks.altar),
				"Used to craft Imbued items, an altar is a multiblock structure centered around the altar block itself. To obtain an altar in survival, rightclick a diamond block with a wand equipped with a Cryptic Transmutation spell. This will consume 1000 essence and turn the diamond block into an altar.");
		registry.addDescription(new ItemStack(ModBlocks.imbued_log),
				"Crafted with three gold nuggets, one log and two imbued bronze ingots in a tier 2+ altar, Imbued Logs are used for higher tier crafting recipes.");
		registry.addDescription(new ItemStack(ModItems.ingot_bronze_imbued),
				"Crafted with four gold, four bronze and four iron ingots in an altar of any tier, Imbued Bronze Ingots are the first item craftable in an altar and are used for higher tier altar and crafting recipes.");
		registry.addDescription(new ItemStack(ModItems.basic_wand_tin),
				"This simple wand can be used to perform basic spells and activate altar crafting. To fill the wand with essence, right-click on a tree with a Vital Extraction spell in your offhand, and wait until the entire tree turns into dead logs. Shift+rightclick to check your current essence level, and rightclick on the ground with any spell card in your offhand to use it.");
		registry.addDescription(new ItemStack(ModItems.basic_wand_copper),
				"This simple wand can be used to perform basic spells and activate altar crafting. To fill the wand with essence, right-click on a tree with a Vital Extraction spell in your offhand, and wait until the entire tree turns into dead logs. Shift+rightclick to check your current essence level, and rightclick on the ground with any spell card in your offhand to use it.");
		registry.addDescription(new ItemStack(ModItems.advanced_wand),
				"By infusing your wand with Imbued Bronze, you unlock more power and capacity. The Advanced Wand cannot break when extracting essence, and can store 10000 essence instead of a mere 1000 in the Basic Wand.");
		registry.addDescription(new ItemStack(ModItems.master_wand), "Only the true master of magic can wield this wand. Crafted with the most expensive items in the mod, the Master Wand can store 100000 essence and is able to cast every spell known to man.");
		registry.addDescription(new ItemStack(ModItems.imbued_stick),
				"Imbued Sticks are a crafting regent item used for spell cards, higher tier wands and other Advanced tier Vitality tech.");
		registry.addDescription(new ItemStack(ModItems.ingot_bronze),
				"Bronze ingots are obtained in a mere crafting table and used to craft some basic items used to improve your bas");
		registry.addDescription(new ItemStack(ModItems.ingot_copper),
				"The most basic metal. Used to craft bronze and basic wands.");
		registry.addDescription(new ItemStack(ModItems.ingot_tin),
				"A simple metal used to craft bronze and basic wands.");
		registry.addDescription(new ItemStack(ModBlocks.totem_bottom), "Unimplemented.");
		registry.addDescription(new ItemStack(ModBlocks.totem_top), "Unimplemented.");
		registry.addDescription(new ItemStack(ModBlocks.totem_middle), "Unimplemented.");
		registry.addDescription(new ItemStack(ModItems.upgrade_template_basic), "Unimplemented.");
		registry.addDescription(new ItemStack(ModItems.ingot_fluxed), "This powerful alloy is created in a Tier 3 altar using four nether stars, four imbued bronze ingots and four diamonds. Used to craft the most highest tier items in the mod.");
		registry.addDescription(new ItemStack(ModBlocks.fluxed_plank), "Crafted in a regular crafting table using fluxed logs, this powerful item is among the most valuable items in the mod.");
		registry.addDescription(new ItemStack(ModBlocks.fluxed_log), "A powerful item, crafted in a tier 3 altar using an imbued log, two fluxed ingots and three diamonds. Used to craft fluxed planks.");
		registry.addDescription(new ItemStack(ModItems.fluxed_stick), "This powerful item is used to craft the Master Wand, and other high tier spells and items. Crafted with two fluxed logs in a regular crafting table.");
		registry.addDescription(new ItemStack(ModItems.spell_card_vital_extraction),
				"In order to cast any spells, you will need essence, which is initially collected from logs using this spell. Simply put this spell card in your offhand and rightclick on a tree while holding a wand and it will begin to fill with essence. Shift+rightclick with your wand to check how much you have ammased in your wand.");
		registry.addDescription(new ItemStack(ModItems.spell_card_lantern),
				"This simple spell can place a torch at the cost of 5 essence. Used by either placing the spell card in your offhand with a wand in your main hand, or by infusing it with a wand in an altar to add it to the wands inventory. Note that only Advanced+ wands can be infused with spells and Basic wands require the spell to be in the offhand always.");
		registry.addDescription(new ItemStack(ModItems.spell_card_explosion_1),
				"For the cost of 50 essence, this spell will create an explosion on the block you aim it at. Due to the essence imbued into the explosion, this blast will not cause any terrain damage and will not hurt the person who casts the spell.");
		registry.addDescription(new ItemStack(ModItems.spell_card_explosion_2),
				"By imbuing the spell with an enhanced bronze alloy, the spell is now far more powerful at the cost of an additionall 30 essence per use, bringing the total cost of casting the spell to 80 essence.");
		registry.addDescription(new ItemStack(ModItems.spell_card_explosion_2),
				"With sixteen regular contained explosion spells infused into one, this perfect blend of power and death will kill enemies in a huge radius for 250 essence.");
		registry.addDescription(new ItemStack(ModItems.spell_card_fireball_1), "For the cost of 25 essence, this spell summons a fireball that shoots off in the direction you are looking. Be careful around your house and other valuable things, because this will destroy anything that it touches!");
		registry.addDescription(new ItemStack(ModItems.spell_card_fireball_2), "For 100 essence, a giant fireball will be summoned and destroy blocks and entities in a large radius around the impact point. Be careful, because the nature of the spell is imprecice, and it can easily shoot far further than you expected, or far closer, blowing yourself up.");
		registry.addDescription(new ItemStack(ModItems.spell_card_fireball_3), "For 300 essence, a fireball of pure destruction will be summoned to destroy everything in a huge radius. Be careful not to use this too close to your base, and note that it will drain your stored essencd very quickly.");
		registry.addDescription(new ItemStack(ModItems.spell_card_rapidfire),
				"For 100 essence, this spell will shoot arrows in a straight line from your cursor for one essence per arrow. With each arrow shot in a row, the cost incrases by one, so by the 50th arrow it would cost 50 essence. The arrows will continue to shoot until you stop holding rightclick or until you run out of essence, whichever happens first.");
		registry.addDescription(new ItemStack(ModItems.spell_card_hunger),
				"If you ever find yourself out of food and hungry, this spell will restore your hunger and saturation levels to full, but will do so at the expense of 500 essence.");
		registry.addDescription(new ItemStack(ModItems.spell_card_transmutation),
				"One of the first arts any good magician must learn is that of transmutation - turning objects into other objects. This spell, if used correctly, will turn a diamond block into an altar at the cost of 1000 essence. This can also be used to create spell bags from bookshelfs for 500 essence.");
		registry.addDescription(new ItemStack(ModItems.spell_card_enhanced_extraction),
				"Once you have a tier 2 altar setup, you will want to be able to collect more essence than you can get from trees in order to cast the more powerful spells crafted with imbued items. The Enhanced Extraction spell, when placed in your offhand, allows you to extract essence from ores in your world. Simply rightclick with a wand while this spell is equipped and watch the magic happen.");
		registry.addDescription(new ItemStack(ModItems.spell_card_flight), "For 2500 essence, this spell grants you with the ability to fly until your next death, or until you use the spell again. Using the spell a second time once you are already able to fly will disable the ability until you use the spell again.");
		registry.addDescription(new ItemStack(ModItems.spell_bag), "Crafted by right-clicking a bookshelf with a wand while the Cryptic Transmutation spell is active, this item can be crafted together with any spell in a normal crafting table to add it to the bag. Then just shift+rightclick while holding the bag to switch the active spell, and put it in your offhand to use it.");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
		// TODO Auto-generated method stub

	}

}
