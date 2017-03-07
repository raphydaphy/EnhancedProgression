package com.raphydaphy.vitality.spell;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellSwapping extends Spell {

	public SpellSwapping() {
		// ints: id, cost, potency, cooldown
		super("swapping", new Essence[] {}, ModItems.SPELL_SWAPPING, 5, 8, 10, 25, false);
	}
	
	// the block to be swapped
	private static final String KEY_TARGET = "BLOCK_TARGET";
	
	// if dey be swapping
	private static final String KEY_SWAPPING = "SWAPPING";
	
	@Override
	public boolean onCastPre(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);

		if (WandHelper.canUseEssence(wand, cost, pair.getKey().getCoreType())) {
			player.getEntityData().setString("wandCurEssenceType", pair.getKey().getCoreType().toString());
			player.getEntityData().setInteger("wandCurEssenceStored", WandHelper.getEssenceStored(wand));
			
			if (player.isSneaking())
			{
				// set the block to swap here
				// one day..
			}
			else if(canSwap(wand) && wand.getTagCompound().getBoolean(KEY_SWAPPING) != true) {
				// will be the block they are swapping
				Block block = Blocks.DIRT;
				// will be swapping block metadata
				int meta = 0;
				List<BlockPos> swap = getBlocksToSwap(world, wand, block.getStateFromMeta(meta), pos, null);
				if(swap.size() > 0) 
				{
					player.getEntityData().setBoolean(KEY_SWAPPING, true);
					player.getEntityData().setInteger(VitalData.POS_X, pos.getX());
					player.getEntityData().setInteger(VitalData.POS_Y, pos.getY());
					player.getEntityData().setInteger(VitalData.POS_Z, pos.getZ());
					player.getEntityData().setString(KEY_TARGET, world.getBlockState(pos).toString());
					player.setActiveHand(hand);
					return false;
				}
			}
			return true;

		} else if (world.isRemote) {
			ClientProxy.setActionText(I18n.format("vitality.wand.notenoughessence.name"),
					pair.getKey().getCoreType().getColor());
		}
		return true;
	}

	@Override
	public boolean onCast(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return true;
	}

	@Override
	public void onCastPost(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		player.getCooldownTracker().setCooldown(wand.getItem(), cooldown);
		//WandHelper.setEssenceStored(wand, player.getEntityData().getInteger("wandCurEssenceStored"));
		player.getEntityData().removeTag(VitalData.POS_X);
		player.getEntityData().removeTag(VitalData.POS_Y);
		player.getEntityData().removeTag(VitalData.POS_Z);
		player.getEntityData().removeTag(KEY_SWAPPING);
		player.getEntityData().removeTag(KEY_TARGET);
	}

	@Override
	public boolean isEssenceValid(Essence essence) {
		return true;
	}

	@Override
	public boolean onCastTick(ItemStack wand, EntityPlayer player, int count) 
	{
		
		if(!canSwap(wand))
		{
			return false;
		}
		
		Block block = Blocks.DIRT;
		if(player.getEntityData().getBoolean(KEY_SWAPPING)) 
		{
			if(!WandHelper.canUseEssence(wand, this.cost * (int)WandHelper.getUsefulInfo(wand).getValue().getCostMultiplier(), WandHelper.getUsefulInfo(wand).getKey().getCoreType())) 
			{
				player.getEntityData().removeTag(KEY_SWAPPING);
				return false;
			}
			System.out.println("swapping");
			int x = player.getEntityData().getInteger(VitalData.POS_X);
			int y = player.getEntityData().getInteger(VitalData.POS_Y);
			int z = player.getEntityData().getInteger(VitalData.POS_Z);
			Block targetBlock = Block.getBlockFromName(player.getEntityData().getString(KEY_TARGET));
			if (targetBlock == null)
			{
				targetBlock = Blocks.STONE;
			}
			System.out.println((block.getDefaultState() == null) + " <== block is null | target is null ==> " + (targetBlock.getDefaultState() == null));
			List<BlockPos> swap = getBlocksToSwap(player.worldObj, wand, block.getDefaultState(), new BlockPos(x, y, z), targetBlock.getDefaultState());
			if(swap.size() == 0) {
				player.getEntityData().removeTag(KEY_SWAPPING);
				return false;
			}

			BlockPos coords = swap.get(player.worldObj.rand.nextInt(swap.size()));
			boolean exchange = swap(player.worldObj, player, coords, wand, block.getDefaultState());
			if(exchange)
			{
				// use essence
				
			}
			else
			{
				player.getEntityData().removeTag(KEY_SWAPPING);
			}
		}
		return true;
	}
	
	@Override
	public void onCastTickSuccess(ItemStack wand, EntityPlayer player, int count) {
		
	}
	
	private boolean swap(World world, EntityPlayer player, BlockPos pos, ItemStack stack, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null)
		{
			return false;
		}

		ItemStack placeStack = new ItemStack(Blocks.DIRT, 64);
		if(placeStack != null) {
			IBlockState stateAt = world.getBlockState(pos);
			Block blockAt = stateAt.getBlock();
			if(!blockAt.isAir(world.getBlockState(pos), world, pos) && stateAt.getPlayerRelativeBlockHardness(player, world, pos) > 0 && stateAt != state) {
				if(!world.isRemote) {
					if(!player.capabilities.isCreativeMode) {
						blockAt.dropBlockAsItem(world, pos, stateAt, 0);
						// here we need to remove the block from the players inventory
						// atm it just creates blocks out of thin air
					}
					world.playEvent(2001, pos, Block.getStateId(state));
					world.setBlockState(pos, state, 1 | 2);
					state.getBlock().onBlockPlacedBy(world, pos, state, player, placeStack);
				}
				return true;
			}
		}
		return false;
	}
	
	public List<BlockPos> getBlocksToSwap(World world, ItemStack wand, IBlockState swapState, BlockPos pos, IBlockState toReplace) {
		// For now adding this so that it always swaps using the block ur looking at
		if(toReplace == null) 
		{
			toReplace = world.getBlockState(pos);
		}

		// All da blocks
		List<BlockPos> swapMePlz = new ArrayList<>();

		// Range is set using potency modifiers
		int range = this.potency * (int)WandHelper.getUsefulInfo(wand).getKey().getPotencyMultiplier();

		// Loop through three dimensions in the range set above
		for(int offsetX = -range; offsetX <= range; offsetX++)
			for(int offsetY = -range; offsetY <= range; offsetY++)
				for(int offsetZ = -range; offsetZ <= range; offsetZ++) {
					BlockPos curBlock = pos.add(offsetX, offsetY, offsetZ);

					IBlockState currentState = world.getBlockState(curBlock);

					// IF the block isnt the type of block that is being replaced
					if(currentState != toReplace)
					{
						continue;
					}

					// We dont need to swap blocks that are already swapped!
					if(currentState == swapState)
					{
						continue;
					}

					// Check to see if the block is visible on any side
					// Used so that blocks underground arent swapped
					for(EnumFacing dir : EnumFacing.VALUES) {
						BlockPos adjPos = curBlock.offset(dir);
						IBlockState adjState = world.getBlockState(adjPos);

						// If the block is visible ;D
						if(!adjState.isSideSolid(world, adjPos, dir.getOpposite())) 
						{
							swapMePlz.add(curBlock);
							break;
						}
					}
				}

		return swapMePlz;
	}
	
	public boolean canSwap(ItemStack stack) {
		// will be the block they are swapping
		Block block = Blocks.DIRT;
		return block != null && block != Blocks.AIR;
	}
}
