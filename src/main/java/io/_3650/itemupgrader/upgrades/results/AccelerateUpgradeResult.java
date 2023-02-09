package io._3650.itemupgrader.upgrades.results;

import com.google.gson.JsonObject;

import io._3650.itemupgrader.api.data.EntryCategory;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class AccelerateUpgradeResult extends UpgradeResult {
	
	private final double amount;
	private final UpgradeEntry<LivingEntity> livingEntry;
	
	public AccelerateUpgradeResult(IUpgradeInternals internals, double amount, UpgradeEntry<LivingEntity> livingEntry) {
		super(internals, UpgradeEntrySet.create(builder -> builder.require(livingEntry)));
		this.amount = amount;
		this.livingEntry = livingEntry;
	}
	
	@Override
	public boolean execute(UpgradeEventData data) {
		LivingEntity living = data.getEntry(this.livingEntry);
		Vec3 mov = living.getDeltaMovement();
		Vec3 look = living.getLookAngle();
		living.setDeltaMovement(mov.add(look.x * 0.1D + (look.x * this.amount - mov.x) * 0.5D, look.y * 0.1D + (look.y * this.amount - mov.y) * 0.5D, look.z * 0.1D + (look.z * this.amount - mov.z) * 0.5D));
		return true;
	}
	
	private final Serializer instance = new Serializer();
	
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return ComponentHelper.arrayify(Component.literal(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.amount)));
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeResultSerializer<AccelerateUpgradeResult> {
		
		@Override
		public AccelerateUpgradeResult fromJson(IUpgradeInternals internals, JsonObject json) {
			double amount = GsonHelper.getAsDouble(json, "amount");
			UpgradeEntry<LivingEntity> livingEntry = EntryCategory.LIVING.fromJson(json);
			return new AccelerateUpgradeResult(internals, amount, livingEntry);
		}
		
		@Override
		public void toNetwork(AccelerateUpgradeResult result, FriendlyByteBuf buf) {
			buf.writeDouble(result.amount);
			result.livingEntry.toNetwork(buf);
		}
		
		@Override
		public AccelerateUpgradeResult fromNetwork(IUpgradeInternals internals, FriendlyByteBuf buf) {
			double amount = buf.readDouble();
			UpgradeEntry<LivingEntity> livingEntry = EntryCategory.LIVING.fromNetwork(buf);
			return new AccelerateUpgradeResult(internals, amount, livingEntry);
		}
		
	}
	
}