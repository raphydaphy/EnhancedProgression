package com.raphydaphy.vitality.spell;

import java.util.AbstractMap.SimpleEntry;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.registry.ModItems;
import com.raphydaphy.vitality.util.VitalData;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpellExcavation extends Spell {

	public SpellExcavation() {
		super("excavation", new Essence[] {}, ModItems.SPELL_EXCAVATION, 3, 3, 1, 10, false);
	}

	public static final Spell INSTANCE = new SpellExcavation();

	@Override
	public boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);
		int cooldown = (int) (pair.getKey().getCooldownMultiplier() * this.cooldown);
		int potency = (int) (pair.getKey().getPotencyMultiplier() * this.potency);

		if (WandHelper.canUseEssence(wand, cost, pair.getKey().getCoreType())) {
			player.setActiveHand(hand);
			return false;

		} else if (world.isRemote) {
			ClientProxy.setActionText(I18n.format("vitality.wand.notenoughessence.name"),
					pair.getKey().getCoreType().getColor());
		}
		return false;
	}

	@Override
	public boolean onCast(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		
		return true;
	}

	@Override
	public void onCastPost(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		player.getCooldownTracker().setCooldown(wand.getItem(), cooldown);
	}

	@Override
	public boolean isEssenceValid(Essence essence) {
		return true;
	}

	@Override
	public boolean onCastTick(ItemStack wand, EntityPlayer player, int count) {
		EntityPlayerMP realPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(player.getUniqueID());
		BlockPos pos = player.rayTrace(6, 8).getBlockPos();
		System.out.println("ticky");
		realPlayer.interactionManager.tryHarvestBlock(pos);
		return true;
	}

	@Override
	public void onCastTickSuccess(ItemStack wand, EntityPlayer player, int count) {
		// TODO Auto-generated method stub
		
	}
	
	

}
