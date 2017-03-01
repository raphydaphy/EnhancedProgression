package com.raphydaphy.vitality.recipe;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.util.NBTHelper;
import com.raphydaphy.vitality.util.registry.ModItems;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeWand implements IRecipe {
	private ItemStack resultItem;

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(InventoryCrafting inv, World worldIn) {
		this.resultItem = null;

		int cores = 0;
		int tips = 0;
		String coreType = "Unknown";
		String tipType1 = "Unknown";
		String tipType2 = "Unknown";

		for (int k1 = 0; k1 < inv.getSizeInventory(); ++k1) {
			ItemStack itemstack = inv.getStackInSlot(k1);

			if (itemstack != null) {
				if (itemstack.getItem() == ModItems.TIP_WOODEN) {
					tips++;
					if (tips == 1) {
						tipType1 = "Wooden";
					} else if (tips == 2) {
						tipType2 = "Wooden";
					}
				}

				if (itemstack.getItem() == ModItems.CORE_ANGELIC) {
					cores++;
					coreType = "Angelic";
				} else if (itemstack.getItem() == ModItems.CORE_ATMOSPHERIC) {
					cores++;
					coreType = "Atmospheric";
				} else if (itemstack.getItem() == ModItems.CORE_DEMONIC) {
					cores++;
					coreType = "Demonic";
				} else if (itemstack.getItem() == ModItems.CORE_ENERGETIC) {
					cores++;
					coreType = "Energetic";
				} else if (itemstack.getItem() == ModItems.CORE_EXOTIC) {
					cores++;
					coreType = "Exotic";
				}
			}
		}

		if (tips > 2 || tips < 2) {
			return false;
		} else if (cores > 1 || cores < 1) {
			return false;
		} else if (tipType1 != tipType2) {
			return false;
		}

		resultItem = new ItemStack(ModItems.WAND);
		resultItem.setStackDisplayName(tipType1 + " Tipped " + coreType + " Wand");
		resultItem.setTagCompound(new NBTTagCompound());
		resultItem.getTagCompound().setString("coreType", coreType);
		resultItem.getTagCompound().setString("tipType", tipType1);
		resultItem.getTagCompound().setInteger("essenceStored", 0);

		int maxEssence = 0;
		switch (tipType1) {
		case "Wooden":
			maxEssence = 1000;
			break;
		default:
			return false;
		}
		NBTHelper.setInt(resultItem, "maxEssence", maxEssence);
		return true;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Nullable
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return this.resultItem.copy();
	}

	/**
	 * Returns the size of the recipe area
	 */
	public int getRecipeSize() {
		return 10;
	}

	@Nullable
	public ItemStack getRecipeOutput() {
		return this.resultItem;
	}

	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

		for (int i = 0; i < aitemstack.length; ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);
			aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
		}

		return aitemstack;
	}
}
