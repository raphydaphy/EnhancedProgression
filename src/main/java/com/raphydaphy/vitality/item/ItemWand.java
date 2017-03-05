package com.raphydaphy.vitality.item;

import java.util.AbstractMap.SimpleEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.raphydaphy.vitality.api.essence.IEssenceContainer;
import com.raphydaphy.vitality.api.spell.Spell;
import com.raphydaphy.vitality.api.spell.SpellHelper;
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
		}
		else if (player.getEntityData().getString("wandCurOperation") == "")
		{
			if (wand.getTagCompound().getInteger(Spell.ACTIVE_KEY) != -1 && !player.isSneaking()) {
				ADWAWOFLBKSD
				ASGFPAKEWF
				SET THE SPELL HERE
				yeah throw some errors u stupid af compiler
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, wand);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack wand, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(wand);
		// System.out.println(world.isRemote + " <== world | essence ==> " +
		// WandHelper.getEssenceStored(stack));
		if (player.getEntityData().getString("wandCurOperation") == "") {
			if (world.getBlockState(pos).getBlock() instanceof IWandable) {
				TileEntity tile = world.getTileEntity(pos);

				if (tile instanceof IEssenceContainer) {
					IEssenceContainer container = (IEssenceContainer) tile;
					System.out.println(world.isRemote + " <== world | container essence ==> " + container.getEssenceStored());
					if (container.getEssenceStored() > 0
							&& pair.getKey().getCoreType() == container.getEssenceType()) {
						System.out.println("started");
						player.getEntityData().setString("wandCurOperation", "extractFromContainer");
						player.getEntityData().setInteger("x", pos.getX());
						player.getEntityData().setInteger("y", pos.getY());
						player.getEntityData().setInteger("z", pos.getZ());
						player.getEntityData().setString("wandCurEssenceType", container.getEssenceType().getName());
						player.getEntityData().setInteger("wandCurEssenceStored", WandHelper.getEssenceStored(wand));
						player.setActiveHand(hand);
						return EnumActionResult.SUCCESS;
					}
				}
			} else if (wand.getTagCompound().getInteger(Spell.ACTIVE_KEY) != -1 && !player.isSneaking()) {

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
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.getEntityData().getString("wandCurOperation") == "extractFromContainer") {
				System.out.println(player.getEntityData().getInteger("wandCurEssenceStored"));
				WandHelper.setEssenceStored(stack, player.getEntityData().getInteger("wandCurEssenceStored"));
			}
			else if (player.getEntityData().getString("wandCurOperation") == "useSpell")
			{
				Spell.spellMap.get(stack.getTagCompound().getInteger(Spell.ACTIVE_KEY)).onCastPre(stack, (EntityPlayer) entity, world, entity.getPosition(), null, null, 0, 0, 0);
				Spell.spellMap.get(stack.getTagCompound().getInteger(Spell.ACTIVE_KEY)).onCast(stack, (EntityPlayer) entity, world, entity.getPosition(), null, null, 0, 0, 0);
				Spell.spellMap.get(stack.getTagCompound().getInteger(Spell.ACTIVE_KEY)).onCastPost(stack, (EntityPlayer) entity, world, entity.getPosition(), null, null, 0, 0, 0);
			}
			player.getEntityData().setString("wandCurEssenceType", "");
			player.getEntityData().setString("wandCurOperation", "");
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!isSelected && entity.getEntityData().getString("wandCurOperation") != "") {
			// this.onPlayerStoppedUsing(stack, world, (EntityLivingBase)
			// entity, 0);
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
			SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(stack);

			try {
				return pair.getValue().getName().toLowerCase() + "_" + pair.getKey().getName().toLowerCase() + "_wand";
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
