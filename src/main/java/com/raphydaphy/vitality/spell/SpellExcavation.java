package com.raphydaphy.vitality.spell;

import java.util.AbstractMap.SimpleEntry;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.network.MessageBlockPos;
import com.raphydaphy.vitality.network.MessageEssenceUpdate;
import com.raphydaphy.vitality.network.PacketManager;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.registry.ModItems;
import com.raphydaphy.vitality.util.ParticleHelper;
import com.raphydaphy.vitality.util.VitalData;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpellExcavation extends Spell {

	public SpellExcavation() {
		super("excavation", new Essence[] {}, ModItems.SPELL_EXCAVATION, 3, 3, 1, 10, false);
	}

	public static final String KEY = "DIG_PROGRESS";

	@Override
	public boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);

		if (WandHelper.canUseEssence(wand, cost, pair.getKey().getCoreType())) {
			player.getEntityData().setString("wandCurEssenceType", pair.getKey().getCoreType().toString());
			player.getEntityData().setInteger("wandCurEssenceStored", WandHelper.getEssenceStored(wand));
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
		world.sendBlockBreakProgress(player.getEntityId(), pos, -1);
		WandHelper.setEssenceStored(wand, player.getEntityData().getInteger("wandCurEssenceStored"));
		player.getEntityData().removeTag(KEY);
		player.getEntityData().removeTag(VitalData.POS_X2);
		player.getEntityData().removeTag(VitalData.POS_Y2);
		player.getEntityData().removeTag(VitalData.POS_Z2);
	}

	@Override
	public boolean isEssenceValid(Essence essence) {
		return true;
	}

	@Override
	public boolean onCastTick(ItemStack wand, EntityPlayer player, int count) {

		BlockPos pos = null;
		EntityPlayerMP realPlayer = null;
		if (!player.worldObj.isRemote)
			realPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
					.getPlayerByUUID(player.getUniqueID());

		if (player.worldObj.isRemote) {
			pos = player.rayTrace(6, 8).getBlockPos();
			PacketManager.INSTANCE.sendToServer(new MessageBlockPos(pos.getX(), pos.getY(), pos.getZ()));
			player.getEntityData().setInteger(VitalData.POS_X2, pos.getX());
			player.getEntityData().setInteger(VitalData.POS_Y2, pos.getY());
			player.getEntityData().setInteger(VitalData.POS_Z2, pos.getZ());
		} else if (!player.worldObj.isRemote) {
			pos = new BlockPos(realPlayer.getEntityData().getInteger(VitalData.POS_X2),
					realPlayer.getEntityData().getInteger(VitalData.POS_Y2),
					realPlayer.getEntityData().getInteger(VitalData.POS_Z2));
		}

		float k = (player.getEntityData().getFloat(KEY)
				+ (WandHelper.getUsefulInfo(wand).getKey().getPotencyMultiplier() * potency / 5));
		if (k > 10 || k <= 0) {
			k = 0.1F;
			player.getEntityData().setFloat(KEY, k);
		}

		if (!areBlockPosEqual(
				new BlockPos(player.getEntityData().getInteger(VitalData.POS_X2),
						player.getEntityData().getInteger(VitalData.POS_Y2),
						player.getEntityData().getInteger(VitalData.POS_Z2)),
				new BlockPos(player.getEntityData().getInteger(VitalData.POS_X),
						player.getEntityData().getInteger(VitalData.POS_Y),
						player.getEntityData().getInteger(VitalData.POS_Z)))) {

			k = 0.1F;
			player.getEntityData().setFloat(KEY, k);
		}

		if (pos != null && player.getEntityData().getInteger(
				"wandCurEssenceStored") > (WandHelper.getUsefulInfo(wand).getValue().getCostMultiplier()) * this.cost) {

			player.getEntityData().setInteger(VitalData.POS_X, pos.getX());
			player.getEntityData().setInteger(VitalData.POS_Y, pos.getY());
			player.getEntityData().setInteger(VitalData.POS_Z, pos.getZ());
			return tryBreakBlockWithCast(wand, player, player.worldObj, pos, k);

		} else if (player.worldObj.isRemote) {
			ClientProxy.setActionText(I18n.format("vitality.wand.notenoughessence.name"),
					(WandHelper.getUsefulInfo(wand).getKey().getCoreType().getColor()));
		}
		player.getEntityData().setInteger(VitalData.POS_X, pos.getX());
		player.getEntityData().setInteger(VitalData.POS_Y, pos.getY());
		player.getEntityData().setInteger(VitalData.POS_Z, pos.getZ());
		return false;
	}

	private boolean areBlockPosEqual(BlockPos pos1, BlockPos pos2) {
		boolean[] flags = { false, false, false };
		if (pos1.getX() == pos2.getX())
			flags[0] = true;
		if (pos1.getY() == pos2.getY())
			flags[1] = true;
		if (pos1.getZ() == pos2.getZ())
			flags[2] = true;

		return flags[0] && flags[1] && flags[2];

	}

	private boolean tryBreakBlockWithCast(ItemStack wand, EntityPlayer player, World world, BlockPos pos, float k) {
		IBlockState state = world.getBlockState(pos);
		if (!state.getBlock().isAir(state, world, pos) && player.canPlayerEdit(pos, player.getHorizontalFacing(), wand))
			;
		{
			float f = state.getBlockHardness(world, pos);

			int i = (int) ((1 / (2 * f)) + k);
			k += (1 / f);
			if (i < 0 || k >= 9.6F)
				i = 10;
			if (!state.getBlock().isAir(state, world, pos) && i >= 10) {

				if (f != -1
						&& f <= (WandHelper.getUsefulInfo(wand).getKey().getPotencyMultiplier() * this.potency * 2)) {
					if (!world.isRemote) {
						world.destroyBlock(pos, true);
					}
					player.getEntityData().setFloat(KEY, 0);
					k = 0;
					return true;
				} else {
					ParticleHelper.spawnParticles(EnumParticleTypes.SMOKE_LARGE, world, true, pos, 20, 1D);
					if (world.isRemote)
						world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
								1.0F);
					player.getEntityData().setFloat(KEY, 0);
					k = 0;
				}
			} else {
				world.sendBlockBreakProgress(player.getEntityId(), pos, i);
				player.worldObj.sendBlockBreakProgress(player.getEntityId(), pos, i);
			}
			player.getEntityData().setFloat(KEY, k);
			return false;
		}

	}

	@Override
	public void onCastTickSuccess(ItemStack wand, EntityPlayer player, int count) {
		BlockPos pos = new BlockPos(player.getEntityData().getInteger(VitalData.POS_X2),
				player.getEntityData().getInteger(VitalData.POS_Y2),
				player.getEntityData().getInteger(VitalData.POS_Z2));
		if (player.worldObj.isRemote) {
			int cost = (int) (player.getEntityData().getInteger("wandCurEssenceStored")
					- (WandHelper.getUsefulInfo(wand).getValue().getCostMultiplier()) * this.cost
							* player.worldObj.getBlockState(new BlockPos(pos)).getBlockHardness(player.worldObj, pos));
			player.getEntityData().setInteger("wandCurEssenceStored", cost);
			PacketManager.INSTANCE.sendToServer(new MessageEssenceUpdate(player.getEntityId(), cost));

		}
	}
}
