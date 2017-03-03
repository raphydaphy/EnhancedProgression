package com.raphydaphy.vitality.api.essence;

/**
 * Single type container.
 * 
 * @author Shadows
 *
 */
public interface IEssenceContainer {
	/**
	 * 
	 * @return Current essence stored.
	 */
	public int getEssenceStored();

	/**
	 * 
	 * @return The type of stored essence.
	 */
	public Essence getEssenceType();

	/**
	 * Hard sets the internal variable to a new essence level. Use sparingly.
	 * 
	 * @param newAmount
	 */
	public void setEssenceStored(int newAmount);

	/**
	 * 
	 * @return The total capacity of this container.
	 */
	public int getCapacity();

	/**
	 * Hard sets the internal variable for the essence type.
	 * 
	 * @param newType
	 */
	public void setEssenceType(Essence newType);

	/**
	 * Adds `toAdd` essence of type `essence`.
	 * 
	 * @param essence
	 * @param toAdd
	 * @return If the operation was successful.
	 */
	public boolean addEssence(Essence essence, int toAdd);

	/**
	 * Removes `toRemove` essence from this container.
	 * 
	 * @param toRemove
	 * @return If the operation was successful.
	 */
	public boolean subtractEssence(int toRemove);
}
