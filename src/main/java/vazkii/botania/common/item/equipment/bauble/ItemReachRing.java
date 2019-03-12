/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 22, 2014, 3:00:09 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;

public class ItemReachRing extends ItemBaubleModifier {

	public ItemReachRing(Properties props) {
		super(props);
	}

	@Override
	void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {
		attributes.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(getBaubleUUID(stack), "Reach Ring", 3.5, 0).setSaved(false));
	}

	/* todo 1.13
	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}
	*/

}
