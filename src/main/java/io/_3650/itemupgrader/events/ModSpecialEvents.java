package io._3650.itemupgrader.events;

import io._3650.itemupgrader.api.ItemUpgraderApi;
import io._3650.itemupgrader.api.data.UpgradeEntry;
import io._3650.itemupgrader.api.data.UpgradeEventData;
import io._3650.itemupgrader.registry.ModUpgradeActions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.registries.ForgeRegistries;

//For "events" that don't use forge's event system
public class ModSpecialEvents {
	
	/*
	 * ENCHANTMENTS
	 */
	
	public static int lootEnchantmentBonus(Enchantment enchantment, int enchantmentLevel, ItemStack dropStack, LootContext context) {
		if (context.hasParam(LootContextParams.TOOL)) {
			UpgradeEventData data = ItemUpgraderApi.runActions(ModUpgradeActions.LOOT_ENCHANTMENT, new UpgradeEventData.Builder(context.getParam(LootContextParams.TOOL))
					.entry(UpgradeEntry.ENCHANTMENT_ID, ForgeRegistries.ENCHANTMENTS.getKey(enchantment))
					.modifiableEntry(UpgradeEntry.ENCHANTMENT_LEVEL, enchantmentLevel));
			return data.getEntry(UpgradeEntry.ENCHANTMENT_LEVEL);
		} else return enchantmentLevel;
	}
	
	public static int riptideBonus(ItemStack stack, LivingEntity living, int riptide) {
		UpgradeEventData data = ItemUpgraderApi.runActions(ModUpgradeActions.ENCHANTMENT_BONUS, new UpgradeEventData.Builder(living)
				.entry(UpgradeEntry.ITEM, stack)
				.entry(UpgradeEntry.ENCHANTMENT_ID, ForgeRegistries.ENCHANTMENTS.getKey(Enchantments.RIPTIDE))
				.modifiableEntry(UpgradeEntry.ENCHANTMENT_LEVEL, riptide));
		return data.getEntry(UpgradeEntry.ENCHANTMENT_LEVEL);
	}
	
	public static byte loyaltyBonus(ItemStack stack, LivingEntity living, int loyalty) {
		UpgradeEventData data = ItemUpgraderApi.runActions(ModUpgradeActions.ENCHANTMENT_BONUS, new UpgradeEventData.Builder(living)
				.entry(UpgradeEntry.ITEM, stack)
				.entry(UpgradeEntry.ENCHANTMENT_ID, ForgeRegistries.ENCHANTMENTS.getKey(Enchantments.LOYALTY))
				.modifiableEntry(UpgradeEntry.ENCHANTMENT_LEVEL, loyalty));
		return data.getEntry(UpgradeEntry.ENCHANTMENT_LEVEL).byteValue();
	}
	
	public static int unbreakingBonus(ItemStack stack, int unbreaking) {
		UpgradeEventData data = ItemUpgraderApi.runActions(ModUpgradeActions.ENCHANTMENT_BONUS, new UpgradeEventData.Builder(stack)
				.entry(UpgradeEntry.ENCHANTMENT_ID, ForgeRegistries.ENCHANTMENTS.getKey(Enchantments.UNBREAKING))
				.modifiableEntry(UpgradeEntry.ENCHANTMENT_LEVEL, unbreaking));
		return data.getEntry(UpgradeEntry.ENCHANTMENT_LEVEL);
	}
	
	public static int itemEnchantability(ItemStack stack, int originalValue) {
		UpgradeEventData data = ItemUpgraderApi.runActions(ModUpgradeActions.ENCHANTABILITY, new UpgradeEventData.Builder(stack)
				.modifiableEntry(UpgradeEntry.ENCHANTABILITY, originalValue));
		return data.getEntry(UpgradeEntry.ENCHANTABILITY);
	}
	
	public static void bowShoot(ItemStack bow, Player player, ItemStack stack, AbstractArrow arrow, boolean hasAmmo) {
		if (hasAmmo) ItemUpgraderApi.runActions(ModUpgradeActions.BOW_SHOOT, new UpgradeEventData.Builder(player)
				.entry(UpgradeEntry.ITEM, bow)
				.entry(UpgradeEntry.BOW_ITEM, stack)
				.entry(UpgradeEntry.PROJECTILE, arrow));
	}
	
	public static void crossbowShoot(ItemStack crossbow, LivingEntity living, ItemStack stack, Projectile projectile) {
		ItemUpgraderApi.runActions(ModUpgradeActions.CROSSBOW_SHOOT, new UpgradeEventData.Builder(living)
				.entry(UpgradeEntry.ITEM, crossbow)
				.entry(UpgradeEntry.BOW_ITEM, stack)
				.entry(UpgradeEntry.PROJECTILE, projectile));
	}
	
	public static float arrowSpeed(ItemStack stack, float baseSpeed) {
		UpgradeEventData data = ItemUpgraderApi.runActions(ModUpgradeActions.ARROW_SPEED, new UpgradeEventData.Builder(stack)
				.modifiableEntry(UpgradeEntry.ARROW_SPEED, baseSpeed));
		return data.getEntry(UpgradeEntry.ARROW_SPEED);
	}
	
	public static float arrowInaccuracy(ItemStack stack, float baseInaccuracy) {
		UpgradeEventData data = ItemUpgraderApi.runActions(ModUpgradeActions.ARROW_INACCURACY, new UpgradeEventData.Builder(stack)
				.modifiableEntry(UpgradeEntry.ARROW_INACCURACY, baseInaccuracy));
		return data.getEntry(UpgradeEntry.ARROW_INACCURACY);
	}
	
}