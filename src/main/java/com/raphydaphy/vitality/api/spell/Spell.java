package com.raphydaphy.vitality.api.spell;

import java.util.HashMap;
import java.util.Map;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.registry.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Spell {
	//ILLUMINATION("Illumination", 0, ModItems.SPELL_ILLUMINATION), FIREBALL("Fireball", 1, ModItems.SPELL_FIREBALL),;

	/**
	 * The big ol' list of spells.
	 */
	public static Map<Integer, Spell> spellMap = new HashMap<Integer, Spell>();

	/**
	 * Points to the currently selected spell for any wand ItemStack
	 */
	public static final String ACTIVE_KEY = "active_spell";

	/**
	 * This is the key that is used in spell bags to store the array of spells
	 * stored within the bag.
	 */
	public static final String ARRAY_KEY = "stored_spells";

	private final Item icon;
	private final int id;
	private final int cost;
	private final int potency;
	private final int cooldown;
	private final Essence[] reqEssence;
	
	public Spell(Essence[] reqEssence, Item icon, int id, int cost, int potency, int cooldown){
		this.icon = icon;
		this.id = id;
		this.cost = cost;
		this.potency = potency;
		this.cooldown = cooldown;
		this.reqEssence = reqEssence;
		spellMap.put(id, this);
	}
	
	public abstract boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ);
	
	public abstract boolean onCast(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ);
	
	public abstract boolean onCastPost(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ);
	
	public Item getIcon(){
		return icon;
	}
	
	public int getId(){
		return id;
	}
	
	public int getCost(){
		return cost;
	}
	
	public int getPotency(){
		return potency;
	}
	
	public int getCooldown(){
		return cooldown;
	}
	
	public Essence[] getReqEssence(){
		return reqEssence;
	}
	
	public boolean isEssenceValid(Essence essence){
		for(Essence essence2 : reqEssence){
			return essence2 == essence;
		}
		return false;
	}
	

}
