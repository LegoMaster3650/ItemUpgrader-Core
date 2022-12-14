package io._3650.itemupgrader.client.renderer;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class ItemUpgraderClientReloader extends SimplePreparableReloadListener<Void> {
	
	public static final ItemUpgraderClientReloader INSTANCE = new ItemUpgraderClientReloader();
	
	@Override
	protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
		UpgradeOverlayRenderer.reload(resourceManager);
		UpgradeOverlayRenderer.clearCaches();
		return null;
	}

	@Override
	protected void apply(Void pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
		
	}
	
}