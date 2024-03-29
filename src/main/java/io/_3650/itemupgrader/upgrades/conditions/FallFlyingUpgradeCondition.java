package io._3650.itemupgrader.upgrades.conditions;

import com.google.gson.JsonObject;

import io._3650.itemupgrader.api.data.EntryCategory;
import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.data.UpgradeEntrySet;
import io._3650.itemupgrader.api.data.UpgradeEventData;
import io._3650.itemupgrader.api.serializer.UpgradeConditionSerializer;
import io._3650.itemupgrader.api.type.UpgradeCondition;
import io._3650.itemupgrader.api.util.ComponentHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FallFlyingUpgradeCondition extends UpgradeCondition {
	
	private final UpgradeEntry<LivingEntity> livingEntry;
	
	public FallFlyingUpgradeCondition(IUpgradeInternals internals, boolean inverted, UpgradeEntry<LivingEntity> livingEntry) {
		super(internals, inverted, UpgradeEntrySet.create(builder -> builder.require(livingEntry)));
		this.livingEntry = livingEntry;
	}
	
	@Override
	public boolean test(UpgradeEventData data) {
		return data.getEntry(this.livingEntry).isFallFlying();
	}
	
	private final Serializer instance = new Serializer();
	
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return ComponentHelper.empty();
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeConditionSerializer<FallFlyingUpgradeCondition> {
		
		@Override
		public FallFlyingUpgradeCondition fromJson(IUpgradeInternals internals, boolean inverted, JsonObject json) {
			UpgradeEntry<LivingEntity> livingEntry = EntryCategory.LIVING.fromJson(json);
			return new FallFlyingUpgradeCondition(internals, inverted, livingEntry);
		}
		
		@Override
		public void toNetwork(FallFlyingUpgradeCondition condition, FriendlyByteBuf buf) {
			condition.livingEntry.toNetwork(buf);
		}
		
		@Override
		public FallFlyingUpgradeCondition fromNetwork(IUpgradeInternals internals, boolean inverted, FriendlyByteBuf buf) {
			UpgradeEntry<LivingEntity> livingEntry = EntryCategory.LIVING.fromNetwork(buf);
			return new FallFlyingUpgradeCondition(internals, inverted, livingEntry);
		}
		
	}
	
}