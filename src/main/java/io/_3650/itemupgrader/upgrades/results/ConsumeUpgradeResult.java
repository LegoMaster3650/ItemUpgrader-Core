package io._3650.itemupgrader.upgrades.results;

import com.google.gson.JsonObject;

import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.data.UpgradeEntrySet;
import io._3650.itemupgrader.api.data.UpgradeEventData;
import io._3650.itemupgrader.api.serializer.UpgradeResultSerializer;
import io._3650.itemupgrader.api.type.UpgradeResult;
import io._3650.itemupgrader.api.util.ComponentHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class ConsumeUpgradeResult extends UpgradeResult {
	
	private final boolean value;
	
	public ConsumeUpgradeResult(IUpgradeInternals internals, boolean value) {
		super(internals, UpgradeEntrySet.CONSUMABLE);
		this.value = value;
	}
	
	@Override
	public boolean execute(UpgradeEventData data) {
		data.setModifiableEntry(UpgradeEntry.CONSUMED, this.value);
		return true;
	}
	
	private final Serializer instance = new Serializer();
	
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return ComponentHelper.arrayify(Component.translatable(this.value ? "tooltip.itemupgrader.deny" : "tooltip.itemupgrader.allow"));
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeResultSerializer<ConsumeUpgradeResult> {
		
		@Override
		public ConsumeUpgradeResult fromJson(IUpgradeInternals internals, JsonObject json) {
			boolean value = GsonHelper.getAsBoolean(json, "value", true);
			return new ConsumeUpgradeResult(internals, value);
		}
		
		@Override
		public void toNetwork(ConsumeUpgradeResult result, FriendlyByteBuf buf) {
			buf.writeBoolean(result.value);
		}
		
		@Override
		public ConsumeUpgradeResult fromNetwork(IUpgradeInternals internals, FriendlyByteBuf buf) {
			boolean value = buf.readBoolean();
			return new ConsumeUpgradeResult(internals, value);
		}
		
	}
	
}