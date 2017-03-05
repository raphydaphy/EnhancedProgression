package com.raphydaphy.vitality.spell;

import java.util.AbstractMap.SimpleEntry;
import java.util.Random;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.registry.ModItems;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellFireball extends Spell{

	public SpellFireball() {
		super(null, ModItems.SPELL_FIREBALL, 0, 5, 1, 5);
	
	}

	@Override
	public boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onCast(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
			SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);
			
				int cooldown = (int) (pair.getKey().getCooldownMultiplier() * this.cooldown);		
		 		int potency = (int) (pair.getKey().getPotencyMultiplier() * this.potency);		
		 		int cost = (int) (pair.getValue().getCostMultiplier() * this.cost);			
				if (WandHelper.canUseEssence(wand, cost, pair.getKey().getCoreType())) {			
		 				Random rand = world.rand;		
		 				for (int i = 0; i < 25; i++) {		
		 					double x = (double) (pos.getX()) + 0.5 + ((rand.nextDouble()) - 0.5);		
		 					double y = (double) pos.getY() + (rand.nextDouble()) + 0.5;		
		 					double z = (double) pos.getZ() + 0.5 + ((rand.nextDouble()) - 0.5);		
		 					int pars = 165;		
		 					if (rand.nextInt(2) == 1) pars = 133;		
		 							
		 					player.getEntityWorld().spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0.0D, 0.0D, 0.0D,		
		 							pars);		
		 				}		
		 						
		 				WandHelper.useEssence(wand, cost, pair.getKey().getCoreType());		
		 				world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERDRAGON_FIREBALL_EPLD, SoundCategory.BLOCKS, 1, 1);		
		 				player.getCooldownTracker().setCooldown(wand.getItem(), cooldown);		
		 				EntityLargeFireball bigBall = new EntityLargeFireball(world, player, player.getLookVec().xCoord, player.getLookVec().yCoord, player.getLookVec().zCoord);		
		 				bigBall.accelerationX = player.getLookVec().xCoord;		
		 				bigBall.accelerationY = player.getLookVec().yCoord;		
		 				bigBall.accelerationZ = player.getLookVec().zCoord;		
		 				bigBall.explosionPower = potency;		
		 				if(!world.isRemote) world.spawnEntityInWorld(bigBall);		
		 				player.getCooldownTracker().setCooldown(wand.getItem(), cooldown);		
		 			return true;		
		 		} else if (world.isRemote){		
		 					
		 				ClientProxy.setActionText(I18n.format("vitality.wand.notenoughessence.name"),		
		 					pair.getKey().getCoreType().getColor());		
		 			}		
		 			return false;		
		 	
		return true;
	}

	@Override
	public boolean onCastPost(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return true;
	}

	
	@Override
	public boolean isEssenceValid(Essence essence){
		return true;
	}
	
}
