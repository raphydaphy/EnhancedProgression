package com.raphydaphy.vitality.api.essence;

public interface IEssenceContainer {
	public int getEssenceStored();

	public Essence getEssenceType();

	public void setEssenceStored(int newAmount);

	public void setEssenceType(Essence newType);
}
