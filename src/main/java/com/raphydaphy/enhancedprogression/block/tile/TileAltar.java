package com.raphydaphy.enhancedprogression.block.tile;

import java.util.List;

import com.raphydaphy.enhancedprogression.EnhancedProgression;
import com.raphydaphy.enhancedprogression.block.multiblock.Multiblock;
import com.raphydaphy.enhancedprogression.block.multiblock.MultiblockSet;
import com.raphydaphy.enhancedprogression.init.ModBlocks;
import com.raphydaphy.enhancedprogression.nbt.AltarRecipe;
import com.raphydaphy.enhancedprogression.nbt.VanillaPacketDispatcher;
import com.raphydaphy.enhancedprogression.recipe.ModRecipes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockQuartz;
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
    private boolean clearOutput = false;
    private int disableTicks = 0;
    
	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 16;
	}
	private static Iterable<BlockPos.MutableBlockPos> QUARTZ_BLOCKS = BlockPos.getAllInBoxMutable(new BlockPos(-2, 0, -2), new BlockPos(2, 0, 2));
	private static final BlockPos[] CLAY_BLOCKS = {
            new BlockPos(-3, 0, -3), new BlockPos(-3, 0, -2),
            new BlockPos(-3, 0, -1), new BlockPos(-2, 0, -3),
            new BlockPos(-3, 0, 0), new BlockPos(-3, 0, 1),
            new BlockPos(-3, 0, 2), new BlockPos(-3, 0, 3),
            new BlockPos(-2, 0, 3), new BlockPos(-1, 0, 3),
            new BlockPos(0, 0, 3), new BlockPos(1, 0, 3),
            new BlockPos(2, 0, 3), new BlockPos(3, 0, 3),
            new BlockPos(3, 0, 2), new BlockPos(3, 0, 1),
            new BlockPos(3, 0, 0), new BlockPos(3, 0, -1),
            new BlockPos(3, 0, -2), new BlockPos(3, 0, -3),
            new BlockPos(2, 0, -3), new BlockPos(1, 0, -3),
            new BlockPos(0, 0, -3), new BlockPos(-1, 0, -3)
         
	};
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
	    super.readFromNBT(compound);
	    confirm = compound.getInteger("confirm");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
	    super.writeToNBT(compound);
	    compound.setInteger("confirm", confirm);
	    return compound;
	}
	
	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();

		for(BlockPos relativePos : QUARTZ_BLOCKS)
		{
			mb.addComponent(relativePos, Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.CHISELED));
		}
		for(BlockPos relativePos : CLAY_BLOCKS)
		{
			mb.addComponent(relativePos, Blocks.HARDENED_CLAY.getDefaultState());
		}
		

		mb.addComponent(new BlockPos(0, 1, 0), ModBlocks.altar.getDefaultState());
		mb.setRenderOffset(new BlockPos(0, 1, 0));

		return mb.makeSet();
	}
	
	boolean hasValidPlatform() 
	{
		return checkAllIterable(QUARTZ_BLOCKS, Blocks.QUARTZ_BLOCK) && checkAllArray(CLAY_BLOCKS, Blocks.HARDENED_CLAY); 
	}
	
	boolean checkAllArray(BlockPos[] relPositions, Block block) {
		for (BlockPos position : relPositions) {
			if(!checkPlatform(position.getX(), position.getZ(), block))
				return false;
		}

		return true;
	}
	
	boolean checkAllIterable(Iterable<MutableBlockPos> relPositions, Block block) {
		for (BlockPos position : relPositions) {
			if(!checkPlatform(position.getX(), position.getZ(), block))
				return false;
		}

		return true;
	}
	
	boolean checkPlatform(int xOff, int zOff, Block block) {
		return worldObj.getBlockState(pos.add(xOff, -1, zOff)).getBlock() == block;
	}
	
	public void onWanded(EntityPlayer player, ItemStack wand) 
	{
		if(player.isSneaking()) 
		{
			confirm = 0;
			markDirty();
			
			if (hasValidPlatform())
			{
				altarTier = 2;
			}
			else
			{
				altarTier = 1;
			}
			if (getWorld().isRemote)
			{
				EnhancedProgression.proxy.setActionText((I18n.format("gui.altarmessage.name") + " " + altarTier));
			}
		}
		else 
		{
			if (altarRecipe != null)
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
			boolean clearTmp = false;
			if(!worldObj.isRemote && confirm > 1) 
			{
				if (altarRecipe != null)
				{
					EntityItem outputItem = new EntityItem(worldObj, getPos().getX() + 0.5, getPos().getY() + 1.5, getPos().getZ() + 0.5, altarRecipe.getOutput());
					worldObj.spawnEntityInWorld(outputItem);
					altarRecipe = null;
					clearOutput = true;
					clearTmp = true;
					confirm = 0;
					markDirty();
					for(int i = 0; i < getSizeInventory(); i++) {
						ItemStack stack = itemHandler.getStackInSlot(i);
						if(stack != null) {
							itemHandler.setStackInSlot(i, null);
						}
					}
				}
			}
			clearOutput = clearTmp;
		}
	}
	
	public boolean hasValidRecipe() {
		for(AltarRecipe recipe : ModRecipes.altarRecipes)
			if(recipe.matches(itemHandler))
				return true;

		return false;
	}
	
	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, false) {
			@Override
			protected int getStackLimit(int slot, ItemStack stack) {
				return 1;
			}
		};
	}
	
	public boolean addItem(ItemStack stack) 
	{

		boolean did = false;

		for(int i = 0; i < getSizeInventory(); i++)
			if(itemHandler.getStackInSlot(i) == null) {
				did = true;
				ItemStack stackToAdd = stack.copy();
				stackToAdd.stackSize = 1;
				itemHandler.setStackInSlot(i, stackToAdd);
				stack.stackSize--;
				break;
			}

		if(did)
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);

		return true;
	}
	@Override
	public void update()
	{
		altarRecipe = null;
		clearOutput = true;
		for(AltarRecipe recipe_ : ModRecipes.altarRecipes) 
		{
			if(recipe_.matches(itemHandler)) 
			{
				altarRecipe = recipe_;
				clearOutput = false;
				break;
			}
		}
		
		if(!worldObj.isRemote) 
		{
			List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
			for(EntityItem item : items)
			{
				if(!item.isDead && item.getEntityItem() != null) {
					ItemStack stack = item.getEntityItem();
					if(addItem(stack) && stack.stackSize == 0)
					{
						item.setDead();
					}
				}
			}
		}

	}
	
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		int screenWidth = res.getScaledWidth() / 2;
		int screenHeight = res.getScaledHeight() /2;

		float angle = -90;
		int radius = 33;
		int amount = 0;
		
		// count the amount of items stored in the altar
		for(int i = 0; i < getSizeInventory(); i++) {
			if(itemHandler.getStackInSlot(i) == null)
				break;
			amount++;
		}
		
		// if any items are held in the altar
		if(amount > 0) {
			float anglePer = 360F / amount;
				

			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			for(int i = 0; i < amount; i++) {
				double xPos = screenWidth + Math.cos(angle * Math.PI / 180D) * radius - 8;
				double yPos = screenHeight + Math.sin(angle * Math.PI / 180D) * radius - 8;
				GlStateManager.pushMatrix();
				GlStateManager.translate(xPos, yPos, 0);
				mc.getRenderItem().renderItemIntoGUI(itemHandler.getStackInSlot(i), 0, 0);
				GlStateManager.translate(-xPos, -yPos, 0);
				GlStateManager.popMatrix();

				angle += anglePer;
			}
			
			if (altarRecipe != null && clearOutput == false)
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(screenWidth - 16, screenHeight - 16, 0);
				GlStateManager.scale(2, 2, 2);
				mc.getRenderItem().renderItemIntoGUI(altarRecipe.getOutput(), 0, 0);
				GlStateManager.translate(-screenWidth, -screenHeight, 0);
				GlStateManager.popMatrix();
			}
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		}
	}

}
