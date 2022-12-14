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

public class ArrowKnockbackUpgradeResult extends UpgradeResult {
	
	private final int knockback;
	
	public ArrowKnockbackUpgradeResult(IUpgradeInternals internals, int knockback) {
		super(internals, UpgradeEntrySet.create(builder -> builder.require(UpgradeEntry.PROJECTILE)));
		this.knockback = knockback;
	}
	
	@Override
	public boolean execute(UpgradeEventData data) {
		if (!(data.getEntry(UpgradeEntry.PROJECTILE) instanceof AbstractArrow arrow)) return false;
		arrow.setKnockback(this.knockback);
		return true;
	}
	
	private final Serializer instance = new Serializer();
	
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return ComponentHelper.arrayify(Component.literal(Integer.valueOf(this.knockback).toString()));
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeResultSerializer<ArrowKnockbackUpgradeResult> {
		
		@Override
		public ArrowKnockbackUpgradeResult fromJson(IUpgradeInternals internals, JsonObject json) {
			int knockback = GsonHelper.getAsInt(json, "knockback", 1);
			return new ArrowKnockbackUpgradeResult(internals, knockback);
		}
		
		@Override
		public void toNetwork(ArrowKnockbackUpgradeResult result, FriendlyByteBuf buf) {
			buf.writeVarInt(result.knockback);
		}
		
		@Override
		public ArrowKnockbackUpgradeResult fromNetwork(IUpgradeInternals internals, FriendlyByteBuf buf) {
			int knockback = buf.readVarInt();
			return new ArrowKnockbackUpgradeResult(internals, knockback);
		}
		
	}
	
}