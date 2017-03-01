package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.essence.EssenceHelper;
import com.raphydaphy.vitality.registry.ModItems;
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
			EssenceHelper.fillVial(ModItems.VIAL_ENERGETIC, (EntityPlayer) attacker, 5);
			ParticleHelper.spawnParticlesServer(EnumParticleTypes.DAMAGE_INDICATOR, target.worldObj, true, target.posX,
					target.posY, target.posZ, 5, 0.25);
		}
		stack.damageItem(1, attacker);
		return true;
	}

}
