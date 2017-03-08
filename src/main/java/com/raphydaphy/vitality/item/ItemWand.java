package com.raphydaphy.vitality.item;

import java.util.AbstractMap.SimpleEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.vitality.api.essence.IEssenceContainer;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.wand.IWandable;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.render.ModelWand.LoaderWand;
import com.raphydaphy.vitality.util.MeshHelper;
import com.raphydaphy.vitality.util.VitalData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWand extends ItemBase {
	public ItemWand(String parName) {
		super(parName, 1, false);
		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelLoader.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(this, MeshHelper.instance());
		ModelLoaderRegistry.registerLoader(LoaderWand.instance);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack wand, World world, EntityPlayer player, EnumHand hand) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);
		if (player.isSneaking()) {
			if (world.isRemote) {
				ClientProxy.setActionText(
						"Storing " + WandHelper.getEssenceStored(wand) + " / " + WandHelper.getMaxEssence(wand) + " "
								+ pair.getKey().getCoreType().getName() + " Essence",
						pair.getKey().getCoreType().getColor());
			}
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, wand);
		} else if (player.getEntityData().getString("wandCurOperation") == "") {
			int k = wand.getTagCompound().getInteger(Spell.ACTIVE_KEY);
			if (k != -1 && !player.isSneaking() && Spell.spellMap.get(k).canBeCast(wand)) {
				player.getEntityData().setString("wandCurOperation", "useSpell");
				player.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, wand);
			} else if (k != -1 && world.isRemote && !Spell.spellMap.get(k).getNeedsBlock()) {
				ClientProxy.setActionText(I18n.format("vitality.wand.notenoughessence.name"),
						pair.getKey().getCoreType().getColor());
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, wand);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);
		if (player.getEntityData().getString("wandCurOperation") == "") {
			if (world.getBlockState(pos).getBlock() instanceof IWandable) {
				TileEntity tile = world.getTileEntity(pos);

				if (tile instanceof IEssenceContainer) {
					IEssenceContainer container = (IEssenceContainer) tile;
					if (container.getEssenceStored() > 0 && pair.getKey().getCoreType() == container.getEssenceType()) {
						player.getEntityData().setString("wandCurOperation", "extractFromContainer");
						player.getEntityData().setInteger(VitalData.POS_X, pos.getX());
						player.getEntityData().setInteger(VitalData.POS_Y, pos.getY());
						player.getEntityData().setInteger(VitalData.POS_Z, pos.getZ());
						player.getEntityData().setString("wandCurEssenceType", container.getEssenceType().getName());
						player.getEntityData().setInteger("wandCurEssenceStored", WandHelper.getEssenceStored(wand));
						player.setActiveHand(hand);
						return EnumActionResult.SUCCESS;
					}
				}
			} else if (wand.getTagCompound().getInteger(Spell.ACTIVE_KEY) != -1 && !player.isSneaking()) {
				Spell spell = Spell.spellMap.get(wand.getTagCompound().getInteger(Spell.ACTIVE_KEY));
				if (spell.isEssenceValid(pair.getKey().getCoreType())
						&& spell.onCastPre(wand, player, world, pos, hand, side, hitX, hitY, hitZ)) {
					player.getEntityData().setString(VitalData.FACING, side.getName());
					player.getEntityData().setFloat(VitalData.HIT_X, hitX);
					player.getEntityData().setFloat(VitalData.HIT_Y, hitY);
					player.getEntityData().setFloat(VitalData.HIT_Z, hitZ);
					player.getEntityData().setString("wandCurOperation", "useSpell");
					player.setActiveHand(hand);
					onPlayerStoppedUsing(wand, world, player, 0);
					return EnumActionResult.SUCCESS;
					}
				else if (spell.isEssenceValid(pair.getKey().getCoreType())
						&& spell.onCastTick(wand, player, 0)) {
					player.getEntityData().setString("wandCurOperation", "useSpell");
					player.setActiveHand(hand);
					return EnumActionResult.SUCCESS;
				}
			}

		}
		return EnumActionResult.PASS;
	}

	@Override
	public void onUsingTick(ItemStack wand, EntityLivingBase entity, int count) 
	{
		if (entity instanceof EntityPlayer) 
		{
			
			EntityPlayer player = (EntityPlayer) entity;
			BlockPos pos = new BlockPos(player.getEntityData().getInteger(VitalData.POS_X),
					player.getEntityData().getInteger(VitalData.POS_Y),
					player.getEntityData().getInteger(VitalData.POS_Z));

			if(player.getEntityData().getString("wandCurOperation").equals("extractFromContainer"))
			{
				IEssenceContainer container = (IEssenceContainer) player.getEntityWorld().getTileEntity(pos);
				if (container.getEssenceStored() > 0) 
				{
					container.subtractEssence(1);
					player.getEntityData().setInteger("wandCurEssenceStored",
							player.getEntityData().getInteger("wandCurEssenceStored") + 1);
				}
			}
			else if(player.getEntityData().getString("wandCurOperation").equals("useSpell"))
			{
				Spell spell = Spell.spellMap.get(wand.getTagCompound().getInteger(Spell.ACTIVE_KEY));
				//player.setActiveHand(player.getActiveHand());
				if (spell.canBeCast(wand) && spell.onCastTick(wand, player, count))
				{
					spell.onCastTickSuccess(wand, player, count);
				}
			}
		}

	}

	@Override
    @Nullable
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        return stack;
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) 
	{
		if (entity instanceof EntityPlayer) 
		{
			EntityPlayer player = (EntityPlayer) entity;
			if (player.getEntityData().getString("wandCurOperation") == "extractFromContainer") 
			{
				WandHelper.setEssenceStored(stack, player.getEntityData().getInteger("wandCurEssenceStored"));
			} 
			else if (player.getEntityData().getString("wandCurOperation") == "useSpell") 
			{
				BlockPos pos = new BlockPos(player.getEntityData().getInteger(VitalData.POS_X),
						player.getEntityData().getInteger(VitalData.POS_Y),
						player.getEntityData().getInteger(VitalData.POS_Z));

				NBTTagCompound tag = player.getEntityData();
				Spell spell = Spell.spellMap.get(stack.getTagCompound().getInteger(Spell.ACTIVE_KEY));
				if (spell.onCast(stack, player, world,
						pos, player.getActiveHand(), EnumFacing.byName(tag.getString(VitalData.FACING)),
						tag.getFloat(VitalData.HIT_X), tag.getFloat(VitalData.HIT_Y), tag.getFloat(VitalData.HIT_Z)))
				{
					
					spell.onCastPost(stack, player, world, pos,
							player.getActiveHand(), player.getHorizontalFacing(), tag.getFloat(VitalData.HIT_X),
							tag.getFloat(VitalData.HIT_Y), tag.getFloat(VitalData.HIT_Z));
				}

			}
			player.getEntityData().removeTag("wandCurEssenceType");
			player.getEntityData().removeTag("wandCurOperation");
			player.getEntityData().removeTag(VitalData.HIT_X);
			player.getEntityData().removeTag(VitalData.HIT_Y);
			player.getEntityData().removeTag(VitalData.HIT_Z);
			player.getEntityData().removeTag(VitalData.POS_X);
			player.getEntityData().removeTag(VitalData.POS_Y);
			player.getEntityData().removeTag(VitalData.POS_Z);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) 
	{
		// if(!isSelected) stack.getTagCompound().setInteger(Spell.ACTIVE_KEY,
		// -1);;
	}

	public int getMaxItemUseDuration(ItemStack stack) 
	{
		return 72000;
	}

	@Override
	@Nullable
	public EnumAction getItemUseAction(ItemStack stack) 
	{
		
		return EnumAction.BOW;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		if (stack.hasTagCompound()) 
		{
			SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(stack);

			try 
			{
				return pair.getKey().getTier().getName().toLowerCase() + "_" + pair.getValue().getName().toLowerCase()
						+ "_" + pair.getKey().getName().toLowerCase() + "_wand";
			} 
			catch (Exception e) 
			{
				return "invalid_wand";
			}
		} 
		else 
		{
			return "invalid_wand";
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) 
	{
		return true;
	}
}
