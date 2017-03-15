package com.raphydaphy.vitality.api.essence;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.item.ItemVial;
import com.raphydaphy.vitality.item.ItemVial.VialQuality;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

public class MiscEssence {
	public static Map<String, Essence> locator = new HashMap<String, Essence>();
	public static Map<VialQuality, Map<Essence, ItemVial>> vialMap = new HashMap<VialQuality, Map<Essence, ItemVial>>();

	/**
	 * 
	 * @param te
	 *            A valid container.
	 * @param vial
	 *            An item stack that has an ItemVial as its item.
	 * @param toAdd
	 *            The int value to add to that container.
	 * @return If the operation was successful.
	 */
	public static boolean fillContainerFromVial(IEssenceContainer te, ItemStack vial, int toAdd) {
		int currentInVial = ItemVial.getCurrentStored(vial);
		if (currentInVial - toAdd >= 0 && te.addEssence(((ItemVial) vial.getItem()).getVialType(), toAdd)) {
			vial.getTagCompound().setInteger(Essence.KEY, currentInVial - toAdd);
			return true;
		}
		return false;

	}

	/**
	 * @param player
	 *            The player holding the vial.
	 * @param hand
	 *            The current active hand that the vial is in.
	 * @param te
	 *            A valid container.
	 * @param vial
	 *            An item stack that has an ItemVial as its item.
	 * @param toRemove
	 *            The int value to remove from that container.
	 * @return If the operation was successful.
	 */
	public static boolean fillVialFromContainer(EntityPlayer player, EnumHand hand, IEssenceContainer te,
			ItemStack vial, int toRemove) {
		Essence typeContainer = te.getEssenceType();
		if (typeContainer == null)
			return false;
		ItemVial vialI = (ItemVial) vial.getItem();
		Essence typeVial = vialI.getVialType();
		int currentInVial = 0;
		if (vialI.hasType())
			currentInVial = ItemVial.getCurrentStored(vial);
		if (typeContainer == typeVial && (currentInVial + toRemove) <= vialI.getMaxStorage()
				&& te.subtractEssence(toRemove)) {
			vial.getTagCompound().setInteger(Essence.KEY, currentInVial + toRemove);
			return true;
		} else if (typeVial != null && typeVial != typeContainer)
			return false;
		else if (typeVial == null && te.subtractEssence(toRemove)) {
			ItemStack newVial = new ItemStack(MiscEssence.vialMap.get(vialI.getQuality()).get(typeContainer));
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger(Essence.KEY, toRemove);
			newVial.setTagCompound(tag);
			player.setHeldItem(hand, newVial);
			return true;
		}

		return false;

	}

	/**
	 * Adds essence to the essence container given as the first paraeter
	 * 
	 * @param stack
	 *            The essence vial ItemStack that essence should be added to
	 * @param toAdd
	 *            How much essence should be added to the vial
	 * @param shouldBind
	 *            If the essence should bind to the players soul on completion
	 * @param entity
	 *            The player that the essence will be bound to. Must be
	 *            EntityPlayer or it will return false.
	 * @param type
	 *            The type of essence that should be put into the vial
	 * @param slot
	 *            What slot in the players inventory is the vial in? Set to 0 if
	 *            you are filling an already full vial
	 * @return Always returns true unless the essence vial was full or not a
	 *         valid vial item
	 */
	public static boolean addEssence(ItemStack stack, int toAdd, boolean shouldBind, EntityPlayer player, Essence type,
			@Nullable /* set to 0 for null */ int slot) {
		// if the essence should bind to the players soul
		if (shouldBind) {
			// bind the essence to the players soul-bound essence
			player.getEntityData().setInteger(type.getMultiKey(),
					player.getEntityData().getInteger(type.getMultiKey()) + toAdd);
		}
		if (stack.getItem() instanceof ItemVial) {
			ItemVial vial = (ItemVial) stack.getItem();
			// if the vial has some essence in it
			if (vial.hasType() && vial.getVialType() == type && stack.hasTagCompound()) {
				// if the vial isnt already full
				if (stack.getTagCompound().getInteger(Essence.KEY) <= vial.getMaxStorage()) {
					// add the amount of essence specified as toAdd to the
					// vial
					stack.getTagCompound().setInteger(Essence.KEY,
							stack.getTagCompound().getInteger(Essence.KEY) + toAdd);
					// the essence was succesfully added, so we return true
					return true;
				}
			} else {
				// create a new itemstack of the vial type needed for the
				// essence to be contained
				ItemStack vialStack = new ItemStack(essenceToVial(type, vial.getQuality()));
				// give the stack a tag compound so that it can store NBT
				vialStack.setTagCompound(new NBTTagCompound());
				// set the amount of essence in the vial to the amount
				// required to add
				vialStack.getTagCompound().setInteger(Essence.KEY, toAdd);
				// add the vial to the players inventory, overriding the
				// empty one
				player.inventory.setInventorySlotContents(slot, vialStack);
				// it worked, so return true :-)
				return true;
			}
		}
		return false;
	}

