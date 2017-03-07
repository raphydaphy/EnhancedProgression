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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellSwapping extends Spell {

	public SpellSwapping() {
		// ints: id, cost, potency, cooldown
		super("swapping", new Essence[] {}, ModItems.SPELL_SWAPPING, 5, 8, 0, 25, true);
	}

	public static final String KEY = "SWAP_WAIT";

	@Override
	public boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);

		if (WandHelper.canUseEssence(wand, cost, pair.getKey().getCoreType())) {
			player.getEntityData().setString("wandCurEssenceType", pair.getKey().getCoreType().toString());
			player.getEntityData().setInteger("wandCurEssenceStored", WandHelper.getEssenceStored(wand));
			player.getEntityData().setInteger(VitalData.POS_X, pos.getX());
			player.getEntityData().setInteger(VitalData.POS_Y, pos.getY());
			player.getEntityData().setInteger(VitalData.POS_Z, pos.getZ());
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
		player.setActiveHand(hand);
		return true;
	}

	@Override
	public void onCastPost(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		player.getCooldownTracker().setCooldown(wand.getItem(), cooldown);
		//WandHelper.setEssenceStored(wand, player.getEntityData().getInteger("wandCurEssenceStored"));
		player.getEntityData().removeTag(KEY);
		player.getEntityData().removeTag(VitalData.POS_X);
		player.getEntityData().removeTag(VitalData.POS_Y);
		player.getEntityData().removeTag(VitalData.POS_Z);
	}

	@Override
	public boolean isEssenceValid(Essence essence) {
		return true;
	}

	@Override
	public boolean onCastTick(ItemStack wand, EntityPlayer player, int count) 
	{
		// position data is set in ItemWand -> onItemUse
		BlockPos pos = new BlockPos(player.getEntityData().getInteger(VitalData.POS_X), 
									player.getEntityData().getInteger(VitalData.POS_Y), 
									player.getEntityData().getInteger(VitalData.POS_Z));
		IBlockState state = player.worldObj.getBlockState(pos);
		Block toReplace = state.getBlock();
		System.out.println(toReplace.toString());
		Block toUse = Blocks.DIRT;
		if (toReplace != Blocks.AIR)
		{
			if (toReplace != toUse)
			{
				replaceBlock(pos, toUse, player);
			}
			replaceBlocksNearby(pos, toReplace, toUse, player);
		}
		return true;
	}
	
	public void replaceBlocksNearby(BlockPos startPos, Block toReplace, Block toUse, EntityPlayer player)
	{
		World world = player.getEntityWorld();
		// check each side of the block
		for (int i = 0; i < 6; i++)
		{
			EnumFacing side;
			// yay i get to use a switch
			// hopefully shadows dosent find this
			// that would be scary ;-;
			switch(i)
			{
			case 0:
				side = EnumFacing.DOWN;
				break;
			case 1:
				side = EnumFacing.UP;
				break;
			case 2:
				side = EnumFacing.NORTH;
				break;
			case 3:
				side = EnumFacing.EAST;
				break;
			case 4:
				side = EnumFacing.SOUTH;
				break;
			case 5:
				side = EnumFacing.WEST;
				break;
			default:
				// just to make eclipse happy
				side = EnumFacing.WEST;
				break;
			}
			BlockPos offset = startPos.offset(side);
			if (world.getBlockState(offset).getBlock() == toReplace)
			{
				if (replaceBlock(startPos.offset(side), toUse, player))
				{
					replaceBlocksNearby(offset, toReplace, toUse, player);
				}
			}
		}
	}

	public boolean replaceBlock(BlockPos pos, Block toUse, EntityPlayer player)
	{
		player.worldObj.destroyBlock(pos, true);
		player.worldObj.setBlockState(pos, Blocks.DIRT.getDefaultState());
		return true;
	}
	
	@Override
	public void onCastTickSuccess(ItemStack wand, EntityPlayer player, int count) {
		
	}
}
