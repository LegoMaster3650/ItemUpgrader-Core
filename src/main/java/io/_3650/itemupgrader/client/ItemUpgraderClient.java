package io._3650.itemupgrader.client;

import io._3650.itemupgrader.client.renderer.ItemUpgraderClientReloader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ItemUpgraderClient {
	
	public ItemUpgraderClient() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::onKeyMappings);
		if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager manager) {
			manager.registerReloadListener(ItemUpgraderClientReloader.INSTANCE);
		}
	}
	
	private void onKeyMappings(RegisterKeyMappingsEvent event) {
		ModKeybinds.init(event);
	}
	
}