package com.raphydaphy.vitality.essence;

public interface IEssenceContainer 
{
	public int getEssenceStored();
	public String getEssenceType();
	
	public void setEssenceStored(int newAmount);
	public void setEssenceType(String newType);
}
