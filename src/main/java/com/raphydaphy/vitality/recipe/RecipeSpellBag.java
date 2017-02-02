package com.raphydaphy.vitality.recipe;

import com.raphydaphy.vitality.init.ModItems;

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
		if (spell.getItem() == ModItems.spell_card_vital_extraction)
		{
			return 800;
		}
		else if (spell.getItem() == ModItems.spell_card_lantern)
		{
			return 810;
		}
		else if (spell.getItem() == ModItems.spell_card_explosion_1)
		{
			return 820;
		}
		else if (spell.getItem() == ModItems.spell_card_explosion_2)
		{
			return 821;
		}
		else if (spell.getItem() == ModItems.spell_card_explosion_3)
		{
			return 822;
		}
		else if (spell.getItem() == ModItems.spell_card_fireball)
		{
			return 830;
		}
		else if (spell.getItem() == ModItems.spell_card_rapidfire)
		{
			return 840;
		}
		else if (spell.getItem() == ModItems.spell_card_transmutation)
		{
			return 85;
		}
		else if (spell.getItem() == ModItems.spell_card_hunger)
		{
			return 860;
		}
		else if (spell.getItem() == ModItems.spell_card_enhanced_extraction)
		{
			return 870;
		}
		else if (spell.getItem() == ModItems.spell_card_flight)
		{
			return 880;
		}
		else if (spell.getItem() == ModItems.spell_card_forcefield)
		{
			return 890;
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
            		for(int i=0; i < curSpells.length; i++)
            		{
            		    if(curSpells[i] == 0)
            		    {
            		    	curSpells[i] = getSpellID(itemstackSpell);
            		    	break;
            		    }
            		}
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
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() 
	{
		return this.resultItem;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) 
	{
		 ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

	        for (int i = 0; i < aitemstack.length; ++i)
	        {
	            ItemStack itemstack = inv.getStackInSlot(i);
	            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
	        }

	        return aitemstack;
	}

}
