package com.raphydaphy.vitality.api.wand;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.wand.WandEnums.WandResult;
import com.raphydaphy.vitality.api.wand.WandEnums.WandTier;

public interface IWandable {

	public WandResult onWandUse(Essence[] reqEssences, int[] costs, WandTier minTier);
	
}
