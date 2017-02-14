package com.raphydaphy.vitality.block.tile;

import java.util.List;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.init.ModItems;
import com.raphydaphy.vitality.item.ItemSpell;
import com.raphydaphy.vitality.item.ItemWand;
import com.raphydaphy.vitality.network.PacketManager;

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
	
	@Override
	public int getAltarTier()
	{
		return 1;
	}
	
	@Override
	public int getSizeInventory()
	{
		// TODO Auto-generated method stub
		return 4;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		confirm = compound.getInteger("confirm");
	}

	public int getSpellID(ItemStack spell)
	{
		if (spell.getItem() == ModItems.spell_card_vital_extraction)
		{
			return 800;
		}
		else if (spell.getItem() == ModItems.spell_card_lantern_1)
		{
			return 810;
		}
		else if (spell.getItem() == ModItems.spell_card_lantern_2)
		{
			return 811;
		}
		else if (spell.getItem() == ModItems.spell_card_lantern_3)
		{
			return 812;
		}
		else if (spell.getItem() == ModItems.spell_card_explosion_1)
		{
			return 820;
		}
		else if (spell.getItem() == ModItems.spell_card_explosion_2)
		{
			return 821;
		}
		else if (spell.getItem() == ModItems.spell_card_explosion_3)
		{
			return 822;
		}
		else if (spell.getItem() == ModItems.spell_card_fireball_1)
		{
			return 830;
		}
		else if (spell.getItem() == ModItems.spell_card_fireball_2)
		{
			return 831;
		}
		else if (spell.getItem() == ModItems.spell_card_fireball_3)
		{
			return 832;
		}
		else if (spell.getItem() == ModItems.spell_card_rapidfire_1)
		{
			return 840;
		}
		else if (spell.getItem() == ModItems.spell_card_rapidfire_2)
		{
			return 841;
		}
		else if (spell.getItem() == ModItems.spell_card_transmutation)
		{
			return 85;
		}
		else if (spell.getItem() == ModItems.spell_card_hunger)
		{
			return 860;
		}
		else if (spell.getItem() == ModItems.spell_card_enhanced_extraction_1)
		{
			return 870;
		}
		else if (spell.getItem() == ModItems.spell_card_enhanced_extraction_2)
		{
			return 871;
		}
		else if (spell.getItem() == ModItems.spell_card_flight)
		{
			return 880;
		}
		else if (spell.getItem() == ModItems.spell_card_forcefield)
		{
			return 890;
		}
		return 0;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("confirm", confirm);
		return compound;
	}
	
	public boolean isSpell(ItemStack spell)
	{
		if (spell != null)
		{
			if (spell.getItem() instanceof ItemSpell)
			{
				return true;
			}
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
	
	public boolean readyToInfuse(EntityPlayer player)
	{
		try
		{
			if (player.getHeldItemOffhand().getItem() != null)
			{
				if (player.getHeldItemOffhand().getItem() == ModItems.spell_bag && player.getHeldItemMainhand().getItem() instanceof ItemWand && hasSpells())
				{
					return true;
				}
			}
		}
		catch (NullPointerException e)
		{
			return false;
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
					Vitality.proxy.setActionText((I18n.format("gui.confirmforge.name")));
				}
				else
				{
					confirm++;
					markDirty();
				}
			}
			if (!worldObj.isRemote && confirm > 1)
			{
				if (readyToInfuse(player))
				{
					for (int i = 0; i < getSizeInventory(); i++)
					{	
						if (itemHandler.getStackInSlot(i).getItem() != null)
						{
							ItemStack resultBag = player.getHeldItemOffhand();
		                	if (!resultBag.hasTagCompound())
		                	{
		                		resultBag.setTagCompound(new NBTTagCompound());
		                	}
		                	
		                	if (resultBag.getTagCompound().getIntArray("spells").length == 0)
		            		{
		            			resultBag.getTagCompound().setIntArray("spells", new int[10]);
		            		}
		            		
		                	for (int j = 0; i < resultBag.getTagCompound().getIntArray("spells").length; i++)
		                	{
		                		for (int curSpell : resultBag.getTagCompound().getIntArray("spells"))
			            		{
			            			if (curSpell == getSpellID(itemHandler.getStackInSlot(i)))
			            			{
			            				Vitality.proxy.setActionText((I18n.format("gui.overlap.name")));
			            				// we need to do something here to deal with the spell already being in the bag
			            			}
			            		}
			            		int[] curSpells = resultBag.getTagCompound().getIntArray("spells");
			            		for(int k=0; k < curSpells.length; k++)
			            		{
			            		    if(curSpells[k] == 0)
			            		    {
			            		    	curSpells[k] = getSpellID(itemHandler.getStackInSlot(i));
			            		    	itemHandler.setStackInSlot(i, null);
			            		    	Vitality.proxy.setActionText((I18n.format("gui.forgesuccess.name")));
			            		    	markDirty();
			            		    	PacketManager.dispatchTE(worldObj, pos);
			            		    	break;
			            		    }
			            		}
			            		resultBag.getTagCompound().setIntArray("spells", curSpells);
		                	}
						}
					}
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
				if (itemHandler.getStackInSlot(i) == null)
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
		if (!worldObj.isRemote && confirm == 0)
		{
			List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class,
					new AxisAlignedBB(pos, pos.add(2, 2, 2)));
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
			if (mc.thePlayer.getHeldItemOffhand() != null)
			{
				if (mc.thePlayer.getHeldItemOffhand().getItem() == ModItems.spell_bag)
				{
					GlStateManager.pushMatrix();
					GlStateManager.translate(screenWidth - 16, screenHeight - 16, 0);
					GlStateManager.scale(2, 2, 2);
					mc.getRenderItem().renderItemIntoGUI(new ItemStack(ModItems.spell_bag), 0, 0);
					GlStateManager.translate(-screenWidth, -screenHeight, 0);
					GlStateManager.popMatrix();
				}
			}
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		}
	}
}
