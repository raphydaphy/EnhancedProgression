package com.raphydaphy.vitality.item;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.essence.MiscEssence;
import com.raphydaphy.vitality.init.Reference;
import com.raphydaphy.vitality.network.MessageParticle;
import com.raphydaphy.vitality.network.PacketManager;
import com.raphydaphy.vitality.registry.IRegisterable;
import com.raphydaphy.vitality.registry.RegistryHelper;
import com.raphydaphy.vitality.util.ParticleHelper;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemExtractionSword extends ItemSword implements IRegisterable {
	private static final ToolMaterial lifeExtractionMaterial = EnumHelper.addToolMaterial("EXTRACTION", 2, 300, 6, 3,
			20);

	public ItemExtractionSword() {
		super(lifeExtractionMaterial);
		String name = "life_extraction_sword";
		setUnlocalizedName(Reference.MOD_ID + "." + name);
		setRegistryName(name);
		setMaxStackSize(1);
		setCreativeTab(Reference.creativeTab);
		GameRegistry.register(this);
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
			Vec3i vec = attacker.getHorizontalFacing().getDirectionVec();
			BlockPos pos = new BlockPos(vec.getX() / 2, 0, vec.getZ() / 2);
			BlockPos pos2 = new BlockPos(0.4D, 2.0D, 0.4D);
			PacketManager.INSTANCE.sendTo(
					new MessageParticle(EnumParticleTypes.CRIT_MAGIC, target.getPosition().add(0.5D, 1.0D, 0.5D),
							attacker.getPosition().add(ParticleHelper.multipleNoZero(pos, pos2))),
					FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
							.getPlayerByUUID(attacker.getUniqueID()));
		}
		stack.damageItem(1, attacker);
		return true;
	}

	@Override
	public void registerModels() {
		RegistryHelper.setModelLoc(this);
	}

	@Override
	public ModelResourceLocation getModelLocation() {
		return RegistryHelper.defaultLoc(this);
	}

}
