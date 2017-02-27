package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.init.Reference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item
{

	protected static String name;

	public ItemBase(String parName, int maxStack)
	{
		name = parName;
		setUnlocalizedName(name);
		setRegistryName(name);
		this.setCreativeTab(Reference.creativeTab);
		this.maxStackSize = maxStack;
	}
	
	public ItemBase(String parName, int maxStack, boolean noCreativeTab)
	{
		name = parName;
		setUnlocalizedName(name);
		setRegistryName(name);
		if (!noCreativeTab)
		{
			this.setCreativeTab(Reference.creativeTab);
		}
		this.maxStackSize = maxStack;
	}

	public void registerItemModel()
	{
		Vitality.proxy.registerItemRenderer(this, 0, name);
	}

	@Override
	public ItemBase setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	
	@SideOnly(Side.CLIENT)
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        return null;
    }
}