package com.raphydaphy.enhancedprogression.nbt;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

public class AltarRecipe {

	private final ItemStack output;
	private final ImmutableList<Object> inputs;
	private final int altarTier;

	public AltarRecipe(int pAltarTier, ItemStack output, Object... inputs) 
	{
		altarTier = pAltarTier;
		this.output = output;

		ImmutableList.Builder<Object> inputsToSet = ImmutableList.builder();
		for(Object obj : inputs) 
		{
			if(obj instanceof String || obj instanceof ItemStack)
				inputsToSet.add(obj);
			else throw new IllegalArgumentException("Invalid input");
		}

		this.inputs = inputsToSet.build();
	}

	public boolean matches(IItemHandler inv) {
		List<Object> inputsMissing = new ArrayList<Object>(inputs);

		for(int i = 0; i < inv.getSlots(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null)
				break;

			int stackIndex = -1, oredictIndex = -1;

			for(int j = 0; j < inputsMissing.size(); j++) 
			{
				Object input = inputsMissing.get(j);
				if(input instanceof String) {
					List<ItemStack> validStacks = OreDictionary.getOres((String) input);
					boolean found = false;
					for(ItemStack ostack : validStacks) 
					{
						ItemStack cstack = ostack.copy();
						if(cstack.getItemDamage() == Short.MAX_VALUE)
							cstack.setItemDamage(stack.getItemDamage());

						if(stack.isItemEqual(cstack)) {
							oredictIndex = j;
							found = true;
							break;
						}
					}


					if(found)
						break;
				} else if(input instanceof ItemStack && simpleAreStacksEqual((ItemStack) input, stack)) {
					stackIndex = j;
					break;
				}
			}

			if(stackIndex != -1)
				inputsMissing.remove(stackIndex);
			else if(oredictIndex != -1)
				inputsMissing.remove(oredictIndex);
			else return false;
		}

		return inputsMissing.isEmpty();
	}

	private boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) 
	{
		return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
	}

	public List<Object> getInputs() 
	{
		return inputs;
	}

	public ItemStack getOutput() 
	{
		return output;
	}
	
	public int getAltarTier()
	{
		return altarTier;
	}

}