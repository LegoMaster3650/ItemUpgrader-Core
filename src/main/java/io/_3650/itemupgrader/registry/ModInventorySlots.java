package io._3650.itemupgrader.registry;

import io._3650.itemupgrader.api.registry.ItemUpgraderRegistry;
import io._3650.itemupgrader.api.slot.InventorySlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModInventorySlots {
	
	//registered to minecraft namespace because these are minecraft slots made by minecraft just being wrapped by itemupgrader
	public static final DeferredRegister<InventorySlot> SLOTS = DeferredRegister.create(ItemUpgraderRegistry.INVENTORY_SLOTS, ResourceLocation.DEFAULT_NAMESPACE);
	
	public static final RegistryObject<InventorySlot> MAINHAND = SLOTS.register("mainhand", () -> new EquipmentInventorySlot(EquipmentSlot.MAINHAND));
	public static final RegistryObject<InventorySlot> OFFHAND = SLOTS.register("offhand", () -> new EquipmentInventorySlot(EquipmentSlot.OFFHAND));
	public static final RegistryObject<InventorySlot> HEAD = SLOTS.register("head", () -> new EquipmentInventorySlot(EquipmentSlot.HEAD));
	public static final RegistryObject<InventorySlot> CHEST = SLOTS.register("chest", () -> new EquipmentInventorySlot(EquipmentSlot.CHEST));
	public static final RegistryObject<InventorySlot> LEGS = SLOTS.register("legs", () -> new EquipmentInventorySlot(EquipmentSlot.LEGS));
	public static final RegistryObject<InventorySlot> FEET = SLOTS.register("feet", () -> new EquipmentInventorySlot(EquipmentSlot.FEET));
	
	private static class EquipmentInventorySlot extends InventorySlot {
		
		private final EquipmentSlot slot;
		
		public EquipmentInventorySlot(EquipmentSlot slot) {
			super(slot);
			this.slot = slot;
		}
		
		@Override
		public ItemStack getItem(LivingEntity living) {
			return living.getItemBySlot(this.slot);
		}
		
		@Override
		public void setItem(LivingEntity living, ItemStack stack) {
			living.setItemSlot(this.slot, stack);
		}
		
		@Override
		public boolean hasItem(LivingEntity living) {
			return living.hasItemInSlot(this.slot);
		}
		
		@Override
		public String getName() {
			return this.slot.getName();
		}
		
		@Override
		public Object getBase() {
			return this.slot;
		}
		
		public String getDisplayType() {
			return switch (this.slot.getType()) {
				default -> InventorySlot.DisplayType.ON;
				case ARMOR -> InventorySlot.DisplayType.ON;
				case HAND -> InventorySlot.DisplayType.IN;
			};
		}
		
	}
	
}