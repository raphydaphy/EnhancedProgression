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
	
	// Logger
	private List<BlockPos> replaceBlock = new ArrayList<BlockPos>();
	private BlockPos curBlock;
	
	public ItemBasicWand(String name) {
		this.name = name;
		setUnlocalizedName(name);
		setRegistryName(name);
		this.maxStackSize = 1;		
	}
	
	@Override
	public ItemBasicWand setCreativeTab(CreativeTabs tab) {
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
		}
		else
		{
			compoundTmp.setInteger("essenceStored", getMaxEssence());
		}
		stack.setTagCompound(compoundTmp);
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
		if (stack.getTagCompound() == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound compound = stack.getTagCompound();
		if (compound.getString("particleAction") != "" && compound.getString("particleAction") != null)
		{
			if (compound.getInteger("particleCount") > 0)
			{
				compound.setInteger("particleCount", compound.getInteger("particleCount") - 1);
				
				double x = compound.getDouble("posX");
				double y = compound.getDouble("posY");
				double z = compound.getDouble("posZ");
				
				switch(compound.getString("particleAction"))
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
			}
			else
			{
				compound.setString("particleAction", "");
			}
		}
		
		
		if(compound.getString("tickSpell") != "" && compound.getString("tickSpell") != null && isSelected)
		{
			int delay = compound.getInteger("delay");
			switch (compound.getString("tickSpell"))
			{
			case "LOGGER":
				if (delay == 0 && replaceBlock.size() > compound.getInteger("currentBlockId") && !world.isRemote)
				{
					curBlock = replaceBlock.get(compound.getInteger("currentBlockId"));
					world.playSound(null, curBlock,SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1, 1);
					world.setBlockState(curBlock, ModBlocks.dead_log.getDefaultState());
					spawnParticles(EnumParticleTypes.SPELL_WITCH, world, true, curBlock, 7, 1);
					this.addEssence(ThreadLocalRandom.current().nextInt(2, 15 + 1), stack);
					
					compound.setInteger("currentBlockId", compound.getInteger("currentBlockId") + 1);
					delay = 10;
					
					if (ThreadLocalRandom.current().nextInt(1, 5000 + 1) == 555)
					{
						if (canBreak())
						{
							world.playSound(null, curBlock,SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1, 1);
							stack.stackSize = 0;
							compound.setString("tickSpell", "");
						}
					}
				}
				else if (replaceBlock.size() > compound.getInteger("currentBlockId") && !world.isRemote)
				{
					delay--;
					
				}
				else if (!world.isRemote)
				{
					compound.setString("tickSpell", "");
				}
				break;
			case "RAPIDFIRE":
				if (delay == 0)
                {
					if (useEssence(compound.getInteger("arrows"),stack))
					{
						if (!world.isRemote)
						{
							compound.setInteger("arrows", compound.getInteger("arrows") + 1);
	                    	compound.setInteger("delay",5);
	                    	
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
						compound.setString("tickSpell", "");
					}
					
                }
				else
				{
					delay --;
				}
				break;
			}
			compound.setInteger("delay",delay);
		}
		stack.setTagCompound(compound);
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
		NBTTagCompound compound = stack.getTagCompound();
		compound.setString("tickSpell", "");
		compound.setString("spell", "");
		stack.setTagCompound(compound);
    }
	 
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
		NBTTagCompound compound = itemStackIn.getTagCompound();
		if (playerIn.isSneaking())
		{
			EnhancedProgression.proxy.setActionText((I18n.format("gui.checkessence.name") + " " + getEssenceStored(itemStackIn) + "/" + getMaxEssence() + " " + (I18n.format("gui.essence.name"))));
		}
		else if(!worldIn.isRemote && ItemStack.areItemsEqual(playerIn.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_rapidfire)))
		{
			if (useEssence(100, itemStackIn))
			{
				compound.setInteger("arrows", 0);
				compound.setInteger("delay 0", 0);
				compound.setString("tickSpell", "RAPIDFIRE");
				
                playerIn.setActiveHand(hand);
                itemStackIn.setTagCompound(compound);
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
				compound.setString("spell", "HUNGER");
				
				playerIn.setActiveHand(hand);
				itemStackIn.setTagCompound(compound);
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
		NBTTagCompound compound = par1ItemStack.getTagCompound();
		
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
			compound.setInteger("currentBlockId", 0);
			compound.setInteger("delay", 0);
			
			for (BlockPos position : WOOD_SEARCH_AREA) 
			{
				if(checkPlatform(position.getX(), position.getY(), position.getZ(), block, pos, world))
				{
					BlockPos toReplace = pos.add(position.getX(), position.getY(), position.getZ());
					replaceBlock.add(toReplace);
				}
			}
			compound.setString("tickSpell", "LOGGER");
			player.setActiveHand(hand);
			par1ItemStack.setTagCompound(compound);
			return EnumActionResult.SUCCESS;
		}
		else if(block instanceof BlockOre && !player.isSneaking()) 
		{
			if (getWandTier() > 1)
			{
				compound.setString("particleAction", "ORE EXTRACT");
				compound.setInteger("particleCount", 2);
				compound.setDouble("posX", pos.getX());
				compound.setDouble("posY", pos.getY());
				compound.setDouble("posZ", pos.getZ());
				
				player.swingArm(hand);
				world.playSound(null, pos,SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1, 1);
				
				if (!world.isRemote)
				{
					if (block == Blocks.EMERALD_ORE)
					{
						world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
						addEssence(1000, par1ItemStack);
					}
				    else if (block == Blocks.DIAMOND_ORE)
					{
				    	world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState());
						addEssence(1000, par1ItemStack);
					}
				    else if (block == Blocks.GOLD_ORE)
					{
				    	world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState());
						addEssence(500, par1ItemStack);
					}
				    else if (block == Blocks.IRON_ORE)
					{
				    	world.setBlockState(pos, Blocks.COAL_ORE.getDefaultState());
						addEssence(200, par1ItemStack);
					}
				    else if (block == Blocks.COAL_ORE)
					{
				    	world.setBlockState(pos, Blocks.STONE.getDefaultState());
						addEssence(100, par1ItemStack);
					}
				}
				
				player.setActiveHand(hand);
				par1ItemStack.setTagCompound(compound);
				
				return EnumActionResult.SUCCESS;
			}
			else
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.betterwandneeded.name")));
				return EnumActionResult.FAIL;
			}
		}
		else
		{
			if (player.isSneaking())
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.checkessence.name") + " " + getEssenceStored(par1ItemStack) + "/" + getMaxEssence() + " " + (I18n.format("gui.essence.name"))));
				return EnumActionResult.SUCCESS;
			}
			else
			{
				if(!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_transmutation)))
				{
					if (block == Blocks.DIAMOND_BLOCK)
					{
						if (useEssence(1000, par1ItemStack))
						{
							compound.setString("particleAction", "TRANSMUTE DIAMOND");
							compound.setInteger("particleCount", 7);
							compound.setDouble("posX", pos.getX());
							compound.setDouble("posY", pos.getY() + 1.2);
							compound.setDouble("posZ", pos.getZ());
							
							world.playSound(null, pos,SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1, 1);
							player.swingArm(hand);
							world.setBlockState(pos, ModBlocks.altar.getDefaultState());
							
							par1ItemStack.setTagCompound(compound);
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
					if (useEssence(5, par1ItemStack))
					{
						BlockPos torchPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
						if (world.getBlockState(torchPos) == Blocks.AIR.getDefaultState())
						{
							compound.setString("particleAction", "PLACE TORCH");
							compound.setInteger("particleCount", 7);
							compound.setDouble("posX", pos.getX());
							compound.setDouble("posY", pos.getY() + 1.2);
							compound.setDouble("posZ", pos.getZ());
							
							world.setBlockState(torchPos, Blocks.TORCH.getDefaultState());
							player.swingArm(hand);
							
							par1ItemStack.setTagCompound(compound);
							return EnumActionResult.SUCCESS;
						}
						else
						{
							EnhancedProgression.proxy.setActionText((I18n.format("gui.obstructed.name")));
							par1ItemStack.setTagCompound(compound);
							return EnumActionResult.PASS;
						}
					}
					else
					{
						EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						par1ItemStack.setTagCompound(compound);
						return EnumActionResult.PASS;
					}
				}
				else if(!world.isRemote && ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(ModItems.spell_card_explosion)))
				{
					if (useEssence(50, par1ItemStack))
					{
						world.createExplosion(player, pos.getX(), pos.getY() + 2, pos.getZ(), 3, false);
						par1ItemStack.setTagCompound(compound);
						return EnumActionResult.SUCCESS;
					}
					else
					{
						EnhancedProgression.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
						par1ItemStack.setTagCompound(compound);
						return EnumActionResult.FAIL;
					}
				}
			}
		
		}
		par1ItemStack.setTagCompound(compound);
		return EnumActionResult.PASS;
	}
	
	 /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
    	NBTTagCompound compoundTmp = stack.getTagCompound();
    	if (compoundTmp.getString("spell") == "HUNGER")
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
    	NBTTagCompound compoundTmp = stack.getTagCompound();
    	if (compoundTmp.getString("tickSpell") == "LOGGER" || compoundTmp.getString("tickSpell") == "RAPIDFIRE")
    	{
    		return EnumAction.BOW;
    	}
    	else if (compoundTmp.getString("spell") == "HUNGER")
    	{
    		return EnumAction.EAT;
    	}
		return EnumAction.NONE;
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
    	NBTTagCompound compound = stack.getTagCompound();
    	if (compound.getString("spell")=="HUNGER")
    	{
    		if (entityLiving instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityLiving;
                if (useEssence(250, stack))
    			{
    				entityplayer.getFoodStats().addStats(20, 20);
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
