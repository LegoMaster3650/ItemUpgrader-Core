package io._3650.itemupgrader.upgrades.results;

import com.google.gson.JsonObject;

import io._3650.itemupgrader.api.data.EntryCategory;
import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.data.UpgradeEntrySet;
import io._3650.itemupgrader.api.data.UpgradeEventData;
import io._3650.itemupgrader.api.serializer.UpgradeResultSerializer;
import io._3650.itemupgrader.api.type.UpgradeResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class TagVarIntUpgradeResult extends UpgradeResult {
	
	private final UpgradeEntry<ItemStack> itemEntry;
	private final String tagName;
	private final ValueModifier modifier;
	private final int value;
	
	public TagVarIntUpgradeResult(
			IUpgradeInternals internals,
			UpgradeEntry<ItemStack> itemEntry,
			String tagName,
			ValueModifier modifier,
			int value) {
		super(internals, UpgradeEntrySet.create(builder -> {
			builder.require(itemEntry);
		}));
		this.itemEntry = itemEntry;
		this.tagName = tagName;
		this.modifier = modifier;
		this.value = value;
	}
	
	@Override
	public boolean execute(UpgradeEventData data) {
		ItemStack stack = data.getEntry(this.itemEntry);
		CompoundTag tag = stack.getOrCreateTag();
		switch (this.modifier) {
		default:
		case SET:
			tag.putInt(this.tagName, this.value);
			break;
		case ADD:
			if (tag.contains(this.tagName, CompoundTag.TAG_INT)) tag.putInt(this.tagName, tag.getInt(this.tagName) + this.value);
			else tag.putInt(this.tagName, this.value);
			break;
		case SUB:
			if (tag.contains(this.tagName, CompoundTag.TAG_INT)) tag.putInt(this.tagName, tag.getInt(this.tagName) - this.value);
			else tag.putInt(this.tagName, -this.value);
			break;
		}
		stack.setTag(tag);
		return true;
	}
	
	private final Serializer instance = new Serializer();
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return new MutableComponent[]{Component.literal(this.tagName), Component.literal(this.modifier.getName()), Component.literal(Integer.toString(this.value))};
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeResultSerializer<TagVarIntUpgradeResult> {
		
		@Override
		public TagVarIntUpgradeResult fromJson(IUpgradeInternals internals, JsonObject json) {
			UpgradeEntry<ItemStack> itemEntry = EntryCategory.ITEM.fromJson(json);
			String tagName = GsonHelper.getAsString(json, "tag");
			ValueModifier modifier = ValueModifier.byName(GsonHelper.getAsString(json, "operation", ValueModifier.SET.getName()));
			int value = GsonHelper.getAsInt(json, "value");
			return new TagVarIntUpgradeResult(internals, itemEntry, tagName, modifier, value);
		}
		
		@Override
		public void toNetwork(TagVarIntUpgradeResult result, FriendlyByteBuf buf) {
			result.itemEntry.toNetwork(buf);
			buf.writeUtf(result.tagName);
			buf.writeEnum(result.modifier);
			buf.writeInt(result.value);
		}
		
		@Override
		public TagVarIntUpgradeResult fromNetwork(IUpgradeInternals internals, FriendlyByteBuf buf) {
			UpgradeEntry<ItemStack> itemEntry = EntryCategory.ITEM.fromNetwork(buf);
			String tagName = buf.readUtf();
			ValueModifier modifier = buf.readEnum(ValueModifier.class);
			int value = buf.readInt();
			return new TagVarIntUpgradeResult(internals, itemEntry, tagName, modifier, value);
		}
		
	}
	
	private static enum ValueModifier {
		SET("="),
		ADD("+"),
		SUB("-");
		
		private final String name;
		
		private ValueModifier(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		public static ValueModifier byName(String name) {
			for (var value : values()) {
				if (value.name.equals(name)) return value;
			}
			return SET;
		}
		
	}
	
}
