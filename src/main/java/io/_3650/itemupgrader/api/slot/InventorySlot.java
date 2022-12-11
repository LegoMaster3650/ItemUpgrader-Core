package io._3650.itemupgrader.api.slot;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io._3650.itemupgrader.ItemUpgraderCore;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Inventory slot wrapper for easy mod inventory compatibility
 * @author LegoMaster3650
 */
public abstract class InventorySlot {
	
	/**
	 * Constructs a new {@linkplain InventorySlot} and saves its information for internal lookups
	 * @param base The base object that composes this slot
	 */
	public InventorySlot(Object base) {
		INSTANCES.add(this);
		CONVERTER.put(base, this);
	}
	
	private static final Set<InventorySlot> INSTANCES = Sets.newIdentityHashSet();
	private static final Map<Object, InventorySlot> CONVERTER = Maps.newIdentityHashMap();
	
	/**
	 * Gets the item in this slot for the given entity, or {@linkplain ItemStack#EMPTY} if not present
	 * @param living The {@linkplain LivingEntity} to get the item from
	 * @return The {@linkplain ItemStack} in the given slot
	 */
	public abstract ItemStack getItem(LivingEntity living);
	
	/**
	 * Sets the item in this slot for the given entity
	 * @param living The {@linkplain LivingEntity} to set the item for
	 * @param stack The {@linkplain ItemStack} to put in the slot
	 */
	public abstract void setItem(LivingEntity living, ItemStack stack);
	
	/**
	 * Swaps the current item in this slot for the given one for the given entity
	 * @param living The {@linkplain LivingEntity} to swap items for
	 * @param stack The {@linkplain ItemStack} to put in the slot
	 * @return The old {@linkplain ItemStack} in the slot
	 */
	public ItemStack swapItem(LivingEntity living, ItemStack stack) {
		ItemStack oldStack = this.getItem(living);
		this.setItem(living, stack);
		return oldStack;
	}
	
	/**
	 * Determines whether there is currently an item in this slot
	 * @param living The {@linkplain LivingEntity} to check the slot in
	 * @return Whether there is currently an item in this slot
	 */
	public abstract boolean hasItem(LivingEntity living);
	
	/**
	 * Gets the String name for this slot
	 * @return The String name for this slot
	 */
	public abstract String getName();
	
	/**
	 * Gets the base object that composes this slot
	 * @return The base object that composes this slot
	 */
	public abstract Object getBase();
	
	private ResourceLocation idCache = null;
	
	/**
	 * Gets the unique identifier for this slot
	 * @return The unique identifier for this slot
	 */
	@Nullable
	public ResourceLocation getId() {
		if (this.idCache == null) this.idCache = ItemUpgraderCore.INVENTORY_SLOT_REGISTRY.get().getKey(this);
		return this.idCache;
	}
	
	/**
	 * (Mostly used internally) Gets the display type keyword for this slot<br>
	 * The translation key for custom types is {@code tooltip.itemupgrader.slots.<displayType>} with a {@code %s} for the slot name itself
	 * @return The display type keyword for this slot
	 * @see InventorySlot.DisplayType
	 */
	@Nullable
	public abstract String getDisplayType();
	
	/**
	 * Gets an slot by its name (converted to a {@linkplain ResourceLocation})
	 * @param id The name of the slot to get
	 * @return The {@linkplain InventorySlot} with that name if present
	 */
	@Nullable
	public static InventorySlot byName(String id) {
		try {
			return byId(new ResourceLocation(id));
		} catch (ResourceLocationException e) {
			return null;
		}
	}
	
	/**
	 * Gets a slot by its identifier
	 * @param id The {@linkplain ResourceLocation} identifier of the slot to get
	 * @return The {@linkplain InventorySlot} with that id if present
	 */
	@Nullable
	public static InventorySlot byId(ResourceLocation id) {
		return ItemUpgraderCore.INVENTORY_SLOT_REGISTRY.get().getValue(id);
	}
	
	/**
	 * Gets an unmodifiable view of the set of every unique slot type
	 * @return An unmodifiable view of the set of every unique slot type
	 */
	public static Set<InventorySlot> values() {
		return Collections.unmodifiableSet(INSTANCES);
	}
	
	/**
	 * Gets a slot by its base object
	 * @param slotBase The object that makes up this slot
	 * @return The slot corresponding to that object if present
	 */
	@Nullable
	public static InventorySlot byBase(Object slotBase) {
		return CONVERTER.get(slotBase);
	}
	
	/**
	 * Default slot display type strings for ItemUpgrader
	 * @author LegoMaster3650
	 */
	public static final class DisplayType {
		/**
		 * The display type used for hand slots or similar
		 */
		public static final String IN =  "in";
		/**
		 * The display type used for equipment slots or similar
		 */
		public static final String ON = "on";
	}
	
}