/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class SleepingHandler {

	private SleepingHandler() {}

	@SubscribeEvent
	public static void trySleep(PlayerSleepInBedEvent event) {
		World world = event.getPlayer().world;
		if (!world.isRemote()) {
			boolean nearGuardian = ((ServerWorld) world).getEntities()
					.filter(e -> e instanceof EntityDoppleganger)
					.anyMatch(e -> ((EntityDoppleganger) e).getPlayersAround().contains(event.getPlayer()));

			if (nearGuardian) {
				event.setResult(PlayerEntity.SleepResult.NOT_SAFE);
			}
		}
	}
}
