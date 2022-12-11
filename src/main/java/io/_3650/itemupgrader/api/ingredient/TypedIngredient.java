package io._3650.itemupgrader.api.ingredient;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io._3650.itemupgrader.ItemUpgraderCore;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

/**
 * Custom ingredient that runs the stack through a {@linkplain TypedCriteria} containing a {@linkplain Predicate} for an {@linkplain ItemStack}
 * @author LegoMaster3650
 * @see TypedCriteria
 */
public class TypedIngredient extends AbstractIngredient {
	
	private final ResourceLocation type;
	private final TypedCriteria criteria;
	
	/**
	 * Constructs a new {@linkplain TypedIngredient}
	 * @param type The identifier for the {@linkplain TypedCriteria} for this ingredient
	 */
	public TypedIngredient(ResourceLocation type) {
		super();
		this.type = type;
		this.criteria = !ItemUpgraderCore.TYPED_CRITERIA_REGISTRY.get().containsKey(this.type) ? TypedCriteria.FALSE.get() : ItemUpgraderCore.TYPED_CRITERIA_REGISTRY.get().getValue(this.type);
	}
	
	@Override
	public boolean test(@Nullable ItemStack stack) {
		if (stack == null) return false;
		return this.criteria.test(stack);
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}
	
	@Override
	public IIngredientSerializer<? extends Ingredient> getSerializer() {
		return Serializer.INSTANCE;
	}
	
	@Override
	public JsonElement toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("type", CraftingHelper.getID(Serializer.INSTANCE).toString());
		json.addProperty("group", this.type.toString());
		return json;
	}
	
	/**
	 * Serializer for {@linkplain TypedIngredient}s
	 * @author LegoMaster3650
	 */
	public static class Serializer implements IIngredientSerializer<TypedIngredient> {
		/**
		 * Singleton instance for the serializer
		 */
		public static final Serializer INSTANCE = new Serializer();
		
		@Override
		public TypedIngredient parse(FriendlyByteBuf buf) {
			return new TypedIngredient(buf.readResourceLocation());
		}
		
		@Override
		public TypedIngredient parse(JsonObject json) {
			return new TypedIngredient(new ResourceLocation(GsonHelper.getAsString(json, "group")));
		}
		
		@Override
		public void write(FriendlyByteBuf buf, TypedIngredient ingredient) {
			buf.writeResourceLocation(ingredient.type);
		}
		
	}
	
}
