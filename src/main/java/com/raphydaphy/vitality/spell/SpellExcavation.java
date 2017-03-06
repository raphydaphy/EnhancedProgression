package com.raphydaphy.vitality.spell;

import java.util.AbstractMap.SimpleEntry;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.registry.ModItems;
import com.raphydaphy.vitality.util.ParticleHelper;

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
import net.minecraft.world.IBlockAccess;
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
	public boolean onCastTick(ItemStack wand, EntityPlayer player, int count) {
		EntityPlayerMP realPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(player.getUniqueID());
		BlockPos pos = player.rayTrace(8, 8).getBlockPos();
		IBlockState state = player.worldObj.getBlockState(pos);
		int k =  player.getEntityData().getInteger(KEY) + 1;
		if(k > 10 || k <= 0){ k = 1;
		player.getEntityData().setInteger(KEY, k);
		}
		if(pos != null && player.worldObj.getBlockState(pos) == state){
		 doTheThingIStoleFromVanilla(wand, player, realPlayer, player.worldObj, pos, k);
			
			
			state = player.worldObj.getBlockState(pos);
		}
		return true;
	}
		
		
	private void doTheThingIStoleFromVanilla(ItemStack wand, EntityPlayer player, EntityPlayerMP realPlayer, World world, BlockPos pos, int k){
		System.out.println("K === " + k);
        float f = 1.0F;
        IBlockState state = world.getBlockState(pos);
        if (!state.getBlock().isAir(state, world, pos)){
            f = state.getPlayerRelativeBlockHardness(realPlayer, realPlayer.worldObj, pos);
        }
        if(f <= 0) f = 1;
        int i = (int)(f * 10.0F) + k;
        if(i <= 0) i = 10;
        if (!state.getBlock().isAir(state, world, pos) && i >= 10)
        {
        	System.out.println(state.getBlockHardness(world, pos) + ":::::" + (WandHelper.getUsefulInfo(wand).getKey().getPotencyMultiplier() * this.potency * 2));
            if (state.getBlockHardness(world, pos) <= (WandHelper.getUsefulInfo(wand).getKey().getPotencyMultiplier() * this.potency * 2)
            	&& state.getBlock() != Blocks.BEDROCK && state.getBlock() != Blocks.COMMAND_BLOCK && state.getBlock() != Blocks.CHAIN_COMMAND_BLOCK && state.getBlock() != Blocks.REPEATING_COMMAND_BLOCK){
	            if(!world.isRemote)	
	            {
	            	world.destroyBlock(pos, true);
	            	world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1, 1.0F);
	            }
	            player.getEntityData().setInteger(KEY, 0);
            }
            else {
            	ParticleHelper.spawnParticles(EnumParticleTypes.SMOKE_LARGE, world, true, pos, 30, 1);
            	if(world.isRemote) world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1.0F);
            }
        }
        else
        {
            world.sendBlockBreakProgress(realPlayer.getEntityId(), pos, i);
            realPlayer.worldObj.sendBlockBreakProgress(realPlayer.getEntityId(), pos, i);
            k = i;
        }
        System.out.println("K2 === " + k);
        player.getEntityData().setInteger(KEY, k);
	}

	@Override
	public void onCastTickSuccess(ItemStack wand, EntityPlayer player, int count) {
		// TODO Auto-generated method stub
		
	}
	
	

}
