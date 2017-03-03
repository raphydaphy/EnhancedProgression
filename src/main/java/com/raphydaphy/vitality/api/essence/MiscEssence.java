package com.raphydaphy.vitality.api.essence;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.raphydaphy.vitality.item.ItemVial;
import com.raphydaphy.vitality.item.ItemVial.VialQuality;
import com.raphydaphy.vitality.registry.ModItems;

import net.minecraft.entity.Entity;
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
	 * @param te A valid container.
	 * @param vial An item stack that has an ItemVial as its item.
	 * @param toAdd The int value to add to that container.
	 * @return If the operation was successful.
	 */
	public static boolean fillContainerFromVial(IEssenceContainer te, ItemStack vial, int toAdd) {
		int currentInVial = ItemVial.getCurrentStored(vial);
		if(te.addEssence(((ItemVial) vial.getItem()).getVialType(), toAdd)){
			vial.getTagCompound().setInteger(Essence.KEY, currentInVial - toAdd);
			return true;
		}
		return false;

	}
	/**
	 * @param player The player holding the vial.
	 * @param hand The current active hand that the vial is in.
	 * @param te A valid container.
	 * @param vial An item stack that has an ItemVial as its item.
	 * @param toRemove The int value to remove from that container.
	 * @return If the operation was successful.
	 */
	public static boolean fillVialFromContainer(EntityPlayer player, EnumHand hand, IEssenceContainer te, ItemStack vial, int toRemove) {
		Essence typeContainer = te.getEssenceType();
		if(typeContainer == null) return false;
		ItemVial vialI = (ItemVial) vial.getItem();
		Essence typeVial = vialI.getVialType();
		int currentInVial = 0;
		if(vialI.hasType()) currentInVial = ItemVial.getCurrentStored(vial);
		if(typeContainer == typeVial && currentInVial + toRemove <= vialI.getMaxStorage() && te.subtractEssence(toRemove)){
			vial.getTagCompound().setInteger(Essence.KEY, currentInVial + toRemove);
			return true;
		}
		else if (typeVial != null && typeVial != typeContainer) return false;
		else if (typeVial == null && te.subtractEssence(toRemove)){
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
	public static boolean addEssence(ItemStack stack, int toAdd, boolean shouldBind, Entity entity, Essence type,
			@Nullable /* set to 0 for null */ int slot) {
		// if the essence should bind to the players soul
		if (shouldBind) {
			// bind the essence to the players soul-bound essence
			System.out.println(type.getMultiKey());
			entity.getEntityData().setInteger(type.getMultiKey(),
					entity.getEntityData().getInteger(type.getMultiKey()) + toAdd);
		}
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (stack.getItem() instanceof ItemVial) {
				ItemVial vial = (ItemVial) stack.getItem();
				// if the vial has some essence in it
				if (vial.hasType() && vial.getVialType() == type) {
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
					ItemStack vialStack = new ItemStack(EssenceToItem(type, vial.getQuality()));
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
		}
		return false;
	}

	/**
	 * Idk the equivelent of tuples in Java so i made two methods one for
	 * getting the int one for ItemStack. Plz no kill.
	 * 
	 * @param entity
	 *            The player whos inventory you want to search.
	 * @return Returns the slot number of the first full vial found in the
	 *         players inventory or an empty vial slot if no full vials are
	 *         found.
	 */
	public static int findVialSlotInInventory(Entity entity, Essence type) {
		// Check if the entity is actually a player
		if (entity instanceof EntityPlayer) {
			// Cast entity to EntityPlayer so that we can access the players
			// inventory
			EntityPlayer player = (EntityPlayer) entity;
			// loop through the players inventory
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				// get the currently selected item in the players inventory
				ItemStack stackAt = player.inventory.getStackInSlot(i);
				// check that the current stack isnt null to prevent
				// NullPointerExceptions
				if (stackAt != null) {
					// check if the stack actually is a vial
					if (stackAt.getItem() instanceof ItemVial) {
						// check that the vial already has some essence
						if (((ItemVial) stackAt.getItem()).hasType()
								&& ((ItemVial) stackAt.getItem()).getVialType() == type) {
							return i;
						}
					}
				}
			}

			// if no full vials were found
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				// get the currently selected item in the players inventory
				ItemStack stackAt = player.inventory.getStackInSlot(i);
				// check that the current stack isnt null to prevent
				// NullPointerExceptions
				if (stackAt != null) {
					// check if the stack actually is a vial
					if (stackAt.getItem() instanceof ItemVial && !(((ItemVial)stackAt.getItem()).hasType())){
						return i;
					}
				}
			}
		}
		return 0;
	}

	/**
	 * Finds the first vial in the players inventory.
	 * 
	 * @param entity
	 *            The player who's inventory you want to search
	 * @return Returns the first full vial in the players inventory or an empty
	 *         vial if no full ones are found
	 */
	public static ItemStack findVialStackInInventory(Entity entity, Essence type) {
		// Check if the entity is actually a player
		if (entity instanceof EntityPlayer) {
			// Cast entity to EntityPlayer so that we can access the players
			// inventory
			EntityPlayer player = (EntityPlayer) entity;
			// loop through the players inventory
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				// get the currently selected item in the players inventory
				ItemStack stackAt = player.inventory.getStackInSlot(i);
				// check that the current stack isnt null to prevent
				// NullPointerExceptions
				if (stackAt != null) {
					// check if the stack actually is a vial
					if (stackAt.getItem() instanceof ItemVial) {
						// check that the vial already has some essence
						if (((ItemVial) stackAt.getItem()).hasType()
								&& ((ItemVial) stackAt.getItem()).getVialType() == type) {
							return stackAt;
						}
					}
				}
			}

			// if no full vials were found
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				// get the currently selected item in the players inventory
				ItemStack stackAt = player.inventory.getStackInSlot(i);
				// check that the current stack isnt null to prevent
				// NullPointerExceptions
				if (stackAt != null) {
					// check if the stack actually is a vial
					if (stackAt.getItem() instanceof ItemVial && !(((ItemVial)stackAt.getItem()).hasType())){
						return stackAt;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Tries to fill an Essence Vial with an amount of Essence The difference
	 * betwen this and addEssence is this finds the vial from the players
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
	public static boolean fillVial(Essence type, int toAdd, boolean shouldBind, Entity entity) {
		// Check if the entity is actually a player
		if (entity instanceof EntityPlayer) {
			// find a vial from the players inventory
			ItemStack stack = findVialStackInInventory(entity, type);
			if (stack != null) {
				// check that a vial was actually found
				if (stack.getItem() instanceof ItemVial) {
					// try to fill the vial with essence
					return addEssence(stack, toAdd, shouldBind, entity, type, findVialSlotInInventory(entity, type));
				}
			}

		}

		return false;
	}

	/**
	 * Probably shit code and needs redo, but it takes an essence type and a
	 * wand tier and returns an Item instance of a vial that stores that essence
	 * type at that tier.
	 * 
	 * @param type
	 *            The type of essence that the vial should be able to hold
	 * @param vialTier
	 *            The tier of essence vial you want to get
	 * @return Returns an Item of the essence type and tier that you specified
	 */
	public static Item EssenceToItem(Essence type, VialQuality vialTier) {
		// TODO: remake this coz its shit
		switch (type) {
		case ANGELIC:
			switch (vialTier) {
			case BASIC:
				return ModItems.VIAL_ANGELIC;
			}
			break;
		case ATMOSPHERIC:
			switch (vialTier) {
			case BASIC:
				return ModItems.VIAL_ATMOSPHERIC;
			}
			break;
		case DEMONIC:
			switch (vialTier) {
			case BASIC:
				return ModItems.VIAL_DEMONIC;
			}
			break;
		case ENERGETIC:
			switch (vialTier) {
			case BASIC:
				return ModItems.VIAL_ENERGETIC;
			}
			break;
		case EXOTIC:
			switch (vialTier) {
			case BASIC:
				return ModItems.VIAL_EXOTIC;
			}
			break;
		}
		return null;
	}
}
