package io._3650.itemupgrader.api.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.slot.InventorySlot;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;

/**
 * Helpful class for dealing with some text component stuff
 * @author LegoMaster3650
 */
public class ComponentHelper {
	
	/**
	 * Simple {@linkplain DecimalFormat} that signs the given number with +/- and limiting it to 2 decimal places
	 */
	public static final DecimalFormat SIGNED_NUMBER = Util.make(new DecimalFormat("+#.##;-#.##"), format -> {
		format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
	});
	
	/**
	 * Simple {@linkplain DecimalFormat} that signs the given number with +/- and turns it into a percentage with up to 2 decimal places
	 */
	public static final DecimalFormat SIGNED_PERCENT = Util.make(new DecimalFormat("+#.##%;-#.##%"), format -> {
		format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
	});
	
	/**
	 * Quickly splits and merges a ResourceLocation around a . instead of a : for text components
	 * @param id The {@linkplain ResourceLocation} to format
	 * @return The given ResourceLocation reformatted
	 */
	public static String keyFormat(ResourceLocation id) {
		return id.getNamespace() + "." + id.getPath();
	}
	
	/**
	 * Quickly formats an entry into a text component key
	 * @param entry The {@linkplain UpgradeEntry} to format
	 * @return The given entry reformatted
	 */
	@Deprecated(forRemoval = true)
	public static String entryFormat(UpgradeEntry<?> entry) {
		return "upgradeEntry." + keyFormat(entry.getId());
	}
	
	/**
	 * Quickly applies a given color to a component
	 * @param color The {@linkplain TextColor} to apply
	 * @param component The {@linkplain MutableComponent} to color
	 * @return The colored component
	 */
	public static MutableComponent applyColor(TextColor color, MutableComponent component) {
		return component.setStyle(component.getStyle().withColor(color));
	}
	
	/**
	 * Utility for turning a single component into an array for the methods that demand an array
	 * @param component The {@linkplain MutableComponent} to turn into an array
	 * @return The component turned into an array
	 */
	public static MutableComponent[] arrayify(MutableComponent component) {
		return new MutableComponent[] {component};
	}
	
	/**
	 * Utility for creating an empty mutable component array for tooltip functions
	 * @return An empty array of {@linkplain MutableComponent}s
	 */
	public static MutableComponent[] empty() {
		return new MutableComponent[0];
	}
	
	/**
	 * Turns a list of components into a single component formatted into a comma seperated list terminating with or<br>
	 * Examples: "A, B, C, or D", "A or B", "A"
	 * @param components A {@linkplain List} of {@linkplain MutableComponent}s to format
	 * @return The components formatted as an or list
	 */
	public static MutableComponent orList(List<MutableComponent> components) {
		if (components.size() <= 0) return Component.translatable("tooltip.itemupgrader.error");
		if (components.size() == 1) return components.get(0);
		if (components.size() == 2) return components.get(0).append(Component.translatable("itemupgrader.text.or")).append(components.get(1));
		MutableComponent main = components.get(0);
		for (var i = 1; i < components.size() - 1; i++) {
			main.append(Component.translatable("itemupgrader.text.list")).append(components.get(i));
		}
		return main.append(Component.translatable("itemupgrader.text.or.list")).append(components.get(components.size() - 1));
	}
	
	/**
	 * Turns a list of components into a single component formatted into a comma seperated list terminating with and<br>
	 * Examples: "A, B, C, and D", "A and B", "A"
	 * @param components A {@linkplain List} of {@linkplain MutableComponent}s to format
	 * @return The components formatted as an and list
	 */
	public static MutableComponent andList(List<MutableComponent> components) {
		if (components.size() <= 0) return Component.translatable("tooltip.itemupgrader.error");
		if (components.size() == 1) return components.get(0);
		if (components.size() == 2) return components.get(0).append(Component.translatable("itemupgrader.text.and")).append(components.get(1));
		MutableComponent main = components.get(0);
		for (var i = 1; i < components.size() - 1; i++) {
			main.append(Component.translatable("itemupgrader.text.list")).append(components.get(i));
		}
		return main.append(Component.translatable("itemupgrader.text.and.list")).append(components.get(components.size() - 1));
	}
	
	/**
	 * Turns a list of components into a single component formatted into a comma seperated list<br>
	 * Examples: "A, B, C, D", "A, B", "A"
	 * @param components A {@linkplain List} of {@linkplain MutableComponent}s to format
	 * @return The components formatted as a list
	 */
	public static MutableComponent list(List<MutableComponent> components) {
		if (components.size() <= 0) return Component.translatable("tooltip.itemupgrader.error");
		if (components.size() == 1) return components.get(0);
		MutableComponent main = components.get(0);
		for (var i = 1; i < components.size() - 1; i++) {
			main.append(Component.translatable("itemupgrader.text.list")).append(components.get(i));
		}
		return main;
	}
	
	/**
	 * Quickly gets a translation key for the given slot
	 * @param slot The {@linkplain InventorySlot} to use
	 * @return A {@linkplain Component} with the item upgrader translation key for the given slot
	 */
	public static MutableComponent componentFromSlot(InventorySlot slot) {
		return Component.translatable("inventorySlot." + slot.getName());
	}
	
	/**
	 * Quickly gets a "When on X" or "When in X" depending on the slot type for the given slot
	 * @param slot The {@linkplain InventorySlot} to use
	 * @return A {@linkplain Component} for the given "When in/on slot"
	 */
	public static MutableComponent slotInOn(InventorySlot slot) {
		return Component.translatable("tooltip.itemupgrader.slots." + slot.getDisplayType(), componentFromSlot(slot));
	}
	
}