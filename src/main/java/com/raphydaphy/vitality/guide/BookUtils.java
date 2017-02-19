package com.raphydaphy.vitality.guide;

import amerifrance.guideapi.page.PageIRecipe;
import net.minecraft.item.crafting.IRecipe;

public class BookUtils
{
    public static PageIRecipe getPageForRecipe(IRecipe recipe)
    {
        return new PageIRecipe(recipe);
    }
}