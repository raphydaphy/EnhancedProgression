package com.raphydaphy.vitality.spell;

import java.util.AbstractMap.SimpleEntry;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.network.MessageBlockPos;
import com.raphydaphy.vitality.network.PacketManager;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.registry.ModItems;
import com.raphydaphy.vitality.util.ParticleHelper;
import com.raphydaphy.vitality.util.VitalData;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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
		player.getEntityData().setInteger(KEY, 0);
	}

	@Override
	public boolean isEssenceValid(Essence essence) {
		return true;
	}

	@Override
	public boolean onCastTick(ItemStack wand, EntityPlayer player, int count) 
		{
		
		BlockPos pos = null;
		EntityPlayerMP realPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(player.getUniqueID());
		if (player.worldObj.isRemote)
		{
			pos = player.rayTrace(realPlayer.interactionManager.getBlockReachDistance(), 8).getBlockPos();
			PacketManager.INSTANCE.sendToServer(new MessageBlockPos(pos.getX(), pos.getY(), pos.getZ()));
			player.getEntityData().setInteger(VitalData.POS_X2, pos.getX());
			player.getEntityData().setInteger(VitalData.POS_Y2, pos.getY());
			player.getEntityData().setInteger(VitalData.POS_Z2, pos.getZ());
		}
		else if(!player.worldObj.isRemote)
		{
			pos = new BlockPos(realPlayer.getEntityData().getInteger(VitalData.POS_X2),
					realPlayer.getEntityData().getInteger(VitalData.POS_Y2),
					realPlayer.getEntityData().getInteger(VitalData.POS_Z2));
		}
		int k =  (int) (player.getEntityData().getInteger(KEY) + (WandHelper.getUsefulInfo(wand).getKey().getPotencyMultiplier() * potency / 5));
		if(k > 10 || k <= 0)
		{ 
			k = 1;
			player.getEntityData().setInteger(KEY, k);
		}
		
		if(!areBlockPosEqual(new BlockPos(player.getEntityData().getInteger(VitalData.POS_X2),
				player.getEntityData().getInteger(VitalData.POS_Y2),
				player.getEntityData().getInteger(VitalData.POS_Z2)), new BlockPos(player.getEntityData().getInteger(VitalData.POS_X),
						player.getEntityData().getInteger(VitalData.POS_Y),
						player.getEntityData().getInteger(VitalData.POS_Z)))){
			
			player.getEntityData().setInteger(KEY, 1);
			k = 1;
		}
		
		if(pos != null && areBlockPosEqual(pos, new BlockPos(player.getEntityData().getInteger(VitalData.POS_X2),
				player.getEntityData().getInteger(VitalData.POS_Y2),
				player.getEntityData().getInteger(VitalData.POS_Z2))))
		{
			tryBreakBlockWithCast(wand, player, realPlayer, player.worldObj, pos, k);
		}
		player.getEntityData().setInteger(VitalData.POS_X, pos.getX());
		player.getEntityData().setInteger(VitalData.POS_Y, pos.getY());
		player.getEntityData().setInteger(VitalData.POS_Z, pos.getZ());
		return true;
	}
	
	private boolean areBlockPosEqual(BlockPos pos1, BlockPos pos2){
		boolean[] flags = {false, false, false};
		if(pos1.getX() == pos2.getX()) flags[0] = true;
		if(pos1.getY() == pos2.getY()) flags[1] = true;
		if(pos1.getZ() == pos2.getZ()) flags[2] = true;
		
		return flags[0] && flags[1] && flags[2];
		
	}
		
		
	private void tryBreakBlockWithCast(ItemStack wand, EntityPlayer player, EntityPlayerMP realPlayer, World world, BlockPos pos, int k){
        IBlockState state = world.getBlockState(pos);
        if (!state.getBlock().isAir(state, world, pos))
        {
    		System.out.println("K === " + k);
            float f = state.getPlayerRelativeBlockHardness(realPlayer, realPlayer.worldObj, pos);
        
        if(f <= 0) f = 1;
        int i = f + k;
        if(i <= 0) i = 10;
        if (!state.getBlock().isAir(state, world, pos) && i >= 10)
        {
        	System.out.println(state.getBlockHardness(world, pos) + ":::::" + (WandHelper.getUsefulInfo(wand).getKey().getPotencyMultiplier() * this.potency * 2));
            if (state.getBlockHardness(world, pos) != -1 && state.getBlockHardness(world, pos) <= (WandHelper.getUsefulInfo(wand).getKey().getPotencyMultiplier() * this.potency * 2))
            {
	            if(!world.isRemote)	
	            {
	            	world.destroyBlock(pos, true);
	            }
	            player.getEntityData().setInteger(KEY, 0);
            }
            else 
            {
            	ParticleHelper.spawnParticles(EnumParticleTypes.SMOKE_LARGE, world, true, pos, 15, 0.202D);
            	if(world.isRemote) world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1.0F);
            }
        }
        else
        {
            world.sendBlockBreakProgress(realPlayer.getEntityId(), pos, i);
            realPlayer.worldObj.sendBlockBreakProgress(realPlayer.getEntityId(), pos, i);
            k = i;
        }
        System.out.println("F === " + f);
        System.out.println("I === " + i);
        System.out.println("K2 === " + k);
        player.getEntityData().setInteger(KEY, k);
        }
        
	}

	@Override
	public void onCastTickSuccess(ItemStack wand, EntityPlayer player, int count) {
		// nothing to do!
	}
}
