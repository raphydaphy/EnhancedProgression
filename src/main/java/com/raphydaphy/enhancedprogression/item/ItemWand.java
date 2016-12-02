package com.raphydaphy.enhancedprogression.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.achievement.ICraftAchievement;
import com.raphydaphy.enhancedprogression.achievement.ModAchievements;
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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
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

/*
 * Main class to control all wands
 * For basic wand, wandTier = 1
 * For advanced, wandTier = 2
 * For master, wandTier = 3
 * maxEssence is 1k, 10k and 100k respectivly
 * canBreak is used when the logger spell is active
 * If the wand can break, it has a 1 in 1000 chance
 * Of doing so while harvesting a log.
 * Name is the same as the registry name and json
 */
public class ItemWand extends Item implements ICraftAchievement
{
	// these variables are the same across each instance of the wand
	protected String name;
	protected int wandTier;
	protected int maxEssence;
	protected boolean canBreak;
	
	// stores the current blocks beind destroyed with the vital extraction spell
	// may break in multiplayer, this needs to be moved to nbt or something
	private List<BlockPos> replaceBlock = new ArrayList<BlockPos>();

	/*
	 * Constructor for the wand
	 * All the variables set here are the same 
	 * across every ItemStack of that instance
	 */
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
	
	/*
	 * Gets the currently selected spell based on the offhand item
	 * Used to check what spell the wand should perform on rightclick
	 */
	public int getActiveSpell(ItemStack offhand)
	{
		if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_vital_extraction)))
		{
			return 80;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_lantern)))
		{
			return 81;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_explosion)))
		{
			return 82;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_fireball)))
		{
			return 83;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_rapidfire)))
		{
			return 84;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_transmutation)))
		{
			return 85;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_hunger)))
		{
			return 86;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_enhanced_extraction)))
		{
			return 87;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_flight)))
		{
			return 88;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_forcefield)))
		{
			return 89;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_bag)))
		{
			if (offhand.hasTagCompound())
			{
				return offhand.getTagCompound().getInteger("selectedSpell");
			}
		}
		return 0;
	}
	/*
	 * Gives the player an achievement when they craft a wand
	 * Used for the master, advanced and basic wand crafting
	 */
	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) 
	{
		switch(this.name)
		{
			case "basic_wand_copper":
			{
				return ModAchievements.craft_basic_wand;
			}
			case "basic_wand_tin":
			{
				return ModAchievements.craft_basic_wand;
			}
			case "advanced_wand":
			{
				return ModAchievements.craft_advanced_wand;
			}
			case "master_wand":
			{
				return ModAchievements.craft_master_wand;
			}
			default:
			{
				return null;
			}
		}
	}
	
	/*
	 * Puts the current wand instance into the creative tab
	 * Used with all instances of the wand item
	 */
	@Override
	public ItemWand setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}

	/*
	 * Client side only
	 * Used to register the item texture
	 * Also used for the wand custom holding json
	 * Called when the wand is initialized in ModItems
	 * 
	 */
	public void registerItemModel()
	{
		EnhancedProgression.proxy.registerItemRenderer(this, 0, name);
	}

	/*
	 * Gets the stored essence integer from NBT
	 * Essence is stored per ItemStack instance
	 */
	public int getEssenceStored(ItemStack stack)
	{
		return NBTLib.getInt(stack, "essenceStored", 0);
	}
	
	/*
	 * Removes essence from the wand
	 * Ran locally on only the instances that need essence removed
	 */
	public boolean useEssence(int amount, ItemStack stack)
	{
		if (getEssenceStored(stack) - amount >= 0)
		{
			NBTLib.setInt(stack, "essenceStored", NBTLib.getInt(stack, "essenceStored", 0) - amount);
			return true;
		}
		return false;
	}

	/*
	 * Opposite of useEssence
	 * Used to add more essence into the wand
	 * Uses wand NBT to access essence values
	 */
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

	/*
	 * Spawns particles based on paramaters used
	 * Can crash people in multiplayer (needs fixing)
	 */
	public static void spawnParticles(EnumParticleTypes particleType, World world, boolean forceSpawn, BlockPos pos,
			int count, double radius)
	{
		spawnParticlesServer(particleType, world, forceSpawn, pos.getX(), pos.getY(), pos.getZ(), count, radius);
	}
	
	/*
	 * Spawns particles server-side only
	 * Sometimes causes Null Pointer Exceptions
	 * Only crashes in multiplayer. Needs fixing.
	 */
	public static void spawnParticlesServer(EnumParticleTypes particleType, World world, boolean forceSpawn, double x,
			double y, double z, int count, double radius)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(world.provider.getDimension())
				.spawnParticle(particleType, forceSpawn, x, y, z, count, radius, radius, radius, 0.005D);
	}

	/*
	 * slightly de-obscated version of the setAim method
	 * setAim is found in the EntityArrow class
	 * Used to set a different landing position than the start pos raytrace
	 * Now removed because rapidfire spell dosent use it anymore
	 */
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
	
	/*
	 * Called whenever the wand is right-clicked in the air
	 * Used for the following spells and actions:
	 * - Checking essence stored on shift-rightclick
	 * - Forcefield spell on rightclick (not sneaking)
	 * - Fireball spell on rightclick (not sneaking)
	 * - Flight spell on rightclick (not sneaking)
	 * - Activates hunger spell animation on rightclick
	 * - Rapidfire spell on rightclick (not sneaking)
	 */
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
				&& ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_forcefield)))
		{
			if (NBTLib.getBoolean(player.getHeldItemOffhand(), "isActive", false) == false)
			{
				if (useEssence(5000, stack))
				{
					NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
					spawnParticles(EnumParticleTypes.PORTAL, worldIn, true, player.getPosition(), 100, 2);
					spawnParticles(EnumParticleTypes.FLAME, worldIn, true, player.getPosition(), 100, 2);
					player.setEntityInvulnerable(true);
					player.setActiveHand(hand);
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
				}
				else
				{
					EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
				}
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
		else if (ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_flight)))
		{
			EntityPlayer entityplayer = (EntityPlayer) player;
			if (entityplayer.capabilities.allowFlying == false)
			{
				if (useEssence(2500, stack))
				{
					entityplayer.capabilities.allowFlying = true;
					spawnParticles(EnumParticleTypes.END_ROD, worldIn, true, player.getPosition(), 100, 2);
					spawnParticles(EnumParticleTypes.ENCHANTMENT_TABLE, worldIn, true, player.getPosition(), 100, 2);
					spawnParticles(EnumParticleTypes.SPELL_WITCH, worldIn, true, player.getPosition(), 100, 2);
					worldIn.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 1, 1);
					entityplayer.swingArm(hand);
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
				}
				else
				{
					EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
				}
			}
			else
			{
				entityplayer.capabilities.allowFlying = false;
				spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, worldIn, true, player.getPosition(), 100, 2);
				spawnParticles(EnumParticleTypes.PORTAL, worldIn, true, player.getPosition(), 100, 2);
				worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.AMBIENT, 1, 1);
				entityplayer.swingArm(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
			
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	}

	/*
	 * Called when the wand is right-clicked onto a block (not in air)
	 * Used for the following spells and actions:
	 * - Activating altar crafting (not sneaking)
	 * - Checking altar level on shift+rightclick
	 * - Vital Extraction spell when rightclicking log
	 * - Enhanced Extraction when rightclicking ore
	 * - Transmmutation spell when rightclicking diamond blocks
	 * - Magic Lantern spell on any block (not sneaking)
	 * - Explosion spell on any block (not sneaking)
	 */
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
	
	/*
	 * Called once a tick when any wand instance is in a players inventory
	 * Used to de-activate spells when the wand is taken out of the selected slot
	 */
	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int itemSlot, boolean isSelected)
    {
		if (!isSelected)
		{
			EntityLivingBase entityIn = (EntityLivingBase) player;
			if (NBTLib.getBoolean(entityIn.getHeldItemOffhand(), "isActive", false) == true)
			{
				NBTLib.setBoolean(entityIn.getHeldItemOffhand(), "isActive", false);
			}
			
		}
    }

	/*
	 * Called once a tick while a player is holding right click
	 * Only called if a wand instance is in the active slot
	 * Used for the following spells:
	 * - Vital extraction to process nearby wood into dead wood
	 * - Forcefield spell to consume 50 essence per tick
	 * - Rapidfire spell to continue firing arrows with essence
	 */
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
			else if (!player.worldObj.isRemote
					&& ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_forcefield)))
			{
				if (useEssence(50, stack))
				{
					NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
					spawnParticles(EnumParticleTypes.PORTAL, player.worldObj, true, player.getPosition(), 100, 2);
					spawnParticles(EnumParticleTypes.FLAME, player.worldObj, true, player.getPosition(), 100, 2);
					player.setEntityInvulnerable(true);
				}
				else
				{
					player.setEntityInvulnerable(false);
					NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", false);
					EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
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
							entityArrow.setIsCritical(true);

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

	/*
	 * Gets the amount of time it takes to call onItemUseFinish
	 * Countdown starts when the player starts holding rightclick
	 * 32 is used whenever the hunger spell is active
	 * 32 is the same value as all food items use in vanilla
	 * 72000 is the same as bows and used for everything else
	 */
	public int getMaxItemUseDuration(ItemStack stack)
	{
		if (NBTLib.getBoolean(stack, "hungerSpellActive", false))
		{
			return 32;
		}
		return 72000;
	}

	/*
	 * Called when the player stops holding rightclick
	 * Only called when a wand instance is selected
	 * Used for the following spells
	 * - Forcefield spell to de-activate the forcefield (not working)
	 * - Fireball spell to shoot a fireball with essence cost
	 * - All spells that require rightclick to be held, to calculate essence
	 */
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase player, int timeLeft)
	{
		if (player.getHeldItemOffhand().getItem() instanceof ItemBase)
		{
			if (ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_fireball)) && !worldIn.isRemote)
			{
				if (useEssence(25, stack))
				{
					EntityLargeFireball bigBall = new EntityLargeFireball(worldIn, player, player.getLookVec().xCoord, player.getLookVec().yCoord, player.getLookVec().zCoord);
					bigBall.accelerationX = player.getLookVec().xCoord;
					bigBall.accelerationY = player.getLookVec().yCoord;
					bigBall.accelerationZ = player.getLookVec().zCoord;
					bigBall.explosionPower = 4;
					worldIn.spawnEntityInWorld(bigBall);
				}
				else
				{
					EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
				}
			}
			else if (ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_forcefield)) && !worldIn.isRemote)
			{
				player.setEntityInvulnerable(false);
			}
			
			if (NBTLib.getInt(player.getHeldItemOffhand(), "essenceStored", 0) > 0)
			{
				NBTLib.setInt(stack, "essenceStored", NBTLib.getInt(player.getHeldItemOffhand(), "essenceStored", 0));
				NBTLib.setInt(player.getHeldItemOffhand(), "essenceStored", 0);
			}
			NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", false);
		}
	}

	/*
	 * Gets the animation played when rightclick is held with the wand
	 * Uses the bow animation unless the hunger spell is active
	 * If hunger spell is active it uses the eating animation
	 */
	@Nullable
	public EnumAction getItemUseAction(ItemStack stack)
	{
		if (NBTLib.getBoolean(stack, "hungerSpellActive", false))
		{
			return EnumAction.EAT;
		}
		return EnumAction.BOW;
	}

	/*
	 * Creates a 10x30x10 area to search for logs
	 * Used by the vital extraction spell as search area
	 */
	private static Iterable<BlockPos.MutableBlockPos> WOOD_SEARCH_AREA = BlockPos
			.getAllInBoxMutable(new BlockPos(-5, -5, -5), new BlockPos(5, 25, 5));

	/*
	 * Checks if the given coordinates is a specific block
	 */
	boolean checkPlatform(int xOff, int yOff, int zOff, Block block, BlockPos pos, World world)
	{
		return world.getBlockState(pos.add(xOff, yOff, zOff)).getBlock() == block;
	}

	/*
	 * Called when the value from getItemMaxUseDuration reaches 0
	 * Value starts counting down when onItemRightClick is called
	 * Used for the hunger spell to feed the player when they finish eating
	 */
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
