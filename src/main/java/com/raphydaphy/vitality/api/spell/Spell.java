package com.raphydaphy.vitality.api.spell;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.api.essence.Essence;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Spell {

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

	protected final Item icon;
	protected final int id;
	protected final int cost;
	protected final int potency;
	protected final int cooldown;
	protected final Essence[] reqEssence;
	protected final String name;

	public Spell(String name, Essence[] reqEssence, Item icon, int id, int cost, int potency, int cooldown) {
		System.out.println("CONSTRUCTING SPELL " + name + " WITH ITEM " + new ItemStack(icon).toString() + "; ID " + id + "; COST " + cost + "; POTENCY " + potency + "; COOLDOWN" + cooldown);
		this.name = name;
		this.icon = icon;
		this.id = id;
		this.cost = cost;
		this.potency = potency;
		this.cooldown = cooldown;
		this.reqEssence = reqEssence;
		System.out.println(new ItemStack(icon).toString());
		System.out.println(id);
		spellMap.put(id, this);
	}

	public abstract boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ);

	public abstract boolean onCast(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ);

	public abstract boolean onCastPost(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ);

	public Item getIcon() {
		return icon;
	}
	
	public String getName(){
		return name;
	}

	public int getId() {
		return id;
	}

	public int getCost() {
		return cost;
	}

	public int getPotency() {
		return potency;
	}

	public int getCooldown() {
		return cooldown;
	}

	@Nullable
	public Essence[] getReqEssence() {
		return reqEssence;
	}

	public boolean isEssenceValid(Essence essence) {
		for (Essence essence2 : reqEssence) {
			return essence2 == essence;
		}
		return false;
	}

}
