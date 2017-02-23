package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.init.Reference;
import com.raphydaphy.vitality.util.EssenceHelper;
import com.raphydaphy.vitality.util.ParticleHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.util.EnumHelper;

public class ItemExtractionSword extends ItemSword 
{
	private static final Item.ToolMaterial lifeExtractionMaterial = EnumHelper.addToolMaterial("EXTRACTION", 2, 300, 6, 3, 20);
	private static String name;
	
	public ItemExtractionSword() 
	{
		super(lifeExtractionMaterial);
		name = "life_extraction_sword";
		setUnlocalizedName(name);
		setRegistryName(name);
		this.setCreativeTab(Reference.creativeTab);
	}

	public void registerItemModel()
	{
		Vitality.proxy.registerItemRenderer(this, 0, name);
	}
	
	@Override
	public ItemExtractionSword setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
		if (target instanceof IMob && attacker instanceof EntityPlayer)
		{
			EssenceHelper.fillVial(ModItems.essence_vial_energetic, (EntityPlayer)attacker, 5);
			ParticleHelper.spawnParticlesServer(EnumParticleTypes.DAMAGE_INDICATOR, target.worldObj, true, target.posX, target.posY, target.posZ, 5, 0.25);
		}
        stack.damageItem(1, attacker);
        return true;
    }

}
