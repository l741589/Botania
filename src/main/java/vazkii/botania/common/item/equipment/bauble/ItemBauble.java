/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 20, 2014, 3:30:06 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public abstract class ItemBauble extends ItemMod implements ICosmeticAttachable, IPhantomInkable {

	private static final String TAG_HASHCODE = "playerHashcode";
	private static final String TAG_BAUBLE_UUID_MOST = "baubleUUIDMost";
	private static final String TAG_BAUBLE_UUID_LEAST = "baubleUUIDLeast";
	private static final String TAG_COSMETIC_ITEM = "cosmeticItem";
	private static final String TAG_PHANTOM_INK = "phantomInk";

	public ItemBauble(Properties props) {
		super(props);
	}

	// Apparently baubles doesn't unequip on death, which causes attribute modifiers to get weird/desync on respawn
	// See Baubles#236
	// Do it for our baubles if they're going to drop
	// TODO there is still some weirdness going on when dying/returning in the End, figure that out
	@SubscribeEvent
	public static void onDeath(LivingDeathEvent evt) {
		/* todo 1.13
		if(!evt.getEntityLiving().world.isRemote
				&& evt.getEntityLiving() instanceof EntityPlayer
				&& !evt.getEntityLiving().world.getGameRules().getBoolean("keepInventory")
				&& !((EntityPlayer) evt.getEntityLiving()).isSpectator()) {
			IItemHandler inv = BaublesApi.getBaublesHandler((EntityPlayer) evt.getEntityLiving());
			for(int i = 0; i < inv.getSlots(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.isEmpty() && stack.getItem().getRegistryName().getNamespace().equals(LibMisc.MOD_ID)) {
					((ItemBauble) stack.getItem()).onUnequipped(stack, evt.getEntityLiving());
				}
			}
		}
		*/
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!EntityDoppleganger.isTruePlayer(player))
			return ActionResult.newResult(EnumActionResult.FAIL, stack);

		/* todo 1.13
		ItemStack toEquip = stack.copy();
		toEquip.setCount(1);

		if(canEquip(toEquip, player)) {
			if(world.isRemote)
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);

			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for(int i = 0; i < baubles.getSlots(); i++) {
				if(baubles.isItemValidForSlot(i, toEquip, player)) {
					ItemStack stackInSlot = baubles.getStackInSlot(i);
					if(stackInSlot.isEmpty() || ((IBauble) stackInSlot.getItem()).canUnequip(stackInSlot, player)) {
						// If toEquip and stackInSlot are stacks with equal value but not identity, ItemStackHandler.setStackInSlot actually does nothing >.>
						// Prevent it from trying to be overly smart by going through empty first
						baubles.setStackInSlot(i, ItemStack.EMPTY);

						baubles.setStackInSlot(i, toEquip);
						((IBauble) toEquip.getItem()).onEquipped(toEquip, player);

						stack.shrink(1);

						PlayerHelper.grantCriterion((EntityPlayerMP) player, new ResourceLocation(LibMisc.MOD_ID, "main/bauble_wear"), "code_triggered");

						if(!stackInSlot.isEmpty()) {
							((IBauble) stackInSlot.getItem()).onUnequipped(stackInSlot, player);

							if(stack.isEmpty()) {
								return ActionResult.newResult(EnumActionResult.SUCCESS, stackInSlot);
							} else {
								ItemHandlerHelper.giveItemToPlayer(player, stackInSlot);
							}
						}

						return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
					}
				}
			}
		}
		*/

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack par1ItemStack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		if(GuiScreen.isShiftKeyDown())
			addHiddenTooltip(par1ItemStack, world, stacks, flags);
		else stacks.add(new TextComponentTranslation("botaniamisc.shiftinfo"));
	}

	@OnlyIn(Dist.CLIENT)
	public void addHiddenTooltip(ItemStack par1ItemStack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		String key = vazkii.botania.client.core.helper.RenderHelper.getKeyDisplayString("Baubles Inventory");

		if(key != null)
			stacks.add(new TextComponentTranslation("botania.baubletooltip", key));

		ItemStack cosmetic = getCosmeticItem(par1ItemStack);
		if(!cosmetic.isEmpty())
			stacks.add(new TextComponentTranslation("botaniamisc.hasCosmetic", cosmetic.getDisplayName()));

		if(hasPhantomInk(par1ItemStack))
			stacks.add(new TextComponentTranslation("botaniamisc.hasPhantomInk"));
	}

	public boolean canEquip(ItemStack stack, EntityLivingBase player) {
		return true;
	}

	public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
		return true;
	}

	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		if(getLastPlayerHashcode(stack) != player.hashCode()) {
			onEquippedOrLoadedIntoWorld(stack, player);
			setLastPlayerHashcode(stack, player.hashCode());
		}
	}

	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		if(player != null) {
			if(!player.world.isRemote) {
				player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.equipBauble, SoundCategory.PLAYERS, 0.1F, 1.3F);
				PlayerHelper.grantCriterion((EntityPlayerMP) player, new ResourceLocation(LibMisc.MOD_ID, "main/bauble_wear"), "code_triggered");
			}

			onEquippedOrLoadedIntoWorld(stack, player);
			setLastPlayerHashcode(stack, player.hashCode());
		}
	}

	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {}

	public void onUnequipped(ItemStack stack, EntityLivingBase player) { }

	@Override
	public ItemStack getCosmeticItem(ItemStack stack) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_COSMETIC_ITEM, true);
		if(cmp == null)
			return ItemStack.EMPTY;
		return ItemStack.read(cmp);
	}

	@Override
	public void setCosmeticItem(ItemStack stack, ItemStack cosmetic) {
		NBTTagCompound cmp = new NBTTagCompound();
		if(!cosmetic.isEmpty())
			cmp = cosmetic.write(cmp);
		ItemNBTHelper.setCompound(stack, TAG_COSMETIC_ITEM, cmp);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return !getContainerItem(stack).isEmpty();
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return getCosmeticItem(itemStack);
	}

	public static UUID getBaubleUUID(ItemStack stack) {
		long most = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_MOST, 0);
		if(most == 0) {
			UUID uuid = UUID.randomUUID();
			ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_MOST, uuid.getMostSignificantBits());
			ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_LEAST, uuid.getLeastSignificantBits());
			return getBaubleUUID(stack);
		}

		long least = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_LEAST, 0);
		return new UUID(most, least);
	}

	public static int getLastPlayerHashcode(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_HASHCODE, 0);
	}

	public static void setLastPlayerHashcode(ItemStack stack, int hash) {
		ItemNBTHelper.setInt(stack, TAG_HASHCODE, hash);
	}

	@Override
	public boolean hasPhantomInk(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false);
	}

	@Override
	public void setPhantomInk(ItemStack stack, boolean ink) {
		ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink);
	}
}
