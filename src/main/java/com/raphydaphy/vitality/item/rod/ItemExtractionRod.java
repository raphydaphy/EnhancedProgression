package com.raphydaphy.vitality.item.rod;

import javax.annotation.Nonnull;

import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.item.ItemBase;
import com.raphydaphy.vitality.util.EssenceHelper;
import com.raphydaphy.vitality.util.NBTHelper;
import com.raphydaphy.vitality.util.ParticleHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/*
 * The Life Extraction Rod is used to extract essence out of ores and other blocks
 * This class defines all the things you can do with the item itself
 */
public class ItemExtractionRod extends ItemBase
{
	
	public ItemExtractionRod() 
	{
		super("life_extraction_rod", 1);
		this.setFull3D();
	}
	
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player, EnumHand hand)
    {
        RayTraceResult raytraceresult = this.rayTrace(worldIn, player, true);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, worldIn, itemStackIn, raytraceresult);
        if (ret != null) return ret;

        if (raytraceresult == null)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
        }
        else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
        }
        else
        {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (!worldIn.isBlockModifiable(player, blockpos))
            {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
            }
            else
            {
                if (!player.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemStackIn))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
                }
                else
                {
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    Material material = iblockstate.getMaterial();

                    if (material == Material.LAVA && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                    {
                    	if (!worldIn.isRemote)	
            			{
            				ParticleHelper.spawnParticles(EnumParticleTypes.SMOKE_NORMAL, worldIn, true, blockpos, 50, 0.5);
            				worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState());
            				worldIn.playSound(null, blockpos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.AMBIENT, 1, 1);
            			}
                    	EssenceHelper.fillVial(ModItems.essence_vial_demonic, player, 5);
            			player.swingArm(hand);
            			player.getCooldownTracker().setCooldown(this, 25);
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
                    }
                    else
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
                    }
                }
            }
        }
    }
	
	/*
	 * Called when the rod is used on a block, by right-clicking
	 * Used for ore and lava extraction
	 */
	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10)
	{
		Block block = world.getBlockState(pos).getBlock();
		
		
		if (block == ModBlocks.angelic_crystal_ore)
		{
			ParticleHelper.outlineAll(pos, EnumParticleTypes.ENCHANTMENT_TABLE, world);
			stack.setTagInfo("counter", new NBTTagInt(40));
			stack.setTagInfo("xPos", new NBTTagInt(pos.getX()));
			stack.setTagInfo("yPos", new NBTTagInt(pos.getY()));
			stack.setTagInfo("zPos", new NBTTagInt(pos.getZ()));
			stack.setTagInfo("essenceType", new NBTTagString("ANGELIC"));
			player.swingArm(hand);
			player.getCooldownTracker().setCooldown(this, 50);
			return EnumActionResult.SUCCESS;
		}
		else if (block == ModBlocks.exotic_crystal_ore)
		{
			//Color color = new Color(0x00C6FF);
			//Vitality.proxy.magicParticle((double)pos.getX(), (double)pos.getY() + 2, (double)pos.getZ(), (float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 1F, 2);
			ParticleHelper.outlineAll(pos, EnumParticleTypes.ENCHANTMENT_TABLE, world);
			stack.setTagInfo("counter", new NBTTagInt(40));
			stack.setTagInfo("xPos", new NBTTagInt(pos.getX()));
			stack.setTagInfo("yPos", new NBTTagInt(pos.getY()));
			stack.setTagInfo("zPos", new NBTTagInt(pos.getZ()));
			stack.setTagInfo("essenceType", new NBTTagString("EXOTIC"));
			player.swingArm(hand);
			player.getCooldownTracker().setCooldown(this, 65);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
		if (NBTHelper.getInt(stack, "counter", -1) != -1)
		{
			System.out.println(NBTHelper.getInt(stack, "counter", -1) + " client: " + world.isRemote);
			if (NBTHelper.getInt(stack, "counter", -1) == 20)
			{
				if (!world.isRemote)
				{
					ParticleHelper.spawnParticlesServer(EnumParticleTypes.PORTAL, world, true, NBTHelper.getInt(stack, "xPos", -1) + 0.5, NBTHelper.getInt(stack, "yPos", -1) - 0.5, NBTHelper.getInt(stack, "zPos", -1) + 0.5, 1000, 0.5);
				}
			}
			else if (NBTHelper.getInt(stack, "counter", -1) == 0)
			{
				if(!world.isRemote)
				{
					world.setBlockState(new BlockPos(NBTHelper.getInt(stack, "xPos", -1), NBTHelper.getInt(stack, "yPos", -1), NBTHelper.getInt(stack, "zPos", -1)), Blocks.STONE.getDefaultState());
					world.playSound(null, new BlockPos(NBTHelper.getInt(stack, "xPos", -1), NBTHelper.getInt(stack, "yPos", -1), NBTHelper.getInt(stack, "zPos", -1)), SoundEvents.BLOCK_STONE_BREAK, SoundCategory.AMBIENT, 1, 1);
					if (NBTHelper.getString(stack, "essenceType", null) == "ANGELIC")
					{
						EssenceHelper.fillVial(ModItems.essence_vial_angelic, (EntityPlayer) entity, 15);
					}
					else if (NBTHelper.getString(stack, "essenceType", null) == "EXOTIC")
					{
						EssenceHelper.fillVial(ModItems.essence_vial_exotic, (EntityPlayer) entity, 30);
					}
				}
			}
			stack.setTagInfo("counter", new NBTTagInt(NBTHelper.getInt(stack, "counter", -1) -1));			
		}
    }
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
}
