package com.raphydaphy.vitality.spell;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.registry.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellFireball extends Spell{

	public SpellFireball() {
		super(ModItems.SPELL_FIREBALL, 0, cost, potency, cooldown);
	
	}

	@Override
	public boolean onCastPre(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCast(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCastPost(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean isEssenceValid(Essence essence){
		return true;
	}
	
}
