package com.raphydaphy.vitality.achievement;

import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.init.Reference;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

public final class ModAchievements
{

	public static AchievementPage vitality_page;
	public static int page_index;

	public static Achievement craft_basic_wand;
	public static Achievement craft_vital_extraction;
	public static Achievement craft_magic_lantern;
	public static Achievement craft_contained_explosion;
	public static Achievement craft_radiant_fireball;
	public static Achievement craft_cryptic_transmutation;
	public static Achievement pickup_imbued_bronze;
	public static Achievement pickup_imbued_log;
	public static Achievement craft_advanced_wand;
	public static Achievement craft_enhanced_extraction;
	public static Achievement craft_rapidfire;
	public static Achievement craft_hunger;
	public static Achievement pickup_fluxed_ingot;
	public static Achievement pickup_fluxed_log;
	public static Achievement craft_master_wand;
	public static Achievement craft_elevated_momentum;
	public static Achievement craft_unabridged_immortality;
	
	public static void init()
	{
		craft_basic_wand = new AchievementMod(AchievementNames.CRAFT_BASIC_WAND, 0, 0, ModItems.basic_wand_copper,
				craft_basic_wand);
		
		craft_vital_extraction = new AchievementMod(AchievementNames.CRAFT_VITAL_EXTRACTION, -2, -2, ModItems.spell_card_vital_extraction,
				craft_basic_wand);
		
		craft_magic_lantern = new AchievementMod(AchievementNames.CRAFT_MAGIC_LANTERN, 2, -2, ModItems.spell_card_lantern,
				craft_basic_wand);
		
		craft_contained_explosion = new AchievementMod(AchievementNames.CRAFT_CONTAINED_EXPLOSION, -2, -4, ModItems.spell_card_explosion_1,
				craft_basic_wand);
		
		craft_radiant_fireball = new AchievementMod(AchievementNames.CRAFT_RADIANT_FIREBALL, 2, -4, ModItems.spell_card_fireball_1,
				craft_basic_wand);
		
		craft_cryptic_transmutation = new AchievementMod(AchievementNames.CRAFT_CRYPTIC_TRANSMUTATION, -2, 0, ModItems.spell_card_transmutation,
				craft_vital_extraction);
		
		pickup_imbued_bronze = new AchievementMod(AchievementNames.PICKUP_IMBUED_BRONZE, -2, 2, ModItems.ingot_bronze_imbued,
				craft_cryptic_transmutation);
		
		pickup_imbued_log = new AchievementMod(AchievementNames.PICKUP_IMBUED_LOG, 0, 2, ModBlocks.imbued_log,
				pickup_imbued_bronze);
		
		craft_advanced_wand = new AchievementMod(AchievementNames.CRAFT_ADVANCED_WAND, -2, 4, ModItems.advanced_wand,
				pickup_imbued_log);
		
		craft_enhanced_extraction = new AchievementMod(AchievementNames.CRAFT_ENHANCED_EXTRACTION, 2, 4, ModItems.spell_card_enhanced_extraction,
				pickup_imbued_log);
		
		craft_rapidfire = new AchievementMod(AchievementNames.CRAFT_RAPIDFIRE, 2, 6, ModItems.spell_card_rapidfire,
				pickup_imbued_log);
		
		craft_hunger = new AchievementMod(AchievementNames.CRAFT_HUNGER, -2, 6, ModItems.spell_card_hunger,
				pickup_imbued_log);
		
		pickup_fluxed_ingot = new AchievementMod(AchievementNames.PICKUP_FLUXED_INGOT, -4, 4, ModItems.ingot_fluxed,
				craft_advanced_wand).setSpecial();
		
		pickup_fluxed_log = new AchievementMod(AchievementNames.PICKUP_FLUXED_LOG, -6, 4, ModBlocks.fluxed_log,
				pickup_fluxed_ingot).setSpecial();
		
		craft_master_wand = new AchievementMod(AchievementNames.CRAFT_MASTER_WAND, -8, 6, ModItems.master_wand,
				pickup_fluxed_log).setSpecial();
		
		craft_elevated_momentum = new AchievementMod(AchievementNames.CRAFT_ELEVATED_MOMENTUM, -4, 6, ModItems.spell_card_flight,
				pickup_fluxed_log).setSpecial();
		
		craft_unabridged_immortality = new AchievementMod(AchievementNames.CRAFT_UNABRIDGED_IMMORTALITY, -6, 8, ModItems.spell_card_forcefield,
				pickup_fluxed_log).setSpecial();
		
		page_index = AchievementPage.getAchievementPages().size();
		vitality_page = new AchievementPage(Reference.NAME,
				AchievementMod.achievements.toArray(new Achievement[AchievementMod.achievements.size()]));
		AchievementPage.registerAchievementPage(vitality_page);

		MinecraftForge.EVENT_BUS.register(AchievementTriggerer.class);
	}

}