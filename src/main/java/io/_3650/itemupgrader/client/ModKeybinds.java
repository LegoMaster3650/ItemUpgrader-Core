package io._3650.itemupgrader.client;

import com.mojang.blaze3d.platform.InputConstants;

import io._3650.itemupgrader.ItemUpgraderCore;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;

public class ModKeybinds {
	
	private static final String MAIN_CATEGORY = "key.categories." + ItemUpgraderCore.MOD_ID;
	
	public static final KeyMapping SHOW_TOOLTIP = new KeyMapping("key." + ItemUpgraderCore.MOD_ID + ".show_tooltip", KeyConflictContext.GUI, InputConstants.Type.KEYSYM, InputConstants.KEY_LSHIFT, MAIN_CATEGORY);
	
	public static KeyMapping showTooltip;
	
	public static void init(RegisterKeyMappingsEvent event) {
		showTooltip = createKeybind(event, "show_tooltip", KeyConflictContext.GUI, InputConstants.KEY_LSHIFT, MAIN_CATEGORY);
	}
	
	private static KeyMapping createKeybind(RegisterKeyMappingsEvent event, String name, KeyConflictContext context, int keycode, String category) {
		KeyMapping keyMap = new KeyMapping("key." + ItemUpgraderCore.MOD_ID + "." + name, context, InputConstants.Type.KEYSYM, keycode, category);
		event.register(keyMap);
		return keyMap;
	}
	
	public static boolean isKeyPressed(KeyMapping key) {
		return key != null && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue());
	}
	
}