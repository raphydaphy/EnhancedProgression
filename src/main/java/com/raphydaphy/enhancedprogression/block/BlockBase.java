package com.raphydaphy.enhancedprogression.block;

import com.raphydaphy.enhancedprogression.EnhancedProgression;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block {

	protected String name;
	public IProperty[] getNonRenderingProperties() {
		return null;
	}
	public Class<? extends ItemBlock> getItemClass() {
		return null;
	}
	
	public String getStateName(IBlockState state) {
		return null;
	}
	
	public IProperty[] getPresetProperties() {
		return null;
	}
	 
	public BlockBase(Material material, String name) {
		super(material);

		this.name = name;

		setUnlocalizedName(name);
		setRegistryName(name);
	}

	public void registerItemModel(ItemBlock itemBlock) {
		EnhancedProgression.proxy.registerItemRenderer(itemBlock, 0, name);
	}

	@Override
	public BlockBase setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

}