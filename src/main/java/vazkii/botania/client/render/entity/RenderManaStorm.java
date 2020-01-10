/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 25, 2015, 2:32:31 AM (GMT)]
 */
package vazkii.botania.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.entity.EntityManaStorm;

import javax.annotation.Nonnull;

public class RenderManaStorm extends EntityRenderer<EntityManaStorm> {

	public RenderManaStorm(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(@Nonnull EntityManaStorm storm, double x, double y, double z, float something, float pticks) {
		GlStateManager.pushMatrix();
		GlStateManager.translated(x, y, z);
		float maxScale = 1.95F;
		float scale = 0.05F + ((float) storm.burstsFired / EntityManaStorm.TOTAL_BURSTS - (storm.deathTime == 0 ? 0 : storm.deathTime + pticks) / EntityManaStorm.DEATH_TIME) * maxScale;
		RenderHelper.renderStar(0x00FF00, scale, scale, scale, storm.getUniqueID().getMostSignificantBits());
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(@Nonnull EntityManaStorm entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}

}
