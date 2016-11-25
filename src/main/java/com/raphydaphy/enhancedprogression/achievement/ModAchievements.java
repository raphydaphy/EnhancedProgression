package com.raphydaphy.enhancedprogression.achievement;

import com.raphydaphy.enhancedprogression.init.ModBlocks;
import com.raphydaphy.enhancedprogression.init.ModItems;
import com.raphydaphy.enhancedprogression.init.Reference;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

public final class ModAchievements
{

	public static AchievementPage enhanced_progression_page;
	public static int page_index;

	public static Achievement craft_basic_wand;
	public static Achievement use_altar;

	public static void init()
	{
		craft_basic_wand = new AchievementMod(AchievementNames.CRAFT_BASIC_WAND, 0, 0, ModItems.basic_wand_copper,
				craft_basic_wand);
		use_altar = new AchievementMod(AchievementNames.USE_ALTAR, 1, 5, ModBlocks.altar, use_altar);

		page_index = AchievementPage.getAchievementPages().size();
		enhanced_progression_page = new AchievementPage(Reference.NAME,
				AchievementMod.achievements.toArray(new Achievement[AchievementMod.achievements.size()]));
		AchievementPage.registerAchievementPage(enhanced_progression_page);

		MinecraftForge.EVENT_BUS.register(AchievementTriggerer.class);
	}

}