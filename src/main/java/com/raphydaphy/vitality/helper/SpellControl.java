package com.raphydaphy.vitality.helper;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.nbt.NBTLib;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpellControl 
{
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
	public boolean addEssence(int amount, ItemStack stack, int max)
	{
		// Checks if the amount of essence to add will not cause the wand to go over the capacity
		if (getEssenceStored(stack) + amount < max + 1)
		{
			// add the amount of essence normally
			NBTLib.setInt(stack, "essenceStored", NBTLib.getInt(stack, "essenceStored", 0) + amount);
		}
		// If the user tried to add too much essence
		else
		{
			// fill the wand to its capacity, but don't go over
			NBTLib.setInt(stack, "essenceStored", max);
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
	public EntityArrow fancySetAim(EntityArrow target, Entity entity, float pitch, float yaw, float velocity,
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
	
	public boolean lanternSpell(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10, Item cooldownStack)
	{
		if (!world.isRemote)
		{
			int essenceVal = 5;
			int cooldown = 20;
			if (getActiveSpell(player.getHeldItemOffhand()) == 811 || getActiveSpell(player.getHeldItemMainhand()) == 811)
			{
				essenceVal = 2;
				cooldown = 10;
			}
			else if (getActiveSpell(player.getHeldItemOffhand()) == 812 || getActiveSpell(player.getHeldItemMainhand()) == 812)
			{
				essenceVal = 0;
				cooldown = 5;
			}
			if (useEssence(essenceVal, stack))
			{
				spawnParticles(EnumParticleTypes.FLAME, world, true,pos, 15, 1);
				world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 1);
				ItemStack stackToPlace = new ItemStack(Blocks.TORCH);
				stackToPlace.onItemUse(player, world, pos, hand, side, par8, par9, par10);
				player.swingArm(hand);
				player.getCooldownTracker().setCooldown(cooldownStack, cooldown);
				return true;
			}
			else
			{
				Vitality.proxy.setActionText((I18n.format("gui.notenoughessence.name")));
				return false;
			}
		}
		else
		{
			return true;
		}
	}
	
	
}
