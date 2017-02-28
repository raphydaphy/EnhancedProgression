package com.raphydaphy.vitality.item;

import javax.annotation.Nonnull;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.init.Reference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWand extends ItemBase {
	public ItemWand(String parName) {
		super(parName, 1);
		this.setHasSubtypes(true);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (player.isSneaking()) {
			Vitality.proxy.setActionText("Storing " + stack.getTagCompound().getInteger("essenceStored") + " / "
					+ stack.getTagCompound().getInteger("maxEssence") + stack.getTagCompound().getInteger("essenceType")
					+ " Essence", TextFormatting.BOLD);
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10) {
		return EnumActionResult.PASS;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {

	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (stack.hasTagCompound()) {
			String coreType = stack.getTagCompound().getString("coreType");
			String tipType = stack.getTagCompound().getString("tipType");

			return tipType.toLowerCase() + "_" + coreType.toLowerCase() + "_wand";
		} else {
			return "invalid_wand";
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		return new ModelResourceLocation(Reference.MOD_ID + ":wand", "inventory");
	}
}
