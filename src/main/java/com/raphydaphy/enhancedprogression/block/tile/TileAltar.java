package com.raphydaphy.enhancedprogression.block.tile;

import java.util.List;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.init.ModBlocks;
import com.raphydaphy.enhancedprogression.nbt.AltarRecipe;
import com.raphydaphy.enhancedprogression.nbt.PacketManager;
import com.raphydaphy.enhancedprogression.recipe.ModRecipes;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;

public class TileAltar extends TileSimpleInventory
{
	public int altarTier = 1;
	AltarRecipe altarRecipe;

	private int confirm = 0;
	private int disableTicks = 0;

	@Override
	public int getSizeInventory()
	{
		// TODO Auto-generated method stub
		return 16;
	}

	private static Iterable<BlockPos.MutableBlockPos> QUARTZ_BLOCKS = BlockPos
			.getAllInBoxMutable(new BlockPos(-2, 0, -2), new BlockPos(2, 0, 2));
	
	private static final BlockPos[] OBSIDIAN_BLOCKS =
	{ new BlockPos(-3, 0, -3), new BlockPos(-3, 0, -2), new BlockPos(-3, 0, -1), new BlockPos(-2, 0, -3),
			new BlockPos(-3, 0, 0), new BlockPos(-3, 0, 1), new BlockPos(-3, 0, 2), new BlockPos(-3, 0, 3),
			new BlockPos(-2, 0, 3), new BlockPos(-1, 0, 3), new BlockPos(0, 0, 3), new BlockPos(1, 0, 3),
			new BlockPos(2, 0, 3), new BlockPos(3, 0, 3), new BlockPos(3, 0, 2), new BlockPos(3, 0, 1),
			new BlockPos(3, 0, 0), new BlockPos(3, 0, -1), new BlockPos(3, 0, -2), new BlockPos(3, 0, -3),
			new BlockPos(2, 0, -3), new BlockPos(1, 0, -3), new BlockPos(0, 0, -3), new BlockPos(-1, 0, -3)

	};
	
