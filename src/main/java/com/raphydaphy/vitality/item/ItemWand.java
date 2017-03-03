package com.raphydaphy.vitality.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.vitality.api.essence.IEssenceContainer;
import com.raphydaphy.vitality.api.wand.IWandable;
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.proxy.ClientProxy;
import com.raphydaphy.vitality.render.ModelWand.LoaderWand;
import com.raphydaphy.vitality.util.MeshHelper;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
			if (world.isRemote) {
				ClientProxy.setActionText("Storing " + TextFormatting.BOLD
						+ WandHelper.getEssenceStored(stack) + " / " + WandHelper.getMaxEssence(stack) + " "
						+ TextFormatting.RESET + TextFormatting.GOLD
						+ WandHelper.getCore(stack).acceptedTypes().get(0).name() + " Essence", TextFormatting.GOLD);
			}
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10) {
		if (player.getEntityData().getString("wandCurOperation") == "") {
			if (world.getBlockState(pos).getBlock() instanceof IWandable) {
				TileEntity tile = world.getTileEntity(pos);

				if (tile instanceof IEssenceContainer) {
					IEssenceContainer container = (IEssenceContainer) tile;
					if (container.getEssenceStored() > 0
							&& WandHelper.getCore(stack).acceptedTypes().contains(container.getEssenceType())) {
						System.out.println("started");
						player.getEntityData().setString("wandCurOperation", "extractFromContainer");
						player.getEntityData().setInteger("wandBlockPosX", pos.getX());
						player.getEntityData().setInteger("wandBlockPosY", pos.getY());
						player.getEntityData().setInteger("wandBlockPosZ", pos.getZ());
						player.getEntityData().setString("wandCurEssenceType", container.getEssenceType().getName());
						player.getEntityData().setInteger("wandCurEssenceStored", WandHelper.getEssenceStored(stack));
						player.setActiveHand(hand);
						return EnumActionResult.SUCCESS;
					}
				}
			}

		}
		return EnumActionResult.PASS;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase entity, int count) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			BlockPos pos = new BlockPos(player.getEntityData().getInteger("wandBlockPosX"),
					player.getEntityData().getInteger("wandBlockPosY"),
					player.getEntityData().getInteger("wandBlockPosZ"));

			switch (player.getEntityData().getString("wandCurOperation")) {
			case "extractFromContainer":
				IEssenceContainer container = (IEssenceContainer) player.getEntityWorld().getTileEntity(pos);
				if (container.getEssenceStored() > 0) {
					System.out.println("doing something");
					container.subtractEssence(1);
					player.getEntityData().setInteger("wandCurEssenceStored",
							player.getEntityData().getInteger("wandCurEssenceStored") + 1);
				}
				break;
			}
		}

	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.getEntityData().getString("wandCurOperation") == "extractFromContainer") {
				System.out.println(player.getEntityData().getInteger("wandCurEssenceStored"));
				WandHelper.setEssenceStored(stack, player.getEntityData().getInteger("wandCurEssenceStored"));
			}
			player.getEntityData().setString("wandCurEssenceType", "");
			player.getEntityData().setString("wandCurOperation", "");
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!isSelected && entity.getEntityData().getString("wandCurOperation") != "") {
			//this.onPlayerStoppedUsing(stack, world, (EntityLivingBase) entity, 0);
		}
	}

	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	@Nullable
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (stack.hasTagCompound()) {
			CoreType coreType = WandHelper.getCore(stack);
			TipType tipType = WandHelper.getTip(stack);

			try {
				return tipType.toString().toLowerCase() + "_" + coreType.toString().toLowerCase() + "_wand";
			} catch (Exception e) {
				return "invalid_wand";
			}
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
