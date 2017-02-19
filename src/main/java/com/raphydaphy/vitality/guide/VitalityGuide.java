package com.raphydaphy.vitality.guide;

import java.awt.Color;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.raphydaphy.vitality.guide.category.CategorySimpleWitchcraft;
import com.raphydaphy.vitality.guide.category.CategoryVitalExtraction;
import com.raphydaphy.vitality.init.JEIPlugin;
import com.raphydaphy.vitality.init.ModItems;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.category.CategoryItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class VitalityGuide
{

	public static Book vitalityGuide = new Book();
	
	public static void initBook()
    {
        vitalityGuide.setTitle("guide.Vitality.title");
        vitalityGuide.setDisplayName("guide.Vitality.display");
        vitalityGuide.setWelcomeMessage("guide.Vitality.welcome");
        vitalityGuide.setAuthor("guide.Vitality.author");
        vitalityGuide.setRegistryName("VitalityGuide");
        vitalityGuide.setSpawnWithBook(true);
        
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			
			ModelLoader.setCustomModelResourceLocation(GuideAPI.guideBook, 5, new ModelResourceLocation(new ResourceLocation("vitality", "magicians_guidebook"), "inventory"));
			
			vitalityGuide.setMappingFunction(new Function<Book, Void>() {
			    @Nullable
			    @Override
			    public Void apply(@Nullable Book input) {
			        int bookMeta = ((FMLControlledNamespacedRegistry<Book>) GuideAPI.BOOKS).getId(input);
			        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(GuideAPI.guideBook, bookMeta, new ModelResourceLocation("vitality:magicians_guidebook", "inventory"));
			        return null;
			    }
			});
		}
    }

    public static void initCategories()
    {
    	vitalityGuide.addCategory(new CategoryItemStack(CategoryVitalExtraction.buildCategory(), "guide.Vitality.category.vital_extraction", new ItemStack(ModItems.basic_wand_copper)));
        vitalityGuide.addCategory(new CategoryItemStack(CategorySimpleWitchcraft.buildCategory(), "guide.Vitality.category.simple_witchcraft", new ItemStack(ModItems.spell_card_lantern_1)));
    }
    
    public static void handleBookRecipe(boolean add)
    {
        if (Loader.isModLoaded("JEI")) 
        {
        	if (JEIPlugin.jeiRuntime == null)
        	{
                return;
        	}
            if (add)
            {
                JEIPlugin.jeiRuntime.getRecipeRegistry().addRecipe(CompatGuideAPI.guideRecipe);
            }
            else
            {
                //JEIPlugin.jeiRuntime.getRecipeRegistry().removeRecipe(CompatGuideAPI.guideRecipe);
            }
        }
    }
}