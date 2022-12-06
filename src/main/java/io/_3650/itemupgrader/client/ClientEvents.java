package io._3650.itemupgrader.client;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import io._3650.itemupgrader.ItemUpgraderCore;
import io._3650.itemupgrader.api.ItemUpgrade;
import io._3650.itemupgrader.api.ItemUpgraderApi;
import io._3650.itemupgrader.api.type.UpgradeAction;
import io._3650.itemupgrader.api.util.ComponentHelper;
import io._3650.itemupgrader.api.util.UpgradeTooltipHelper;
import io._3650.itemupgrader.registry.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ItemUpgraderCore.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
	
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (ItemUpgraderApi.hasUpgrade(stack)) {
			ItemUpgrade upgrade = ItemUpgraderApi.getUpgrade(stack);
			if (upgrade == null) return;
			if (!upgrade.isVisible()) return;
			String upgradeTranslationKey = "upgrade." + ComponentHelper.keyFormat(upgrade.getId());
			List<MutableComponent> tooltip = Lists.newArrayList();
			if (Config.CLIENT.requiresKeyHeld.get() && !ModKeybinds.isKeyPressed(ModKeybinds.SHOW_TOOLTIP)) {
				//Upgrade Header
				tooltip.add(Component.translatable("tooltip.itemupgrader.upgrade", ComponentHelper.applyColor(upgrade.getColor(), Component.translatable(upgradeTranslationKey + ".icon"))).withStyle(ChatFormatting.GOLD));
				//Shift(or other key) to expand
				tooltip.add(upgradeLine(Component.translatable("tooltip.itemupgrader.expand", Component.keybind("key.itemupgrader.show_tooltip").withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY)));
			} else {
				//Upgrade Header
				tooltip.add(Component.translatable("tooltip.itemupgrader.upgrade", ComponentHelper.applyColor(upgrade.getColor(), Component.translatable(upgradeTranslationKey)).withStyle(ChatFormatting.BOLD)).withStyle(ChatFormatting.GOLD));
				
				//Description
				boolean hasDescription = upgrade.hasDescription();
				if (hasDescription) {
					int linecount = upgrade.getDescriptionLines();
					if (linecount > 1) {
						for (int i = 1; i <= linecount; i++) {
							tooltip.add(upgradeLine(Component.translatable(upgradeTranslationKey + ".description." + i).withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC)));
						}
					} else {
						tooltip.add(upgradeLine(Component.translatable(upgradeTranslationKey + ".description").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC)));
					}
				}
				
				//Slots Prep
				boolean doSlotsDisplay = false;
				int slotsDisplayIndex = tooltip.size();
				
				//Action Contents
				ListMultimap<EquipmentSlot, UpgradeAction> slotActions = MultimapBuilder.linkedHashKeys().arrayListValues().build();
				
				for (ResourceLocation actionId : upgrade.getValidActions()) {
					for (UpgradeAction action : upgrade.getActions(actionId)) {
						if (action.isVisible()) {
							if (action.getValidSlots().isEmpty()) {
								tooltip.add(upgradeLine(UpgradeTooltipHelper.action(action, stack)));
								doSlotsDisplay = true;
							} else {
								for (var slot : action.getValidSlots()) {
									slotActions.put(slot, action);
								}
							}
						}
					}
				}
				
				boolean actionEmptyLine = doSlotsDisplay;
				for (var slot : slotActions.keySet()) {
					if (actionEmptyLine || hasDescription) tooltip.add(upgradeLine(Component.literal("")));
					tooltip.add(upgradeLine(Component.translatable("tooltip.itemupgrader.slots", ComponentHelper.slotInOn(slot)).withStyle(ChatFormatting.GRAY)));
					for (var action : slotActions.get(slot)) {
						tooltip.add(upgradeLine(UpgradeTooltipHelper.action(action, stack)));
						actionEmptyLine = true;
					}
				}
				
				//Slots Post
				if (doSlotsDisplay) {
					List<MutableComponent> slotsList = Lists.newArrayList();
					for (EquipmentSlot slot : upgrade.getValidSlots()) {
						slotsList.add(ComponentHelper.slotInOn(slot));
					}
					tooltip.add(slotsDisplayIndex, upgradeLine(Component.translatable("tooltip.itemupgrader.slots", slotsList.size() == 0 ? Component.translatable("equipmentSlot.any") : ComponentHelper.orList(slotsList)).withStyle(ChatFormatting.GRAY)));
					if (hasDescription) tooltip.add(slotsDisplayIndex, upgradeLine(Component.literal("")));
				}
				
			}
			if (tooltip.size() == 1) tooltip.add(upgradeLine(Component.translatable("tooltip.itemupgrader.no_description").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)));
			event.getToolTip().addAll(1, tooltip);
			if (Config.CLIENT.showUpgradeID.get() && event.getFlags().isAdvanced()) event.getToolTip().add(Component.translatable("tooltip.itemupgrader.advanced_id", Component.literal(upgrade.getId().toString())).withStyle(ChatFormatting.DARK_GRAY));
		}
	}
	
	private static MutableComponent upgradeLine(MutableComponent component) {
		return Component.translatable("tooltip.itemupgrader.prefix").withStyle(ChatFormatting.DARK_GRAY).append(component);
	}
	
	@SubscribeEvent
	public static void onKeyMappings(RegisterKeyMappingsEvent event) {
		ModKeybinds.init(event);
	}
	
}