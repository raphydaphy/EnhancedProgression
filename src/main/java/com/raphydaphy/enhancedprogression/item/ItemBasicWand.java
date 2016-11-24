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

	private int essenceStored = 0;
	private int essenceCapacity = 0;
	
	private boolean unfinished = false;
	private String task;
	
	// Particles
	private EnumParticleTypes particleType = EnumParticleTypes.END_ROD;
	private int particles = 0;
	private int particleRadius = 0;
	private BlockPos particlePos;
	
	// Logger
	private List<BlockPos> replaceBlock = new ArrayList<BlockPos>();
	private int currentBlockId = 0;
	private BlockPos curBlock;
	private int delay = 0;
	
	// Rapidfire
	private int arrows = 0;
	
	public ItemBasicWand(String name) {
		this.name = name;
		this.setEssenceCapacity(1000);
		setUnlocalizedName(name);
		setRegistryName(name);
		this.maxStackSize = 1;
	}
	
	@Override
	public ItemBasicWand setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
	
	public void setEssenceCapacity(int capacity)
	{
		essenceCapacity = capacity;
	}
	
	public int getEssenceStored()
	{
		return essenceStored;
	}
	
	public int getMaxEssence()
	{
		return essenceCapacity;
	}
	
	public boolean useEssence(int amount)
	{
		if (getEssenceStored() - amount >= 0)
		{
			essenceStored -= amount;
			return true;
		}
		return false;
	}
	
	public boolean addEssence(int amount)
	{
		if (getEssenceStored() + amount < getMaxEssence() + 1)
		{
			essenceStored += amount;
		}
		else
		{
			essenceStored = getMaxEssence();
		}
		return true;
	}
	
	private static Iterable<BlockPos.MutableBlockPos> WOOD_SEARCH_AREA = BlockPos.getAllInBoxMutable(new BlockPos(-5, -5, -5), new BlockPos(5, 25, 5));
	
	boolean checkPlatform(int xOff, int yOff, int zOff, Block block, BlockPos pos, World world) 
	{	
		return world.getBlockState(pos.add(xOff, yOff, zOff)).getBlock() == block;
	}
	
	public void registerItemModel() {
		EnhancedProgression.proxy.registerItemRenderer(this, 0, name);
	}
	
	private EntityArrow fancySetAim(EntityArrow target, Entity entity, float pitch, float yaw, float velocity, float knockbackResistance, float xOff, float yOff, float zOff)
    {
        float x = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float y = -MathHelper.sin(pitch * 0.017453292F);
        float z = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        target.setThrowableHeading((double)x - xOff, (double)y - yOff, (double)z - xOff, velocity, 0);
        target.motionX += entity.motionX;
        target.motionZ += entity.motionZ;

        if (!entity.onGround)
        {
            target.motionY += entity.motionY;
        }
        
        return target;
    }
	
	 @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }
	 
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected)
    {
		
		if(unfinished)
		{
			if (isSelected)
			{
				if (task == "LOGGER")
				{
					if (delay == 0 && replaceBlock.size() > currentBlockId && !world.isRemote)
					{
						curBlock = replaceBlock.get(currentBlockId);
						world.playSound(null, curBlock,SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1, 1);
						world.setBlockState(curBlock, ModBlocks.dead_log.getDefaultState());
						spawnParticles(EnumParticleTypes.SPELL_WITCH, world, true, curBlock, 7, 1);
						this.addEssence(ThreadLocalRandom.current().nextInt(2, 15 + 1));
						
						currentBlockId++;
						delay = 10;
						
						if (ThreadLocalRandom.current().nextInt(1, 50 + 1) == 6)
						{
							if (canBreak())
							{
								world.playSound(null, curBlock,SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1, 1);
								stack.stackSize = 0;
								unfinished = false;
							}
						}
					}
					else if (replaceBlock.size() > currentBlockId && !world.isRemote)
					{
						delay--;
						
					}
					else if (!world.isRemote)
					{
						unfinished = false;
						task = "";
					}
				}
				else if (task == "RAPIDFIRE")
				{
					if (delay == 0)
                    {
						if (useEssence(arrows))
						{
							if (!world.isRemote)
							{
								arrows++;
		                    	delay = 5;
		                    	
		                    	int xOff = 0;
		                    	int yOff = 0;
		                    	int zOff = 0;
		                    	
		                    	EntityLivingBase shooter = (EntityLivingBase) entityIn;
		                    	shooter.posX += xOff;
		                    	shooter.posY += yOff;
		                    	shooter.posZ += zOff;
		                    	
								ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
		                    	ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.ARROW));
								EntityArrow entityArrow = itemarrow.createArrow(world, itemstack, shooter);
								
		                        entityArrow = fancySetAim(entityArrow, shooter, entityIn.rotationPitch, entityIn.rotationYaw, 5.0F, 1.0F, xOff, yOff, zOff);
		                        entityArrow.setKnockbackStrength(1);
		                        entityArrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
		                        
		                    	world.spawnEntityInWorld(entityArrow);
		                    	world.playSound(null, shooter.posX, shooter.posY, shooter.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
							}
						}
						else
						{
							EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
							unfinished = false;
						}
						
                    }
					else
					{
						delay --;
					}
				}
				else if (task == "PARTICLES")
				{
					if (particles > 0)
					{
						particles--;
						spawnParticles(particleType, world, true, particlePos, particles, particleRadius);
					}
					else
					{
						unfinished = false;
						task = "";
					}
				}
			}
			else
			{
				unfinished = false;
				task = "";
			}
		}
    }

	public static void spawnParticles(EnumParticleTypes particleType, World world, boolean forceSpawn, BlockPos pos, int count, double radius) {
		spawnParticlesServer(particleType, world, forceSpawn, pos.getX(), pos.getY(), pos.getZ(), count, radius);
	}
	
	public static void spawnParticlesServer(EnumParticleTypes particleType, World world, boolean forceSpawn, double x, double y, double z, int count, double radius) {
		FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(world.provider.getDimension()).spawnParticle(particleType, forceSpawn, x, y, z, count, radius, radius, radius, 0.005D);
	}
	
	public boolean canBreak()
	{
		return true;
	}
	
	public NBTTagCompound getNBTShareTag(ItemStack stack)
    {
        return stack.getTagCompound();
    }
	
	
	public boolean getShareTag()
    {
        return true;
    }
	 
	public boolean updateItemStackNBT(NBTTagCompound nbt)
    {
        return false;
    }
	
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
		if (task != "PARTICLES")
		{
			unfinished = false;
		}
    }
	 
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
		if (playerIn.isSneaking())
		{
			EnhancedProgression.proxy.setActionText((I18n.format("gui.checkessence.name") + " " + getEssenceStored() + "/" + getMaxEssence() + " " + (I18n.format("gui.essence.name"))));
		}
		else if(!worldIn.isRemote && ItemStack.areItemsEqual(playerIn.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_rapidfire)))
		{
			if (useEssence(100))
			{
				task = "RAPIDFIRE";
				
				delay = 0;
				arrows = 0;
				
				unfinished = true;
				
                playerIn.setActiveHand(hand);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
			}
			else
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
			}
		}
		else if(!worldIn.isRemote && ItemStack.areItemsEqual(playerIn.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_hunger)))
		{
			if (playerIn.canEat(false))
			{
				task = "HUNGER";
				playerIn.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
			}
		}
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
    }
	
	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) 
	{
		Block block = world.getBlockState(pos).getBlock();

		if(block instanceof BlockAltar) 
		{
			boolean wanded;
			wanded = ((BlockAltar) block).onUsedByWand(player, par1ItemStack, world, pos, side);
			if(wanded && world.isRemote)
			{
				player.swingArm(hand);
			}
			
			return wanded ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		}
		else if(block instanceof BlockLog) 
		{
			replaceBlock.clear();
			currentBlockId = 0;
			delay = 0;
			
			for (BlockPos position : WOOD_SEARCH_AREA) 
			{
				if(checkPlatform(position.getX(), position.getY(), position.getZ(), block, pos, world))
				{
					BlockPos toReplace = pos.add(position.getX(), position.getY(), position.getZ());
					replaceBlock.add(toReplace);
				}
			}
			task = "LOGGER";
			unfinished = true;
			player.setActiveHand(hand);
			return EnumActionResult.SUCCESS;
		}
		else if(block instanceof BlockOre && !player.isSneaking()) 
		{
			task = "PARTICLES";
			particleType = EnumParticleTypes.DAMAGE_INDICATOR;
			particles = 2;
			particleRadius = 1;
			particlePos = new BlockPos(pos.getX(), pos.getY() + 1.2, pos.getZ());
			unfinished = true;
			player.swingArm(hand);
			world.playSound(null, pos,SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1, 1);
			
			if (!world.isRemote)
			{
				if (block == Blocks.EMERALD_ORE)
				{
					world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
					addEssence(1000);
				}
			    else if (block == Blocks.DIAMOND_ORE)
				{
			    	world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState());
					addEssence(1000);
				}
			    else if (block == Blocks.GOLD_ORE)
				{
			    	world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState());
					addEssence(500);
				}
			    else if (block == Blocks.IRON_ORE)
				{
			    	world.setBlockState(pos, Blocks.COAL_ORE.getDefaultState());
					addEssence(200);
				}
			    else if (block == Blocks.COAL_ORE)
				{
			    	world.setBlockState(pos, Blocks.STONE.getDefaultState());
					addEssence(100);
				}
			}
			
			player.setActiveHand(hand);
			return EnumActionResult.SUCCESS;
		}
		else
		{
			if (player.isSneaking())
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.checkessence.name") + " " + getEssenceStored() + "/" + getMaxEssence() + " " + (I18n.format("gui.essence.name"))));
				return EnumActionResult.SUCCESS;
			}
			else
			{
				if(!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_transmutation)))
				{
					if (block == Blocks.DIAMOND_BLOCK)
					{
						if (useEssence(1000))
						{
							task = "PARTICLES";
							particleType = EnumParticleTypes.END_ROD;
							particles = 7;
							particleRadius = 1;
							particlePos = new BlockPos(pos.getX(), pos.getY() + 1.2, pos.getZ());
							unfinished = true;
							
							world.playSound(null, pos,SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1, 1);
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
				else if(!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_lantern)))
				{
					if (useEssence(5))
					{
						BlockPos torchPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
						if (world.getBlockState(torchPos) == Blocks.AIR.getDefaultState())
						{
							task = "PARTICLES";
							particleType = EnumParticleTypes.FLAME;
							particles = 7;
							particleRadius = 1;
							particlePos = new BlockPos(torchPos.getX(), torchPos.getY(), torchPos.getZ());
							unfinished = true;
							
							world.setBlockState(torchPos, Blocks.TORCH.getDefaultState());
							player.swingArm(hand);
							return EnumActionResult.SUCCESS;
						}
						else
						{
							EnhancedProgression.proxy.setActionText((I18n.format("gui.obstructed.name")));
							return EnumActionResult.PASS;
						}
					}
					else
					{
						EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						return EnumActionResult.PASS;
					}
				}
				else if(!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_explosion)))
				{
					if (useEssence(50))
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
		
		}
		return EnumActionResult.PASS;
	}
	
	 /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
    	if (task == "HUNGER")
    	{
    		return 32;
    	}
    	else
    	{
    		return 72000;
    	}
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Nullable
    public EnumAction getItemUseAction(ItemStack stack)
    {
    	if (task == "LOGGER" || task == "RAPIDFIRE")
    	{
    		return EnumAction.BOW;
    	}
    	else if (task == "HUNGER")
    	{
    		return EnumAction.EAT;
    	}
		return EnumAction.NONE;
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
    	if (task=="HUNGER")
    	{
    		if (entityLiving instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityLiving;
                if (useEssence(250))
    			{
    				entityplayer.heal(10);
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
