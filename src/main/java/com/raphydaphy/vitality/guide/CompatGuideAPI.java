package com.raphydaphy.vitality.guide;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CompatGuideAPI
{
    static IRecipe guideRecipe = null;
    private static boolean worldFlag;

    public void loadMapping()
    {
        if (!worldFlag) 
        {
            GameRegistry.addRecipe(guideRecipe);
            VitalityGuide.handleBookRecipe(true);
            worldFlag = true;
        } 
        else 
        {
            CraftingManager.getInstance().getRecipeList().remove(guideRecipe);
            VitalityGuide.handleBookRecipe(false);
            worldFlag = false;
        }
    }

    public String getModId()
    {
        return "guideapi";
    }

    public boolean enableCompat()
    {
        return true;
    }
}