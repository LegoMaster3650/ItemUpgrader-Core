package io._3650.itemupgrader.client;

import com.mojang.blaze3d.platform.InputConstants;

import io._3650.itemupgrader.ItemUpgraderCore;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;

public class ModKeybinds {
	
	private static final String MAIN_CATEGORY = "key.categories." + ItemUpgraderCore.MOD_ID;
	
	public static final KeyMapping SHOW_TOOLTIP = createKeybind("show_tooltip", KeyConflictContext.GUI, InputConstants.KEY_LSHIFT, MAIN_CATEGORY);
	
	public static void init(RegisterKeyMappingsEvent event) {
		event.register(SHOW_TOOLTIP);
	}
	
	private static KeyMapping createKeybind(String name, KeyConflictContext context, int keycode, String category) {
		return new KeyMapping("key." + ItemUpgraderCore.MOD_ID + "." + name, context, InputConstants.Type.KEYSYM, keycode, category);
	}
	
	public static boolean isKeyPressed(KeyMapping key) {
		return key != null && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue());
	}
	
}