	/**
	 * @param entity
	 *            The player whose inventory you want to search.
	 * @return An object that contains data about the SlotID and the ItemStack
	 *         in that Slot. (SlottedStack)
	 */
	public static SlottedStack findValidVial(EntityPlayer player, Essence type, int toBeAdded) {
		// loop through the players inventory
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			// get the currently selected item in the players inventory
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			// check that the current stack isnt null to prevent
			// check if the stack actually is a vial
			if (stackAt != null && stackAt.getItem() instanceof ItemVial) {
				// check that the vial already has some essence
				if (((ItemVial) stackAt.getItem()).hasType() && ((ItemVial) stackAt.getItem()).getVialType() == type
						&& canBeAdded(stackAt, toBeAdded)) {
					return new SlottedStack(i, stackAt);
				} else if (!((ItemVial) stackAt.getItem()).hasType()) {
					return new SlottedStack(i, stackAt);
				}
			}
		}
		return null;
	}

	/**
	 * Checks if adding this number of essence would break the container
	 * (Overflow/Underflow)
	 * 
	 * @param stack
	 *            A Non-Empty Vial Stack.
	 * @param toBeAdded
	 *            Int value of how much essence to add. Can be negative.
	 * @return If adding this much essence would not break the container.
	 */

	public static boolean canBeAdded(ItemStack stack, int toBeAdded) {
		int k = ItemVial.getCurrentStored(stack) + toBeAdded;
		return (k >= 0) && (k <= ((ItemVial) stack.getItem()).getMaxStorage());
	}

	/**
	 * Tries to fill an Essence Vial with an amount of Essence. The difference
	 * between this and addEssence is this finds the vial from the players
	 * inventory, and addEssence takes an ItemStack argument.
	 * 
	 * @param type
	 *            The type of essence that should be filled into the vial
	 * @param toAdd
	 *            How much essence should be added to the vial
	 * @param shouldBind
	 *            If the essence should be bound to your soul network
	 * @param entity
	 *            The player who the essence should be bound to.
	 * @return Returns true if the vial was successfully filled with Essence
	 */
	public static boolean fillVial(Essence type, int toAdd, boolean shouldBind, EntityPlayer player) {
		// Check if the entity is actually a player
		// find a vial from the players inventory
		SlottedStack slotstack = findValidVial(player, type, toAdd);
		if (slotstack != null) {
			// check that a vial was actually found
			if (slotstack.getStack().getItem() instanceof ItemVial) {
				// try to fill the vial with essence
				return addEssence(slotstack.getStack(), toAdd, shouldBind, player, type, slotstack.getSlotID());

			}
		}

		return false;
	}

	/**
	 * @param type
	 *            The type of essence that the vial should be able to hold
	 * @param vialTier
	 *            The tier of essence vial you want to get
	 * @return Returns an Item of the essence type and tier that you specified
	 */
	public static Item essenceToVial(Essence type, VialQuality vialTier) {
		return vialMap.get(vialTier).get(type);
	}

}
