package com.raphydaphy.vitality.item.rod;

import java.util.List;

import javax.annotation.Nonnull;

import com.raphydaphy.vitality.item.ItemBase;
import com.raphydaphy.vitality.util.ParticleHelper;
import com.raphydaphy.vitality.util.shadows.registry.ModBlocks;
import com.raphydaphy.vitality.util.shadows.registry.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * The Life Extraction Rod is used to extract essence out of ores and other blocks
 * This class defines all the things you can do with the item itself
 */
public class ItemTransmutationRod extends ItemBase {

	public ItemTransmutationRod() {
		super("transmutation_rod", 1);
		this.setFull3D();
	}

	/*
	 * Called when the rod is used on a block, by right-clicking Used to search
	 * for items to transmutate
	 */
	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10) {
		Block block = world.getBlockState(pos).getBlock();

		if (block == Blocks.CAULDRON) {
			if (!world.isRemote) {
				world.setBlockState(pos, ModBlocks.EXTRACTION_CRUCIBLE.getDefaultState());
				world.playSound(null, pos, SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.BLOCKS, 1F, 1F);
				ParticleHelper.spawnParticlesServer(EnumParticleTypes.SMOKE_LARGE, world, true, pos.getX() + 0.5,
						pos.getY(), pos.getZ() + 0.5, 20, 0.25);
			}
			player.swingArm(hand);
			player.getCooldownTracker().setCooldown(this, 10);
			return EnumActionResult.SUCCESS;
		} else if (block instanceof BlockGlass) {
			if (!world.isRemote) {
				world.setBlockState(pos, ModBlocks.JAR.getDefaultState());
				world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1, 1);
				ParticleHelper.spawnParticlesServer(EnumParticleTypes.PORTAL, world, true, pos.getX() + 0.5, pos.getY(),
						pos.getZ() + 0.5, 50, 0.5);
			}
		} else {
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,
					new AxisAlignedBB(pos.add(-2, -2, -2), pos.add(2, 2, 2)));
			for (EntityItem item : items) {
				if (item.getEntityItem().getItem() == Items.GLASS_BOTTLE) {
					EntityItem vialEmpty = new EntityItem(world, item.posX, item.posY, item.posZ,
							new ItemStack(ModItems.VIAL_EMPTY));
					if (!world.isRemote) {
						world.spawnEntityInWorld(vialEmpty);
						if (item.getEntityItem().stackSize > 1) {
							item.getEntityItem().stackSize -= 1;
						} else {
							item.setDead();
						}
						ParticleHelper.spawnParticles(EnumParticleTypes.CRIT_MAGIC, world, true, item.getPosition(), 50,
								2);
					}
					world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP,
							SoundCategory.AMBIENT, 1, 1);
					player.getCooldownTracker().setCooldown(this, 10);
					return EnumActionResult.SUCCESS;
				} else if (item.getEntityItem().getItem() == Items.IRON_SWORD) {
					if (!world.isRemote) {
						world.spawnEntityInWorld(new EntityItem(world, item.posX, item.posY, item.posZ,
								new ItemStack(ModItems.EXTRACTION_SWORD)));
						item.setDead();
						ParticleHelper.spawnParticles(EnumParticleTypes.END_ROD, world, true, item.getPosition(), 50,
								2);
					}
					world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP,
							SoundCategory.AMBIENT, 1, 1);
					player.getCooldownTracker().setCooldown(this, 10);
					return EnumActionResult.SUCCESS;
				}
			}
		}

		return EnumActionResult.PASS;
	}
}
