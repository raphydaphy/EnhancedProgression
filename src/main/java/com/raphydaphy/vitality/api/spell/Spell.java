package com.raphydaphy.vitality.api.spell;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;

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
	protected final boolean needsBlock;

	public Spell(String name, Essence[] reqEssence, Item icon, int id, int cost, int potency, int cooldown,
			boolean needsBlock) {
		this.name = name;
		this.icon = icon;
		this.id = id;
		this.cost = cost;
		this.potency = potency;
		this.cooldown = cooldown;
		this.reqEssence = reqEssence;
		this.needsBlock = needsBlock;
		spellMap.put(id, this);
	}

	public boolean canBeCast(ItemStack wand) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);
		return !needsBlock && isEssenceValid(pair.getKey().getCoreType())
				&& pair.getValue().getCostMultiplier() * cost <= WandHelper.getEssenceStored(wand);
	}

	/**
	 * This is the pre-cast spell event. This is fired automatically as part of
	 * the spell handlers. This is ONLY fired when a block is right clicked with
	 * the wand. Using a wand in the air bypasses this.
	 * 
	 * @param wand
	 *            The Wand ItemStack
	 * @param player
	 *            The player holding the Wand
	 * @param world
	 *            The World
	 * @param pos
	 *            The Pos of the Block being clicked
	 * @param hand
	 *            The current active player hand
	 * @param side
	 *            The current EnumFacing of the block being clicked
	 * @param hitX
	 *            The hitX of the Block (0.0-1.0) similar to AABB's
	 * @param hitY
	 *            hitX but a Y coord
	 * @param hitZ
	 *            hitX but a Z coord
	 * @return If onCastPre was successful. Always return false to NEVER allow
	 *         casting when clicked on a block.
	 */
	public abstract boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ);

	/**
	 * This is the cast spell event. This is fired after onCastPre returns true
	 * or onPlayerStoppedUsing is called. onPlayerStoppedUsing is always called
	 * once the wand is released from beind held after clicking the air,
	 * provided the necessary essence is there.
	 * 
	 * @param wand
	 *            The Wand ItemStack
	 * @param player
	 *            The player holding the Wand
	 * @param world
	 *            The World
	 * @param pos
	 *            The Pos of the Block being clicked
	 * @param hand
	 *            The current active player hand
	 * @param side
	 *            The current EnumFacing of the block being clicked (null if
	 *            onItemRightClick)
	 * @param hitX
	 *            The hitX of the Block (0.0-1.0) similar to AABB's (0 if
	 *            onItemRightClick)
	 * @param hitY
	 *            hitX but a Y coord (0 if onItemRightClick)
	 * @param hitZ
	 *            hitX but a Z coord (0 if onItemRightClick)
	 * @return If onCast was successful. Returning false will disable onCastPost
	 */
	public abstract boolean onCast(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ);

	/**
	 * This is the post-cast spell event. Do your essence removal here! This is
	 * always called immediately after onCast returns true, otherwise it is
	 * never called.
	 * 
	 * @param wand
	 *            The Wand ItemStack
	 * @param player
	 *            The player holding the Wand
	 * @param world
	 *            The World
	 * @param pos
	 *            The Pos of the Block being clicked
	 * @param hand
	 *            The current active player hand
	 * @param side
	 *            The current EnumFacing of the block being clicked (null if
	 *            onItemRightClick)
	 * @param hitX
	 *            The hitX of the Block (0.0-1.0) similar to AABB's (0 if
	 *            onItemRightClick)
	 * @param hitY
	 *            hitX but a Y coord (0 if onItemRightClick)
	 * @param hitZ
	 *            hitX but a Z coord (0 if onItemRightClick)
	 */
	public abstract void onCastPost(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ);

	public abstract boolean onCastTick(ItemStack wand, EntityPlayer player, int count);

	public abstract void onCastTickSuccess(ItemStack wand, EntityPlayer player, int count);

	public Item getIcon() {
		return icon;
	}

	public String getName() {
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

	public boolean getNeedsBlock() {
		return needsBlock;
	}

	public boolean isEssenceValid(Essence essence) {
		for (Essence essence2 : reqEssence) {
			 if(essence2 == essence) return true;
		}
		return false;
	}

}
