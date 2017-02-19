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
import com.raphydaphy.vitality.guide.VitalityGuide;
import com.raphydaphy.vitality.helper.Vector3;
import com.raphydaphy.vitality.init.ModBlocks;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.nbt.NBTLib;

import amerifrance.guideapi.api.GuideAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraft.nbt.NBTTagCompound;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

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
	private SpellControl spell = new SpellControl();
	
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
		if (player.isSneaking() && (spell.getActiveSpell(player.getHeldItemOffhand()) > 919 || spell.getActiveSpell(player.getHeldItemOffhand()) < 910))
		{
			if (worldIn.isRemote)
			{
				Vitality.proxy.setActionText((I18n.format("gui.checkessence.name") + " "
					+ spell.getEssenceStored(stack) + "/" + maxEssence + " " + (I18n.format("gui.essence.name"))));
			}
		}
		else if (!worldIn.isRemote && spell.getActiveSpell(player.getHeldItemOffhand()) > 839 && spell.getActiveSpell(player.getHeldItemOffhand()) < 850)
		{
			int essenceVal = 100;
			if (spell.getActiveSpell(player.getHeldItemOffhand()) == 841)
			{
				essenceVal = 250;
			}
			if (spell.useEssence(essenceVal, stack))
			{
				NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
				NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay", 0);
				NBTLib.setInt(player.getHeldItemOffhand(), "arrowCount", 0);
				NBTLib.setInt(player.getHeldItemOffhand(), "essenceStored", spell.getEssenceStored(stack));

				player.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
			else
			{
				Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			}
		}
		else if (!worldIn.isRemote && spell.getActiveSpell(player.getHeldItemOffhand()) == 890)
		{
			if (NBTLib.getBoolean(player.getHeldItemOffhand(), "isActive", false) == false)
			{
				if (spell.useEssence(5000, stack))
				{
					NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
					spell.spawnParticles(EnumParticleTypes.PORTAL, worldIn, true, player.getPosition(), 100, 2);
					spell.spawnParticles(EnumParticleTypes.FLAME, worldIn, true, player.getPosition(), 100, 2);
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
		else if (!worldIn.isRemote && spell.getActiveSpell(player.getHeldItemOffhand()) > 829 && spell.getActiveSpell(player.getHeldItemOffhand()) < 840)
		{
			if (NBTLib.getBoolean(player.getHeldItemOffhand(), "isActive", false) == false)
			{
				NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
				player.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		else if (!worldIn.isRemote && spell.getActiveSpell(player.getHeldItemOffhand()) == 860)
		{
			if (player.canEat(false))
			{
				NBTLib.setBoolean(stack, "hungerSpellActive", true);
				player.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		else if (spell.getActiveSpell(player.getHeldItemOffhand()) == 880)
		{
			EntityPlayer entityplayer = (EntityPlayer) player;
			if (entityplayer.capabilities.allowFlying == false)
			{
				if (spell.useEssence(2500, stack))
				{
					entityplayer.capabilities.allowFlying = true;
					if (!worldIn.isRemote)
					{
						spell.spawnParticles(EnumParticleTypes.END_ROD, worldIn, true, player.getPosition(), 100, 2);
						spell.spawnParticles(EnumParticleTypes.ENCHANTMENT_TABLE, worldIn, true, player.getPosition(), 100, 2);
						spell.spawnParticles(EnumParticleTypes.SPELL_WITCH, worldIn, true, player.getPosition(), 100, 2);
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
					spell.spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, worldIn, true, player.getPosition(), 100, 2);
					spell.spawnParticles(EnumParticleTypes.PORTAL, worldIn, true, player.getPosition(), 100, 2);
				}
				worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.AMBIENT, 1, 1);
				entityplayer.swingArm(hand);
				player.getCooldownTracker().setCooldown(this, 100);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
			
		}
		// Angelic Placement
		else if (spell.getActiveSpell(player.getHeldItemOffhand()) > 909 && spell.getActiveSpell(player.getHeldItemOffhand()) < 920)
		{
			int cooldown = 20;
			int essenceVal = 50;
			
			// 1 = dirt, 2 = planks, 3 = cobble
			int currentActiveBlock = player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock");
			if (currentActiveBlock == 0)
			{
				player.getHeldItemOffhand().getTagCompound().setInteger("activeBlock", 1);
			}
			if (spell.getActiveSpell(player.getHeldItemOffhand()) == 911)
			{
				cooldown = 10;
				essenceVal = 25;
			}
			else if (spell.getActiveSpell(player.getHeldItemOffhand()) == 912)
			{
				cooldown = 5;
				essenceVal = 10;
			}
			
			Vector3 playerVec = Vector3.fromEntityCenter(player);
			Vector3 lookVec = new Vector3(player.getLookVec()).multiply(3);
			Vector3 placeVec = playerVec.add(lookVec);
			
			int posX = MathHelper.absFloor(placeVec.x);
			int posY = MathHelper.absFloor(placeVec.y) + 1;
			int posZ = MathHelper.absFloor(placeVec.z);
	        BlockPos pos = new BlockPos(posX, posY, posZ);
	        Block therealblock = worldIn.getBlockState(pos).getBlock();
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
								+ spell.getEssenceStored(stack) + "/" + maxEssence + " " + (I18n.format("gui.essence.name"))));
					}
				}
				else if (therealblock == Blocks.AIR || therealblock == Blocks.FLOWING_LAVA || therealblock == Blocks.FLOWING_WATER)
				{
					if (spell.useEssence(essenceVal, stack))
					{
						if (player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock") == 1)
						{
							if (!worldIn.isRemote)
							{
								System.out.println("dirt placing now?");
								//World.s
							}
						}
						else if (player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock") == 2)
						{
							if (!worldIn.isRemote)
							{
								tryPlaceABlock(Blocks.PLANKS, stack, player, worldIn, pos, player.getHorizontalFacing(), (float) (posX - pos.getX()), (float) (posY - pos.getY()), (float) (posZ - pos.getZ()));
							}
						}
						else if (player.getHeldItemOffhand().getTagCompound().getInteger("activeBlock") == 3)
						{
							if (!worldIn.isRemote)
							{
								tryPlaceABlock(Blocks.COBBLESTONE, stack, player, worldIn, pos, player.getHorizontalFacing(), (float) (posX - pos.getX()), (float) (posY - pos.getY()), (float) (posZ - pos.getZ()));
							}
						}
						if (!worldIn.isRemote)
						{
							spell.spawnParticles(EnumParticleTypes.FLAME, player.worldObj, true, pos, 4, 1);
						}
						player.swingArm(hand);
						player.getCooldownTracker().setCooldown(this, cooldown);
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
					}
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
					spell.getActiveSpell(player.getHeldItemOffhand()) == 800)
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
				NBTLib.setInt(player.getHeldItemOffhand(), "essenceStored", spell.getEssenceStored(stack));
				NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
				return EnumActionResult.SUCCESS;
			}

		}
		// If the player right-clicks on a ore block
		// Used for Enhancced Extraction spell
		else if (block instanceof BlockOre && !player.isSneaking() && 
				spell.getActiveSpell(player.getHeldItemOffhand()) > 869 && 
				spell.getActiveSpell(player.getHeldItemOffhand()) < 880)
		{
			if (!world.isRemote)
			{
				spell.spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, player.worldObj, true, pos, 3, 1);
			}
			player.swingArm(hand);
			world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1, 1);

			if (!world.isRemote)
			{
				if (block == Blocks.EMERALD_ORE)
				{
					world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
					spell.addEssence(1000, stack, maxEssence);
				}
				else if (block == Blocks.DIAMOND_ORE)
				{
					world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState());
					spell.addEssence(500, stack, maxEssence);
				}
				else if (block == Blocks.GOLD_ORE)
				{
					world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState());
					spell.addEssence(300, stack, maxEssence);
				}
				else if (block == Blocks.IRON_ORE)
				{
					world.setBlockState(pos, Blocks.COAL_ORE.getDefaultState());
					spell.addEssence(200, stack, maxEssence);
				}
				else if (block == Blocks.COAL_ORE)
				{
					world.setBlockState(pos, Blocks.STONE.getDefaultState());
					spell.addEssence(100, stack, maxEssence);
				}

				int cooldown = 10;
				if (spell.getActiveSpell(player.getHeldItemOffhand()) == 871)
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
			if (!world.isRemote && spell.getActiveSpell(player.getHeldItemOffhand()) == 850)
			{
				// Checks if player right-clicks a diamond block
				// Used for the Cryptic Transmutation spell to make an altar
				if (block == Blocks.DIAMOND_BLOCK)
				{
					if (spell.useEssence(1000, stack))
					{
						spell.spawnParticles(EnumParticleTypes.END_ROD, world, true,
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
					if (spell.useEssence(500, stack))
					{
						spell.spawnParticles(EnumParticleTypes.FIREWORKS_SPARK, world, true, pos, 50, 1);
						spell.spawnParticles(EnumParticleTypes.SMOKE_LARGE, world, true, pos, 50, 1);
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
			if (spell.getActiveSpell(player.getHeldItemOffhand()) > 869 && 
					spell.getActiveSpell(player.getHeldItemOffhand()) < 880)
			{
				if (block instanceof BlockMobSpawner)
				{
					// Balances since rougelike adds so many spawners
					if (Loader.isModLoaded("roguelike"))
					{
						spell.addEssence(2000, stack, maxEssence);
					}
					else
					{
						spell.addEssence(15000, stack, maxEssence);
					}
					world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
					if (!world.isRemote)
					{ spell.spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true,pos, 10, 1); }
					world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.BLOCKS, 1, 1);
					player.getCooldownTracker().setCooldown(this, 100);
					return EnumActionResult.SUCCESS;
				}
				else
				{
					List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,
							new AxisAlignedBB(pos.add(-2, -2, -2), pos.add(2, 2, 2)));
					for (EntityItem item : items)
					{
						if (item.getEntityItem().getItem() == ModItems.essence_vial_full)
						{
							if (!world.isRemote)
							{
								if (spell.useEssence(1000, item.getEntityItem()))
								{
									spell.addEssence(1000, stack, maxEssence);
									spell.spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true, item.getPosition(), 100, 2);
									player.getCooldownTracker().setCooldown(this, 10);
								}
							}
							if (spell.getEssenceStored(item.getEntityItem()) > 999)
							{
								world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.AMBIENT, 1, 1);
							}
						}
					}
				}
			}
			else if (spell.getActiveSpell(player.getHeldItemOffhand()) > 819 && spell.getActiveSpell(player.getHeldItemOffhand()) < 830)
			{
				int essenceAmount = 50;
				int blastPower = 20;
				int radius = 2;
				
				if (spell.getActiveSpell(player.getHeldItemOffhand()) == 821)
				{
					essenceAmount = 80;
					blastPower = 50;
					radius = 4;
				}
				else if (spell.getActiveSpell(player.getHeldItemOffhand()) == 822)
				{
					essenceAmount = 250;
					blastPower = 100;
					radius = 8;
				}
				if (spell.useEssence(essenceAmount, stack))
				{
			        BlockPos bombPos = pos;
					world.playSound(null, bombPos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 1);
					if (!world.isRemote)
					{
						spell.spawnParticles(EnumParticleTypes.EXPLOSION_HUGE, world, true, bombPos, radius*10, radius);
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
			else if (!world.isRemote && spell.getActiveSpell(player.getHeldItemOffhand()) > 899 && spell.getActiveSpell(player.getHeldItemOffhand()) < 910)
			{
				if (block instanceof IGrowable)
		        {
					int essenceVal = 50;
					int cooldown = 20;
					if (spell.getActiveSpell(player.getHeldItemOffhand()) == 901)
					{
						essenceVal = 25;
						cooldown = 10;
					}
					else if (spell.getActiveSpell(player.getHeldItemOffhand()) == 902)
					{
						essenceVal = 10;
						cooldown = 5;
					}
		            IGrowable igrowable = (IGrowable)block;
		            IBlockState iblockstate = world.getBlockState(pos);
		            if (igrowable.canGrow(world, pos, iblockstate, world.isRemote))
		            {
		                if (spell.useEssence(essenceVal, stack))
		                {
		                	if (!world.isRemote)
		                	{
			                	spell.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY, world, true, pos, 15, 1);
		                        igrowable.grow(world, world.rand, pos, iblockstate);
		                        return EnumActionResult.SUCCESS;
		                	}
		                	player.getCooldownTracker().setCooldown(this, cooldown);
		                }
		                else
		                {
		                	if (world.isRemote)
		                	{
		                		Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
		                	}
							return EnumActionResult.FAIL;
		                }
		            }
		        }

			}
			else if (spell.getActiveSpell(player.getHeldItemOffhand()) == 800)
			{
				if (block instanceof BlockCrops)
				{
					if (!world.isRemote)
					{ spell.spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true,pos, 10, 1); }
					try
					{
						spell.addEssence(((Integer)world.getBlockState(pos).getValue(BlockCrops.AGE)).intValue() * 10, stack, maxEssence);
					}
					catch (Exception e)
					{
						spell.addEssence(50, stack, maxEssence);
					}
					world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
					world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.BLOCKS, 1, 1);
					
					return EnumActionResult.SUCCESS;
				}
				else if (block instanceof BlockSapling)
				{
					spell.addEssence(35, stack, maxEssence);
					if (!world.isRemote)
					{ spell.spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true,pos, 10, 1); }
					world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
					world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.BLOCKS, 1, 1);
					return EnumActionResult.SUCCESS;
				}
				else if (block instanceof BlockCactus)
				{
					spell.addEssence(25, stack, maxEssence);
					if (!world.isRemote)
					{ spell.spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true,pos, 10, 1); }
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
					world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.BLOCKS, 1, 1);
					return EnumActionResult.SUCCESS;
				}
				else
				{
					List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,
							new AxisAlignedBB(pos.add(-2, -2, -2), pos.add(2, 2, 2)));
					for (EntityItem item : items)
					{
						if (item.getEntityItem().getItem() == ModItems.essence_vial_full)
						{
							if (!world.isRemote)
							{
								if (spell.useEssence(1000, item.getEntityItem()))
								{
									spell.addEssence(1000, stack, maxEssence);
									spell.spawnParticles(EnumParticleTypes.DAMAGE_INDICATOR, world, true, item.getPosition(), 100, 2);
									player.getCooldownTracker().setCooldown(this, 10);
								}
							}
							if (spell.getEssenceStored(item.getEntityItem()) > 999)
							{
								world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.AMBIENT, 1, 1);
							}
						}
					}
				}
			}
			else if (spell.getActiveSpell(player.getHeldItemOffhand()) > 919 && spell.getActiveSpell(player.getHeldItemOffhand()) < 930)
			{
				int essenceVal = 250;
				int cooldown = 20;
				if (spell.getActiveSpell(player.getHeldItemOffhand()) == 921)
				{
					essenceVal = 100;
					cooldown = 10;
				}
				EntityLightningBolt lightning = new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false);
				if (spell.useEssence(essenceVal, stack))
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
					Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
					return EnumActionResult.FAIL;
				}
				
			}
			// If the Magic Lantern spell is used on a block
			else if (spell.getActiveSpell(player.getHeldItemOffhand()) > 809 && spell.getActiveSpell(player.getHeldItemOffhand()) < 820)
			{
				if (spell.lanternSpell(stack, player, world, pos, hand,side, par8, par9,  par10, this))
				{
					return EnumActionResult.SUCCESS;
				}
				return EnumActionResult.FAIL;
			}
			// we need a spell for this soon
			else
			{
				List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,
						new AxisAlignedBB(pos.add(-2, -2, -2), pos.add(2, 2, 2)));
				for (EntityItem item : items)
				{
					if (item.getEntityItem().getItem() == Items.BOOK)
					{
						EntityItem guideItem = new EntityItem(world, item.posX, item.posY, item.posZ, GuideAPI.getStackFromBook(VitalityGuide.vitalityGuide).copy());
						if (!world.isRemote)
						{
							world.spawnEntityInWorld(guideItem);
							item.setDead();
							spell.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY, world, true, item.getPosition(), 50, 2);
						}
						world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 1, 1);
						player.getCooldownTracker().setCooldown(this, 10);
					}
					if (item.getEntityItem().getItem() == ModItems.essence_vial_empty)
					{
						if (spell.useEssence(1000, stack))
						{
							ItemStack fullBottleStack = new ItemStack(ModItems.essence_vial_full);
							if (!fullBottleStack.hasTagCompound())
							{
								fullBottleStack.setTagCompound(new NBTTagCompound());
							}
							fullBottleStack.getTagCompound().setInteger("essenceStored", 1000);
							EntityItem fullBottle = new EntityItem(world, item.posX, item.posY, item.posZ, fullBottleStack.copy());
							if (!world.isRemote)
							{
								world.spawnEntityInWorld(fullBottle);
								spell.spawnParticles(EnumParticleTypes.CLOUD, world, true, item.getPosition(), 100, 2);
								item.setDead();
							}
							world.playSound(player, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
							player.getCooldownTracker().setCooldown(this, 10);
						}
						else
						{
							Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
							return EnumActionResult.FAIL;
						}
					}
					else if (item.getEntityItem().getItem() == ModItems.essence_vial_full)
					{
						
						if (spell.useEssence(1000, stack))
						{
							if (item.getEntityItem().hasTagCompound())
							{
								if (!world.isRemote)
								{
									spell.addEssence(1000, item.getEntityItem(), 10000000);
									spell.spawnParticles(EnumParticleTypes.CLOUD, world, true, item.getPosition(), 100, 2);
								}
								world.playSound(player, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
								player.getCooldownTracker().setCooldown(this, 10);
							}
						}
					}
					else if (item.getEntityItem().getItem() == Items.WOODEN_PICKAXE)
					{
						if (spell.useEssence(1000, stack))
						{
							ItemStack multitoolStack = new ItemStack(ModItems.magic_multitool);
							EntityItem fullBottle = new EntityItem(world, item.posX, item.posY, item.posZ, multitoolStack.copy());
							if (!world.isRemote)
							{
								world.spawnEntityInWorld(fullBottle);
								item.setDead();
								spell.spawnParticles(EnumParticleTypes.SMOKE_LARGE, world, true, item.getPosition(), 100, 2);
							}
							world.playSound(player, player.getPosition(), SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.AMBIENT, 1, 1);
							player.getCooldownTracker().setCooldown(this, 10);
						}
						else
						{
							Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
							return EnumActionResult.FAIL;
						}
					}
					else if (item.getEntityItem().getItem() == ModItems.magic_multitool)
					{
						if (spell.useEssence(10000, stack))
						{
							ItemStack multitoolStack = new ItemStack(ModItems.imbued_multitool);
							EntityItem fullBottle = new EntityItem(world, item.posX, item.posY, item.posZ, multitoolStack.copy());
							if (!world.isRemote)
							{
								world.spawnEntityInWorld(fullBottle);
								item.setDead();
								spell.spawnParticles(EnumParticleTypes.SMOKE_LARGE, world, true, item.getPosition(), 100, 2);
							}
							world.playSound(player, player.getPosition(), SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.AMBIENT, 1, 1);
							player.getCooldownTracker().setCooldown(this, 10);
						}
						else
						{
							Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
							return EnumActionResult.FAIL;
						}
					}
					else if (item.getEntityItem().getItem() == ModItems.imbued_multitool)
					{
						if (spell.useEssence(100000, stack))
						{
							ItemStack multitoolStack = new ItemStack(ModItems.fluxed_multitool);
							EntityItem fullBottle = new EntityItem(world, item.posX, item.posY, item.posZ, multitoolStack.copy());
							if (!world.isRemote)
							{
								world.spawnEntityInWorld(fullBottle);
								item.setDead();
								spell.spawnParticles(EnumParticleTypes.SMOKE_LARGE, world, true, item.getPosition(), 100, 2);
							}
							world.playSound(player, player.getPosition(), SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.AMBIENT, 1, 1);
							player.getCooldownTracker().setCooldown(this, 10);
						}
						else
						{
							Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
							return EnumActionResult.FAIL;
						}
					}
					
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
			if (spell.getActiveSpell(player.getHeldItemOffhand()) == 800)
			{
				if (NBTLib.getInt(player.getHeldItemOffhand(), "tickDelay", 0) == 0
						&& replaceBlock.size() > NBTLib.getInt(player.getHeldItemOffhand(), "currentBlockId", 0)
						&& !player.worldObj.isRemote)
				{
					BlockPos curBlock = replaceBlock
							.get(NBTLib.getInt(player.getHeldItemOffhand(), "currentBlockId", 0));
					player.worldObj.playSound(null, curBlock, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);
					player.worldObj.setBlockState(curBlock, ModBlocks.dead_log.getDefaultState());
					spell.spawnParticles(EnumParticleTypes.SPELL_WITCH, player.worldObj, true, curBlock, 7, 1);
					spell.addEssence(ThreadLocalRandom.current().nextInt(2, 15 + 1), player.getHeldItemOffhand(), maxEssence);

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
			else if (!player.worldObj.isRemote && spell.getActiveSpell(player.getHeldItemOffhand()) == 890)
			{
				NBTLib.setBoolean(player.getHeldItemOffhand(), "isActive", true);
				spell.spawnParticles(EnumParticleTypes.PORTAL, player.worldObj, true, player.getPosition(), 100, 2);
				spell.spawnParticles(EnumParticleTypes.FLAME, player.worldObj, true, player.getPosition(), 100, 2);
				player.setEntityInvulnerable(true);
			}
			else if (spell.getActiveSpell(player.getHeldItemOffhand()) > 839 && spell.getActiveSpell(player.getHeldItemOffhand()) < 850)
			{
				if (NBTLib.getInt(player.getHeldItemOffhand(), "tickDelay", 0) == 0)
				{
					if (spell.useEssence(NBTLib.getInt(player.getHeldItemOffhand(), "arrowCount", 0),
							player.getHeldItemOffhand()))
					{
						if (!player.worldObj.isRemote)
						{
							NBTLib.setInt(player.getHeldItemOffhand(), "arrowCount",
									NBTLib.getInt(player.getHeldItemOffhand(), "arrowCount", 0) + 1);
							
							if (spell.getActiveSpell(player.getHeldItemOffhand()) == 840)
							{
								NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay", 5);
							}
							else if (spell.getActiveSpell(player.getHeldItemOffhand()) == 841)
							{
								NBTLib.setInt(player.getHeldItemOffhand(), "tickDelay", 1);
							}
							
							
							ItemStack itemstack = new ItemStack(Items.SPECTRAL_ARROW);
							ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow
									? itemstack.getItem() : Items.ARROW));
							EntityArrow entityArrow = itemarrow.createArrow(player.worldObj, itemstack, player);

							entityArrow = spell.fancySetAim(entityArrow, player, player.rotationPitch, player.rotationYaw,
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
			if (spell.getActiveSpell(player.getHeldItemOffhand()) > 829 && spell.getActiveSpell(player.getHeldItemOffhand()) < 840 && !worldIn.isRemote)
			{
				int essenceCost = 25;
				if (spell.getActiveSpell(player.getHeldItemOffhand()) == 831)
				{
					essenceCost = 100;
				}
				else if (spell.getActiveSpell(player.getHeldItemOffhand()) == 832)
				{
					essenceCost = 300;
				}
				if (spell.useEssence(essenceCost, stack))
				{
					playerIn.getCooldownTracker().setCooldown(this, 50);
					EntityLargeFireball bigBall = new EntityLargeFireball(worldIn, player, player.getLookVec().xCoord, player.getLookVec().yCoord, player.getLookVec().zCoord);
					bigBall.accelerationX = player.getLookVec().xCoord;
					bigBall.accelerationY = player.getLookVec().yCoord;
					bigBall.accelerationZ = player.getLookVec().zCoord;
					bigBall.explosionPower = 4;
					if (spell.getActiveSpell(player.getHeldItemOffhand()) == 831)
					{
						bigBall.explosionPower = 6;
					}
					else if (spell.getActiveSpell(player.getHeldItemOffhand()) == 832)
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
			else if (spell.getActiveSpell(player.getHeldItemOffhand()) == 890 && !worldIn.isRemote)
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
				if (spell.useEssence(500, stack))		
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
