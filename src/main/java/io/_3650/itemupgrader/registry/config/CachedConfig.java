package io._3650.itemupgrader.registry.config;

import java.util.regex.Pattern;

import io._3650.itemupgrader.ItemUpgraderCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ItemUpgraderCore.MOD_ID, bus = Bus.MOD)
public class CachedConfig {
	
	private static Pattern upgradeItemBlacklist = null;
	
	public static boolean isBlacklisted(Item item) {
		if (upgradeItemBlacklist == null) {
			String regexStr = Config.COMMON.upgradeItemBlacklist.get();
			upgradeItemBlacklist = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE);
		}
		ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
		return itemId != null && upgradeItemBlacklist.matcher(itemId.toString()).find();
	}
	
	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent.Reloading event) {
		if (event.getConfig().getSpec() == Config.COMMON_SPEC) {
			upgradeItemBlacklist = null;
		}
	}
	
}