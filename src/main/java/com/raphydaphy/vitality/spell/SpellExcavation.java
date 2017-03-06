package com.raphydaphy.vitality.spell;

import java.util.AbstractMap.SimpleEntry;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.registry.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpellExcavation extends Spell {

	public SpellExcavation() {
		super("excavation", new Essence[] {}, ModItems.SPELL_EXCAVATION, 3, 3, 1, 10, false);
	}

	public static final String KEY = "THE_REAL_PASTA";

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
		player.setActiveHand(hand);
		return true;
	}

	@Override
	public void onCastPost(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		System.out.println("ARJIOAEJR AIWJ APIDWKAWDPOAKW PAKWD PAWKDPAOWDKPAOWDKOAWD");
		player.getCooldownTracker().setCooldown(wand.getItem(), cooldown);
		world.sendBlockBreakProgress(player.getEntityId(), pos, -1);
	}

	@Override
	public boolean isEssenceValid(Essence essence) {
		return true;
	}

	@Override
	public boolean onCastTick(ItemStack wand, EntityPlayer player, int count) {
		EntityPlayerMP realPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(player.getUniqueID());
		BlockPos pos = player.rayTrace(6, 8).getBlockPos();
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

        int i = (int)(f * 10.0F) + k;
        if (!state.getBlock().isAir(state, world, pos) && i >= 10)
        {
            if (state.getBlockHardness(world, pos) <= potency){
            if(!world.isRemote)	world.destroyBlock(pos, true);
            	//state.getBlock().dropBlockAsItem(world, pos, state, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, wand));
            	player.getEntityData().setInteger(KEY, 0);
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
		System.out.println("ARJIOAEJR AIWJ APIDWKAWDPOAKW PAKWD PAWKDPAOWDKPAOWDKOAWD");
		
	}
	
	

}
