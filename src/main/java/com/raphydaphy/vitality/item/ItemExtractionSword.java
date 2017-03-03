package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.essence.MiscEssence;
import com.raphydaphy.vitality.util.ParticleHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.util.EnumHelper;

public class ItemExtractionSword extends SwordBase {
	private static final Item.ToolMaterial lifeExtractionMaterial = EnumHelper.addToolMaterial("EXTRACTION", 2, 300, 6,
			3, 20);

	public ItemExtractionSword() {
		super("life_extraction_sword", lifeExtractionMaterial);
	}

	@Override
	public ItemExtractionSword setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (target instanceof IMob && attacker instanceof EntityPlayer) {
			MiscEssence.fillVial(Essence.ENERGETIC, 5, true, (EntityPlayer) attacker);
			if (!attacker.worldObj.isRemote) {
				ParticleHelper.spawnParticlesServer(EnumParticleTypes.DAMAGE_INDICATOR, target.worldObj, true,
						target.posX, target.posY, target.posZ, 5, 0.25);
			}
		}
		stack.damageItem(1, attacker);
		return true;
	}

}
