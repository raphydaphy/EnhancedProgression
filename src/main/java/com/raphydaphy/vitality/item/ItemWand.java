package com.raphydaphy.vitality.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.vitality.Vitality;
import com.raphydaphy.vitality.essence.EssenceHelper;
import com.raphydaphy.vitality.essence.IEssenceContainer;
import com.raphydaphy.vitality.essence.IWandable;
import com.raphydaphy.vitality.init.Reference;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
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
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (player.isSneaking()) {
			// i want to remove the [] brackets from the essence name but not
			// sure how rn
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

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelLoader.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(this, MeshHelper.instance());
		ModelLoaderRegistry.registerLoader(LoaderWand.instance);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float par8, float par9, float par10) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		if (world.getBlockState(pos).getBlock() instanceof IWandable) {
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof IEssenceContainer) {
				if (stack.getTagCompound().getString("curAction") != "extractFromContainer") {
					IEssenceContainer container = (IEssenceContainer) tile;
					int essenceStored = container.getEssenceStored();
					String essenceType = container.getEssenceType();

					if (essenceStored > 0) {
						if (EssenceHelper.coreToAcceptedEssenceTypesList(EssenceHelper.getWandCore(stack))
								.contains(essenceType)) {
							System.out.println("restarting stuff");
							stack.setTagInfo("posX", new NBTTagInt(pos.getX()));
							stack.setTagInfo("posY", new NBTTagInt(pos.getY()));
							stack.setTagInfo("posZ", new NBTTagInt(pos.getZ()));
							stack.setTagInfo("essenceTypeOperation", new NBTTagString("essenceTypeOperation"));
							stack.setTagInfo("useAction", new NBTTagString("BOW"));
							stack.setTagInfo("curAction", new NBTTagString("extractFromContainer"));
							return EnumActionResult.SUCCESS;
						}
					}
				}
			}
		}
		System.out.println("No action found");
		stack.setTagInfo("curAction", new NBTTagString("nothing"));
		return EnumActionResult.PASS;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		// this dosent get called
		System.out.println("mega hacks achieved");

	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (isSelected) {
			System.out.println(NBTHelper.getString(stack, "useAction", "nothing"));
			if (stack.hasTagCompound()) {
				if (entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;

					BlockPos pos = new BlockPos(NBTHelper.getInt(stack, "posX", 0), NBTHelper.getInt(stack, "posY", 0),
							NBTHelper.getInt(stack, "posZ", 0));
					switch (NBTHelper.getString(stack, "curAction", "nothing")) {
					case "extractFromContainer":
						System.out.println("hi");
						TileEntity tile = player.worldObj.getTileEntity(pos);
						IEssenceContainer container = (IEssenceContainer) tile;
						int essenceStored = container.getEssenceStored();
						if (essenceStored > 0) {
							container.setEssenceStored(essenceStored - 1);
							EssenceHelper.addEssenceFree(stack, 1, EssenceHelper.getMaxEssence(stack),
									NBTHelper.getString(stack, "essenceTypeOperation", "Unknown"));
						} else {
							this.onItemUseFinish(stack, world, (EntityLivingBase) entity);
						}
						return;
					default:
						System.out.println("stopped");
						NBTHelper.setString(stack, "useAction", "NONE");
						return;
					}
				}

			}
		} else {
			if (NBTHelper.getString(stack, "curAction", "nothing") != "nothing") {
				this.onItemUseFinish(stack, world, (EntityLivingBase) entity);
				return;
			}
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase player, int timeLeft) {
		// this dosent get called
		System.out.println("IT WORKS :D");
		resetUseInfo(stack);
	}

	public static void resetUseInfo(ItemStack stack) {
		// this dosent get called
		System.out.println("resetting use info");
		NBTHelper.setString(stack, "curAction", "nothing");
		NBTHelper.setString(stack, "useAction", "NONE");
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
	@Nullable
	public EnumAction getItemUseAction(ItemStack stack) {
		switch (NBTHelper.getString(stack, "useAction", "NONE")) {
		case "BOW":
			return EnumAction.NONE;
		case "EAT":
			return EnumAction.EAT;
		case "BLOCK":
			return EnumAction.BLOCK;
		case "DRINK":
			return EnumAction.DRINK;
		case "NONE":
			return EnumAction.BOW;
		}
		return EnumAction.BOW;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}
