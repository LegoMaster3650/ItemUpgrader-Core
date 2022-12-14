package io._3650.itemupgrader.upgrades.results.modify;

import com.google.gson.JsonObject;

import io._3650.itemupgrader.api.data.EntryCategory;
import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.data.UpgradeEntrySet;
import io._3650.itemupgrader.api.data.UpgradeEventData;
import io._3650.itemupgrader.api.serializer.UpgradeResultSerializer;
import io._3650.itemupgrader.api.slot.InventorySlot;
import io._3650.itemupgrader.api.type.UpgradeResult;
import io._3650.itemupgrader.api.util.ComponentHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class UpdateSlotItemUpgradeResult extends UpgradeResult {
	
	private final UpgradeEntry<ItemStack> itemEntry;
	private final UpgradeEntry<LivingEntity> livingEntry;
	private final InventorySlot slot;
	
	public UpdateSlotItemUpgradeResult(IUpgradeInternals internals, UpgradeEntry<ItemStack> itemEntry, UpgradeEntry<LivingEntity> livingEntry, InventorySlot slot) {
		super(internals, UpgradeEntrySet.SLOT.with(builder -> {
			builder.requireAll(itemEntry, livingEntry);
		}));
		this.itemEntry = itemEntry;
		this.livingEntry = livingEntry;
		this.slot = slot;
	}
	
	@Override
	public boolean execute(UpgradeEventData data) {
		LivingEntity living = data.getEntry(this.livingEntry);
		ItemStack stack = this.slot.getItem(living);
		if (stack != null) {
			data.forceModifyEntry(UpgradeEntry.SLOT, this.slot);
			data.forceModifyEntry(this.itemEntry, stack);
			return true;
		} else return false;
	}
	
	private final Serializer instance = new Serializer();
	
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return new MutableComponent[] {Component.translatable(this.itemEntry.getDescriptionId()), ComponentHelper.componentFromSlot(this.slot)};
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeResultSerializer<UpdateSlotItemUpgradeResult> {
		
		@Override
		public UpdateSlotItemUpgradeResult fromJson(IUpgradeInternals internals, JsonObject json) {
			UpgradeEntry<ItemStack> itemEntry = EntryCategory.ITEM.fromJson(json);
			UpgradeEntry<LivingEntity> livingEntry = EntryCategory.LIVING.fromJson(json);
			InventorySlot slot = InventorySlot.byName(GsonHelper.getAsString(json, "slot"));
			return new UpdateSlotItemUpgradeResult(internals, itemEntry, livingEntry, slot);
		}
		
		@Override
		public void toNetwork(UpdateSlotItemUpgradeResult result, FriendlyByteBuf buf) {
			result.itemEntry.toNetwork(buf);
			result.livingEntry.toNetwork(buf);
			buf.writeResourceLocation(result.slot.getId());
		}
		
		@Override
		public UpdateSlotItemUpgradeResult fromNetwork(IUpgradeInternals internals, FriendlyByteBuf buf) {
			UpgradeEntry<ItemStack> itemEntry = EntryCategory.ITEM.fromNetwork(buf);
			UpgradeEntry<LivingEntity> livingEntry = EntryCategory.LIVING.fromNetwork(buf);
			InventorySlot slot = InventorySlot.byId(buf.readResourceLocation());
			return new UpdateSlotItemUpgradeResult(internals, itemEntry, livingEntry, slot);
		}
		
	}
	
}