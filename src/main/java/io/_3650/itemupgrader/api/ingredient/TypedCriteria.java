package io._3650.itemupgrader.api.ingredient;

import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Criteria for {@linkplain TypedIngredient} registered in the <b>TYPED_CRITERIA</b> ItemUpgraderRegistry
 * @author LegoMaster3650
 * @see TypedIngredient
 */
public class TypedCriteria {
	
	/**Utility for quickly getting an always true {@linkplain TypedCriteria}*/
	public static final Supplier<TypedCriteria> TRUE = () -> new TypedCriteria(stack -> true, null);
	/**Utility for quickly getting an always true {@linkplain TypedCriteria}*/
	public static final Supplier<TypedCriteria> FALSE = () -> new TypedCriteria(stack -> true, null);
	
	private final Predicate<ItemStack> predicate;
	@Nullable
	private final TagKey<Item> tag;
	
	/**
	 * Constructs a new TypedCriteria with the given item predicate
	 * @param predicate A predicate of an {@linkplain ItemStack} to check for typing against
	 * @param tag The tag to use for additional whitelisted items. <b>Please use the same name as the criteria!</b>
	 */
	public TypedCriteria(Predicate<ItemStack> predicate, @Nullable TagKey<Item> tag) {
		this.predicate = predicate;
		this.tag = tag;
	}
	
	/**
	 * Constructs a new supplier for a typed criteria
	 * @param predicate A predicate of an {@linkplain ItemStack} to check for typing against
	 * @param tag The tag to use for additional whitelisted items. <b>Please use the same name as the criteria!</b>
	 * @return A new {@linkplain Supplier} for a {@linkplain TypedCriteria}
	 */
	public static Supplier<TypedCriteria> of(Predicate<ItemStack> predicate, @Nullable TagKey<Item> tag) {
		return () -> new TypedCriteria(predicate, tag);
	}
	
	/**
	 * Tests this predicate against the given item
	 * @param stack The {@linkplain ItemStack} to test
	 * @return Whether the item passes the predicate's test
	 */
	public boolean test(ItemStack stack) {
		return stack.is(this.tag) || this.predicate.test(stack);
	}
	
}