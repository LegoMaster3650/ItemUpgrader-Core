package io._3650.itemupgrader.upgrades.conditions;

import com.google.gson.JsonObject;

import io._3650.itemupgrader.api.data.EntryCategory;
import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.data.UpgradeEntrySet;
import io._3650.itemupgrader.api.data.UpgradeEventData;
import io._3650.itemupgrader.api.serializer.UpgradeConditionSerializer;
import io._3650.itemupgrader.api.type.UpgradeCondition;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class HasEnchantmentUpgradeCondition extends UpgradeCondition {
	
	private final UpgradeEntry<ItemStack> itemEntry;
	private final ResourceLocation enchantId;
	private final Enchantment enchant;
	private final int level;
	
	public HasEnchantmentUpgradeCondition(IUpgradeInternals internals, boolean inverted, UpgradeEntry<ItemStack> itemEntry, ResourceLocation enchantId, int level) {
		super(internals, inverted, UpgradeEntrySet.create(builder -> {
			builder.require(itemEntry);
		}));
		this.itemEntry = itemEntry;
		this.enchantId = enchantId;
		this.enchant = ForgeRegistries.ENCHANTMENTS.getValue(enchantId);
		this.level = level;
	}
	
	@Override
	public boolean test(UpgradeEventData data) {
		ItemStack stack = data.getEntry(this.itemEntry);
		return stack.getEnchantmentLevel(this.enchant) >= this.level;
	}
	
	private final Serializer instance = new Serializer();
	
	@Override
	public Serializer getSerializer() {
		return instance;
	}
	
	@Override
	public MutableComponent[] getTooltip(ItemStack stack) {
		return new MutableComponent[] {Component.translatable(this.enchant.getDescriptionId()), this.level == 1 ? Component.literal("") : Component.literal(" ").append(Component.translatable("enchantment.level." + this.level))};
	}
	
	@Override
	public void hackyToNetworkReadJavadoc(FriendlyByteBuf buf) {
		this.getSerializer().toNetwork(this, buf);
	}
	
	public static class Serializer extends UpgradeConditionSerializer<HasEnchantmentUpgradeCondition> {
		
		@Override
		public HasEnchantmentUpgradeCondition fromJson(IUpgradeInternals internals, boolean inverted, JsonObject json) {
			UpgradeEntry<ItemStack> itemEntry = EntryCategory.ITEM.fromJson(json);
			ResourceLocation enchantId = new ResourceLocation(GsonHelper.getAsString(json, "enchantment"));
			if (!ForgeRegistries.ENCHANTMENTS.containsKey(enchantId)) throw new IllegalArgumentException("Effect does not exist: " + enchantId);
			int level = GsonHelper.getAsInt(json, "level", 1);
			return new HasEnchantmentUpgradeCondition(internals, inverted, itemEntry, enchantId, level);
		}
		
		@Override
		public void toNetwork(HasEnchantmentUpgradeCondition condition, FriendlyByteBuf buf) {
			condition.itemEntry.toNetwork(buf);
			buf.writeResourceLocation(condition.enchantId);
			buf.writeVarInt(condition.level);
		}
		
		@Override
		public HasEnchantmentUpgradeCondition fromNetwork(IUpgradeInternals internals, boolean inverted, FriendlyByteBuf buf) {
			UpgradeEntry<ItemStack> itemEntry = EntryCategory.ITEM.fromNetwork(buf);
			ResourceLocation enchantId = buf.readResourceLocation();
			int level = buf.readVarInt();
			return new HasEnchantmentUpgradeCondition(internals, inverted, itemEntry, enchantId, level);
		}
		
	}
	
}