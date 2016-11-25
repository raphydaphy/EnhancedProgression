package com.raphydaphy.enhancedprogression.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.block.BlockAltar;
import com.raphydaphy.enhancedprogression.init.ModBlocks;
import com.raphydaphy.enhancedprogression.init.ModItems;
import com.raphydaphy.enhancedprogression.nbt.ItemNBTHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOre;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBasicWand extends Item
{
	protected String name;

	// Logger
	private List<BlockPos> replaceBlock = new ArrayList<BlockPos>();
	private BlockPos curBlock;

	public ItemBasicWand(String name)
	{
		this.name = name;
		setUnlocalizedName(name);
		setRegistryName(name);
		this.maxStackSize = 1;
	}

	@Override
	public ItemBasicWand setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}

	public int getWandTier()
	{
		return 1;
	}

	public int getEssenceStored(ItemStack stack)
	{
		NBTTagCompound compoundTmp = stack.getTagCompound();
		return compoundTmp.getInteger("essenceStored");
	}

	public int getMaxEssence()
	{
		return 1000;
	}

	public boolean useEssence(int amount, ItemStack stack)
	{
		NBTTagCompound compoundTmp = stack.getTagCompound();
		if (getEssenceStored(stack) - amount >= 0)
		{
			compoundTmp.setInteger("essenceStored", compoundTmp.getInteger("essenceStored") - amount);
			return true;
		}
		stack.setTagCompound(compoundTmp);
		return false;
	}

	public void addEssence(int amount, ItemStack stack)
	{
		NBTTagCompound compoundTmp = stack.getTagCompound();
		if (getEssenceStored(stack) + amount < getMaxEssence() + 1)
		{
			compoundTmp.setInteger("essenceStored", compoundTmp.getInteger("essenceStored") + amount);
		} else
		{
			compoundTmp.setInteger("essenceStored", getMaxEssence());
		}
		stack.setTagCompound(compoundTmp);
	}

	private static Iterable<BlockPos.MutableBlockPos> WOOD_SEARCH_AREA = BlockPos
			.getAllInBoxMutable(new BlockPos(-5, -5, -5), new BlockPos(5, 25, 5));

	boolean checkPlatform(int xOff, int yOff, int zOff, Block block, BlockPos pos, World world)
	{
		return world.getBlockState(pos.add(xOff, yOff, zOff)).getBlock() == block;
	}

	public void registerItemModel()
	{
		EnhancedProgression.proxy.registerItemRenderer(this, 0, name);
	}

	private EntityArrow fancySetAim(EntityArrow target, Entity entity, float pitch, float yaw, float velocity,
			float knockbackResistance, float xOff, float yOff, float zOff)
	{
		float x = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float y = -MathHelper.sin(pitch * 0.017453292F);
		float z = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		target.setThrowableHeading((double) x - xOff, (double) y - yOff, (double) z - xOff, velocity, 0);
		target.motionX += entity.motionX;
		target.motionZ += entity.motionZ;

		if (!entity.onGround)
		{
			target.motionY += entity.motionY;
		}

		return target;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected)
	{
		ItemNBTHelper.detectNBT(stack);
		if (ItemNBTHelper.getString(stack, "particleAction", "") != "")
		{
			if (ItemNBTHelper.getInt(stack, "particleCount", 0) > 0)
			{
				ItemNBTHelper.setInt(stack, "particleCount", ItemNBTHelper.getInt(stack, "particleCount", 0) - 1);

				double x = ItemNBTHelper.getInt(stack, "posX", 0);
				double y = ItemNBTHelper.getInt(stack, "posY", 0);
				double z = ItemNBTHelper.getInt(stack, "posZ", 0);

				switch (ItemNBTHelper.getString(stack, "particleAction", ""))
				{
				case "ORE EXTRACT":
					spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true, new BlockPos(x, y, z), 2, 1);
					break;
				case "TRANSMUTE DIAMOND":
					spawnParticles(EnumParticleTypes.END_ROD, world, true, new BlockPos(x, y, z), 2, 1);
					break;
				case "PLACE TORCH":
					spawnParticles(EnumParticleTypes.FLAME, world, true, new BlockPos(x, y, z), 2, 1);
					break;
				}
			} else
			{
				ItemNBTHelper.setString(stack, "particleAction", "");
			}
		}

		if (ItemNBTHelper.getString(stack, "tickSpell", "") != "" && isSelected)
		{
			int delay = ItemNBTHelper.getInt(stack, "delay", 0);
			switch (ItemNBTHelper.getString(stack, "tickSpell", ""))
			{
			case "LOGGER":
				if (delay == 0 && replaceBlock.size() > ItemNBTHelper.getInt(stack, "currentBlockId", 0)
						&& !world.isRemote)
				{
					curBlock = replaceBlock.get(ItemNBTHelper.getInt(stack, "currentBlockId", 0));
					world.playSound(null, curBlock, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1, 1);
					world.setBlockState(curBlock, ModBlocks.dead_log.getDefaultState());
					spawnParticles(EnumParticleTypes.SPELL_WITCH, world, true, curBlock, 7, 1);
					this.addEssence(ThreadLocalRandom.current().nextInt(2, 15 + 1), stack);

					ItemNBTHelper.setInt(stack, "currentBlockId", ItemNBTHelper.getInt(stack, "currentBlockId", 0) + 1);
					delay = 10;

					if (ThreadLocalRandom.current().nextInt(1, 1000 + 1) == 555 && canBreak())
					{
						world.playSound(null, curBlock, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1, 1);
						stack.stackSize = 0;
						ItemNBTHelper.setString(stack, "tickSpell", "");
					}
				} else if (replaceBlock.size() > ItemNBTHelper.getInt(stack, "currentBlockId", 0) && !world.isRemote)
				{
					delay--;
				} else if (!world.isRemote)
				{
					ItemNBTHelper.setString(stack, "tickSpell", "");
				}
				break;
			case "RAPIDFIRE":
				if (delay == 0)
				{
					if (useEssence(ItemNBTHelper.getInt(stack, "arrows", 0), stack))
					{
						if (!world.isRemote)
						{
							ItemNBTHelper.setInt(stack, "arrows", ItemNBTHelper.getInt(stack, "arrows", 0) + 1);
							delay = 5;

							int xOff = 0;
							int yOff = 0;
							int zOff = 0;

							EntityLivingBase shooter = (EntityLivingBase) entityIn;
							shooter.posX += xOff;
							shooter.posY += yOff;
							shooter.posZ += zOff;

							ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
							ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow
									? itemstack.getItem() : Items.ARROW));
							EntityArrow entityArrow = itemarrow.createArrow(world, itemstack, shooter);

							entityArrow = fancySetAim(entityArrow, shooter, entityIn.rotationPitch,
									entityIn.rotationYaw, 5.0F, 1.0F, xOff, yOff, zOff);
							entityArrow.setKnockbackStrength(1);
							entityArrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;

							world.spawnEntityInWorld(entityArrow);
							world.playSound(null, shooter.posX, shooter.posY, shooter.posZ,
									SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F,
									1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
						}
					} else
					{
						EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						ItemNBTHelper.setString(stack, "tickSpell", "");
					}

				} else
				{
					delay--;
				}
				break;
			}
			ItemNBTHelper.setInt(stack, "delay", delay);
		} else
		{
			ItemNBTHelper.setString(stack, "tickSpell", "");
		}
	}

	public static void spawnParticles(EnumParticleTypes particleType, World world, boolean forceSpawn, BlockPos pos,
			int count, double radius)
	{
		spawnParticlesServer(particleType, world, forceSpawn, pos.getX(), pos.getY(), pos.getZ(), count, radius);
	}

	public static void spawnParticlesServer(EnumParticleTypes particleType, World world, boolean forceSpawn, double x,
			double y, double z, int count, double radius)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(world.provider.getDimension())
				.spawnParticle(particleType, forceSpawn, x, y, z, count, radius, radius, radius, 0.005D);
	}

	public boolean canBreak()
	{
		return true;
	}

	@Override
	public NBTTagCompound getNBTShareTag(ItemStack stack)
	{
		return stack.getTagCompound();
	}

	@Override
	public boolean getShareTag()
	{
		return true;
	}

	@Override
	public boolean updateItemStackNBT(NBTTagCompound nbt)
	{
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		if (playerIn.isSneaking())
		{
			EnhancedProgression.proxy.setActionText((I18n.format("gui.checkessence.name") + " "
					+ getEssenceStored(stack) + "/" + getMaxEssence() + " " + (I18n.format("gui.essence.name"))));
		} else if (!worldIn.isRemote
				&& ItemStack.areItemsEqual(playerIn.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_rapidfire)))
		{
			if (useEssence(100, stack) && ItemNBTHelper.getString(stack, "tickSpell", "") != "RAPIDFIRE")
			{
				ItemNBTHelper.setInt(stack, "arrows", 0);
				ItemNBTHelper.setInt(stack, "delay", 0);
				ItemNBTHelper.setString(stack, "tickSpell", "RAPIDFIRE");

				playerIn.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			} else if (getEssenceStored(stack) < 100)
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			}
		} else if (!worldIn.isRemote
				&& ItemStack.areItemsEqual(playerIn.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_hunger)))
		{
			if (playerIn.canEat(false))
			{
				ItemNBTHelper.setString(stack, "spell", "HUNGER");

				playerIn.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10)
	{
		Block block = world.getBlockState(pos).getBlock();

		if (block instanceof BlockAltar)
		{
			boolean wanded;
			wanded = ((BlockAltar) block).onUsedByWand(player, stack, world, pos, side);
			if (wanded && world.isRemote)
			{
				player.swingArm(hand);
			}

			return wanded ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		} else if (block instanceof BlockLog)
		{
			if (ItemNBTHelper.getString(stack, "tickSpell", "") != "LOGGER")
			{
				replaceBlock.clear();
				ItemNBTHelper.setInt(stack, "currentBlockId", 0);
				ItemNBTHelper.setInt(stack, "delay", 0);

				for (BlockPos position : WOOD_SEARCH_AREA)
				{
					if (checkPlatform(position.getX(), position.getY(), position.getZ(), block, pos, world))
					{
						BlockPos toReplace = pos.add(position.getX(), position.getY(), position.getZ());
						replaceBlock.add(toReplace);
					}
				}

				player.setActiveHand(hand);
				System.out.println(world.isRemote);
				ItemNBTHelper.setString(stack, "tickSpell", "LOGGER");
				return EnumActionResult.SUCCESS;
			}

		} else if (block instanceof BlockOre && !player.isSneaking())
		{
			if (getWandTier() > 1)
			{
				ItemNBTHelper.setString(stack, "particleAction", "ORE EXTRACT");
				ItemNBTHelper.setInt(stack, "particleCount", 2);
				ItemNBTHelper.setDouble(stack, "posX", pos.getX());
				ItemNBTHelper.setDouble(stack, "posY", pos.getY());
				ItemNBTHelper.setDouble(stack, "posZ", pos.getZ());

				player.setActiveHand(hand);
				world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1, 1);

				if (!world.isRemote)
				{
					if (block == Blocks.EMERALD_ORE)
					{
						world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
						addEssence(500, stack);
					} else if (block == Blocks.DIAMOND_ORE)
					{
						world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState());
						addEssence(400, stack);
					} else if (block == Blocks.GOLD_ORE)
					{
						world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState());
						addEssence(300, stack);
					} else if (block == Blocks.IRON_ORE)
					{
						world.setBlockState(pos, Blocks.COAL_ORE.getDefaultState());
						addEssence(200, stack);
					} else if (block == Blocks.COAL_ORE)
					{
						world.setBlockState(pos, Blocks.STONE.getDefaultState());
						addEssence(100, stack);
					}
				}
				return EnumActionResult.SUCCESS;
			} else
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.betterwandneeded.name")));
				return EnumActionResult.FAIL;
			}
		} else
		{
			if (player.isSneaking())
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.checkessence.name") + " "
						+ getEssenceStored(stack) + "/" + getMaxEssence() + " " + (I18n.format("gui.essence.name"))));
				return EnumActionResult.SUCCESS;
			} else
			{
				if (!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(),
						new ItemStack(ModItems.spell_card_transmutation)))
				{
					if (block == Blocks.DIAMOND_BLOCK)
					{
						if (useEssence(1000, stack))
						{
							ItemNBTHelper.setString(stack, "particleAction", "TRANSMUTE DIAMOND");
							ItemNBTHelper.setInt(stack, "particleCount", 7);
							ItemNBTHelper.setDouble(stack, "posX", pos.getX());
							ItemNBTHelper.setDouble(stack, "posY", pos.getY() + 1.2);
							ItemNBTHelper.setDouble(stack, "posZ", pos.getZ());

							world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1, 1);
							player.swingArm(hand);
							world.setBlockState(pos, ModBlocks.altar.getDefaultState());

							return EnumActionResult.SUCCESS;
						} else
						{
							EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
							return EnumActionResult.FAIL;
						}
					} else
					{
						EnhancedProgression.proxy.setActionText((I18n.format("gui.invalidblock.name")));
						return EnumActionResult.FAIL;
					}
				} else if (!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(),
						new ItemStack(ModItems.spell_card_lantern)))
				{
					if (useEssence(5, stack))
					{
						BlockPos torchPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
						if (world.getBlockState(torchPos) == Blocks.AIR.getDefaultState())
						{
							ItemNBTHelper.setString(stack, "particleAction", "PLACE TORCH");
							ItemNBTHelper.setInt(stack, "particleCount", 7);
							ItemNBTHelper.setDouble(stack, "posX", pos.getX());
							ItemNBTHelper.setDouble(stack, "posY", pos.getY() + 1.2);
							ItemNBTHelper.setDouble(stack, "posZ", pos.getZ());

							world.setBlockState(torchPos, Blocks.TORCH.getDefaultState());
							player.swingArm(hand);

							return EnumActionResult.SUCCESS;
						} else
						{
							EnhancedProgression.proxy.setActionText((I18n.format("gui.obstructed.name")));
							return EnumActionResult.PASS;
						}
					} else
					{
						EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						return EnumActionResult.PASS;
					}
				} else if (!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(),
						new ItemStack(ModItems.spell_card_explosion)))
				{
					if (useEssence(50, stack))
					{
						world.createExplosion(player, pos.getX(), pos.getY() + 2, pos.getZ(), 3, false);
						return EnumActionResult.SUCCESS;
					} else
					{
						EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						return EnumActionResult.FAIL;
					}
				}
			}

		}
		return EnumActionResult.PASS;
	}

	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		System.out.println("WHY ISNT IT BEING CALLED ;( ;( ;( ;( ;(");
		ItemNBTHelper.setString(stack, "tickSpell", "");
		ItemNBTHelper.setString(stack, "spell", "");
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		if (ItemNBTHelper.getString(stack, "spell", "") == "HUNGER")
		{
			return 32;
		} else
		{
			return 72000;
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		if (ItemNBTHelper.getString(stack, "spell", "") == "HUNGER")
		{
			return EnumAction.EAT;
		}
		return EnumAction.BOW;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		if (ItemNBTHelper.getString(stack, "spell", "") == "HUNGER")
		{
			if (entityLiving instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer) entityLiving;
				if (useEssence(250, stack))
				{
					entityplayer.getFoodStats().addStats(20, 20);
					ItemNBTHelper.setString(stack, "spell", "");
					return stack;
				} else
				{
					EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
					return stack;
				}
			}

		}
		return stack;
	}
}
