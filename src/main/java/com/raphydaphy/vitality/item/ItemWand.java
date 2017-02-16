package com.raphydaphy.vitality.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.achievement.ICraftAchievement;
import com.raphydaphy.vitality.achievement.ModAchievements;
import com.raphydaphy.vitality.block.BlockAltar;
import com.raphydaphy.vitality.block.BlockSpellForge;
import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.nbt.NBTLib;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
			// ID of the vital extraction spell
			return 800;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_lantern_1)))
		{
			// ID of the magic lantern spell
			return 810;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_lantern_2)))
		{
			// ID of the imbued lantern spell
			return 811;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_lantern_3)))
		{
			// ID of the fluxed lantern spell
			return 812;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_explosion_1)))
		{
			// ID of the explosion spell
			return 820;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_explosion_2)))
		{
			// ID of the explosion spell
			return 821;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_explosion_3)))
		{
			// ID of the explosion spell
			return 822;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_fireball_1)))
		{
			// ID of the radiant fireball spell
			return 830;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_fireball_2)))
		{
			// ID of the imbued fireball spell
			return 831;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_fireball_3)))
		{
			// ID of the fluxed fireball spell
			return 832;
		}
		else if (ItemStack.areItemsEqual(offhand,  new ItemStack(ModItems.spell_card_fertilization_1)))
		{
			// ID of the renewed fertilization spell
			return 900;
		}
		else if (ItemStack.areItemsEqual(offhand,  new ItemStack(ModItems.spell_card_fertilization_2)))
		{
			// ID of the imbued fertilization spell
			return 901;
		}
		else if (ItemStack.areItemsEqual(offhand,  new ItemStack(ModItems.spell_card_fertilization_3)))
		{
			// ID of the fluxed fertilization spell
			return 902;
		}
		else if (ItemStack.areItemsEqual(offhand,  new ItemStack(ModItems.spell_card_placement_1)))
		{
			// ID of the angelic placement spell
			return 910;
		}
		else if (ItemStack.areItemsEqual(offhand,  new ItemStack(ModItems.spell_card_placement_2)))
		{
			// ID of the imbued placement spell
			return 911;
		}
		else if (ItemStack.areItemsEqual(offhand,  new ItemStack(ModItems.spell_card_placement_3)))
		{
			// ID of the fluxed placement spell
			return 912;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_rapidfire_1)))
		{
			// ID of the rapidfire spell
			return 840;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_rapidfire_2)))
		{
			// ID of the rapidfire spell
			return 841;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_transmutation)))
		{
			// ID of the cryptic transmutation spell
			return 850;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_hunger)))
		{
			// ID of the hunger spell
			return 860;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_lightning_1)))
		{
			// ID of the imbued lightning spell
			return 920;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_lightning_2)))
		{
			// ID of the fluxed lightning spell
			return 921;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_enhanced_extraction_1)))
		{
			// ID of the enhanced extraction spell
			return 870;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_enhanced_extraction_2)))
		{
			// ID of the enhanced extraction spell
			return 871;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_flight)))
		{
			// ID of the flight spell
			return 880;
		}
		else if (ItemStack.areItemsEqual(offhand, new ItemStack(ModItems.spell_card_forcefield)))
		{
			// ID of the forcefield spell
			return 890;
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
		Vitality.proxy.registerItemRenderer(this, 0, name);
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
		// Checks if the amount of essence to add will not cause the wand to go over the capacity
		if (getEssenceStored(stack) + amount < maxEssence + 1)
		{
			// add the amount of essence normally
			NBTLib.setInt(stack, "essenceStored", NBTLib.getInt(stack, "essenceStored", 0) + amount);
		}
		// If the user tried to add too much essence
		else
		{
			// fill the wand to its capacity, but don't go over
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
		// simply runs the main particle spawning method without so many arguments
		spawnParticlesServer(particleType, world, forceSpawn, pos.getX(), pos.getY(), pos.getZ(), count, radius);
	}
	
		
	public static void spawnParticlesServer(EnumParticleTypes particleType, World world, boolean forceSpawn, double x,
			double y, double z, int count, double radius)
	{
		// spawns some particles through the server instance
		// this prevents NullPointerExceptions but also seems to cause them sometimes
		// TODO: Fix crashes
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
		if (player.isSneaking() && (getActiveSpell(player.getHeldItemOffhand()) > 919 || getActiveSpell(player.getHeldItemOffhand()) < 910))
		{
			Vitality.proxy.setActionText((I18n.format("gui.checkessence.name") + " "
					+ getEssenceStored(stack) + "/" + maxEssence + " " + (I18n.format("gui.essence.name"))));
		}
		else if (!worldIn.isRemote && getActiveSpell(player.getHeldItemOffhand()) > 839 && getActiveSpell(player.getHeldItemOffhand()) < 850)
		{
			int essenceVal = 100;
			if (getActiveSpell(player.getHeldItemOffhand()) == 841)
			{
				essenceVal = 250;
			}
			if (useEssence(essenceVal, stack))
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
				Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			}
		}
		else if (!worldIn.isRemote && getActiveSpell(player.getHeldItemOffhand()) == 890)
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
					Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
				}
			}
		}
		else if (!worldIn.isRemote && getActiveSpell(player.getHeldItemOffhand()) > 829 && getActiveSpell(player.getHeldItemOffhand()) < 840)
		{
			if (NBTLib.getBoolean(player.getHeldItemOffhand(), "isActive", false) == false)
			{
				NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
				player.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		else if (!worldIn.isRemote && getActiveSpell(player.getHeldItemOffhand()) == 860)
		{
			if (player.canEat(false))
			{
				NBTLib.setBoolean(stack, "hungerSpellActive", true);
				player.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		else if (getActiveSpell(player.getHeldItemOffhand()) == 880)
		{
			EntityPlayer entityplayer = (EntityPlayer) player;
			if (entityplayer.capabilities.allowFlying == false)
			{
				if (useEssence(2500, stack))
				{
					entityplayer.capabilities.allowFlying = true;
					if (!worldIn.isRemote)
					{
						spawnParticles(EnumParticleTypes.END_ROD, worldIn, true, player.getPosition(), 100, 2);
						spawnParticles(EnumParticleTypes.ENCHANTMENT_TABLE, worldIn, true, player.getPosition(), 100, 2);
						spawnParticles(EnumParticleTypes.SPELL_WITCH, worldIn, true, player.getPosition(), 100, 2);
					}
					worldIn.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 1, 1);
					entityplayer.swingArm(hand);
					player.getCooldownTracker().setCooldown(this, 100);
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
				}
				else
				{
					Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name"))); 
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
				}
			}
			else
			{
				entityplayer.capabilities.allowFlying = false;
				if (!worldIn.isRemote)
				{
					spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, worldIn, true, player.getPosition(), 100, 2);
					spawnParticles(EnumParticleTypes.PORTAL, worldIn, true, player.getPosition(), 100, 2);
				}
				worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.AMBIENT, 1, 1);
				entityplayer.swingArm(hand);
				player.getCooldownTracker().setCooldown(this, 100);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
			
		}
		// Angelic Placement
		else if (getActiveSpell(player.getHeldItemOffhand()) > 909 && getActiveSpell(player.getHeldItemOffhand()) < 920)
		{
			int cooldown = 20;
			int essenceVal = 50;
			
			// 1 = dirt, 2 = planks, 3 = cobble
			int currentActiveBlock = player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock");
			if (currentActiveBlock == 0)
			{
				player.getHeldItemOffhand().getTagCompound().setInteger("activeBlock", 1);
			}
			if (getActiveSpell(player.getHeldItemOffhand()) == 911)
			{
				cooldown = 10;
				essenceVal = 25;
			}
			else if (getActiveSpell(player.getHeldItemOffhand()) == 912)
			{
				cooldown = 5;
				essenceVal = 10;
			}
			
			Vec3d lookVec = player.getLookVec();
	        double posX = player.posX + lookVec.xCoord * 2;
	        double posY = player.posY + player.eyeHeight + lookVec.yCoord * 2;
	        double posZ = player.posX + lookVec.zCoord * 2;
	        BlockPos pos = new BlockPos(posX, posY, posZ);
	        Block therealblock = worldIn.getBlockState(pos).getBlock();
	        EnumFacing facing = EnumFacing.getFacingFromVector((float) lookVec.xCoord, (float) lookVec.yCoord, (float) lookVec.zCoord);
	        
			if (pos != null)
			{
				if (player.isSneaking())
				{
					if (therealblock == Blocks.DIRT)
					{
						if (currentActiveBlock != 1)
						{
							player.getHeldItemOffhand().getTagCompound().setInteger("activeBlock", 1);
							Vitality.proxy.setActionText((I18n.format("gui.setblockplacement.name") + " Dirt"));
						}
					}
					else if (therealblock == Blocks.PLANKS)
					{
						if (currentActiveBlock != 2)
						{
							player.getHeldItemOffhand().getTagCompound().setInteger("activeBlock", 2);
							Vitality.proxy.setActionText((I18n.format("gui.setblockplacement.name") + " Oak Planks"));
						}
					}
					else if (therealblock == Blocks.COBBLESTONE)
					{
						if (currentActiveBlock != 3)
						{
							player.getHeldItemOffhand().getTagCompound().setInteger("activeBlock", 3);
							Vitality.proxy.setActionText((I18n.format("gui.setblockplacement.name") + " Cobblestone"));
						}
					}
					else
					{
						Vitality.proxy.setActionText((I18n.format("gui.checkessence.name") + " "
								+ getEssenceStored(stack) + "/" + maxEssence + " " + (I18n.format("gui.essence.name"))));
					}
				}
				else if (therealblock == Blocks.AIR || therealblock == Blocks.FLOWING_LAVA || therealblock == Blocks.FLOWING_WATER)
				{
					if (useEssence(essenceVal, stack))
					{
						if (player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock") == 1)
						{
							if (!worldIn.isRemote)
							{
								tryPlaceABlock(Blocks.DIRT, stack, player, worldIn, pos, facing, (float) (posX - pos.getX()), (float) (posY - pos.getY()), (float) (posZ - pos.getZ()));
							}
						}
						else if (player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock") == 2)
						{
							if (!worldIn.isRemote)
							{
								tryPlaceABlock(Blocks.PLANKS, stack, player, worldIn, pos, facing, (float) (posX - pos.getX()), (float) (posY - pos.getY()), (float) (posZ - pos.getZ()));
							}
						}
						else if (player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock") == 3)
						{
							if (!worldIn.isRemote)
							{
								tryPlaceABlock(Blocks.COBBLESTONE, stack, player, worldIn, pos, facing, (float) (posX - pos.getX()), (float) (posY - pos.getY()), (float) (posZ - pos.getZ()));
							}
						}
						if (!worldIn.isRemote)
						{
							spawnParticles(EnumParticleTypes.FLAME, player.worldObj, true, pos, 4, 1);
						}
						player.swingArm(hand);
						player.getCooldownTracker().setCooldown(this, cooldown);
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
					}
				}
				else
				{
					tryPlaceABlock(Blocks.DIRT, stack, player, worldIn, pos, facing, (float) (posX - pos.getX()), (float) (posY - pos.getY()), (float) (posZ - pos.getZ()));
					/*
					if (worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.AIR)
					{
						if (useEssence(essenceVal, stack))
						{
							if (player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock") == 1)
							{
								if (!worldIn.isRemote)
								{
									worldIn.setBlockState(theblock.getBlockPos().add(0, 1, 0), Blocks.DIRT.getDefaultState());
								}
							}
							else if (player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock") == 2)
							{
								if (!worldIn.isRemote)
								{
									worldIn.setBlockState(theblock.getBlockPos().add(0, 1, 0), Blocks.PLANKS.getDefaultState());
								}
							}
							else if (player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock") == 3)
							{
								if (!worldIn.isRemote)
								{
									worldIn.setBlockState(theblock.getBlockPos().add(0, 1, 0), Blocks.COBBLESTONE.getDefaultState());
								}
							}
							if (!worldIn.isRemote)
							{
								spawnParticles(EnumParticleTypes.FLAME, player.worldObj, true, theblock.getBlockPos().add(0, 1, 0), 4, 1);
							}
							
							if (currentActiveBlock == 1)
							{
								worldIn.playSound(null, theblock.getBlockPos().add(0, 1, 0), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1, 1);
							}
							else if (currentActiveBlock == 2)
							{
								worldIn.playSound(null, theblock.getBlockPos().add(0, 1, 0), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);
							}
							else
							{
								worldIn.playSound(null, theblock.getBlockPos().add(0, 1, 0), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1, 1);
							}
							
							player.swingArm(hand);
							player.getCooldownTracker().setCooldown(this, cooldown);
							return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
						}
					}
					*/
				}
				
			}
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
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

		// If the player right-clicks on an altar
		// Used for all altar crafting recipes
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
		else if (block instanceof BlockSpellForge)
		{
			((BlockSpellForge) block).onUsedByWand(player, stack, world, pos, side);
			if (world.isRemote)
			{
				player.swingArm(hand);
			}

			return EnumActionResult.SUCCESS;
		}
		// If the player right-clicks on a log
		// Used for the Vital Extraction spell
		else if (block instanceof BlockLog)
		{
			if (NBTLib.getBoolean(player.getHeldItemOffhand(), "isActive", false) == false &&
				 getActiveSpell(player.getHeldItemOffhand()) == 800)
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
		// If the player right-clicks on a ore block
		// Used for Enhancced Extraction spell
		else if (block instanceof BlockOre && !player.isSneaking() && 
				getActiveSpell(player.getHeldItemOffhand()) > 869 && 
				getActiveSpell(player.getHeldItemOffhand()) < 880)
		{
			if (!world.isRemote)
			{
				spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, player.worldObj, true, pos, 3, 1);
			}
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

				int cooldown = 10;
				if (getActiveSpell(player.getHeldItemOffhand()) == 871)
				{
					cooldown = 3;
				}
				player.swingArm(hand);
				player.getCooldownTracker().setCooldown(this, cooldown);
				return EnumActionResult.SUCCESS;
			}
		}
		// For spells that don't use a specific block class
		else if (!player.isSneaking())
		{
			if (!world.isRemote && getActiveSpell(player.getHeldItemOffhand()) == 850)
			{
				// Checks if player right-clicks a diamond block
				// Used for the Cryptic Transmutation spell to make an altar
				if (block == Blocks.DIAMOND_BLOCK)
				{
					if (useEssence(1000, stack))
					{
						spawnParticles(EnumParticleTypes.END_ROD, world, true,
								new BlockPos(pos.getX(), pos.getY() + 1.2, pos.getZ()), 20, 1);
						world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1, 1);
						player.swingArm(hand);
						player.getCooldownTracker().setCooldown(this, 100);
						world.setBlockState(pos, ModBlocks.altar.getDefaultState());
						return EnumActionResult.SUCCESS;
					}
					else
					{
						Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						return EnumActionResult.FAIL;
					}
				}
				else if (block == Blocks.BOOKSHELF)
				{
					if (useEssence(500, stack))
					{
						spawnParticles(EnumParticleTypes.FIREWORKS_SPARK, world, true, pos, 50, 1);
						spawnParticles(EnumParticleTypes.SMOKE_LARGE, world, true, pos, 50, 1);
						world.playSound(null, pos, SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.BLOCKS, 1, 1);
						player.swingArm(hand);
						player.getCooldownTracker().setCooldown(this, 100);
						net.minecraft.inventory.InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(),
								new ItemStack(ModItems.spell_bag));
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
						return EnumActionResult.SUCCESS;
					}
					else
					{
						Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						return EnumActionResult.FAIL;
					}
				}
				else
				{
					Vitality.proxy.setActionText((I18n.format("gui.invalidblock.name")));
					return EnumActionResult.FAIL;
				}
			}
			else if (getActiveSpell(player.getHeldItemOffhand()) > 819 && getActiveSpell(player.getHeldItemOffhand()) < 830)
			{
				int essenceAmount = 50;
				int blastPower = 50;
				int radius = 2;
				
				if (getActiveSpell(player.getHeldItemOffhand()) == 821)
				{
					essenceAmount = 80;
					blastPower = 150;
					radius = 4;
				}
				else if (getActiveSpell(player.getHeldItemOffhand()) == 822)
				{
					essenceAmount = 250;
					blastPower = 300;
					radius = 8;
				}
				if (useEssence(essenceAmount, stack))
				{
			        BlockPos bombPos = pos;
					world.playSound(null, bombPos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 1);
					if (!world.isRemote)
					{
						spawnParticles(EnumParticleTypes.EXPLOSION_HUGE, world, true, bombPos, radius*10, radius);
					}
					List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(bombPos.add(-radius, -radius, -radius), bombPos.add(radius,radius,radius)));
					for (EntityLivingBase living : entities)
					{
						if (living != player)
						{
							living.attackEntityFrom(DamageSource.magic, blastPower);
						}
					}
					player.swingArm(hand);
					player.getCooldownTracker().setCooldown(this, 25);
					return EnumActionResult.SUCCESS;
				}
				else
				{
					Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
					return EnumActionResult.FAIL;
				}
			}
			else if (!world.isRemote && getActiveSpell(player.getHeldItemOffhand()) > 899 && getActiveSpell(player.getHeldItemOffhand()) < 910)
			{
				if (block instanceof IGrowable)
		        {
					int essenceVal = 100;
					if (getActiveSpell(player.getHeldItemOffhand()) == 901)
					{
						essenceVal = 50;
					}
					else if (getActiveSpell(player.getHeldItemOffhand()) == 902)
					{
						essenceVal = 25;
					}
		            IGrowable igrowable = (IGrowable)block;
		            IBlockState iblockstate = world.getBlockState(pos);
		            if (igrowable.canGrow(world, pos, iblockstate, world.isRemote))
		            {
		                if (!world.isRemote && useEssence(essenceVal, stack))
		                {
		                    if (igrowable.canUseBonemeal(world, world.rand, pos, iblockstate))
		                    {
		                    	spawnParticles(EnumParticleTypes.VILLAGER_HAPPY, world, true, pos, 15, 1);
		                        igrowable.grow(world, world.rand, pos, iblockstate);
		                        return EnumActionResult.SUCCESS;
		                    }
		                }
		                
		            }
		        }

			}
			else if (getActiveSpell(player.getHeldItemOffhand()) == 800)
			{
				if (block instanceof BlockCrops)
				{
					if (!world.isRemote)
					{ spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true,pos, 10, 1); }
					addEssence(((Integer)world.getBlockState(pos).getValue(BlockCrops.AGE)).intValue() * 10, stack);
					world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
					
					return EnumActionResult.SUCCESS;
				}
				else if (block instanceof BlockSapling)
				{
					addEssence(35, stack);
					if (!world.isRemote)
					{ spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true,pos, 10, 1); }
					world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
					return EnumActionResult.SUCCESS;
				}
				else if (block instanceof BlockCactus)
				{
					addEssence(25, stack);
					if (!world.isRemote)
					{ spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true,pos, 10, 1); }
					world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
					return EnumActionResult.SUCCESS;
				}
			}
			else if (getActiveSpell(player.getHeldItemOffhand()) > 919 && getActiveSpell(player.getHeldItemOffhand()) < 930)
			{
				int essenceVal = 250;
				int cooldown = 20;
				if (getActiveSpell(player.getHeldItemOffhand()) == 921)
				{
					essenceVal = 100;
					cooldown = 10;
				}
				EntityLightningBolt lightning = new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false);
				if (useEssence(essenceVal, stack))
				{
					if (!world.isRemote)
					{
						world.addWeatherEffect(lightning);
					}
					player.getCooldownTracker().setCooldown(this, cooldown);
					return EnumActionResult.SUCCESS;
				}
				else
				{
					return EnumActionResult.PASS;
				}
				
			}
			// If the Magic Lantern spell is used on a block
			else if (!world.isRemote && getActiveSpell(player.getHeldItemOffhand()) > 809 && getActiveSpell(player.getHeldItemOffhand()) < 820)
			{
				int essenceVal = 5;
				int cooldown = 20;
				if (getActiveSpell(player.getHeldItemOffhand()) == 811)
				{
					essenceVal = 2;
					cooldown = 10;
				}
				else if (getActiveSpell(player.getHeldItemOffhand()) == 812)
				{
					essenceVal = 0;
					cooldown = 5;
				}
				BlockPos torchPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
				if (world.getBlockState(torchPos) == Blocks.AIR.getDefaultState() ||
					world.getBlockState(torchPos) == Blocks.FLOWING_WATER.getDefaultState() ||
					world.getBlockState(torchPos) == Blocks.WATER.getDefaultState() ||
					world.getBlockState(torchPos) == Blocks.LAVA.getDefaultState() ||
					world.getBlockState(torchPos) == Blocks.FLOWING_LAVA.getDefaultState())
				{
					if (useEssence(essenceVal, stack))
					{
						spawnParticles(EnumParticleTypes.FLAME, world, true,new BlockPos(torchPos.getX(), torchPos.getY(), torchPos.getZ()), 15, 1);
						world.setBlockState(torchPos, Blocks.TORCH.getDefaultState());
						player.swingArm(hand);
						player.getCooldownTracker().setCooldown(this, cooldown);
						return EnumActionResult.SUCCESS;
					}
					else
					{
						Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						return EnumActionResult.PASS;
					}
				}
				else
				{
					Vitality.proxy.setActionText((I18n.format("gui.obstructed.name")));
					return EnumActionResult.PASS;
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
		if (player instanceof EntityLivingBase)
		{
			EntityLivingBase entityIn = (EntityLivingBase) player;
			if (entityIn.getHeldItemOffhand() != null)
			{
				if (entityIn.getHeldItemOffhand().getItem() instanceof ItemSpellBag)
				{
					
				}
			}
			if (entityIn.getHeldItemMainhand() != null)
			{
				if (!(entityIn.getHeldItemMainhand().getItem() instanceof ItemWand))
				{
					if (NBTLib.getBoolean(entityIn.getHeldItemOffhand(), "isActive", false) == true)
					{
						NBTLib.setBoolean(entityIn.getHeldItemOffhand(), "isActive", false);
					}
				}
			}
		}
    }
	
	public boolean placeABlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState newState)
	{
        if (!world.setBlockState(pos, newState, 3))
        {
        	return false;
        }
        
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == newState.getBlock())
        {
            state.getBlock().onBlockPlacedBy(world, pos, state, player, stack);
        }
 
        return true;
    }

	public EnumActionResult tryPlaceABlock(Block block, ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing,float hitX, float hitY, float hitZ) 
	{
        if (player.canPlayerEdit(pos, facing, stack) && world.canBlockBePlaced(block, pos, false, facing, null, stack)) {
            if (placeABlock(stack, player, world, pos, block.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, 0, player))) 
            {
                IBlockState placedState = world.getBlockState(pos);
                
                if (block == Blocks.DIRT)
                {
                	world.playSound(player, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, (Blocks.COBBLESTONE.getSoundType().getVolume() + 1.0F) / 2.0F, Blocks.DIRT.getSoundType().getPitch() * 0.8F);
                }
                else if (block == Blocks.PLANKS)
                {
                	world.playSound(player, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, (Blocks.PLANKS.getSoundType().getVolume() + 1.0F) / 2.0F, Blocks.PLANKS.getSoundType().getPitch() * 0.8F);
                }
                else
                {
                	world.playSound(player, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, (Blocks.COBBLESTONE.getSoundType().getVolume() + 1.0F) / 2.0F, Blocks.COBBLESTONE.getSoundType().getPitch() * 0.8F);
                }
            }
 
            return EnumActionResult.SUCCESS;
        }
 
        return EnumActionResult.FAIL;
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
			if (getActiveSpell(player.getHeldItemOffhand()) == 800)
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
			else if (!player.worldObj.isRemote && getActiveSpell(player.getHeldItemOffhand()) == 890)
			{
				NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
				spawnParticles(EnumParticleTypes.PORTAL, player.worldObj, true, player.getPosition(), 100, 2);
				spawnParticles(EnumParticleTypes.FLAME, player.worldObj, true, player.getPosition(), 100, 2);
				player.setEntityInvulnerable(true);
			}
			else if (getActiveSpell(player.getHeldItemOffhand()) > 839 && getActiveSpell(player.getHeldItemOffhand()) < 850)
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
							
							if (getActiveSpell(player.getHeldItemOffhand()) == 840)
							{
								NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay", 5);
							}
							else if (getActiveSpell(player.getHeldItemOffhand()) == 841)
							{
								NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay", 1);
							}
							
							
							ItemStack itemstack = new ItemStack(Items.SPECTRAL_ARROW);
							ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow
									? itemstack.getItem() : Items.ARROW));
							EntityArrow entityArrow = itemarrow.createArrow(player.worldObj, itemstack, player);

							entityArrow = fancySetAim(entityArrow, player, player.rotationPitch, player.rotationYaw,
									5.0F, 1.0F);
							entityArrow.setKnockbackStrength(1);
							entityArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
							//entityArrow.setIsCritical(true);

							player.worldObj.spawnEntityInWorld(entityArrow);
							
							player.worldObj.playSound(null, player.posX, player.posY, player.posZ,
									SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F,
									1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
						}
					}
					else
					{
						Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
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
		if (player.getHeldItemOffhand().getItem() instanceof ItemBase || player.getHeldItemMainhand().getItem() instanceof ItemSpellBag)
		{
			EntityPlayer playerIn = (EntityPlayer) player;
			if (getActiveSpell(player.getHeldItemOffhand()) > 829 && getActiveSpell(player.getHeldItemOffhand()) < 840 && !worldIn.isRemote)
			{
				int essenceCost = 25;
				if (getActiveSpell(player.getHeldItemOffhand()) == 831)
				{
					essenceCost = 100;
				}
				else if (getActiveSpell(player.getHeldItemOffhand()) == 832)
				{
					essenceCost = 300;
				}
				if (useEssence(essenceCost, stack))
				{
					playerIn.getCooldownTracker().setCooldown(this, 50);
					EntityLargeFireball bigBall = new EntityLargeFireball(worldIn, player, player.getLookVec().xCoord, player.getLookVec().yCoord, player.getLookVec().zCoord);
					bigBall.accelerationX = player.getLookVec().xCoord;
					bigBall.accelerationY = player.getLookVec().yCoord;
					bigBall.accelerationZ = player.getLookVec().zCoord;
					bigBall.explosionPower = 4;
					if (getActiveSpell(player.getHeldItemOffhand()) == 831)
					{
						bigBall.explosionPower = 6;
					}
					else if (getActiveSpell(player.getHeldItemOffhand()) == 832)
					{
						bigBall.explosionPower = 10;
					}
					worldIn.spawnEntityInWorld(bigBall);
				}
				else
				{
					Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
				}
			}
			else if (getActiveSpell(player.getHeldItemOffhand()) == 890 && !worldIn.isRemote)
			{
				playerIn.getCooldownTracker().setCooldown(this, 1000);
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
					entityplayer.getCooldownTracker().setCooldown(this, 100);
					NBTLib.setBoolean(stack, "hungerSpellActive", false);		
					return stack;
				}		
				else
				{
					if (worldIn.isRemote)
					{
						Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));		
					}
					return stack;		
				}
			}
		}
		return stack;
	}
}