	private static final BlockPos[] IMBUED_LOG_BLOCKS =
		{ new BlockPos(-4, 0, -3), new BlockPos(-4, 0, -2),  new BlockPos(-4, 0, -1),  new BlockPos(-4, 0, 0),
			new BlockPos(-4, 0, 1), new BlockPos(-4, 0, 2), new BlockPos(-4, 0, 3),
		  new BlockPos(-4, 0, 4), new BlockPos(-3, 0, 4), new BlockPos(-2, 0, 4), new BlockPos(-1, 0, 4), 
		  	new BlockPos(0, 0, 4), new BlockPos(1, 0, 4), new BlockPos(2, 0, 4), new BlockPos(3, 0, 4), 
		  new BlockPos(4, 0, 4), new BlockPos(4, 0, 3), new BlockPos(4, 0, 2), new BlockPos(4, 0, 1), 
		  	new BlockPos(4, 0, 0), new BlockPos(4, 0, -1), new BlockPos(4, 0, -2), new BlockPos(4, 0, -3), 
		  new BlockPos(4, 0, -4)
		};

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		confirm = compound.getInteger("confirm");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("confirm", confirm);
		return compound;
	}
	
	private int getAltarTier()
	{
		boolean quartzValid = checkAllIterable(QUARTZ_BLOCKS, Blocks.QUARTZ_BLOCK);
		boolean obsidianValid = checkAllArray(OBSIDIAN_BLOCKS, Blocks.OBSIDIAN);
		boolean imbuedValid = checkAllArray(IMBUED_LOG_BLOCKS, ModBlocks.imbued_log);
		
		if (quartzValid && obsidianValid && imbuedValid)
		{
			altarTier = 3;
			PacketManager.dispatchTE(worldObj, pos);
			return 3;
		}
		else if (quartzValid && obsidianValid)
		{
			altarTier = 2;
			PacketManager.dispatchTE(worldObj, pos);
			return 2;
		}
		altarTier = 1;
		PacketManager.dispatchTE(worldObj, pos);
		return 1;
	}

	boolean checkAllArray(BlockPos[] relPositions, Block block)
	{
		for (BlockPos position : relPositions)
		{
			if (!checkPlatform(position.getX(), position.getZ(), block))
				return false;
		}

		return true;
	}

	boolean checkAllIterable(Iterable<MutableBlockPos> relPositions, Block block)
	{
		for (BlockPos position : relPositions)
		{
			if (!checkPlatform(position.getX(), position.getZ(), block))
				return false;
		}

		return true;
	}

	boolean checkPlatform(int xOff, int zOff, Block block)
	{
		return worldObj.getBlockState(pos.add(xOff, -1, zOff)).getBlock() == block;
	}

	public void onWanded(EntityPlayer player, ItemStack wand)
	{
		if (player.isSneaking())
		{
			confirm = 0;
			markDirty();

			if (getWorld().isRemote)
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.altarmessage.name") + " " + getAltarTier()));
			}
		}
		else
		{
			if (hasValidRecipe())
			{
				if (worldObj.isRemote)
				{
					EnhancedProgression.proxy.setActionText((I18n.format("gui.confirm.name")));
				}
				else
				{
					confirm++;
					markDirty();
				}
			}
			if (!worldObj.isRemote && confirm > 1 && disableTicks == 0)
			{
				if (hasValidRecipe())
				{
					disableTicks = 50;
					EntityItem outputItem = new EntityItem(worldObj, getPos().getX() + 0.5, getPos().getY() + 1.5,
							getPos().getZ() + 0.5, currentOutput().copy());
					worldObj.spawnEntityInWorld(outputItem);
					altarRecipe = null;
					emptyAltar();
					confirm = 0;
					markDirty();
					PacketManager.dispatchTE(worldObj, pos);
				}
			}
			else if (!worldObj.isRemote && hasValidItems() && !hasValidRecipe())
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.wrongtier.name") + " " + getRequiredTier()
						+ " " + I18n.format("tile.altar.name")));
			}
			else if (!worldObj.isRemote && !hasValidRecipe())
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.norecipe.name")));
			}
		}
	}

	public boolean hasValidRecipe()
	{
		for (AltarRecipe recipe : ModRecipes.altarRecipes)
		{
			if (recipe.matches(itemHandler))
			{
				if (getRequiredTier() <= getAltarTier())
				{
					return true;
				}
			}
		}

		return false;
	}

	public boolean hasValidItems()
	{
		for (AltarRecipe recipe : ModRecipes.altarRecipes)
		{
			if (recipe.matches(itemHandler))
			{
				return true;
			}
		}

		return false;
	}

	public int getRequiredTier()
	{
		for (AltarRecipe recipe : ModRecipes.altarRecipes)
		{
			if (recipe.matches(itemHandler))
			{
				return recipe.getAltarTier();
			}
		}
		PacketManager.dispatchTE(worldObj, pos);
		return 0;
	}

	public ItemStack currentOutput()
	{
		for (AltarRecipe recipe : ModRecipes.altarRecipes)
			if (recipe.matches(itemHandler))
				return recipe.getOutput();

		return null;

	}

	public void emptyAltar()
	{
		for (int i = 0; i < getSizeInventory(); i++)
		{
			itemHandler.setStackInSlot(i, null);
		}
	}

	@Override
	protected SimpleItemStackHandler createItemHandler()
	{
		return new SimpleItemStackHandler(this, false)
		{
			@Override
			protected int getStackLimit(int slot, ItemStack stack)
			{
				return 1;
			}
		};
	}

	public boolean addItem(ItemStack stack)
	{
		boolean did = false;

		for (int i = 0; i < getSizeInventory(); i++)
			if (itemHandler.getStackInSlot(i) == null && disableTicks == 0)
			{
				did = true;
				ItemStack stackToAdd = stack.copy();
				stackToAdd.stackSize = 1;
				itemHandler.setStackInSlot(i, stackToAdd);
				stack.stackSize--;
				break;
			}

		if (did)
		{
			PacketManager.dispatchTE(worldObj, pos);
		}

		return true;
	}

	@Override
	public void update()
	{

		if (!worldObj.isRemote && confirm == 0 && disableTicks == 0)
		{
			List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class,
					new AxisAlignedBB(pos, pos.add(1, 1, 1)));
			for (EntityItem item : items)
			{
				if (!item.isDead && item.getEntityItem() != null)
				{
					ItemStack stack = item.getEntityItem();
					if (addItem(stack) && stack.stackSize == 0)
					{
						item.setDead();
					}
				}
			}
		}

		else if (disableTicks > 0)
		{
			--disableTicks;
		}
	}

	public void renderHUD(Minecraft mc, ScaledResolution res)
	{
		int screenWidth = res.getScaledWidth() / 2;
		int screenHeight = res.getScaledHeight() / 2;

		float angle = -90;
		int radius = 33;
		int amount = 0;

		// count the amount of items stored in the altar
		for (int i = 0; i < getSizeInventory(); i++)
		{
			if (itemHandler.getStackInSlot(i) == null)
				break;
			amount++;
		}
		// if any items are held in the altar
		if (amount > 0)
		{
			float anglePer = 360F / amount;

			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			for (int i = 0; i < amount; i++)
			{
				double xPos = screenWidth + Math.cos(angle * Math.PI / 180D) * radius - 8;
				double yPos = screenHeight + Math.sin(angle * Math.PI / 180D) * radius - 8;
				GlStateManager.pushMatrix();
				GlStateManager.translate(xPos, yPos, 0);
				mc.getRenderItem().renderItemIntoGUI(itemHandler.getStackInSlot(i), 0, 0);
				GlStateManager.translate(-xPos, -yPos, 0);
				GlStateManager.popMatrix();

				angle += anglePer;
			}

			if (hasValidRecipe())
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(screenWidth - 16, screenHeight - 16, 0);
				GlStateManager.scale(2, 2, 2);
				mc.getRenderItem().renderItemIntoGUI(currentOutput(), 0, 0);
				GlStateManager.translate(-screenWidth, -screenHeight, 0);
				GlStateManager.popMatrix();
			}
			else if (hasValidItems() && !hasValidRecipe())
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(screenWidth - 16, screenHeight - 16, 0);
				GlStateManager.scale(2, 2, 2);
				mc.getRenderItem().renderItemIntoGUI(currentOutput(), 0, 0);
				mc.fontRendererObj.drawStringWithShadow("x", screenWidth, screenHeight, 0xFFFFFF);
				GlStateManager.translate(-screenWidth, -screenHeight, 0);
				GlStateManager.popMatrix();

			}
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		}
	}

}
