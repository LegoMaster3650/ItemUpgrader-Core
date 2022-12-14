package io._3650.itemupgrader.upgrades.results;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import io._3650.itemupgrader.api.data.EntryCategory;
import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.data.UpgradeEntrySet;
import io._3650.itemupgrader.api.data.UpgradeEventData;
import io._3650.itemupgrader.api.serializer.UpgradeResultSerializer;
import io._3650.itemupgrader.api.type.UpgradeResult;
import io._3650.itemupgrader.api.util.ComponentHelper;
import io._3650.itemupgrader.api.util.UpgradeJsonHelper;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class CommandUpgradeResult extends UpgradeResult {
	
	private final String commandFormat;
	private final UpgradeEntry<Vec3> posEntry;
	private final Vec3 posOffset;
	private final Vec2 rotation;
	private final UpgradeEntry<Entity> entityEntry;
	private final boolean ignoreEntity;
	private final int permissionLevel;
	
	public CommandUpgradeResult(
			IUpgradeInternals internals,
			String commandFormat,
			UpgradeEntry<Vec3> posEntry,
			@Nullable Vec3 posOffset,
			Vec2 rotation,
			UpgradeEntry<Entity> entityEntry,
			boolean ignoreEntity,
			int permissionLevel) {
		super(internals, UpgradeEntrySet.create(builder -> {
			builder.requireAll(UpgradeEntry.LEVEL, posEntry);
		}));
		this.commandFormat = commandFormat;
		this.posEntry = posEntry;
		this.posOffset = posOffset;
		this.rotation = rotation;
		this.entityEntry = entityEntry;
		this.ignoreEntity = ignoreEntity;
		this.permissionLevel = permissionLevel;
	}
	
	@Override
	public boolean execute(UpgradeEventData data) {
		if (!(data.getEntry(UpgradeEntry.LEVEL) instanceof ServerLevel level)) return false;
		Entity entity = data.getEntryOrNull(this.entityEntry);
		Vec3 pos = data.getEntry(this.posEntry).add(this.posOffset);
		CommandSourceStack sourceStack;
		if (entity == null || this.ignoreEntity) sourceStack = new CommandSourceStack(
				CommandSource.NULL,
				pos,
				this.rotation,
				level,
				this.permissionLevel,
				"ItemUpgrader." + this.getId(),
				Component.literal("ItemUpgrader" + this.getId()),
				level.getServer(),
				entity);
		else sourceStack = new CommandSourceStack(
				CommandSource.NULL,
				pos,
				entity.getRotationVector().add(this.rotation),
				level,
				this.permissionLevel,
				entity.getName().getString(),
				entity.getDisplayName(),
				level.getServer(),
				entity);
		return level.getServer().getCommands().performPrefixedCommand(sourceStack, this.commandFormat) > 0;
	}
	
	private final Serializer instance = new Serializer();
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return ComponentHelper.arrayify(Component.translatable("upgradeCommand." + this.getDescriptionId()));
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeResultSerializer<CommandUpgradeResult> {
		
		@Override
		public CommandUpgradeResult fromJson(IUpgradeInternals internals, JsonObject json) {
			String commandFormat = GsonHelper.getAsString(json, "command");
			UpgradeEntry<Vec3> posEntry = EntryCategory.POSITION.fromJson(json);
			Vec3 posOffset = UpgradeJsonHelper.getPosition(json, "offset");
			Vec2 rotation = UpgradeJsonHelper.getVec2(json, "rotation", Vec2.ZERO);
			UpgradeEntry<Entity> entityEntry = EntryCategory.ENTITY.fromJson(json);
			boolean ignoreEntity = GsonHelper.getAsBoolean(json, "ignore_entity", false);
			int permissionLevel = Math.min(2, GsonHelper.getAsInt(json, "permission_level", 2));
			return new CommandUpgradeResult(internals, commandFormat, posEntry, posOffset, rotation, entityEntry, ignoreEntity, permissionLevel);
		}
		
		@Override
		public void toNetwork(CommandUpgradeResult result, FriendlyByteBuf buf) {
			buf.writeUtf(result.commandFormat);
			result.posEntry.toNetwork(buf);
			buf.writeDouble(result.posOffset.x).writeDouble(result.posOffset.y).writeDouble(result.posOffset.z);
			buf.writeFloat(result.rotation.x).writeFloat(result.rotation.y);
			result.entityEntry.toNetwork(buf);
			buf.writeBoolean(result.ignoreEntity);
			buf.writeInt(result.permissionLevel);
		}
		
		@Override
		public CommandUpgradeResult fromNetwork(IUpgradeInternals internals, FriendlyByteBuf buf) {
			String commandFormat = buf.readUtf();
			UpgradeEntry<Vec3> posEntry = EntryCategory.POSITION.fromNetwork(buf);
			Vec3 posOffset = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
			Vec2 rotation = new Vec2(buf.readFloat(), buf.readFloat());
			UpgradeEntry<Entity> entityEntry = EntryCategory.ENTITY.fromNetwork(buf);
			boolean ignoreEntity = buf.readBoolean();
			int permissionLevel = buf.readInt();
			return new CommandUpgradeResult(internals, commandFormat, posEntry, posOffset, rotation, entityEntry, ignoreEntity, permissionLevel);
		}
		
	}
	
}