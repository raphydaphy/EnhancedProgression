package com.raphydaphy.vitality.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.raphydaphy.vitality.api.essence.Essence;
import com.raphydaphy.vitality.api.essence.MiscEssence;
import com.raphydaphy.vitality.block.BlockEssenceJar;
import com.raphydaphy.vitality.proxy.ClientProxy;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemVial extends ItemBase {

	int maxStored;
	Essence type;
	Item emptyVial = null;
	VialQuality quality;

	/**
	 * Use this constructor for the Empty Vial Variant.
	 * 
	 * @param name
	 *            - The Registry name.
	 * @param type
	 *            - The Essence type.
	 * @param maxStored
	 *            - The maximum amount this vial can hold.
	 */
	public ItemVial(String name, Essence type, int maxStored, VialQuality quality) {
		super(name, 1, true);
		this.maxStored = maxStored;
		this.type = type;
		this.quality = quality;
		Map<Essence, ItemVial> map = MiscEssence.vialMap.get(quality);
		if (map != null) {
			map.put(type, this);
			MiscEssence.vialMap.put(quality, map);
		} else if (map == null) {
			Map<Essence, ItemVial> map2 = new HashMap<Essence, ItemVial>();
			map2.put(type, this);
			MiscEssence.vialMap.put(quality, map2);
		}
	}

	/**
	 * Use this constructor for valid vials.
	 * 
	 * @param name
	 *            - The Registry name.
	 * @param type
	 *            - The Essence type.
	 * @param maxStored
	 *            - The maximum amount this vial can hold.
	 * @param emptyVial
	 *            - The item representing the Empty Vial for this group.
	 */
	public ItemVial(String name, @Nonnull Essence type, int maxStored, VialQuality quality,
			@Nonnull ItemVial emptyVial) {
		this(name, type, maxStored, quality);
		this.emptyVial = emptyVial;
	}

	public int getMaxStorage() {
		return maxStored;
	}

	public static int getCurrentStored(ItemStack stack) {
		return stack.getTagCompound().getInteger(Essence.KEY);
	}

	public Essence getVialType() {
		return type;
	}

	public boolean hasType() {
		return type != null;
	}

	public VialQuality getQuality() {
		return quality;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
		if (this.emptyVial != null)
			list.add(getFullVial());
		else
			list.add(new ItemStack(this));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player,
			EnumHand hand) {
		if (player.isSneaking()) {
			if (worldIn.isRemote && hasType()) {
				ClientProxy.setActionText(
						"Storing " + stack.getTagCompound().getInteger(Essence.KEY) + " / " + maxStored + " " + type.getName() + " Essence",
						type.getColor());
				player.swingArm(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}

		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	public ItemStack getFullVial() {
		ItemStack stack = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(Essence.KEY, getMaxStorage());
		stack.setTagCompound(tag);
		return stack;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof BlockEssenceJar) {
				((BlockEssenceJar) block).onBlockActivated(world, pos, world.getBlockState(pos), player, hand, stack,
						side, pos.getX(), pos.getY(), pos.getZ());
			
		}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (entity instanceof EntityPlayer) {
			ItemStack stack2 = stack;
			if (hasType() && !stack.hasTagCompound())
				stack2 = new ItemStack(emptyVial);
			else if (hasType() && stack.getTagCompound().getInteger(Essence.KEY) == 0)
				stack2 = new ItemStack(emptyVial);

			if (!ItemStack.areItemStacksEqual(stack, stack2))
				((EntityPlayer) entity).inventory.setInventorySlotContents(itemSlot, stack2);
		}
	}

	public enum VialQuality {
		BASIC, EMPOWERED,
	}

}
