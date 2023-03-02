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
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PositionUpgradeCondition extends UpgradeCondition {
	
	private final UpgradeEntry<Vec3> posEntry;
	private final double xMin;
	private final double xMax;
	private final double yMin;
	private final double yMax;
	private final double zMin;
	private final double zMax;
	
	public PositionUpgradeCondition(IUpgradeInternals internals, boolean inverted, UpgradeEntry<Vec3> posEntry, double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
		super(internals, inverted, UpgradeEntrySet.create(builder -> builder.require(posEntry)));
		this.posEntry = posEntry;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.zMin = zMin;
		this.zMax = zMax;
	}
	
	@Override
	public boolean test(UpgradeEventData data) {
		Vec3 pos = data.getEntry(this.posEntry);
		return pos.x >= xMin && pos.x <= xMax && pos.y >= yMin && pos.y <= yMax && pos.z >= zMin && pos.z <= zMax;
	}
	
	private final Serializer instance = new Serializer();
	
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return ComponentHelper.empty(); // TODO Tooltip
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeConditionSerializer<PositionUpgradeCondition> {
		
		@Override
		public PositionUpgradeCondition fromJson(IUpgradeInternals internals, boolean inverted, JsonObject json) {
			UpgradeEntry<Vec3> posEntry = EntryCategory.POSITION.fromJson(json);
			double xMin, xMax;
			if (GsonHelper.isObjectNode(json, "x")) {
				JsonObject xJ = GsonHelper.getAsJsonObject(json, "x");
				xMin = GsonHelper.getAsDouble(xJ, "min", Double.NEGATIVE_INFINITY);
				xMax = GsonHelper.getAsDouble(xJ, "max", Double.POSITIVE_INFINITY);
			} else {
				xMin = Double.NEGATIVE_INFINITY;
				xMax = Double.POSITIVE_INFINITY;
			}
			double yMin, yMax;
			if (GsonHelper.isObjectNode(json, "y")) {
				JsonObject yJ = GsonHelper.getAsJsonObject(json, "y");
				yMin = GsonHelper.getAsDouble(yJ, "min", Double.NEGATIVE_INFINITY);
				yMax = GsonHelper.getAsDouble(yJ, "max", Double.POSITIVE_INFINITY);
			} else {
				yMin = Double.NEGATIVE_INFINITY;
				yMax = Double.POSITIVE_INFINITY;
			}
			double zMin, zMax;
			if (GsonHelper.isObjectNode(json, "z")) {
				JsonObject zJ = GsonHelper.getAsJsonObject(json, "z");
				zMin = GsonHelper.getAsDouble(zJ, "min", Double.NEGATIVE_INFINITY);
				zMax = GsonHelper.getAsDouble(zJ, "max", Double.POSITIVE_INFINITY);
			} else {
				zMin = Double.NEGATIVE_INFINITY;
				zMax = Double.POSITIVE_INFINITY;
			}
			return new PositionUpgradeCondition(internals, inverted, posEntry, xMin, xMax, yMin, yMax, zMin, zMax);
		}
		
		@Override
		public void toNetwork(PositionUpgradeCondition condition, FriendlyByteBuf buf) {
			condition.posEntry.toNetwork(buf);
			buf.writeDouble(condition.xMin);
			buf.writeDouble(condition.xMax);
			buf.writeDouble(condition.yMin);
			buf.writeDouble(condition.yMax);
			buf.writeDouble(condition.zMin);
			buf.writeDouble(condition.zMax);
		}
		
		@Override
		public PositionUpgradeCondition fromNetwork(IUpgradeInternals internals, boolean inverted, FriendlyByteBuf buf) {
			UpgradeEntry<Vec3> posEntry = EntryCategory.POSITION.fromNetwork(buf);
			double xMin = buf.readDouble();
			double xMax = buf.readDouble();
			double yMin = buf.readDouble();
			double yMax = buf.readDouble();
			double zMin = buf.readDouble();
			double zMax = buf.readDouble();
			return new PositionUpgradeCondition(internals, inverted, posEntry, xMin, xMax, yMin, yMax, zMin, zMax);
		}
		
	}
	
}