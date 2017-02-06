package com.raphydaphy.vitality.block.tile;

import java.util.List;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.item.ItemWand;
import com.raphydaphy.vitality.nbt.AltarRecipe;
import com.raphydaphy.vitality.network.PacketManager;
import com.raphydaphy.vitality.recipe.ModRecipes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

public class TileSpellForge extends TileAltar 
{
	public int altarTier = 1;
	private int confirm = 0;
	private int disableTicks = 0;
	
	@Override
	public int getAltarTier()
	{
		return 1;
	}
	
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
	
	public boolean isSpell(ItemStack spell)
	{
		if (spell.getItem() == ModItems.spell_card_enhanced_extraction || 
			spell.getItem() == ModItems.spell_card_explosion_1 ||
			spell.getItem() == ModItems.spell_card_explosion_2 ||
			spell.getItem() == ModItems.spell_card_explosion_3 || 
			spell.getItem() == ModItems.spell_card_fireball_1 || 
			spell.getItem() == ModItems.spell_card_fireball_2 || 
			spell.getItem() == ModItems.spell_card_fireball_3 || 
			spell.getItem() == ModItems.spell_card_flight ||
			spell.getItem() == ModItems.spell_card_forcefield ||
			spell.getItem() == ModItems.spell_card_hunger ||
			spell.getItem() == ModItems.spell_card_lantern_1 ||
			spell.getItem() == ModItems.spell_card_lantern_2 || 
			spell.getItem() == ModItems.spell_card_lantern_3 ||
			spell.getItem() == ModItems.spell_card_rapidfire_1 ||
			spell.getItem() == ModItems.spell_card_rapidfire_2 ||
			spell.getItem() == ModItems.spell_card_transmutation ||
			spell.getItem() == ModItems.spell_card_vital_extraction)
		{
			return true;
		}
		return false;
	}

	public boolean hasSpells()
	{
		for (int i = 0; i < getSizeInventory(); i++)
		{
			if (isSpell(itemHandler.getStackInSlot(i)))
			{
				return true;
			}
		}

		return false;
	}
	
	@Override
	public void emptyAltar()
	{
		for (int i = 0; i < getSizeInventory(); i++)
		{
			itemHandler.setStackInSlot(i, null);
		}
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
	
	public boolean readyToInfuse(EntityPlayer player)
	{
		if (player.getHeldItemOffhand().getItem() == ModItems.spell_bag && player.getHeldItemMainhand().getItem() instanceof ItemWand && hasSpells())
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void onWanded(EntityPlayer player, ItemStack wand)
	{
		if (player.isSneaking())
		{
			confirm = 0;
			markDirty();

			if (getWorld().isRemote)
			{
				Vitality.proxy.setActionText((I18n.format("gui.spellforgemessage.name") + " " + getAltarTier()));
			}
		}
		else
		{
			if (readyToInfuse(player))
			{
				if (worldObj.isRemote)
				{
					Vitality.proxy.setActionText((I18n.format("gui.confirm.name")));
				}
				else
				{
					confirm++;
					markDirty();
				}
			}
			if (!worldObj.isRemote && confirm > 1 && disableTicks == 0)
			{
				if (readyToInfuse(player))
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
			else if (!worldObj.isRemote && hasSpells() && !readyToInfuse(player))
			{
				Vitality.proxy.setActionText((I18n.format("gui.nobag.name")));
			}
			else if (!worldObj.isRemote && !hasSpells())
			{
				Vitality.proxy.setActionText((I18n.format("gui.nospells.name")));
			}
		}
	}
	
	public boolean addItem(ItemStack stack)
	{
		if (isSpell(stack))
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
		return false;
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
