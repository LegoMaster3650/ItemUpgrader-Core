package io._3650.itemupgrader.api.slot;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io._3650.itemupgrader.ItemUpgraderCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class InventorySlot {
	
	public InventorySlot(Object base) {
		INSTANCES.add(this);
		CONVERTER.put(base, this);
	}
	
	private static final Set<InventorySlot> INSTANCES = Sets.newIdentityHashSet();
	private static final Map<Object, InventorySlot> CONVERTER = Maps.newIdentityHashMap();
	
	public abstract ItemStack getItem(LivingEntity living);
	
	public abstract void setItem(LivingEntity living, ItemStack stack);
	
	public ItemStack swapItem(LivingEntity living, ItemStack stack) {
		ItemStack oldStack = this.getItem(living);
		this.setItem(living, stack);
		return oldStack;
	}
	
	public abstract boolean hasItem(LivingEntity living);
	
	public abstract String getName();
	
	public abstract Object getBase();
	
	private ResourceLocation idCache = null;
	
	@Nullable
	public ResourceLocation getId() {
		if (this.idCache == null) this.idCache = ItemUpgraderCore.INVENTORY_SLOT_REGISTRY.get().getKey(this);
		return this.idCache;
	}
	
	@Nullable
	public abstract DisplayType getType();
	
	@Nullable
	public static InventorySlot byName(String id) {
		return byId(new ResourceLocation(id));
	}
	
	@Nullable
	public static InventorySlot byId(ResourceLocation id) {
		return ItemUpgraderCore.INVENTORY_SLOT_REGISTRY.get().getValue(id);
	}
	
	public static Set<InventorySlot> values() {
		return Collections.unmodifiableSet(INSTANCES);
	}
	
	@Nullable
	public static InventorySlot byBase(Object slotBase) {
		return CONVERTER.get(slotBase);
	}
	
	public static enum DisplayType {
		IN("in"),
		ON("on");
		
		private final String name;
		
		private DisplayType(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	
}