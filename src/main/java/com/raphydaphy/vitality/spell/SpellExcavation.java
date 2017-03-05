package com.raphydaphy.vitality.spell;

import java.util.AbstractMap.SimpleEntry;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.registry.ModItems;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellExcavation extends Spell {

	public SpellExcavation() {
		super("excavation", new Essence[] {}, ModItems.SPELL_EXCAVATION, 3, 3, 1, 10, true);
	}

	public static final Spell INSTANCE = new SpellExcavation();

	@Override
	public boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);
		int cooldown = (int) (pair.getKey().getCooldownMultiplier() * this.cooldown);
		int cost = (int) (pair.getValue().getCostMultiplier() * this.cost);
		int potency = (int) (pair.getKey().getPotencyMultiplier() * this.potency);

		if (WandHelper.canUseEssence(wand, cost, pair.getKey().getCoreType())) {

			//WandHelper.useEssence(wand, cost, pair.getKey().getCoreType());
			
			//world.setBlockToAir(pos);
			world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1, 1);
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
		System.out.println("magix");
		world.sendBlockBreakProgress(0, pos, 50);
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

}
