package com.raphydaphy.vitality.init;

import com.raphydaphy.vitality.guide.VitalityGuide;

import amerifrance.guideapi.api.GuideAPI;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin
{
	public static IJeiRuntime jeiRuntime;
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
		registry.addDescription(GuideAPI.getStackFromBook(VitalityGuide.vitalityGuide),"This book teaches you everything you need to know about Vitality, and more. To craft it, first you will need a Basic Wand. Make either a Tin or Copper version of a Basic Wand, then throw a regular Book on the ground and right-click it with the wand. Make sure you have no items in your offhand when doing so. This will cause the book to turn into the Magicians Guidebook, which you can then open like a normal book to learn about Vitality. Good luck :)");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
		// TODO Auto-generated method stub

	}

}
