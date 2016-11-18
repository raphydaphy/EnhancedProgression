package com.raphydaphy.enhancedprogression.recipe;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.raphydaphy.enhancedprogression.nbt.AltarRecipe;

import net.minecraft.item.ItemStack;

public final class ModRecipes 
{
	public static final List<AltarRecipe> altarRecipes = new ArrayList<AltarRecipe>();
	
	public static AltarRecipe registerAltarRecipe(ItemStack output,  Object... inputs) {
		Preconditions.checkArgument(inputs.length <= 16);
		AltarRecipe recipe = new AltarRecipe(output, inputs);
		altarRecipes.add(recipe);
		return recipe;
	}
	
}
