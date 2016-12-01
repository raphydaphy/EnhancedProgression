package com.raphydaphy.enhancedprogression.recipe;

import com.raphydaphy.enhancedprogression.init.ModItems;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeSpellBag implements IRecipe
{
	private ItemStack resultItem;
	
	public int getSpellID(ItemStack spell)
	{
		switch(spell.getUnlocalizedName())
		{
			case "spell_card_vital_extraction": return 80;
			case "spell_card_lantern": return 81;
			case "spell_card_explosion": return 82;
			case "spell_card_fireball": return 83;
			case "spell_card_rapidfire": return 84;
			case "spell_card_transmutation": return 85;
			case "spell_card_hunger": return 86;
			case "spell_card_enhanced_extraction": return 87;
			case "spell_card_flight": return 88;
			case "spell_card_forcefield": return 89;
		}
		return 0;
	}
	
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) 
	{
		boolean foundValidItem = false;
		for (int k1 = 0; k1 < inv.getSizeInventory(); ++k1)
        {
            ItemStack itemstack = inv.getStackInSlot(k1);

            if (itemstack != null)
            {
                if (itemstack.getItem() == ModItems.spell_bag)
                {
                	resultItem = itemstack.copy();
                	if (!resultItem.hasTagCompound())
                	{
                		resultItem.setTagCompound(new NBTTagCompound());
                	}
                	foundValidItem = true;
                }
            }
        }
		if (!foundValidItem)
		{
			return false;
		}
		
		for (int k1 = 0; k1 < inv.getSizeInventory(); ++k1)
        {
            ItemStack itemstackSpell = inv.getStackInSlot(k1);

            if (itemstackSpell != null)
            {
                if (getSpellID(itemstackSpell) != 0)
                {
                	if (resultItem.getTagCompound().getIntArray("spells").length == 0)
            		{
            			resultItem.getTagCompound().setIntArray("spells", new int[10]);
            		}
            		
            		for (int curSpell : resultItem.getTagCompound().getIntArray("spells"))
            		{
            			if (curSpell == getSpellID(itemstackSpell))
            			{
            				return false;
            			}
            		}
            		int[] curSpells = resultItem.getTagCompound().getIntArray("spells");
            		curSpells[curSpells.length + 1] = getSpellID(itemstackSpell);
            		resultItem.getTagCompound().setIntArray("spells", curSpells);
            		return true;
                }
            }
        }
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) 
	{
		return this.resultItem.copy();
	}

	@Override
	public int getRecipeSize() 
	{
		return 0;
	}

	@Override
	public ItemStack getRecipeOutput() 
	{
		return this.resultItem;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) 
	{
		return null;
	}

}
