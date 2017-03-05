package com.raphydaphy.vitality.spell;

import java.util.Random;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.registry.ModBlocks;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellIllumination extends Spell{

	public SpellIllumination(Item icon, int id, int cost, int potency, int cooldown) {
		super(icon, id, cost, potency, cooldown);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCast(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		int cooldown = WandHelper.getCore(wand).getCooldownMultiplier() * cooldown;
		int cost = WandHelper.getCore(wand).getPotencyMultiplier() * cost;
		int potency = WandHelper.getCore(wand).getCostMultiplier() * potency;

		if (WandHelper.canUseEssence(wand, cost, WandHelper.getCore(wand).getCoreType())) {
				Random rand = world.rand;
				for (int i = 0; i < 25; i++) {
					double x = (double) (pos.getX()) + 0.5 + ((rand.nextDouble()) - 0.5);
					double y = (double) pos.getY() + (rand.nextDouble()) + 0.75;
					double z = (double) pos.getZ() + 0.5 + ((rand.nextDouble()) - 0.5);
					int pars = 133;
					if (rand.nextInt(2) == 1) pars = 165;
					world.spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0.0D, 0.0D, 0.0D,
							pars);
				}
				WandHelper.useEssence(wand, cost, WandHelper.getCore(wand).getCoreType());
				world.playSound(null, pos, SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.BLOCKS, 1, 1);
				ItemStack stackToPlace = new ItemStack(ModBlocks.LIGHT_ORB);
				stackToPlace.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
				player.swingArm(hand);
				player.getCooldownTracker().setCooldown(wand.getItem(), cooldown);
			
			return true;
				
		} else if (world.isRemote){
			ClientProxy.setActionText(I18n.format("vitality.wand.notenoughessence.name"),
					WandHelper.getCore(wand).getCoreType().getColor());
		}
		return false;
	}

	@Override
	public boolean onCastPost(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}
	
	@Override
	public boolean isEssenceValid(Essence essence){
		return true;
	}

}
