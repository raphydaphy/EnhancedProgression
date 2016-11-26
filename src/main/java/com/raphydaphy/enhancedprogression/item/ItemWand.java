package com.raphydaphy.enhancedprogression.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.block.BlockAltar;
import com.raphydaphy.enhancedprogression.init.ModBlocks;
import com.raphydaphy.enhancedprogression.init.ModItems;
import com.raphydaphy.enhancedprogression.nbt.NBTLib;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOre;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
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

public class ItemWand extends Item
{
	protected String name;
	protected int wandTier;
	protected int maxEssence;
	protected boolean canBreak;

	private List<BlockPos> replaceBlock = new ArrayList<BlockPos>();

	public ItemWand(String name, int wandTier, int maxEssence, boolean canBreak)
	{
		this.name = name;
		setUnlocalizedName(name);
		setRegistryName(name);
		this.maxStackSize = 1;
		this.maxEssence = maxEssence;
		this.wandTier = wandTier;
		this.canBreak = canBreak;
	}

	@Override
	public ItemWand setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}

	public void registerItemModel()
	{
		EnhancedProgression.proxy.registerItemRenderer(this, 0, name);
	}

	public int getEssenceStored(ItemStack stack)
	{
		return NBTLib.getInt(stack, "essenceStored", 0);
	}

	public boolean useEssence(int amount, ItemStack stack)
	{
		if (getEssenceStored(stack) - amount >= 0)
		{
			NBTLib.setInt(stack, "essenceStored", NBTLib.getInt(stack, "essenceStored", 0) - amount);
			return true;
		}
		return false;
	}

	public boolean addEssence(int amount, ItemStack stack)
	{
		if (getEssenceStored(stack) + amount < maxEssence + 1)
		{
			NBTLib.setInt(stack, "essenceStored", NBTLib.getInt(stack, "essenceStored", 0) + amount);
		}
		else
		{
			NBTLib.setInt(stack, "essenceStored", maxEssence);
		}
		return true;
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

	private EntityArrow fancySetAim(EntityArrow target, Entity entity, float pitch, float yaw, float velocity,
			float knockbackResistance)
	{
		float x = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float y = -MathHelper.sin(pitch * 0.017453292F);
		float z = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		target.setThrowableHeading((double) x, (double) y, (double) z, velocity, 0);
		target.motionX += entity.motionX;
		target.motionZ += entity.motionZ;

		if (!entity.onGround)
		{
			target.motionY += entity.motionY;
		}

		return target;
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player, EnumHand hand)
	{
		if (player.isSneaking())
		{
			EnhancedProgression.proxy.setActionText((I18n.format("gui.checkessence.name") + " "
					+ getEssenceStored(stack) + "/" + maxEssence + " " + (I18n.format("gui.essence.name"))));
		}
		else if (!worldIn.isRemote
				&& ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_rapidfire)))
		{
			if (useEssence(100, stack))
			{
				NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
				NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay", 0);
				NBTLib.setInt(player.getHeldItemOffhand(), "arrowCount", 0);
				NBTLib.setInt(player.getHeldItemOffhand(), "essenceStored", getEssenceStored(stack));

				player.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
			else
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			}
		}
		else if (!worldIn.isRemote
				&& ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_fireball)))
		{
			if (NBTLib.getBoolean(player.getHeldItemOffhand(), "isActive", false) == false)
			{
				NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
				player.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
			else
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			}
		}
		else if (!worldIn.isRemote
				&& ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_hunger)))
		{
			if (player.canEat(false))
			{
				NBTLib.setBoolean(stack, "hungerSpellActive", true);
				player.setActiveHand(hand);
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
		}
		else if (block instanceof BlockLog)
		{
			if (NBTLib.getBoolean(player.getHeldItemOffhand(), "isActive", false) == false && ItemStack
					.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_vital_extraction)))
			{
				replaceBlock.clear();

				for (BlockPos position : WOOD_SEARCH_AREA)
				{
					if (checkPlatform(position.getX(), position.getY(), position.getZ(), block, pos, world))
					{
						BlockPos toReplace = pos.add(position.getX(), position.getY(), position.getZ());
						replaceBlock.add(toReplace);
					}
				}

				player.setActiveHand(hand);
				NBTLib.setInt(player.getHeldItemOffhand(), "currentBlockId", 0);
				NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay", 0);
				NBTLib.setInt(player.getHeldItemOffhand(), "essenceStored", getEssenceStored(stack));
				NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
				return EnumActionResult.SUCCESS;
			}

		}
		else if (block instanceof BlockOre && !player.isSneaking() && ItemStack
				.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_enhanced_extraction)))
		{
			spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true,
					new BlockPos(pos.getX(), pos.getY() + 1.2, pos.getZ()), 3, 1);
			player.swingArm(hand);
			world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1, 1);

			if (!world.isRemote)
			{
				if (block == Blocks.EMERALD_ORE)
				{
					world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
					addEssence(1000, stack);
				}
				else if (block == Blocks.DIAMOND_ORE)
				{
					world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState());
					addEssence(500, stack);
				}
				else if (block == Blocks.GOLD_ORE)
				{
					world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState());
					addEssence(300, stack);
				}
				else if (block == Blocks.IRON_ORE)
				{
					world.setBlockState(pos, Blocks.COAL_ORE.getDefaultState());
					addEssence(200, stack);
				}
				else if (block == Blocks.COAL_ORE)
				{
					world.setBlockState(pos, Blocks.STONE.getDefaultState());
					addEssence(100, stack);
				}

				player.swingArm(hand);
				return EnumActionResult.SUCCESS;
			}
		}
		else if (!player.isSneaking())
		{
			if (!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(),
					new ItemStack(ModItems.spell_card_transmutation)))
			{
				if (block == Blocks.DIAMOND_BLOCK)
				{
					if (useEssence(1000, stack))
					{
						spawnParticles(EnumParticleTypes.END_ROD, world, true,
								new BlockPos(pos.getX(), pos.getY() + 1.2, pos.getZ()), 20, 1);
						world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1, 1);
						player.swingArm(hand);
						world.setBlockState(pos, ModBlocks.altar.getDefaultState());
						return EnumActionResult.SUCCESS;
					}
					else
					{
						EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						return EnumActionResult.FAIL;
					}
				}
				else
				{
					EnhancedProgression.proxy.setActionText((I18n.format("gui.invalidblock.name")));
					return EnumActionResult.FAIL;
				}
			}
			else if (!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_lantern)))
			{
				BlockPos torchPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
				if (world.getBlockState(torchPos) == Blocks.AIR.getDefaultState())
				{
					if (useEssence(5, stack))
					{
						
						spawnParticles(EnumParticleTypes.FLAME, world, true,new BlockPos(torchPos.getX(), torchPos.getY(), torchPos.getZ()), 15, 1);
						world.setBlockState(torchPos, Blocks.TORCH.getDefaultState());
						player.swingArm(hand);
						return EnumActionResult.SUCCESS;
					}
					else
					{
						EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						return EnumActionResult.PASS;
					}
				}
				else
				{
					EnhancedProgression.proxy.setActionText((I18n.format("gui.obstructed.name")));
					return EnumActionResult.PASS;
				}
			}
			else if (!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(),
					new ItemStack(ModItems.spell_card_explosion)))
			{
				if (useEssence(50, stack))
				{
					world.createExplosion(player, pos.getX(), pos.getY() + 2, pos.getZ(), 3, false);
					return EnumActionResult.SUCCESS;
				}
				else
				{
					EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
					return EnumActionResult.FAIL;
				}
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if (NBTLib.getBoolean(player.getHeldItemOffhand(), "isActive", false) == true)
		{
			if (ItemStack.areItemsEqual(player.getHeldItemOffhand(),
					new ItemStack(ModItems.spell_card_vital_extraction)))
			{
				if (NBTLib.getInt(player.getHeldItemOffhand(), "tickDelay", 0) == 0
						&& replaceBlock.size() > NBTLib.getInt(player.getHeldItemOffhand(), "currentBlockId", 0)
						&& !player.worldObj.isRemote)
				{
					BlockPos curBlock = replaceBlock
							.get(NBTLib.getInt(player.getHeldItemOffhand(), "currentBlockId", 0));
					player.worldObj.playSound(null, curBlock, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1, 1);
					player.worldObj.setBlockState(curBlock, ModBlocks.dead_log.getDefaultState());
					spawnParticles(EnumParticleTypes.SPELL_WITCH, player.worldObj, true, curBlock, 7, 1);
					this.addEssence(ThreadLocalRandom.current().nextInt(2, 15 + 1), player.getHeldItemOffhand());

					NBTLib.setInt(player.getHeldItemOffhand(), "currentBlockId",
							NBTLib.getInt(player.getHeldItemOffhand(), "currentBlockId", 0) + 1);
					NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay", 10);

					if (ThreadLocalRandom.current().nextInt(1, 1000 + 1) == 555 && canBreak)
					{
						player.worldObj.playSound(null, curBlock, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS,
								1, 1);
						stack.stackSize = 0;
						NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", false);
					}
				}
				else if (replaceBlock.size() > NBTLib.getInt(player.getHeldItemOffhand(), "currentBlockId", 0)
						&& !player.worldObj.isRemote)
				{
					NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay",
							NBTLib.getInt(player.getHeldItemOffhand(), "tickDelay", 0) - 1);
				}
				else if (!player.worldObj.isRemote)
				{
					NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", false);
				}
			}
			else if (ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_rapidfire)))
			{
				if (NBTLib.getInt(player.getHeldItemOffhand(), "tickDelay", 0) == 0)
				{
					if (useEssence(NBTLib.getInt(player.getHeldItemOffhand(), "arrowCount", 0),
							player.getHeldItemOffhand()))
					{
						if (!player.worldObj.isRemote)
						{
							NBTLib.setInt(player.getHeldItemOffhand(), "arrowCount",
									NBTLib.getInt(player.getHeldItemOffhand(), "arrowCount", 0) + 1);
							NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay", 5);

							ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
							ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow
									? itemstack.getItem() : Items.ARROW));
							EntityArrow entityArrow = itemarrow.createArrow(player.worldObj, itemstack, player);

							entityArrow = fancySetAim(entityArrow, player, player.rotationPitch, player.rotationYaw,
									5.0F, 1.0F);
							entityArrow.setKnockbackStrength(1);
							entityArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

							player.worldObj.spawnEntityInWorld(entityArrow);
							player.worldObj.playSound(null, player.posX, player.posY, player.posZ,
									SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F,
									1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
						}
					}
					else
					{
						EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", false);
					}

				}
				else
				{
					NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay",
							NBTLib.getInt(player.getHeldItemOffhand(), "tickDelay", 0) - 1);
				}
			}
		}
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		if (NBTLib.getBoolean(stack, "hungerSpellActive", false))
		{
			return 32;
		}
		return 72000;
	}

	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase player, int timeLeft)
	{
		if (ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_fireball)) && !worldIn.isRemote)
		{
			if (useEssence(40, stack))
			{
				EntityLargeFireball bigBall = new EntityLargeFireball(worldIn);
				bigBall.setPosition(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
				bigBall.motionX = player.getLookVec().xCoord;
				bigBall.motionY = player.getLookVec().yCoord;
				bigBall.motionZ = player.getLookVec().zCoord;
				worldIn.spawnEntityInWorld(bigBall);
			}
			else
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
			}
		}
		if (NBTLib.getInt(player.getHeldItemOffhand(), "essenceStored", 0) > 0)
		{
			NBTLib.setInt(stack, "essenceStored", NBTLib.getInt(player.getHeldItemOffhand(), "essenceStored", 0));
			NBTLib.setInt(player.getHeldItemOffhand(), "essenceStored", 0);
		}
		NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", false);
	}

	@Nullable
	public EnumAction getItemUseAction(ItemStack stack)
	{
		if (NBTLib.getBoolean(stack, "hungerSpellActive", false))
		{
			return EnumAction.EAT;
		}
		return EnumAction.BOW;
	}

	private static Iterable<BlockPos.MutableBlockPos> WOOD_SEARCH_AREA = BlockPos
			.getAllInBoxMutable(new BlockPos(-5, -5, -5), new BlockPos(5, 25, 5));

	boolean checkPlatform(int xOff, int yOff, int zOff, Block block, BlockPos pos, World world)
	{
		return world.getBlockState(pos.add(xOff, yOff, zOff)).getBlock() == block;
	}

	@Nullable
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{		
		if (NBTLib.getBoolean(stack, "hungerSpellActive", false) == true)		
		{		
		if (entityLiving instanceof EntityPlayer)		
		{		
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;		
			if (useEssence(500, stack))		
			{		
				entityplayer.getFoodStats().addStats(20, 20);		
				NBTLib.setBoolean(stack, "hungerSpellActive", false);		
				return stack;		
			}		
			else		
			{		
				EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));		
				return stack;		
			}		
		}		
		}		
		return stack;		
	}
}
