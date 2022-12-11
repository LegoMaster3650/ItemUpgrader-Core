package io._3650.itemupgrader.client.renderer;

import io._3650.itemupgrader.ItemUpgraderCore;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = ItemUpgraderCore.MOD_ID, value = Dist.CLIENT, bus = Bus.MOD)
public class RendererEvents {
	
	// TODO remove in 1.19.3 (replaced with vanilla stitching stuff)
	@SubscribeEvent
	public static void onTextureStitchPre(TextureStitchEvent.Pre event) {
		if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
			UpgradeOverlayRenderer.getTextures().forEach(event::addSprite);
		}
		UpgradeOverlayRenderer.clearCaches();
	}
	
}