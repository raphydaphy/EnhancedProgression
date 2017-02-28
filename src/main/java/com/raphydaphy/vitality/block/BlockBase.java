package com.raphydaphy.vitality.block;

import com.raphydaphy.vitality.init.Reference;
import com.raphydaphy.vitality.util.shadows.registry.IRegisterable;
import com.raphydaphy.vitality.util.shadows.registry.RegistryHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockBase extends Block implements IRegisterable{

	/*
	 * Constructor for BlockBase, accepts a Material and String material is the
	 * type of block, such as ROCK or WOOD name is the unlocalized name of the
	 * block, such as tin_ore
	 */
	public BlockBase(Material material, String name, boolean registerTab) {
		super(material);
		if (registerTab)
			this.setCreativeTab(Reference.creativeTab);
		setUnlocalizedName(Reference.MOD_ID + "." + name);
		setRegistryName(name);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

	public BlockBase(Material material, String name) {
		this(material, name, true);
	}

	@Override
	public void registerModels() {
		RegistryHelper.setModelLoc(this);
	}

}