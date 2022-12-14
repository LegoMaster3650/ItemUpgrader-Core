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

public class ArrowPowerUpgradeResult extends UpgradeResult {
	
	private final double power;
	
	public ArrowPowerUpgradeResult(IUpgradeInternals internals, double power) {
		super(internals, UpgradeEntrySet.create(builder -> builder.require(UpgradeEntry.PROJECTILE)));
		this.power = power;
	}
	
	@Override
	public boolean execute(UpgradeEventData data) {
		if (!(data.getEntry(UpgradeEntry.PROJECTILE) instanceof AbstractArrow arrow)) return false;
		arrow.setBaseDamage(arrow.getBaseDamage() + this.power * 0.5D + 0.5D);
		return true;
	}
	
	private final Serializer instance = new Serializer();
	
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return ComponentHelper.arrayify(Component.literal(Double.valueOf(this.power).toString()));
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeResultSerializer<ArrowPowerUpgradeResult> {
		
		@Override
		public ArrowPowerUpgradeResult fromJson(IUpgradeInternals internals, JsonObject json) {
			double power = GsonHelper.getAsDouble(json, "power", 1.0D);
			return new ArrowPowerUpgradeResult(internals, power);
		}
		
		@Override
		public void toNetwork(ArrowPowerUpgradeResult result, FriendlyByteBuf buf) {
			buf.writeDouble(result.power);
		}
		
		@Override
		public ArrowPowerUpgradeResult fromNetwork(IUpgradeInternals internals, FriendlyByteBuf buf) {
			double power = buf.readDouble();
			return new ArrowPowerUpgradeResult(internals, power);
		}
		
	}
	
}