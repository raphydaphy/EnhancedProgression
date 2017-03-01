package com.raphydaphy.vitality.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.vitality.essence.EssenceHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.render.ModelWand.LoaderWand;
import com.raphydaphy.vitality.util.MeshHelper;
import com.raphydaphy.vitality.util.NBTHelper;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
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
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (player.isSneaking()) {
			// i want to remove the [] brackets from the essence name but not sure how rn
			if (world.isRemote)
				ClientProxy
						.setActionText("Storing " + EssenceHelper.getEssenceStored(stack) + " / "
								+ EssenceHelper.getMaxEssence(stack) + " " + EssenceHelper
										.coreToAcceptedEssenceTypesList(EssenceHelper.getWandCore(stack)).toString()
								+ " Essence", TextFormatting.BOLD);
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10) 
	{
		//if (player.getEntityData().getString("wandCurOperation") != "fun")
		//{
			//player.getEntityData().setString("wandCurOperation", "fun");
			//NBTHelper.setString(stack, "useAction", "BOW");
			//System.out.println("use started");
			//player.setActiveHand(hand);
			//return EnumActionResult.SUCCESS;
		//}
		return EnumActionResult.PASS;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) 
	{
		
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) 
	{
		
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) 
	{
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entity;
			stack.getTagCompound().setString("useAction", null);
			player.getEntityData().setString("wandCurOperation", null);
		}
		System.out.println("done");
	}
	
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}
	
	@Override
	@Nullable
	public EnumAction getItemUseAction(ItemStack stack) 
	{
		switch (NBTHelper.getString(stack, "useAction", null)) 
		{
		case "BOW":
			return EnumAction.BOW;
		case "EAT":
			return EnumAction.EAT;
		case "BLOCK":
			return EnumAction.BLOCK;
		case "DRINK":
			return EnumAction.DRINK;
		default:
			return EnumAction.NONE;
		}
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
}
