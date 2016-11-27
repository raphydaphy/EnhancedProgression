package com.raphydaphy.enhancedprogression.init;

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
		registry.addDescription(new ItemStack(ModItems.upgrade_template_basic), "Only the true master of magic can wield this wand. Crafted with the most expensive items in the mod, the Master Wand can store 100000 essence and is able to cast every spell known to man.");
		registry.addDescription(new ItemStack(ModItems.imbued_stick),
				"Imbued Sticks are a crafting regent item used for spell cards, higher tier wands and other Advanced tier Enhanced Progression tech.");
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
		registry.addDescription(new ItemStack(ModItems.spell_card_vital_extraction),
				"In order to cast any spells, you will need essence, which is initially collected from logs using this spell. Simply put this spell card in your offhand and rightclick on a tree while holding a wand and it will begin to fill with essence. Shift+rightclick with your wand to check how much you have ammased in your wand.");
		registry.addDescription(new ItemStack(ModItems.spell_card_lantern),
				"This simple spell can place a torch at the cost of 5 essence. Used by either placing the spell card in your offhand with a wand in your main hand, or by infusing it with a wand in an altar to add it to the wands inventory. Note that only Advanced+ wands can be infused with spells and Basic wands require the spell to be in the offhand always.");
		registry.addDescription(new ItemStack(ModItems.spell_card_explosion),
				"For the cost of 50 essence, this spell will create an explosion on the block you aim it at. Due to the essence imbued into the explosion, this blast will not cause any terrain damage and will not hurt the person who casts the spell.");
		registry.addDescription(new ItemStack(ModItems.spell_card_fireball), "For the cost of 25 essence, this spell summons a fireball that shoots off in the direction you are looking. Be careful around your house and other valuable things, because this will destroy anything that it touches!");
		registry.addDescription(new ItemStack(ModItems.spell_card_rapidfire),
				"For 100 essence, this spell will shoot arrows in a straight line from your cursor for one essence per arrow. With each arrow shot in a row, the cost incrases by one, so by the 50th arrow it would cost 50 essence. The arrows will continue to shoot until you stop holding rightclick or until you run out of essence, whichever happens first.");
		registry.addDescription(new ItemStack(ModItems.spell_card_hunger),
				"If you ever find yourself out of food and hungry, this spell will restore your hunger and saturation levels to full, but will do so at the expense of 500 essence.");
		registry.addDescription(new ItemStack(ModItems.spell_card_transmutation),
				"One of the first arts any good magician must learn is that of transmutation - turning objects into other objects. This spell, if used correctly, will turn a diamond block into an altar at the cost of 1000 essence.");
		registry.addDescription(new ItemStack(ModItems.spell_card_enhanced_extraction),
				"Once you have a tier 2 altar setup, you will want to be able to collect more essence than you can get from trees in order to cast the more powerful spells crafted with imbued items. The Enhanced Extraction spell, when placed in your offhand, allows you to extract essence from ores in your world. Simply rightclick with a wand while this spell is equipped and watch the magic happen.");
		registry.addDescription(new ItemStack(ModItems.spell_card_flight), "For 2500 essence, this spell grants you with the ability to fly until your next death, or until you use the spell again. Using the spell a second time once you are already able to fly will disable the ability until you use the spell again.");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
		// TODO Auto-generated method stub

	}

}
