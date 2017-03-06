package com.raphydaphy.vitality.spell;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.registry.ModItems;
import com.raphydaphy.vitality.util.ParticleHelper;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellExplosion extends Spell {

	public SpellExplosion() {
		super("explosion", new Essence[] {}, ModItems.SPELL_EXPLOSION, 2, 10, 1, 10, false);
	}

	@Override
	public boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);
		int cooldown = (int) (pair.getKey().getCooldownMultiplier() * this.cooldown);
		int potency = (int) (pair.getKey().getPotencyMultiplier() * this.potency);
		int cost = (int) (pair.getValue().getCostMultiplier() * this.cost);

		if (WandHelper.canUseEssence(wand, cost, pair.getKey().getCoreType())) {

			WandHelper.useEssence(wand, cost, pair.getKey().getCoreType());
			BlockPos bombPos = pos;
			world.playSound(null, bombPos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 1);
			if (!world.isRemote)
			{
				ParticleHelper.spawnParticles(EnumParticleTypes.EXPLOSION_HUGE, world, true, bombPos, potency*10, potency);
			}
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(bombPos.add(-potency, -potency, -potency), bombPos.add(potency,potency,potency)));
			for (EntityLivingBase living : entities)
			{
				if (living != player)
				{
					living.attackEntityFrom(DamageSource.magic, potency);
				}
			}
			player.swingArm(hand);
			player.getCooldownTracker().setCooldown(wand.getItem(), cooldown);

			return true;

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
	}

	@Override
	public boolean isEssenceValid(Essence essence) {
		return true;
	}

	@Override
	public boolean onCastTick(ItemStack wand, EntityPlayer player, int count) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCastTickSuccess(ItemStack wand, EntityPlayer player, int count) {
		// TODO Auto-generated method stub
		
	}

}
