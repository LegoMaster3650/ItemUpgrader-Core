package io._3650.itemupgrader.registry;

import io._3650.itemupgrader.ItemUpgraderCore;
import io._3650.itemupgrader.api.ingredient.TypedCriteria;
import io._3650.itemupgrader.api.registry.ItemUpgraderRegistry;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTypedCriteria {
	
	public static final DeferredRegister<TypedCriteria> CRITERIA = DeferredRegister.create(ItemUpgraderRegistry.TYPED_CRITERIA, ItemUpgraderCore.MOD_ID);
	
	//Always True or False criteria
	public static final RegistryObject<TypedCriteria> TRUE = CRITERIA.register("true", TypedCriteria.TRUE);
	public static final RegistryObject<TypedCriteria> FALSE = CRITERIA.register("false", TypedCriteria.FALSE);
	
	//Tool criteria
	public static final TagKey<Item> TAG_SWORD = ItemTags.create(ItemUpgraderRegistry.ugRes("sword"));
	public static final RegistryObject<TypedCriteria> SWORD = CRITERIA.register("sword", TypedCriteria.of(stack -> stack.getItem() instanceof SwordItem, TAG_SWORD));
	public static final TagKey<Item> TAG_AXE = ItemTags.create(ItemUpgraderRegistry.ugRes("axe"));
	public static final RegistryObject<TypedCriteria> AXE = CRITERIA.register("axe", TypedCriteria.of(stack -> stack.getItem() instanceof AxeItem, TAG_AXE));
	public static final TagKey<Item> TAG_PICKAXE = ItemTags.create(ItemUpgraderRegistry.ugRes("pickaxe"));
	public static final RegistryObject<TypedCriteria> PICKAXE = CRITERIA.register("pickaxe", TypedCriteria.of(stack -> stack.getItem() instanceof PickaxeItem, TAG_PICKAXE));
	public static final TagKey<Item> TAG_SHOVEL = ItemTags.create(ItemUpgraderRegistry.ugRes("shovel"));
	public static final RegistryObject<TypedCriteria> SHOVEL = CRITERIA.register("shovel", TypedCriteria.of(stack -> stack.getItem() instanceof ShovelItem, TAG_SHOVEL));
	public static final TagKey<Item> TAG_HOE = ItemTags.create(ItemUpgraderRegistry.ugRes("hoe"));
	public static final RegistryObject<TypedCriteria> HOE = CRITERIA.register("hoe", TypedCriteria.of(stack -> stack.getItem() instanceof HoeItem, TAG_HOE));
	public static final TagKey<Item> TAG_FISHING_ROD = ItemTags.create(ItemUpgraderRegistry.ugRes("fishing_rod"));
	public static final RegistryObject<TypedCriteria> FISHING_ROD = CRITERIA.register("fishing_rod", TypedCriteria.of(stack -> stack.getItem() instanceof FishingRodItem, TAG_FISHING_ROD));
	public static final TagKey<Item> TAG_BOW = ItemTags.create(ItemUpgraderRegistry.ugRes("bow"));
	public static final RegistryObject<TypedCriteria> BOW = CRITERIA.register("bow", TypedCriteria.of(stack -> stack.getItem() instanceof BowItem, TAG_BOW));
	public static final TagKey<Item> TAG_CROSSBOW = ItemTags.create(ItemUpgraderRegistry.ugRes("crossbow"));
	public static final RegistryObject<TypedCriteria> CROSSBOW = CRITERIA.register("crossbow", TypedCriteria.of(stack -> stack.getItem() instanceof CrossbowItem, TAG_CROSSBOW));
	public static final TagKey<Item> TAG_TRIDENT = ItemTags.create(ItemUpgraderRegistry.ugRes("trident"));
	public static final RegistryObject<TypedCriteria> TRIDENT = CRITERIA.register("trident", TypedCriteria.of(stack -> stack.getItem() instanceof TridentItem, TAG_TRIDENT));
	
	public static final TagKey<Item> TAG_SHIELD = ItemTags.create(ItemUpgraderRegistry.ugRes("shield"));
	public static final RegistryObject<TypedCriteria> SHIELD = CRITERIA.register("shield", TypedCriteria.of(stack -> stack.getItem().canPerformAction(stack, ToolActions.SHIELD_BLOCK), TAG_SHIELD));
	
	public static final TagKey<Item> TAG_ELYTRA = ItemTags.create(ItemUpgraderRegistry.ugRes("elytra"));
	public static final RegistryObject<TypedCriteria> ELYTRA = CRITERIA.register("elytra", TypedCriteria.of(stack -> stack.getItem() instanceof ElytraItem, TAG_ELYTRA));
	
	//Equipment Slot criteria
	public static final TagKey<Item> TAG_HEAD = ItemTags.create(ItemUpgraderRegistry.ugRes("head"));
	public static final RegistryObject<TypedCriteria> HEAD = CRITERIA.register("head", TypedCriteria.of(stack -> LivingEntity.getEquipmentSlotForItem(stack) == EquipmentSlot.HEAD, TAG_HEAD));
	public static final TagKey<Item> TAG_CHEST = ItemTags.create(ItemUpgraderRegistry.ugRes("chest"));
	public static final RegistryObject<TypedCriteria> CHEST = CRITERIA.register("chest", TypedCriteria.of(stack -> LivingEntity.getEquipmentSlotForItem(stack) == EquipmentSlot.CHEST, TAG_CHEST));
	public static final TagKey<Item> TAG_LEGS = ItemTags.create(ItemUpgraderRegistry.ugRes("legs"));
	public static final RegistryObject<TypedCriteria> LEGS = CRITERIA.register("legs", TypedCriteria.of(stack -> LivingEntity.getEquipmentSlotForItem(stack) == EquipmentSlot.LEGS, TAG_LEGS));
	public static final TagKey<Item> TAG_FEET = ItemTags.create(ItemUpgraderRegistry.ugRes("feet"));
	public static final RegistryObject<TypedCriteria> FEET = CRITERIA.register("feet", TypedCriteria.of(stack -> LivingEntity.getEquipmentSlotForItem(stack) == EquipmentSlot.FEET, TAG_FEET));
	public static final TagKey<Item> TAG_MAINHAND = ItemTags.create(ItemUpgraderRegistry.ugRes("mainhand"));
	public static final RegistryObject<TypedCriteria> MAINHAND = CRITERIA.register("mainhand", TypedCriteria.of(stack -> LivingEntity.getEquipmentSlotForItem(stack) == EquipmentSlot.MAINHAND, TAG_MAINHAND));
	public static final TagKey<Item> TAG_OFFHAND = ItemTags.create(ItemUpgraderRegistry.ugRes("offhand"));
	public static final RegistryObject<TypedCriteria> OFFHAND = CRITERIA.register("offhand", TypedCriteria.of(stack -> LivingEntity.getEquipmentSlotForItem(stack) == EquipmentSlot.OFFHAND, TAG_OFFHAND));
	
	//Armor criteria (only accepts actual armor, not elytras)
	public static final TagKey<Item> TAG_HELMET = ItemTags.create(ItemUpgraderRegistry.ugRes("helmet"));
	public static final RegistryObject<TypedCriteria> HELMET = CRITERIA.register("helmet", TypedCriteria.of(stack -> armorCheck(stack.getItem(), EquipmentSlot.HEAD), TAG_HELMET));
	public static final TagKey<Item> TAG_CHESTPLATE = ItemTags.create(ItemUpgraderRegistry.ugRes("chestplate"));
	public static final RegistryObject<TypedCriteria> CHESTPLATE = CRITERIA.register("chestplate", TypedCriteria.of(stack -> armorCheck(stack.getItem(), EquipmentSlot.CHEST), TAG_CHESTPLATE));
	public static final TagKey<Item> TAG_LEGGINGS = ItemTags.create(ItemUpgraderRegistry.ugRes("leggings"));
	public static final RegistryObject<TypedCriteria> LEGGINGS = CRITERIA.register("leggings", TypedCriteria.of(stack -> armorCheck(stack.getItem(), EquipmentSlot.LEGS), TAG_LEGGINGS));
	public static final TagKey<Item> TAG_BOOTS = ItemTags.create(ItemUpgraderRegistry.ugRes("boots"));
	public static final RegistryObject<TypedCriteria> BOOTS = CRITERIA.register("boots", TypedCriteria.of(stack -> armorCheck(stack.getItem(), EquipmentSlot.FEET), TAG_BOOTS));
	
	//Misc.
	public static final TagKey<Item> TAG_BOOK = ItemTags.create(ItemUpgraderRegistry.ugRes("book"));
	public static final RegistryObject<TypedCriteria> BOOK = CRITERIA.register("book", TypedCriteria.of(stack -> stack.getItem() instanceof BookItem, TAG_BOOK));
	public static final TagKey<Item> TAG_ENCHANTABLE = ItemTags.create(ItemUpgraderRegistry.ugRes("enchantable"));
	@SuppressWarnings("deprecation") //I am intentionally ignoring stack context here
	public static final RegistryObject<TypedCriteria> ENCHANTABLE = CRITERIA.register("enchantable", TypedCriteria.of(stack -> stack.getItem().getEnchantmentValue() > 0, TAG_ENCHANTABLE));
	public static final TagKey<Item> TAG_UNBREAKING_ENCHANTABLE = ItemTags.create(ItemUpgraderRegistry.ugRes("unbreaking_enchantable"));
	public static final RegistryObject<TypedCriteria> UNBREAKING_ENCHANTABLE = CRITERIA.register("unbreaking_enchantable", TypedCriteria.of(stack -> Enchantments.UNBREAKING.canEnchant(stack), TAG_UNBREAKING_ENCHANTABLE));
	public static final TagKey<Item> TAG_EFFICIENCY_ENCHANTABLE = ItemTags.create(ItemUpgraderRegistry.ugRes("efficiency_enchantable"));
	public static final RegistryObject<TypedCriteria> EFFICIENCY_ENCHANTABLE = CRITERIA.register("efficiency_enchantable", TypedCriteria.of(stack -> Enchantments.BLOCK_EFFICIENCY.canEnchant(stack), TAG_EFFICIENCY_ENCHANTABLE));
	public static final TagKey<Item> TAG_FORTUNE_ENCHANTABLE = ItemTags.create(ItemUpgraderRegistry.ugRes("fortune_enchantable"));
	public static final RegistryObject<TypedCriteria> FORTUNE_ENCHANTABLE = CRITERIA.register("fortune_enchantable", TypedCriteria.of(stack -> Enchantments.BLOCK_FORTUNE.canEnchant(stack), TAG_FORTUNE_ENCHANTABLE));
	public static final TagKey<Item> TAG_SHARPNESS_ENCHANTABLE = ItemTags.create(ItemUpgraderRegistry.ugRes("sharpness_enchantable"));
	public static final RegistryObject<TypedCriteria> SHARPNESS_ENCHANTABLE = CRITERIA.register("sharpness_enchantable", TypedCriteria.of(stack -> Enchantments.SHARPNESS.canEnchant(stack), TAG_SHARPNESS_ENCHANTABLE));
	public static final TagKey<Item> TAG_LOOTING_ENCHANTABLE = ItemTags.create(ItemUpgraderRegistry.ugRes("looting_enchantable"));
	public static final RegistryObject<TypedCriteria> LOOTING_ENCHANTABLE = CRITERIA.register("looting_enchantable", TypedCriteria.of(stack -> Enchantments.MOB_LOOTING.canEnchant(stack), TAG_LOOTING_ENCHANTABLE));
	
	//Utility functions
	private static boolean armorCheck(Item item, EquipmentSlot slot) {
		return item instanceof ArmorItem armor ? armor.getSlot() == slot : false;
	}
	
}