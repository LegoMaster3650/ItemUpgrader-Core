package io._3650.itemupgrader.upgrades.results;

import com.google.gson.JsonObject;

import io._3650.itemupgrader.api.data.UpgradeEntrySet;
import io._3650.itemupgrader.api.data.UpgradeEventData;
import io._3650.itemupgrader.api.serializer.UpgradeResultSerializer;
import io._3650.itemupgrader.api.type.UpgradeResult;
import io._3650.itemupgrader.api.util.ComponentHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ConsumeUpgradeResult extends UpgradeResult {
	
	public ConsumeUpgradeResult(IUpgradeInternals internals) {
		super(internals, UpgradeEntrySet.CONSUMABLE);
	}
	
	@Override
	public boolean execute(UpgradeEventData data) {
		data.consume();
		return true;
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
	
	public static class Serializer extends UpgradeResultSerializer<ConsumeUpgradeResult> {
		
		@Override
		public ConsumeUpgradeResult fromJson(IUpgradeInternals internals, JsonObject json) {
			return new ConsumeUpgradeResult(internals);
		}
		
		@Override
		public void toNetwork(ConsumeUpgradeResult result, FriendlyByteBuf buf) {
			// nothing to write
		}
		
		@Override
		public ConsumeUpgradeResult fromNetwork(IUpgradeInternals internals, FriendlyByteBuf buf) {
			return new ConsumeUpgradeResult(internals);
		}
		
	}
	
}