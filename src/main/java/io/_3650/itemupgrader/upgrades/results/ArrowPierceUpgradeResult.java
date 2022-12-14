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
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;

public class ArrowPierceUpgradeResult extends UpgradeResult {
	
	private final byte amount;
	
	public ArrowPierceUpgradeResult(IUpgradeInternals internals, byte amount) {
		super(internals, UpgradeEntrySet.create(builder -> {
			builder.require(UpgradeEntry.PROJECTILE);
		}));
		this.amount = amount;
	}
	
	@Override
	public boolean execute(UpgradeEventData data) {
		if (!(data.getEntry(UpgradeEntry.PROJECTILE) instanceof AbstractArrow arrow)) return false;
		arrow.setPierceLevel((byte)(arrow.getPierceLevel() + this.amount));
		return true;
	}
	
	private final Serializer instance = new Serializer();
	
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return ComponentHelper.arrayify(Component.literal(Byte.valueOf(this.amount).toString()));
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeResultSerializer<ArrowPierceUpgradeResult> {
		
		@Override
		public ArrowPierceUpgradeResult fromJson(IUpgradeInternals internals, JsonObject json) {
			byte amount = GsonHelper.getAsByte(json, "amount");
			return new ArrowPierceUpgradeResult(internals, amount);
		}
		
		@Override
		public void toNetwork(ArrowPierceUpgradeResult result, FriendlyByteBuf buf) {
			buf.writeByte(result.amount);
		}
		
		@Override
		public ArrowPierceUpgradeResult fromNetwork(IUpgradeInternals internals, FriendlyByteBuf buf) {
			byte amount = buf.readByte();
			return new ArrowPierceUpgradeResult(internals, amount);
		}
		
	}
	
}