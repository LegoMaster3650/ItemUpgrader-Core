package io._3650.itemupgrader.upgrades.conditions;

import com.google.gson.JsonObject;

import io._3650.itemupgrader.api.ItemUpgrade;
import io._3650.itemupgrader.api.ItemUpgraderApi;
import io._3650.itemupgrader.api.data.EntryCategory;
import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.data.UpgradeEntrySet;
import io._3650.itemupgrader.api.data.UpgradeEventData;
import io._3650.itemupgrader.api.serializer.UpgradeConditionSerializer;
import io._3650.itemupgrader.api.slot.InventorySlot;
import io._3650.itemupgrader.api.type.UpgradeCondition;
import io._3650.itemupgrader.api.util.ComponentHelper;
import io._3650.itemupgrader.upgrades.ItemUpgradeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class HasUpgradeCondition extends UpgradeCondition {
	
	private final UpgradeEntry<LivingEntity> livingEntry;
	private final InventorySlot slot;
	private final ResourceLocation upgradeId;
	
	public HasUpgradeCondition(IUpgradeInternals internals, boolean inverted, UpgradeEntry<LivingEntity> livingEntry, InventorySlot slot, ResourceLocation upgradeId) {
		super(internals, inverted, UpgradeEntrySet.create(builder -> {
			builder.require(livingEntry);
		}));
		this.livingEntry = livingEntry;
		this.slot = slot;
		this.upgradeId = upgradeId;
	}

	@Override
	public boolean test(UpgradeEventData data) {
		ItemStack stack = this.slot.getItem(data.getEntry(this.livingEntry));
		if (!ItemUpgraderApi.hasUpgrade(stack)) return false;
		return ItemUpgraderApi.getUpgradeKey(stack).equals(this.upgradeId);
	}

	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		ItemUpgrade upgrade = ItemUpgradeManager.INSTANCE.getUpgrade(this.upgradeId);
		MutableComponent upgradeComponent = Component.translatable("upgrade." + ComponentHelper.keyFormat(this.upgradeId));
		if (upgrade != null) upgradeComponent = ComponentHelper.applyColor(upgrade.getColor(), upgradeComponent);
		MutableComponent slotComponent = ComponentHelper.slotInOn(this.slot);
		return new MutableComponent[] {upgradeComponent, slotComponent};
	}

	@Override
	public Serializer getSerializer() {
		return new Serializer();
	}

	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeConditionSerializer<HasUpgradeCondition> {

		@Override
		public HasUpgradeCondition fromJson(IUpgradeInternals internals, boolean inverted, JsonObject json) {
			UpgradeEntry<LivingEntity> livingEntry = EntryCategory.LIVING.fromJson(json, "entity");
			InventorySlot slot = InventorySlot.byName(GsonHelper.getAsString(json, "slot"));
			ResourceLocation upgradeId = new ResourceLocation(GsonHelper.getAsString(json, "upgrade"));
			return new HasUpgradeCondition(internals, inverted, livingEntry, slot, upgradeId);
		}

		@Override
		public void toNetwork(HasUpgradeCondition condition, FriendlyByteBuf buf) {
			condition.livingEntry.toNetwork(buf);
			buf.writeResourceLocation(condition.slot.getId());
			buf.writeResourceLocation(condition.upgradeId);
		}

		@Override
		public HasUpgradeCondition fromNetwork(IUpgradeInternals internals, boolean inverted, FriendlyByteBuf buf) {
			UpgradeEntry<LivingEntity> livingEntry = EntryCategory.LIVING.fromNetwork(buf);
			InventorySlot netSlot = InventorySlot.byId(buf.readResourceLocation());
			ResourceLocation netUpgradeId = buf.readResourceLocation();
			return new HasUpgradeCondition(internals, inverted, livingEntry, netSlot, netUpgradeId);
		}
		
	}
	
}