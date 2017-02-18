package com.raphydaphy.vitality.item;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.helper.ToolHelper;
import com.raphydaphy.vitality.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemMagicTool extends ItemTool
{
	protected String name;
	protected String repairOredict;
	protected int essencePerDmg = 10;
	
	public ItemMagicTool(float attack, float speed, ToolMaterial toolMat, String unlocalizedName, int epd)
	{
        super(attack, speed, toolMat, new HashSet<Block>());

        this.name = unlocalizedName;
        // this.essencePerDmg = epd;
        this.setMaxDamage(this.getMaxDamage()*4);
        this.setHarvestLevel("pickaxe", toolMat.getHarvestLevel());
		setUnlocalizedName(unlocalizedName);
		setRegistryName(unlocalizedName);
		this.setCreativeTab(Vitality.creativeTab);
    }
	
	public void registerItemModel()
	{
		Vitality.proxy.registerItemRenderer(this, 0, name);
	}

	public ItemMagicTool setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, @Nonnull EntityLivingBase par3EntityLivingBase) {
		ToolHelper.damageItem(par1ItemStack, 1, par3EntityLivingBase, essencePerDmg);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(@Nonnull ItemStack stack, @Nonnull World world, IBlockState state, @Nonnull BlockPos pos, @Nonnull EntityLivingBase entity) {
		if(state.getBlockHardness(world, pos) != 0F)
		{
			ToolHelper.damageItem(stack, 1, entity, essencePerDmg);
		}

		return true;
	}
	public void breakOtherBlock(EntityPlayer player, ItemStack stack, BlockPos pos, BlockPos originPos, EnumFacing side) 
	{
		if (player.isSneaking() || player.getHeldItemMainhand().getItem() == ModItems.magic_multitool)
		{
			return;
		}
		World world = player.worldObj;

		if(world.isAirBlock(pos))
			return;

		int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
		boolean silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0;
		boolean doX = side.getFrontOffsetX() == 0;
		boolean doY = side.getFrontOffsetY() == 0;
		boolean doZ = side.getFrontOffsetZ() == 0;


		int range = 1;
		if (player.getHeldItemMainhand().getItem() == ModItems.fluxed_multitool)
		{
			range = 3;
		}
		int rangeY = Math.max(1, range);

		BlockPos beginDiff = new BlockPos(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
		BlockPos endDiff = new BlockPos(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);

		ToolHelper.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, null, silk, fortune);
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
		RayTraceResult raycast = ToolHelper.raytraceFromEntity(player.worldObj, player, true, 10);
		if(!player.worldObj.isRemote && raycast != null) {
			breakOtherBlock(player, stack, pos, pos, raycast.sideHit);
		}

		return false;
	}
	
	@Override
	public void onUpdate(ItemStack par1ItemStack, World world, Entity entity, int par4, boolean par5) {
		super.onUpdate(par1ItemStack, world, entity, par4, par5);
		if (par1ItemStack.getItemDamage() != 0)
		{
			if(entity instanceof EntityPlayer && !((EntityPlayer) entity).isSwingInProgress)
			{
				EntityPlayer player = (EntityPlayer) entity;
				SpellControl spellMan = new SpellControl();
				for(int i = 0; i < player.inventory.getSizeInventory(); i++) 
				{
					ItemStack stackAt = player.inventory.getStackInSlot(i);
					if (stackAt != null)
					{
						if (stackAt.getItem() == ModItems.essence_vial_full)
						{
							if (stackAt.hasTagCompound())
							{
								if (spellMan.useEssence(essencePerDmg, stackAt))
								{
									par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() -1);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
    public float getStrVsBlock(ItemStack stack, IBlockState state){
        if(state.getBlock() == Blocks.WEB){
            return 15.0F;
        }
        else{
            return state.getBlock().getHarvestTool(state) == null || state.getBlock().getHarvestTool(state).isEmpty() || this.getToolClasses(stack).contains(state.getBlock().getHarvestTool(state)) ? this.efficiencyOnProperMaterial : 1.0F;
        }
    }
	
	 @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack){

        return state.getBlock().getMaterial(state).isToolNotRequired() || (state.getBlock() == Blocks.SNOW_LAYER || state.getBlock() == Blocks.SNOW || (state.getBlock() == Blocks.OBSIDIAN ? this.toolMaterial.getHarvestLevel() >= 3 : (state.getBlock() != Blocks.DIAMOND_BLOCK && state.getBlock() != Blocks.DIAMOND_ORE ? (state.getBlock() != Blocks.EMERALD_ORE && state.getBlock() != Blocks.EMERALD_BLOCK ? (state.getBlock() != Blocks.GOLD_BLOCK && state.getBlock() != Blocks.GOLD_ORE ? (state.getBlock() != Blocks.IRON_BLOCK && state.getBlock() != Blocks.IRON_ORE ? (state.getBlock() != Blocks.LAPIS_BLOCK && state.getBlock() != Blocks.LAPIS_ORE ? (state.getBlock() != Blocks.REDSTONE_ORE && state.getBlock() != Blocks.LIT_REDSTONE_ORE ? (state.getBlock().getMaterial(state) == Material.ROCK || (state.getBlock().getMaterial(state) == Material.IRON || state.getBlock().getMaterial(state) == Material.ANVIL)) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2)));
    }
	
	public Set<String> getToolClasses(ItemStack stack){
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.add("pickaxe");
        hashSet.add("axe");
        hashSet.add("shovel");
        return hashSet;
    }

}
