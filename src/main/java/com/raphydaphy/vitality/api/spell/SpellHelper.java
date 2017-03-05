package com.raphydaphy.vitality.api.spell;

import java.util.Random;

import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.registry.ModBlocks;
import com.raphydaphy.vitality.registry.ModItems;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class SpellHelper {
	// TODO: this class is shit but maps are too hard so rip

	// maps are too hard 4 me
	public static Spell getSpellFromID(int id) {
		switch (id) {
		case 0:
			return Spell.ILLUMINATION;
		case 1:
			return Spell.FIREBALL;
		}
		return null;
	}

	// maps are still too hard
	public static int getIDFromSpell(Spell spell) {
		switch (spell) {
		case ILLUMINATION:
			return 0;
		case FIREBALL:
			return 1;
		}
		return 0;
	}

	// pleb version coz i cant figure out how to make `item` not null in Spell
	// enum
	public static Item spellToItem(Spell spell) {
		switch (spell) {
		case ILLUMINATION:
			return ModItems.SPELL_ILLUMINATION;
		case FIREBALL:
			return ModItems.SPELL_FIREBALL;
		}
		return Item.getItemFromBlock(Blocks.BARRIER);
	}

	public static boolean lanternSpell(ItemStack wand, EntityPlayer caster, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ, BlockPos pos) {
		int cooldown = WandHelper.getCore(wand).getCoreType().getCooldown();
		int cost = WandHelper.getCore(wand).getCoreType().getPotency();
		int potency = WandHelper.getCore(wand).getCoreType().getCost();

		if (WandHelper.canUseEssence(wand, cost, WandHelper.getCore(wand).getCoreType())) {
			if (caster.getEntityWorld().isRemote) {
				Random rand = caster.getEntityWorld().rand;
				for (int i = 0; i < 25; i++) {
					double x = (double) (pos.getX()) + 0.5 + ((rand.nextDouble()) - 0.5);
					double y = (double) pos.getY() + (rand.nextDouble()) + 0.5;
					double z = (double) pos.getZ() + 0.5 + ((rand.nextDouble()) - 0.5);
					int[] pars = new int[1];
					if (rand.nextInt(2) == 1) {
						pars[0] = 165;
					} else {
						pars[0] = 133;
					}
					caster.getEntityWorld().spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0.0D, 0.0D, 0.0D,
							pars);
				}
			} else {
				WandHelper.useEssence(wand, cost, WandHelper.getCore(wand).getCoreType());
				caster.getEntityWorld().playSound(null, pos, SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.BLOCKS, 1, 1);
				ItemStack stackToPlace = new ItemStack(ModBlocks.LIGHT_ORB);
				stackToPlace.onItemUse(caster, caster.getEntityWorld(), pos, hand, facing, hitX, hitY, hitZ);
				caster.swingArm(hand);
				caster.getCooldownTracker().setCooldown(wand.getItem(), cooldown);
			}
			return true;
		} else {
			ClientProxy.setActionText(I18n.format("vitality.wand.notenoughessence.name"),
					WandHelper.getCore(wand).getCoreType().getColor());
			return false;
		}
	}
	
	public static boolean fireballSpell(ItemStack wand, EntityPlayer caster) {
		int cooldown = WandHelper.getCore(wand).getCoreType().getCooldown() * 2;
		int cost = WandHelper.getCore(wand).getCoreType().getPotency();
		int potency = WandHelper.getCore(wand).getCoreType().getCost() * 3;

		if (WandHelper.canUseEssence(wand, cost, WandHelper.getCore(wand).getCoreType())) {
			if (caster.getEntityWorld().isRemote) {
				/*
				Random rand = caster.getEntityWorld().rand;
				for (int i = 0; i < 25; i++) {
					double x = (double) (pos.getX()) + 0.5 + ((rand.nextDouble()) - 0.5);
					double y = (double) pos.getY() + (rand.nextDouble()) + 0.5;
					double z = (double) pos.getZ() + 0.5 + ((rand.nextDouble()) - 0.5);
					int[] pars = new int[1];
					if (rand.nextInt(2) == 1) {
						pars[0] = 165;
					} else {
						pars[0] = 133;
					}
					caster.getEntityWorld().spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0.0D, 0.0D, 0.0D,
							pars);
				}
				*/
			} else {
				WandHelper.useEssence(wand, cost, WandHelper.getCore(wand).getCoreType());
				caster.getEntityWorld().playSound(null, caster.getPosition(), SoundEvents.ENTITY_ENDERDRAGON_FIREBALL_EPLD, SoundCategory.BLOCKS, 1, 1);
				caster.getCooldownTracker().setCooldown(wand.getItem(), cooldown);
				EntityLargeFireball bigBall = new EntityLargeFireball(caster.worldObj, caster, caster.getLookVec().xCoord, caster.getLookVec().yCoord, caster.getLookVec().zCoord);
				bigBall.accelerationX = caster.getLookVec().xCoord;
				bigBall.accelerationY = caster.getLookVec().yCoord;
				bigBall.accelerationZ = caster.getLookVec().zCoord;
				bigBall.explosionPower = potency;
				caster.worldObj.spawnEntityInWorld(bigBall);
				caster.getCooldownTracker().setCooldown(wand.getItem(), cooldown);
			}
			return true;
		} else {
			ClientProxy.setActionText(I18n.format("vitality.wand.notenoughessence.name"),
					WandHelper.getCore(wand).getCoreType().getColor());
			return false;
		}
	}
}
